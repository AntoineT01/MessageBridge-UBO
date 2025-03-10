package com.ubo.tp.message.components.user.auth;

import com.ubo.tp.message.components.IComponent;

import java.awt.Component;
import java.awt.event.ActionListener;

/**
 * Interface pour les composants d'authentification.
 */
public interface IAuthComponent<T extends Component> extends IComponent<T> {

  /**
   * Affiche un message d'erreur dans le composant.
   * @param message Le message d'erreur à afficher.
   */
  void showError(String message);

  /**
   * Définit un écouteur pour les événements d'authentification réussie.
   * @param listener L'écouteur à définir.
   */
  void setAuthSuccessListener(ActionListener listener);

  /**
   * Affiche la vue de connexion.
   */
  void showLoginView();

  /**
   * Affiche la vue d'inscription.
   */
  void showRegisterView();
}