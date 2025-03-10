package com.ubo.tp.message.components.navigation.view;

import com.ubo.tp.message.components.directory.controller.DirectoryController;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.event.ActionListener;

/**
 * Interface pour la vue de navigation lorsque l'utilisateur est connecté.
 */
public interface INavigationViewConnected {

  /**
   * Met à jour les informations de l'utilisateur.
   */
  void updateUserInfo(String name, String tag);

  /**
   * Définit l'écouteur pour le bouton de profil.
   */
  void setProfileButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton de messages.
   */
  void setMessagesButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton de recherche.
   */
  void setSearchButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton de déconnexion.
   */
  void setLogoutButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton "À propos".
   */
  void setAboutButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton de changement de répertoire.
   */
  void setChangeDirectoryButtonListener(ActionListener listener);

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