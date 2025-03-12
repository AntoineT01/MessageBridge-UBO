package com.ubo.tp.message.components.navigation.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.components.directory.controller.DirectoryController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;


public class NavigationViewConnected extends JPanel implements INavigationViewConnected {

  // Menu principal
  private JMenuBar menuBar;

  // Éléments de menu
  private JMenuItem profileMenuItem;
  private JMenuItem messagesMenuItem;
  private JMenuItem searchMenuItem;
  private JMenuItem settingsMenuItem; // Nouveau élément de menu pour les paramètres
  private JMenuItem logoutMenuItem;
  private JMenuItem aboutMenuItem;
  private JMenuItem exitMenuItem;

  // Informations utilisateur
  private JLabel userInfoLabel;

  // Contrôleur de répertoire
  private DirectoryController directoryController;

  // Frame parent
  private JFrame parentFrame;

  /**
   * Constructeur
   */
  public NavigationViewConnected() {
    initGUI();
  }

  /**
   * Initialiser l'interface graphique
   */
  private void initGUI() {
    JMenuItem changeDirectoryMenuItem;
    JMenu helpMenu;
    JMenu userMenu;
    JMenu fileMenu;
    // Définir la disposition
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(5, 0, 5, 0));

    // Créer la barre de menu
    menuBar = new JMenuBar();

    // Menu Fichier
    fileMenu = new JMenu("Fichier");
    fileMenu.setToolTipText("Opérations sur les fichiers");

    changeDirectoryMenuItem = new JMenuItem("Changer le répertoire d'échange");
    changeDirectoryMenuItem.setToolTipText("Sélectionner un nouveau répertoire d'échange pour les messages");
    fileMenu.add(changeDirectoryMenuItem);
    fileMenu.addSeparator();

    exitMenuItem = new JMenuItem("Quitter");
    exitMenuItem.setIcon(IconFactory.createCloseIcon(IconFactory.ICON_SMALL));
    exitMenuItem.setToolTipText("Fermer l'application");
    fileMenu.add(exitMenuItem);

    // Menu Utilisateur
    userMenu = new JMenu("Utilisateur");
    userMenu.setToolTipText("Actions liées à l'utilisateur");

    profileMenuItem = new JMenuItem("Mon profil");
    profileMenuItem.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    profileMenuItem.setToolTipText("Afficher mon profil");
    userMenu.add(profileMenuItem);

    messagesMenuItem = new JMenuItem("Messages");
    messagesMenuItem.setIcon(IconFactory.createMessageIcon(IconFactory.ICON_SMALL));
    messagesMenuItem.setToolTipText("Afficher les messages");
    userMenu.add(messagesMenuItem);

    searchMenuItem = new JMenuItem("Rechercher des utilisateurs");
    searchMenuItem.setToolTipText("Rechercher et consulter les profils des utilisateurs");
    userMenu.add(searchMenuItem);

    userMenu.addSeparator();

    // Ajout du menu Paramètres
    settingsMenuItem = new JMenuItem("Paramètres");
    settingsMenuItem.setToolTipText("Configurer les paramètres de l'application");
    userMenu.add(settingsMenuItem);

    userMenu.addSeparator();

    logoutMenuItem = new JMenuItem("Se déconnecter");
    logoutMenuItem.setToolTipText("Se déconnecter de l'application");
    userMenu.add(logoutMenuItem);

    // Menu Aide
    helpMenu = new JMenu("?");
    helpMenu.setToolTipText("Aide et informations");

    aboutMenuItem = new JMenuItem("A propos");
    aboutMenuItem.setIcon(IconFactory.createInfoIcon(IconFactory.ICON_SMALL));
    aboutMenuItem.setToolTipText("Informations sur l'application");
    helpMenu.add(aboutMenuItem);

    // Ajouter les menus à la barre
    menuBar.add(fileMenu);
    menuBar.add(userMenu);
    menuBar.add(helpMenu);

    // Panel d'informations utilisateur
    JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    userInfoLabel = new JLabel("");
    userInfoLabel.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    userInfoPanel.add(userInfoLabel);

    // Ajouter les éléments au panel
    add(menuBar, BorderLayout.CENTER);
    add(userInfoPanel, BorderLayout.EAST);

    // Configurer l'action sur le répertoire d'échange
    changeDirectoryMenuItem.addActionListener(e -> {
      if (directoryController != null && parentFrame != null) {
        boolean success = directoryController.selectAndChangeExchangeDirectory(parentFrame);
        if (success) {
          JOptionPane.showMessageDialog(parentFrame,
                                        "Le répertoire d'échange a été changé avec succès.",
                                        "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(parentFrame,
                                        "Impossible de changer le répertoire d'échange.",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }

  /**
   * Mettre à jour les informations d'utilisateur
   */
  public void updateUserInfo(String name, String tag) {
    userInfoLabel.setText(name + " (" + tag + ")");
  }

  /**
   * Définir l'écouteur pour le bouton de profil
   */
  public void setProfileButtonListener(ActionListener listener) {
    profileMenuItem.addActionListener(listener);
  }

  /**
   * Définir l'écouteur pour le bouton de messages
   */
  public void setMessagesButtonListener(ActionListener listener) {
    messagesMenuItem.addActionListener(listener);
  }

  /**
   * Définir l'écouteur pour le bouton de recherche
   */
  public void setSearchButtonListener(ActionListener listener) {
    searchMenuItem.addActionListener(listener);
  }

  /**
   * Définir l'écouteur pour le bouton de déconnexion
   */
  public void setLogoutButtonListener(ActionListener listener) {
    logoutMenuItem.addActionListener(listener);
  }

  /**
   * Définir l'écouteur pour le bouton "À propos"
   */
  public void setAboutButtonListener(ActionListener listener) {
    aboutMenuItem.addActionListener(listener);
  }

  /**
   * Définir l'écouteur pour le bouton de changement de répertoire
   */
  public void setChangeDirectoryButtonListener(ActionListener listener) {
    // Méthode vide, l'action est déjà configurée
  }

  /**
   * Définir l'écouteur pour le bouton de paramètres
   */
  public void setSettingsButtonListener(ActionListener listener) {
    settingsMenuItem.addActionListener(listener);
  }

  /**
   * Définir le contrôleur de répertoire
   */
  public void setDirectoryController(DirectoryController directoryController) {
    this.directoryController = directoryController;
  }

  /**
   * Définir l'écouteur pour le bouton de sortie
   */
  public void setExitButtonListener(ActionListener listener) {
    exitMenuItem.addActionListener(listener);
  }

  /**
   * Définir la frame parent
   */
  public void setParentFrame(JFrame parentFrame) {
    this.parentFrame = parentFrame;
  }

  /**
   * Obtenir la barre de menu
   */
  public JMenuBar getMenuBar() {
    return menuBar;
  }
}