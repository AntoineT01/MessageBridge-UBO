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
  private final MessagePanel messagePanel;

  /**
   * Construit un MessageController avec le service de message, la session et la vue associée.
   *
   * @param messageService le service de gestion des messages
   * @param session la session active
   * @param messagePanel la vue pour l’affichage et l’envoi des messages
   */
  public MessageController(IMessageService messageService, ISession session, MessagePanel messagePanel) {
    this.messageService = messageService;
    this.session = session;
    this.messagePanel = messagePanel;
    // Enregistrement comme observateur pour recevoir les notifications de nouveaux messages
    this.messageService.addObserver(this);
  }

  /**
   * Traite l’envoi d’un message.
   *
   * @param text le texte du message
   */
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

  /**
   * Traite la recherche de messages en fonction d’une requête.
   *
   * @param query la requête de recherche
   */
  public void searchMessages(String query) {
    Set<Message> results = messageService.searchMessages(query);
    messagePanel.updateSearchResults(results);
  }

  @Override
  public void onMessageSent(Message message) {
    // Si le message émane d’un utilisateur différent et que l’utilisateur connecté s’est abonné à cet utilisateur,
    // une notification est affichée.
    User connectedUser = session.getConnectedUser();
    if (connectedUser != null && !message.getSender().equals(connectedUser)
        && connectedUser.getFollows().contains(message.getSender().getUserTag())) {
      JOptionPane.showMessageDialog(messagePanel,
        "Nouvelle publication de " + message.getSender().getUserTag(),
        "Notification", JOptionPane.INFORMATION_MESSAGE);
    }
    // Mise à jour de la vue avec le nouveau message
    messagePanel.addMessageToFeed(message);
  }
}
