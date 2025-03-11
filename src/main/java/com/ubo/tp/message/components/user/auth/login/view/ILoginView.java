package com.ubo.tp.message.components.user.auth.login.view;

import java.awt.event.ActionListener;

/**
 * Interface pour la vue de connexion.
 */
public interface ILoginView {

  /**
   * Définit l'écouteur d'événements pour le bouton de connexion.
   */
  void setLoginButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur d'événements pour le bouton d'inscription.
   */
  void setRegisterButtonListener(ActionListener listener);

  /**
   * Récupère le tag utilisateur saisi.
   */
  String getUserTag();

  /**
   * Récupère le mot de passe saisi.
   */
  String getUserPassword();

  /**
   * Affiche un message d'erreur.
   */
  void setErrorMessage(String message);

  /**
   * Réinitialise les champs de formulaire.
   */
  void resetFields();

  /**
   * Active ou désactive tous les composants de la vue.
   */
  void setEnabled(boolean enabled);
}