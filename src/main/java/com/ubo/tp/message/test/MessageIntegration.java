package com.ubo.tp.message.test;

import com.ubo.tp.message.components.message.controller.MessageController;
import com.ubo.tp.message.components.message.service.IMessageService;
import com.ubo.tp.message.components.message.service.MessageService;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.components.message.view.MessagePanel;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

/**
 * Classe d'intégration de la partie message dans l'application.
 * Cette classe instancie le service, la vue et le contrôleur, attache les listeners,
 * et fournit un panneau pouvant être intégré dans l'IHM principale.
 */
public class MessageIntegration {

  private IMessageService messageService;
  private MessagePanel messagePanel;
  private MessageController messageController;

  /**
   * Construit l'intégration de la partie message.
   *
   * @param database l'instance de la base de données
   * @param session  la session active
   */
  public MessageIntegration(IDatabase database, ISession session) {
    // Instanciation du service de message en lui injectant la base de données existante
    messageService = new MessageService(database);

    // Instanciation de la vue message
    messagePanel = new MessagePanel();

    // Instanciation du contrôleur qui orchestre les actions entre la vue et le service
    messageController = new MessageController(messageService, session, messagePanel);

    // Attacher les listeners de la vue aux méthodes du contrôleur
    attachListeners();
  }

  /**
   * Attache les écouteurs d'action de la vue aux méthodes correspondantes du contrôleur.
   */
  private void attachListeners() {
    // Listener pour envoyer un message
    ActionListener sendAction = e -> {
      // Récupérer le texte du message depuis la zone de saisie (méthode ajoutée dans MessagePanel)
      String text = messagePanel.getMessageText();
      messageController.sendMessage(text);
    };
    messagePanel.getSendButton().addActionListener(sendAction);

    // Listener pour effectuer une recherche
    ActionListener searchAction = e -> {
      String query = messagePanel.getSearchField().getText();
      messageController.searchMessages(query);
    };
    messagePanel.getSearchButton().addActionListener(searchAction);
  }

  /**
   * Retourne le panneau de message, destiné à être intégré dans une fenêtre principale.
   *
   * @return le MessagePanel
   */
  public JPanel getMessagePanel() {
    return messagePanel;
  }

  /**
   * Méthode main de test de l'intégration.
   * Pour tester, on crée une fenêtre simple et on y intègre la partie message.
   */
  public static void main(String[] args) {
    // Pour le test, on crée une instance de base de données et de session.
    // Dans votre application, ces instances sont déjà présentes.
    IDatabase database = new com.ubo.tp.message.core.database.Database();
    ISession session = new com.ubo.tp.message.core.session.Session();

    // Connecter un utilisateur fictif afin de pouvoir envoyer des messages
    User testUser = new User(
      java.util.UUID.randomUUID(), "TestUser", "password", "Test User", new java.util.HashSet<>(), ""
    );
    session.connect(testUser);

    // Instanciation de l'intégration message
    MessageIntegration integration = new MessageIntegration(database, session);

    // Création d'une fenêtre pour afficher l'interface message
    JFrame frame = new JFrame("Intégration de la Partie Message");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.add(integration.getMessagePanel(), BorderLayout.CENTER);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
