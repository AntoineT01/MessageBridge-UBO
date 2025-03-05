package com.ubo.tp.message.components.user.auth;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.SessionManager;
import com.ubo.tp.message.ihm.auth.controller.LoginController;
import com.ubo.tp.message.ihm.auth.model.LoginModel;
import com.ubo.tp.message.ihm.auth.view.LoginView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Implémentation du composant d'authentification.
 * Cette classe utilise le pattern MVC pour gérer l'authentification.
 */
public class AuthComponent implements IAuthComponent {

  /**
   * Panneau principal.
   */
  protected final JPanel mainPanel;

  /**
   * Vue de connexion.
   */
  protected final LoginView view;

  /**
   * Contrôleur de connexion.
   */
  protected final LoginController controller;

  /**
   * Modèle de connexion.
   */
  protected final LoginModel model;

  /**
   * Constructeur.
   * @param database La base de données.
   * @param sessionManager Le gestionnaire de session.
   */
  public AuthComponent(IDatabase database, SessionManager sessionManager) {
    this.mainPanel = new JPanel(new BorderLayout());

    // Création des composants MVC
    this.model = new LoginModel(database, sessionManager);
    this.view = new LoginView();
    this.controller = new LoginController(view, model);

    // Ajout de la vue au panneau principal
    this.mainPanel.add(view, BorderLayout.CENTER);

    initialize();
  }

  @Override
  public JComponent getComponent() {
    return mainPanel;
  }

  @Override
  public void initialize() {
    // Initialisation déjà faite dans le contrôleur
  }

  @Override
  public void reset() {
    controller.reset();
  }

  @Override
  public void setEnabled(boolean enabled) {
    controller.setEnabled(enabled);
    mainPanel.setEnabled(enabled);
  }

  @Override
  public void showError(String message) {
    controller.showError(message);
  }

  @Override
  public void setAuthSuccessListener(ActionListener listener) {
    controller.setLoginSuccessListener(listener);
  }

  @Override
  public void setAuthSwitchListener(ActionListener listener) {
    controller.setRegisterScreenListener(listener);
  }

  /**
   * Récupère le bouton d'inscription (pour la compatibilité avec le code existant).
   */
  public JButton getRegisterButton() {
    return view.getRegisterButton();
  }
}