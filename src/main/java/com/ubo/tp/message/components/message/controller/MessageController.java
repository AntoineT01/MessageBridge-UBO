package com.ubo.tp.message.components.message.controller;

import com.ubo.tp.message.components.message.service.IMessageService;
import com.ubo.tp.message.components.message.service.MessageObserver;
import com.ubo.tp.message.components.message.service.MessageValidationException;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.components.message.view.MessagePanel;
import com.ubo.tp.message.core.session.ISession;
import javax.swing.JOptionPane;
import java.util.Set;

/**
 * Contrôleur pour les interactions liées aux messages.
 * Gère l’envoi, la recherche et la notification des messages.
 */
public class MessageController implements MessageObserver {

  private final IMessageService messageService;
  private final ISession session;
  private final ModernChatPanel messagePanel;

  public MessageController(IMessageService messageService, ISession session, ModernChatPanel messagePanel) {
    this.messageService = messageService;
    this.session = session;
    this.messagePanel = messagePanel;
    // Enregistrement comme observateur pour recevoir les notifications de nouveaux messages
    this.messageService.addObserver(this);
  }

  public void sendMessage(String text) {
    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      JOptionPane.showMessageDialog(messagePanel, "Aucun utilisateur connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      messageService.sendMessage(connectedUser, text);
      messagePanel.clearMessageInput();
    } catch (MessageValidationException e) {
      JOptionPane.showMessageDialog(messagePanel, e.getMessage(), "Erreur de validation", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void searchMessages(String query) {
    Set<Message> results = messageService.searchMessages(query);
    messagePanel.updateSearchResults(results);
  }

  @Override
  public void onMessageSent(Message message) {
    User connectedUser = session.getConnectedUser();
    // Notification si l'utilisateur connecté ne l'est pas et qu'il suit l'expéditeur
    if (connectedUser != null && !message.getSender().equals(connectedUser)
        && connectedUser.getFollows().contains(message.getSender().getUserTag())) {
      // Affichage d'une notification toast non bloquante pendant 3 secondes (3000 ms)
      ToastNotification.showToast("Nouvelle publication de " + message.getSender().getUserTag(), 3000);
    }
    // Mise à jour de la vue avec le nouveau message
    messagePanel.addMessageToFeed(message);
  }
}
