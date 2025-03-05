package com.ubo.tp.message.components.user.auth;

import com.ubo.tp.message.ihm.IComponent;

import java.awt.event.ActionListener;

/**
 * Interface pour les composants d'authentification.
 * Cette interface étend l'interface de base IComponent avec des
 * fonctionnalités spécifiques à l'authentification.
 */
public interface IAuthComponent extends IComponent {

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
   * Définit un écouteur pour les événements de changement d'écran d'authentification.
   * @param listener L'écouteur à définir.
   */
  void setAuthSwitchListener(ActionListener listener);
}