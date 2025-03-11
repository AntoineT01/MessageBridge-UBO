package com.ubo.tp.message.components.user.auth.login.controller;

import com.ubo.tp.message.components.user.auth.login.model.LoginModel;
import com.ubo.tp.message.components.user.auth.login.view.ILoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contrôleur pour la gestion de la connexion
 */
public class LoginController {
  /**
   * Vue associée
   */
  private final ILoginView view; // Utilisation de l'interface au lieu de la classe concrète

  /**
   * Modèle associé
   */
  private final LoginModel model;

  /**
   * Écouteur pour les événements de connexion réussie
   */
  private ActionListener loginSuccessListener;

  /**
   * Constructeur
   */
  public LoginController(ILoginView view, LoginModel model) {
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
    view.setLoginButtonListener(_ -> attemptLogin());
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