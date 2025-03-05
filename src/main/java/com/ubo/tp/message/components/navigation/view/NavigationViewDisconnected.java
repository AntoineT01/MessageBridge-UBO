package com.ubo.tp.message.components.navigation.view;

import com.ubo.tp.message.common.ui.IconFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vue de la barre de navigation pour un utilisateur déconnecté.
 * Version modifiée pour utiliser les JMenus.
 */
public class NavigationViewDisconnected extends JPanel {

  /**
   * Barre de menu
   */
  private JMenuBar menuBar;

  /**
   * Menu Fichier
   */
  private JMenu fileMenu;

  /**
   * Menu Connexion
   */
  private JMenu connectionMenu;

  /**
   * Menu Aide
   */
  private JMenu helpMenu;

  /**
   * Items de menu
   */
  private JMenuItem loginMenuItem;
  private JMenuItem registerMenuItem;
  private JMenuItem changeDirectoryMenuItem;
  private JMenuItem aboutMenuItem;

  /**
   * Constructeur
   */
  public NavigationViewDisconnected() {
    initGUI();
  }

  /**
   * Initialisation de l'interface graphique
   */
  private void initGUI() {
    // Utiliser un BorderLayout pour le panneau principal
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(5, 0, 5, 0));

    // Créer la barre de menu
    menuBar = new JMenuBar();

    // 1) Menu Fichier
    fileMenu = new JMenu("Fichier");
    fileMenu.setToolTipText("Opérations sur les fichiers");

    changeDirectoryMenuItem = new JMenuItem("Changer le répertoire d'échange");
    changeDirectoryMenuItem.setToolTipText("Sélectionner un nouveau répertoire d'échange pour les messages");
    fileMenu.add(changeDirectoryMenuItem);
    fileMenu.addSeparator();

    JMenuItem exitMenuItem = new JMenuItem("Quitter");
    exitMenuItem.setIcon(IconFactory.createCloseIcon(IconFactory.ICON_SMALL));
    exitMenuItem.setToolTipText("Fermer l'application");
    fileMenu.add(exitMenuItem);

    // 2) Menu Connexion
    connectionMenu = new JMenu("Connexion");
    connectionMenu.setToolTipText("Options de connexion");

    loginMenuItem = new JMenuItem("Se connecter");
    loginMenuItem.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    loginMenuItem.setToolTipText("Se connecter à l'application");
    connectionMenu.add(loginMenuItem);

    registerMenuItem = new JMenuItem("S'inscrire");
    registerMenuItem.setToolTipText("Créer un nouveau compte");
    connectionMenu.add(registerMenuItem);

    // 3) Menu A propos
    helpMenu = new JMenu("?");
    helpMenu.setToolTipText("Aide et informations");

    aboutMenuItem = new JMenuItem("A propos");
    aboutMenuItem.setIcon(IconFactory.createInfoIcon(IconFactory.ICON_SMALL));
    aboutMenuItem.setToolTipText("Informations sur l'application");
    helpMenu.add(aboutMenuItem);

    // Ajout des menus à la barre
    menuBar.add(fileMenu);
    menuBar.add(connectionMenu);
    menuBar.add(helpMenu);

    // Ajouter la barre de menu au panneau
    add(menuBar, BorderLayout.CENTER);

    // Ajouter un message de bienvenue
    JLabel welcomeLabel = new JLabel("Bienvenue dans MessageApp - Veuillez vous connecter ou vous inscrire");
    welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
    add(welcomeLabel, BorderLayout.EAST);
  }

  /**
   * Définit l'écouteur pour le bouton de connexion.
   */
  public void setLoginButtonListener(ActionListener listener) {
    loginMenuItem.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton d'inscription.
   */
  public void setRegisterButtonListener(ActionListener listener) {
    registerMenuItem.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton "À propos".
   */
  public void setAboutButtonListener(ActionListener listener) {
    aboutMenuItem.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton de changement de répertoire.
   */
  public void setChangeDirectoryButtonListener(ActionListener listener) {
    changeDirectoryMenuItem.addActionListener(listener);
  }

  /**
   * Obtient la barre de menu
   */
  public JMenuBar getMenuBar() {
    return menuBar;
  }
}