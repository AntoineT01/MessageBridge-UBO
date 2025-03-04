package com.ubo.tp.message.ihm.login;

import com.ubo.tp.message.common.ImageUtils;
import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog implements ISessionObserver {
  protected final ISession session;
  protected final LoginController loginController;

  public LoginDialog(JFrame parent, IDatabase database, ISession session, EntityManager entityManager) {
    super(parent, "Connexion", true);
    this.session = session;

    // S'abonner aux événements de session
    session.addObserver(this);

    // Configuration de la fenêtre
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setSize(400, 500);
    this.setLocationRelativeTo(parent);
    this.setResizable(false);

    // Définir l'icône de la fenêtre
    ImageIcon windowIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 32, 32);
    if (windowIcon != null) {
      this.setIconImage(windowIcon.getImage());
    }

    // Création du contrôleur et récupération du panneau principal
    loginController = new LoginController(database, session, entityManager, this);
    JPanel mainPanel = loginController.getCardPanel();

    // Ajouter au contenu de la fenêtre
    this.setContentPane(mainPanel);
  }

  @Override
  public void notifyLogin(User connectedUser) {
    // Fermeture de la fenêtre lorsqu'un utilisateur se connecte
    this.dispose();
  }

  @Override
  public void notifyLogout() {
    // Réaffichage de la fenêtre lorsqu'un utilisateur se déconnecte
    this.setVisible(true);
  }
}