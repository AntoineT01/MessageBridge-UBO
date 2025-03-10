package com.ubo.tp.message.components.message.controller;

import com.ubo.tp.message.common.ui.ToastNotification;
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

    // S'inscrire comme observateur de la base de données
    database.addObserver(this);
    this.mEntityManager = entityManager;
  }

  @Override
  public void sendMessage(String text) {
    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      // Utiliser null au lieu de messageView comme parent
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
      // L'ajout en base déclenche notifyMessageAdded
      mEntityManager.writeMessageFile(newMessage);
      messageView.clearMessageInput();
    } catch (MessageValidationException e) {
      // Utiliser null au lieu de messageView comme parent
      JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur de validation", JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void searchMessages(String query) {
    List<Message> allMessages = model.getMessages();
    List<Message> results;
    if (query == null || query.trim().isEmpty()) {
      results = allMessages;
    } else {
      String trimmedQuery = query.trim();
      if (trimmedQuery.contains("@") && !trimmedQuery.contains("#")) {
        String userTag = trimmedQuery.replace("@", "");
        results = allMessages.stream()
          .filter(m -> m.getSender().getUserTag().equalsIgnoreCase(userTag)
                       || m.containsUserTag(userTag))
          .collect(Collectors.toList());
      } else if (trimmedQuery.contains("#") && !trimmedQuery.contains("@")) {
        String tag = trimmedQuery.replace("#", "");
        results = allMessages.stream()
          .filter(m -> m.containsTag(tag))
          .collect(Collectors.toList());
      } else {
        String cleanedQuery = trimmedQuery.replace("@", "").replace("#", "");
        results = allMessages.stream()
          .filter(m -> m.getSender().getUserTag().equalsIgnoreCase(cleanedQuery)
                       || m.containsUserTag(cleanedQuery)
                       || m.containsTag(cleanedQuery))
          .collect(Collectors.toList());
      }
    }
    messageView.updateSearchResults(results);
  }

  // Cette méthode est appelée par la base via l'observer.
  @Override
  public void notifyMessageAdded(Message addedMessage) {
    SwingUtilities.invokeLater(() -> {
      User connectedUser = session.getConnectedUser();
      if (connectedUser != null
          && !addedMessage.getSender().equals(connectedUser)
          && connectedUser.getFollows().contains(addedMessage.getSender().getUserTag())) {
        // Affichage du toast
        ToastNotification.showToast("Nouvelle publication de " + addedMessage.getSender().getUserTag(), 3000);
      }
      // Ajout dans le modèle, ce qui déclenchera la mise à jour de la vue
      model.addMessage(addedMessage);
    });
  }

  @Override
  public void notifyMessageDeleted(Message deletedMessage) {
    SwingUtilities.invokeLater(() -> model.removeMessage(deletedMessage));
  }

  @Override
  public void notifyMessageModified(Message modifiedMessage) {
    // À implémenter si nécessaire
  }

  @Override
  public void notifyUserAdded(User addedUser) {
    // Optionnel
  }

  @Override
  public void notifyUserDeleted(User deletedUser) {
    // Optionnel
  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    // Optionnel
  }

  @Override
  public void onMessageSent(Message message) {
    // Cette méthode n'est plus utilisée directement dans cette architecture.
  }
}
