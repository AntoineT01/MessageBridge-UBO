package com.ubo.tp.message.ihm.message;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.service.IMessageService;

import javax.swing.JPanel;
import java.awt.event.ActionListener;

/**
 * Classe d'intégration de la partie message dans l'application.
 * Cette classe instancie le service, la vue et le contrôleur, attache les listeners,
 * et fournit un panneau pouvant être intégré dans l'IHM principale.
 */
public class MessageIntegration {

  private ModernChatPanel messagePanel = null;
  private MessageController messageController = null;

  // Nouveau constructeur utilisant un MessageService partagé
  public MessageIntegration(IDatabase database, ISession session, IMessageService sharedMessageService) {
    // Préparation de l'action "envoyer"
    ActionListener sendAction = e -> {
      String text = messagePanel.getMessageText();
      messageController.sendMessage(text);
    };

    // Préparation de l'action "rechercher"
    ActionListener searchAction = e -> {
      String query = messagePanel.getSearchQuery();
      messageController.searchMessages(query);
    };

    // Instanciation de la vue moderne
    messagePanel = new ModernChatPanel(session, sendAction, searchAction);

    // Utilisation du MessageService partagé pour instancier le contrôleur
    messageController = new MessageController(sharedMessageService, session, messagePanel);
  }

  public JPanel getMessagePanel() {
    return messagePanel;
  }
}
