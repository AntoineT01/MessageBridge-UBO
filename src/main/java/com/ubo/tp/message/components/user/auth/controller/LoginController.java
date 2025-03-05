package com.ubo.tp.message.ihm.auth.controller;

import com.ubo.tp.message.ihm.auth.model.LoginModel;
import com.ubo.tp.message.ihm.auth.view.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contrôleur pour la gestion de la connexion
 */
public class LoginController {
  /**
   * Vue associée
   */
  private final LoginView view;

  /**
   * Modèle associé
   */
  private final LoginModel model;

  /**
   * Écouteur pour le changement vers l'écran d'inscription
   */
  private ActionListener registerScreenListener;

  /**
   * Écouteur pour les événements de connexion réussie
   */
  private ActionListener loginSuccessListener;

  /**
   * Constructeur
   */
  public LoginController(LoginView view, LoginModel model) {
    this.view = view;
    this.model = model;

    // Initialiser les écouteurs d'événements
    this.initEventListeners();
  }

  /**
   * Initialise les écouteurs d'événements
   */
  private void initEventListeners() {
    // Écouteur pour le bouton de connexion
    view.setLoginButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        attemptLogin();
      }
    });
  }

  /**
   * Tente une connexion avec les identifiants saisis
   */
  private void attemptLogin() {
    // Récupérer les valeurs des champs
    String userTag = view.getUserTag();
    String password = view.getUserPassword();

    // Tenter l'authentification
    LoginModel.LoginResult result = model.authenticate(userTag, password);

    if (result.isSuccess()) {
      // Connexion réussie
      model.login(result.getUser());
      view.resetFields();

      // Notifier les écouteurs de connexion réussie
      if (loginSuccessListener != null) {
        loginSuccessListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "login_success"));
      }
    } else {
      // Afficher le message d'erreur
      view.setErrorMessage(result.getErrorMessage());
    }
  }

  /**
   * Définit l'écouteur pour le changement vers l'écran d'inscription
   */
  public void setRegisterScreenListener(ActionListener listener) {
    this.registerScreenListener = listener;
    view.setRegisterButtonListener(listener);
  }

  /**
   * Définit l'écouteur pour les événements de connexion réussie
   */
  public void setLoginSuccessListener(ActionListener listener) {
    this.loginSuccessListener = listener;
  }

  /**
   * Réinitialise le contrôleur et la vue
   */
  public void reset() {
    view.resetFields();
  }

  /**
   * Affiche un message d'erreur dans la vue
   */
  public void showError(String message) {
    view.setErrorMessage(message);
  }

  /**
   * Active ou désactive la vue
   */
  public void setEnabled(boolean enabled) {
    view.setEnabled(enabled);
  }
}