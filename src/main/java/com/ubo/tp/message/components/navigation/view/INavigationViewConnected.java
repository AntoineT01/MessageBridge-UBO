package com.ubo.tp.message.components.navigation.view;

import com.ubo.tp.message.components.directory.controller.DirectoryController;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.event.ActionListener;


public interface INavigationViewConnected {

  /**
   * Mettre à jour les informations d'utilisateur
   */
  void updateUserInfo(String name, String tag);

  /**
   * Définir l'écouteur du bouton de profil
   */
  void setProfileButtonListener(ActionListener listener);

  /**
   * Définir l'écouteur du bouton de messages
   */
  void setMessagesButtonListener(ActionListener listener);

  /**
   * Définir l'écouteur du bouton de recherche
   */
  void setSearchButtonListener(ActionListener listener);

  /**
   * Définir l'écouteur du bouton de déconnexion
   */
  void setLogoutButtonListener(ActionListener listener);

  /**
   * Définir l'écouteur du bouton À propos
   */
  void setAboutButtonListener(ActionListener listener);

  /**
   * Définir l'écouteur du bouton de changement de répertoire
   */
  void setChangeDirectoryButtonListener(ActionListener listener);

  /**
   * Définir l'écouteur du bouton de paramètres
   */
  void setSettingsButtonListener(ActionListener listener);

  /**
   * Définir le contrôleur de répertoire
   */
  void setDirectoryController(DirectoryController directoryController);

  /**
   * Définir l'écouteur du bouton de sortie
   */
  void setExitButtonListener(ActionListener listener);

  /**
   * Définir la frame parent
   */
  void setParentFrame(JFrame parentFrame);

  /**
   * Obtenir la barre de menu
   */
  JMenuBar getMenuBar();

  /**
   * Activer/désactiver les composants
   */
  void setEnabled(boolean enabled);
}