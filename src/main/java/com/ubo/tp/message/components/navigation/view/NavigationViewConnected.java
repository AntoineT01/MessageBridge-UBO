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

/**
 * Vue de la barre de navigation pour un utilisateur connecté.
 * Version modifiée pour utiliser les JMenus.
 */
public class NavigationViewConnected extends JPanel implements INavigationViewConnected {

  /**
   * Barre de menu
   */
  private JMenuBar menuBar;

  /**
   * Items de menu
   */
  private JMenuItem profileMenuItem;
  private JMenuItem messagesMenuItem;
  private JMenuItem searchMenuItem;
  private JMenuItem logoutMenuItem;
  private JMenuItem aboutMenuItem;
  private JMenuItem exitMenuItem;

  /**
   * Zone d'information utilisateur
   */
  private JLabel userInfoLabel;

  /**
   * Contrôleur de répertoire
   */
  private DirectoryController directoryController;

  /**
   * Frame parente
   */
  private JFrame parentFrame;

  /**
   * Constructeur
   */
  public NavigationViewConnected() {
    initGUI();
  }

  /**
   * Initialisation de l'interface graphique
   */
  private void initGUI() {
    JMenuItem changeDirectoryMenuItem;
    JMenu helpMenu;
    JMenu userMenu;
    JMenu fileMenu;
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

    exitMenuItem = new JMenuItem("Quitter");
    exitMenuItem.setIcon(IconFactory.createCloseIcon(IconFactory.ICON_SMALL));
    exitMenuItem.setToolTipText("Fermer l'application");
    fileMenu.add(exitMenuItem);

    // 2) Menu Utilisateur
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

    logoutMenuItem = new JMenuItem("Se déconnecter");
    logoutMenuItem.setToolTipText("Se déconnecter de l'application");
    userMenu.add(logoutMenuItem);

    // 3) Menu A propos
    helpMenu = new JMenu("?");
    helpMenu.setToolTipText("Aide et informations");

    aboutMenuItem = new JMenuItem("A propos");
    aboutMenuItem.setIcon(IconFactory.createInfoIcon(IconFactory.ICON_SMALL));
    aboutMenuItem.setToolTipText("Informations sur l'application");
    helpMenu.add(aboutMenuItem);

    // Ajout des menus à la barre
    menuBar.add(fileMenu);
    menuBar.add(userMenu);
    menuBar.add(helpMenu);

    // Création du panel d'information utilisateur (à droite)
    JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    userInfoLabel = new JLabel("");
    userInfoLabel.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    userInfoPanel.add(userInfoLabel);

    // Ajout des composants au panneau principal
    add(menuBar, BorderLayout.CENTER);
    add(userInfoPanel, BorderLayout.EAST);

    // Action par défaut pour le changement de répertoire
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
   * Met à jour les informations de l'utilisateur.
   */
  public void updateUserInfo(String name, String tag) {
    userInfoLabel.setText(name + " (" + tag + ")");
  }

  /**
   * Définit l'écouteur pour le bouton de profil.
   */
  public void setProfileButtonListener(ActionListener listener) {
    profileMenuItem.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton de messages.
   */
  public void setMessagesButtonListener(ActionListener listener) {
    messagesMenuItem.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton de recherche.
   */
  public void setSearchButtonListener(ActionListener listener) {
    searchMenuItem.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton de déconnexion.
   */
  public void setLogoutButtonListener(ActionListener listener) {
    logoutMenuItem.addActionListener(listener);
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
    // Nous gérons maintenant directement cette action
  }

  /**
   * Définit le contrôleur de répertoire
   */
  public void setDirectoryController(DirectoryController directoryController) {
    this.directoryController = directoryController;
  }

  /**
   * Définit l'écouteur pour le bouton de quitter.
   */
  public void setExitButtonListener(ActionListener listener) {
    exitMenuItem.addActionListener(listener);
  }

  /**
   * Définit la frame parente
   */
  public void setParentFrame(JFrame parentFrame) {
    this.parentFrame = parentFrame;
  }

  /**
   * Obtient la barre de menu
   */
  public JMenuBar getMenuBar() {
    return menuBar;
  }
}