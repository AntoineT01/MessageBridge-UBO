package com.ubo.tp.message.components.user.auth.register.view;

import com.ubo.tp.message.core.database.IDatabase;

import java.awt.event.ActionListener;

/**
 * Interface pour la vue d'inscription.
 */
public interface IRegisterView {

  /**
   * Définit l'écouteur d'événements pour le bouton d'inscription.
   */
  void setRegisterButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur d'événements pour le bouton de connexion.
   */
  void setLoginButtonListener(ActionListener listener);

  /**
   * Récupère le tag utilisateur saisi.
   */
  String getUserTag();

  /**
   * Récupère le nom d'utilisateur saisi.
   */
  String getUserName();

  /**
   * Récupère le mot de passe saisi.
   */
  String getUserPassword();

  /**
   * Récupère la confirmation du mot de passe.
   */
  String getConfirmPassword();

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

  /**
   * Récupère la base de données (pour le contrôleur).
   */
  IDatabase getDatabase();
}