package com.ubo.tp.message.components;

/**
 * Interface de base pour tous les composants de l'application.
 */
public interface IComponent<T> {

  /**
   * Récupère le composant Swing principal.
   *
   * @return Le composant Swing qui représente ce composant.
   */
  T getComponent();

  /**
   * Initialise le composant.
   */
  void initialize();

  /**
   * Réinitialise l'état du composant.
   */
  void reset();

  /**
   * Active ou désactive le composant.
   * @param enabled true pour activer, false pour désactiver.
   */
  void setEnabled(boolean enabled);
}