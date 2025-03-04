package com.ubo.tp.message.ihm.login;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class LoginController {
  protected final IDatabase database;
  protected final ISession session;
  protected final EntityManager entityManager;
  protected final CardLayout cardLayout;
  protected final JPanel cardPanel;
  protected final LoginView loginView;
  protected final SignupView signupView;
  protected final Component parentComponent;

  public LoginController(IDatabase database, ISession session, EntityManager entityManager, Component parentComponent) {
    this.database = database;
    this.session = session;
    this.entityManager = entityManager;
    this.parentComponent = parentComponent;

    // Initialisation du CardLayout et des vues
    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);
    loginView = new LoginView();
    signupView = new SignupView();

    // Configuration des contrôleurs
    loginView.setController(this);
    signupView.setController(this);

    // Ajout des vues au CardLayout
    cardPanel.add(loginView, "login");
    cardPanel.add(signupView, "signup");

    // Affichage initial de la vue de login
    cardLayout.show(cardPanel, "login");
  }

  public void login(String userTag, String password) {
    if (userTag == null || userTag.trim().isEmpty() || password == null || password.isEmpty()) {
      loginView.showError("Veuillez remplir tous les champs");
      return;
    }

    // Recherche de l'utilisateur par son tag
    User foundUser = findUserByTag(userTag);

    if (foundUser != null && password.equals(foundUser.getUserPassword())) {
      // Connexion réussie
      session.connect(foundUser);
      loginView.clearFields();

      // Ferme le dialogue parent si c'est un dialogue
      if (parentComponent instanceof Window) {
        ((Window) parentComponent).setVisible(false);
      }
    } else {
      // Échec de connexion
      loginView.showError("Identifiants incorrects");
    }
  }

  private User findUserByTag(String userTag) {
    Set<User> users = database.getUsers();
    for (User user : users) {
      if (user.getUserTag().equals(userTag)) {
        return user;
      }
    }
    return null;
  }

  public void signup(User newUser) {
    // Vérification de l'unicité du tag
    if (findUserByTag(newUser.getUserTag()) != null) {
      signupView.showError("Ce tag utilisateur est déjà utilisé");
      return;
    }

    // Ajout de l'utilisateur à la base de données
    database.addUser(newUser);

    // Écriture du fichier utilisateur
    try {
      entityManager.writeUserFile(newUser);
    } catch (RuntimeException e) {
      JOptionPane.showMessageDialog(
        signupView,
        "Impossible d'écrire le fichier utilisateur : " + e.getMessage(),
        "Erreur",
        JOptionPane.ERROR_MESSAGE
      );
    }

    // Retour à la vue de login
    JOptionPane.showMessageDialog(
      signupView,
      "Compte créé avec succès !",
      "Création de compte",
      JOptionPane.INFORMATION_MESSAGE
    );

    signupView.clearFields();
    cardLayout.show(cardPanel, "login");
  }

  public void openSignupView() {
    cardLayout.show(cardPanel, "signup");
  }

  public void cancelSignup() {
    signupView.clearFields();
    cardLayout.show(cardPanel, "login");
  }

  public JPanel getCardPanel() {
    return cardPanel;
  }
}