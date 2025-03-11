package com.ubo.tp.message.components.navigation.view;

import com.ubo.tp.message.components.directory.controller.DirectoryController;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.event.ActionListener;

/**
 * Interface pour la vue de navigation lorsque l'utilisateur est déconnecté.
 */
public interface INavigationViewDisconnected {

  /**
   * Définit l'écouteur pour le bouton de connexion.
   */
  void setLoginButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton d'inscription.
   */
  void setRegisterButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton "À propos".
   */
  void setAboutButtonListener(ActionListener listener);

  /**
   * Définit le contrôleur de répertoire
   */
  void setDirectoryController(DirectoryController directoryController);

  /**
   * Définit l'écouteur pour le bouton de quitter.
   */
  void setExitButtonListener(ActionListener listener);

  /**
   * Définit la frame parente
   */
  void setParentFrame(JFrame parentFrame);

  /**
   * Obtient la barre de menu
   */
  JMenuBar getMenuBar();

  /**
   * Active ou désactive la vue.
   */
  void setEnabled(boolean enabled);
}