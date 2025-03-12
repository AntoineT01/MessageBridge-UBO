package com.ubo.tp.message.components.message.controller;

import com.ubo.tp.message.common.ui.EnhancedNotification;
import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.components.message.view.IChatView;
import com.ubo.tp.message.components.message.view.ModernChatView;
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

    // S'enregistrer comme observateur de la base de données
    database.addObserver(this);
    this.mEntityManager = entityManager;
  }

  @Override
  public void sendMessage(String text) {
    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      // Notification d'erreur
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

      // Récupération des éventuelles pièces jointes si nous utilisons ModernChatView
      List<String> attachments = null;
      if (messageView instanceof ModernChatView) {
        attachments = ((ModernChatView) messageView).getCurrentAttachments();
        System.out.println("Récupération des pièces jointes du message: " + attachments.size());
        for (String attachment : attachments) {
          System.out.println("  - " + attachment);
        }
      }

      // Création du message avec ou sans pièces jointes
      Message newMessage;
      if (attachments != null && !attachments.isEmpty()) {
        System.out.println("Création d'un message avec " + attachments.size() + " pièce(s) jointe(s)");
        newMessage = new Message(connectedUser, text, attachments);
      } else {
        System.out.println("Création d'un message sans pièce jointe");
        newMessage = new Message(connectedUser, text);
      }

      // Écriture du message dans le fichier
      mEntityManager.writeMessageFile(newMessage);
      messageView.clearMessageInput();
    } catch (MessageValidationException e) {
      // Affichage de l'erreur de validation
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
            || m.containsUserTag(userTag))
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

  // Implémentation des méthodes IDatabaseObserver
  @Override
  public void notifyMessageAdded(Message addedMessage) {
    SwingUtilities.invokeLater(() -> {
      User connectedUser = session.getConnectedUser();
      if (connectedUser != null
        && !addedMessage.getSender().equals(connectedUser)
        && connectedUser.getFollows().contains(addedMessage.getSender().getUserTag())) {

        // Création d'un aperçu du message
        String messagePreview = addedMessage.getText();
        if (messagePreview.length() > 50) {
          messagePreview = messagePreview.substring(0, 50) + "...";
        }

        // Ajout d'une indication s'il y a des pièces jointes
        if (addedMessage.hasAttachments()) {
          messagePreview += " [" + addedMessage.getAttachments().size() + " pièce(s) jointe(s)]";
        }

        // Affichage d'une notification
        EnhancedNotification.showNotification(
          "Nouvelle publication de " + addedMessage.getSender().getName(),
          messagePreview,
          IconFactory.createUserIcon(IconFactory.ICON_SMALL),
          // Action quand on clique sur la notification
          () -> {
            // Afficher uniquement ce message
            List<Message> messageList = model.getMessages();
            messageView.updateSearchResults(messageList.stream()
                                              .filter(m -> m.equals(addedMessage))
                                              .collect(Collectors.toList()));
          }
        );
      }

      // Ajout du message au modèle
      model.addMessage(addedMessage);
    });
  }

  @Override
  public void notifyMessageDeleted(Message deletedMessage) {
    SwingUtilities.invokeLater(() -> model.removeMessage(deletedMessage));
  }

  @Override
  public void notifyMessageModified(Message modifiedMessage) {
    // Non implémenté
  }

  @Override
  public void notifyUserAdded(User addedUser) {
    // Non implémenté
  }

  @Override
  public void notifyUserDeleted(User deletedUser) {
    // Non implémenté
  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    // Non implémenté
  }

  @Override
  public void onMessageSent(Message message) {
    // Non implémenté
  }
}