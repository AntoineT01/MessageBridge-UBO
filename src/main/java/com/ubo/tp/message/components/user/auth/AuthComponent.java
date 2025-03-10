package com.ubo.tp.message.components.user.auth;

import com.ubo.tp.message.components.user.auth.login.controller.LoginController;
import com.ubo.tp.message.components.user.auth.login.model.LoginModel;
import com.ubo.tp.message.components.user.auth.login.view.LoginView;
import com.ubo.tp.message.components.user.auth.register.controller.RegisterController;
import com.ubo.tp.message.components.user.auth.register.model.RegisterModel;
import com.ubo.tp.message.components.user.auth.register.view.RegisterView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

/**
 * Implémentation du composant d'authentification.
 * Cette classe utilise le pattern MVC pour gérer l'authentification, incluant
 * à la fois la connexion et l'inscription.
 */
public class AuthComponent implements IAuthComponent {

  /**
   * Panneau principal.
   */
  protected final JPanel mainPanel;

  /**
   * Panneau de connexion.
   */
  protected final JPanel loginPanel;

  /**
   * Panneau d'inscription.
   */
  protected final JPanel registerPanel;

  /**
   * Vue de connexion.
   */
  protected final LoginView loginView;

  /**
   * Contrôleur de connexion.
   */
  protected final LoginController loginController;

  /**
   * Modèle de connexion.
   */
  protected final LoginModel loginModel;

  /**
   * Vue d'inscription.
   */
  protected final RegisterView registerView;

  /**
   * Contrôleur d'inscription.
   */
  protected final RegisterController registerController;

  /**
   * Modèle d'inscription.
   */
  protected final RegisterModel registerModel;

  /**
   * Écouteur pour les événements d'authentification réussie.
   */
  protected ActionListener authSuccessListener;

  /**
   * Constructeur.
   * @param database La base de données.
   * @param entityManager Le gestionnaire d'entités.
   * @param sessionManager Le gestionnaire de session.
   */
  public AuthComponent(IDatabase database, EntityManager entityManager, SessionManager sessionManager) {
    // Initialisation du panneau principal
    this.mainPanel = new JPanel(new BorderLayout());

    // Création des composants MVC pour la connexion
    this.loginModel = new LoginModel(database, sessionManager);
    this.loginView = new LoginView();
    this.loginController = new LoginController(loginView, loginModel);

    // Création du panel de connexion
    this.loginPanel = new JPanel(new BorderLayout());
    this.loginPanel.add(loginView, BorderLayout.CENTER);

    // Création des composants MVC pour l'inscription
    this.registerModel = new RegisterModel(database);
    this.registerView = new RegisterView(database);
    this.registerController = new RegisterController(registerView, registerModel, entityManager, sessionManager);

    // Création du panel d'inscription
    this.registerPanel = new JPanel(new BorderLayout());
    this.registerPanel.add(registerView, BorderLayout.CENTER);

    // Par défaut, on affiche la vue de connexion
    this.mainPanel.add(loginPanel, BorderLayout.CENTER);

    // Configuration des écouteurs pour basculer entre les vues
    setupSwitchListeners();

    initialize();
  }

  /**
   * Configure les écouteurs pour basculer entre les écrans.
   */
  private void setupSwitchListeners() {
    // Quand on clique sur "S'inscrire" dans l'écran de connexion, on bascule vers l'écran d'inscription
    loginController.setRegisterScreenListener(e -> showRegisterView());

    // Quand on clique sur "Se connecter" dans l'écran d'inscription, on bascule vers l'écran de connexion
    registerController.setLoginScreenListener(e -> showLoginView());
  }

  @Override
  public JComponent getComponent() {
    return mainPanel;
  }

  @Override
  public void initialize() {
    // Par défaut, on montre la vue de connexion
    showLoginView();
  }

  @Override
  public void reset() {
    loginController.reset();
    registerController.reset();
  }

  @Override
  public void setEnabled(boolean enabled) {
    loginController.setEnabled(enabled);
    registerController.setEnabled(enabled);
    mainPanel.setEnabled(enabled);
  }

  @Override
  public void showError(String message) {
    // Affiche l'erreur dans le composant actuellement visible
    Component visibleComponent = null;
    for (Component component : mainPanel.getComponents()) {
      if (component.isVisible()) {
        visibleComponent = component;
        break;
      }
    }

    if (visibleComponent == loginPanel) {
      loginController.showError(message);
    } else if (visibleComponent == registerPanel) {
      registerController.showError(message);
    }
  }

  @Override
  public void setAuthSuccessListener(ActionListener listener) {
    this.authSuccessListener = listener;
    loginController.setLoginSuccessListener(listener);
    registerController.setRegisterSuccessListener(listener);
  }

  @Override
  public void showLoginView() {
    mainPanel.removeAll();
    mainPanel.add(loginPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  @Override
  public void showRegisterView() {
    mainPanel.removeAll();
    mainPanel.add(registerPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
  }

}