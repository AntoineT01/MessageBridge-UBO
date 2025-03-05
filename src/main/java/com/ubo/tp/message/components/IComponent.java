package com.ubo.tp.message.ihm;

import javax.swing.JComponent;

/**
 * Interface de base pour tous les composants de l'application.
 */
public interface IComponent {

  /**
   * Récupère le composant Swing principal.
   * @return Le composant Swing qui représente ce composant.
   */
  JComponent getComponent();

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