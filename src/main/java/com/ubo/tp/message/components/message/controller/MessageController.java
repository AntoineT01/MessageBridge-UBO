package com.ubo.tp.message.components.message.controller;

import com.ubo.tp.message.common.ui.EnhancedNotification;
import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.components.message.view.IChatView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISession;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.List;
import java.util.stream.Collectors;

public class MessageController implements IMessageController, IDatabaseObserver {

  private final ISession session;
  private final IChatView messageView;
  private final MessageModel model;
  protected EntityManager mEntityManager;

  private static final int MAX_MESSAGE_LENGTH = 200;

  public MessageController(ISession session, IChatView messageView, IDatabase database, MessageModel model,
                           EntityManager entityManager) {
    this.session = session;
    this.messageView = messageView;
    this.model = model;

    // S'abonner aux notifications de la base de données
    database.addObserver(this);
    this.mEntityManager = entityManager;
  }

  @Override
  public void sendMessage(String text) {
    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      // Aucun utilisateur connecté - afficher un message d'erreur
      JOptionPane.showMessageDialog(null, "Aucun utilisateur connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      if (text == null || text.trim().isEmpty()) {
        throw new MessageValidationException("Le texte du message ne peut pas être vide.");
      }
      if (text.length() > MAX_MESSAGE_LENGTH) {
        throw new MessageValidationException("Le texte du message ne peut pas dépasser " + MAX_MESSAGE_LENGTH + " caractères.");
      }
      Message newMessage = new Message(connectedUser, text);
      // Envoyer le message
      mEntityManager.writeMessageFile(newMessage);
      messageView.clearMessageInput();
    } catch (MessageValidationException e) {
      // Afficher l'erreur de validation
      JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur de validation", JOptionPane.ERROR_MESSAGE);
    }
  }

  public static List<Message> searchMessages(String query, List<Message> allMessages) {
    List<Message> results;
    if (query == null || query.trim().isEmpty()) {
      results = allMessages;
    } else {
      String trimmedQuery = query.trim();
      if (trimmedQuery.contains("@") && !trimmedQuery.contains("#")) {
        String userTag = trimmedQuery.replace("@", "");
        results = allMessages.stream()
          .filter(m -> m.getSender().getUserTag().equalsIgnoreCase("@" + userTag)
            || m.containsUserTag("@" + userTag))
          .toList();
      } else if (trimmedQuery.contains("#") && !trimmedQuery.contains("@")) {
        String tag = trimmedQuery.replace("#", "");
        results = allMessages.stream()
          .filter(m -> m.containsTag(tag))
          .toList();
      } else {
        String cleanedQuery = trimmedQuery.replace("@", "").replace("#", "");
        results = allMessages.stream()
          .filter(m -> m.getSender().getUserTag().equalsIgnoreCase("@" + cleanedQuery)
            || m.containsUserTag(cleanedQuery)
            || m.containsTag(cleanedQuery))
          .toList();
      }
    }
    return results;
  }

  // Méthode améliorée pour notifier l'utilisateur des nouveaux messages
  @Override
  public void notifyMessageAdded(Message addedMessage) {
    SwingUtilities.invokeLater(() -> {
      User connectedUser = session.getConnectedUser();
      if (connectedUser != null
        && !addedMessage.getSender().equals(connectedUser)
        && connectedUser.getFollows().contains(addedMessage.getSender().getUserTag())) {

        // Contenu du message tronqué si trop long
        String messagePreview = addedMessage.getText();
        if (messagePreview.length() > 50) {
          messagePreview = messagePreview.substring(0, 50) + "...";
        }

        // Création d'une notification enrichie
        EnhancedNotification.showNotification(
          "Nouvelle publication de " + addedMessage.getSender().getName(),
          messagePreview,
          IconFactory.createUserIcon(IconFactory.ICON_SMALL),
          // Action lors du clic sur la notification
          () -> {
            // Mettre en avant le message dans l'interface
            // (code à adapter selon votre implémentation d'interface)
            List<Message> messageList = model.getMessages();
            messageView.updateSearchResults(messageList.stream()
                                              .filter(m -> m.equals(addedMessage))
                                              .collect(Collectors.toList()));
          }
        );
      }

      // Ajouter le message au modèle
      model.addMessage(addedMessage);
    });
  }

  @Override
  public void notifyMessageDeleted(Message deletedMessage) {
    SwingUtilities.invokeLater(() -> model.removeMessage(deletedMessage));
  }

  @Override
  public void notifyMessageModified(Message modifiedMessage) {
    // Implémentation par défaut
  }

  @Override
  public void notifyUserAdded(User addedUser) {
    // Implémentation par défaut
  }

  @Override
  public void notifyUserDeleted(User deletedUser) {
    // Implémentation par défaut
  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    // Implémentation par défaut
  }

  @Override
  public void onMessageSent(Message message) {
    // Implémentation par défaut
  }
}