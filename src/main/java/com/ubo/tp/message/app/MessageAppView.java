package com.ubo.tp.message.app;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.common.utils.ImageUtils;
import com.ubo.tp.message.core.database.IDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Vue principale de l'application MessageApp.
 * Remplace MessageAppMainView.java du package ihm.
 */
public class MessageAppView extends JFrame {

  /**
   * Base de données de l'application
   */
  private final IDatabase database;

  /**
   * Panneau de contenu principal
   */
  private JPanel contentPanel;

  /**
   * Menu utilisateur
   */
  private JMenu userMenu;

  /**
   * Constructeur
   */
  public MessageAppView(IDatabase database) {
    this.database = database;
    initGUI();
  }

  /**
   * Initialisation de l'interface graphique
   */
  private void initGUI() {
    // Définition du titre et de l'icône de la fenêtre
    this.setTitle("MessageApp");
    this.setSize(800, 600);

    ImageIcon windowIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 32, 32);
    if(windowIcon != null){
      this.setIconImage(windowIcon.getImage());
    }

    // --- Barre de menu ---
    JMenuBar menuBar = new JMenuBar();

    // 1) Menu Fichier
    JMenu menuFichier = new JMenu("Fichier");
    menuFichier.setToolTipText("Opérations sur les fichiers");

    // Ajout d'un item pour changer le répertoire d'échange
    JMenuItem itemChangeDir = new JMenuItem("Changer le répertoire d'échange");
    itemChangeDir.setToolTipText("Sélectionner un nouveau répertoire d'échange pour les messages");
    menuFichier.add(itemChangeDir);
    menuFichier.addSeparator();

    JMenuItem itemQuitter = new JMenuItem("Quitter");
    itemQuitter.setIcon(IconFactory.createCloseIcon(IconFactory.ICON_SMALL));
    itemQuitter.setToolTipText("Fermer l'application");
    itemQuitter.addActionListener(e -> {
      firePropertyChange("ACTION_EXIT", false, true);
    });
    menuFichier.add(itemQuitter);
    menuBar.add(menuFichier);

    // 2) Menu Utilisateur (initialement désactivé)
    userMenu = new JMenu("Utilisateur");
    userMenu.setToolTipText("Actions liées à l'utilisateur");
    userMenu.setEnabled(false);

    JMenuItem itemProfile = new JMenuItem("Mon profil");
    itemProfile.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    itemProfile.setToolTipText("Afficher mon profil");
    itemProfile.addActionListener(e -> {
      firePropertyChange("ACTION_SHOW_PROFILE", false, true);
    });
    userMenu.add(itemProfile);

    JMenuItem itemSearchUsers = new JMenuItem("Rechercher des utilisateurs");
    itemSearchUsers.setToolTipText("Rechercher et consulter les profils des utilisateurs");
    itemSearchUsers.addActionListener(e -> {
      firePropertyChange("ACTION_SEARCH_USERS", false, true);
    });
    userMenu.add(itemSearchUsers);

    userMenu.addSeparator();

    JMenuItem itemLogout = new JMenuItem("Se déconnecter");
    itemLogout.setToolTipText("Se déconnecter de l'application");
    itemLogout.addActionListener(e -> {
      firePropertyChange("ACTION_LOGOUT", false, true);
    });
    userMenu.add(itemLogout);

    menuBar.add(userMenu);

    // 3) Menu A propos
    JMenu menuAPropos = new JMenu("?");
    menuAPropos.setToolTipText("Aide et informations");

    JMenuItem itemAbout = new JMenuItem("A propos");
    itemAbout.setIcon(IconFactory.createInfoIcon(IconFactory.ICON_SMALL));
    itemAbout.setToolTipText("Informations sur l'application");
    itemAbout.addActionListener(e -> {
      showAboutDialog();
    });
    menuAPropos.add(itemAbout);
    menuBar.add(menuAPropos);

    // On ajoute la barre de menu à la fenêtre
    this.setJMenuBar(menuBar);

    // Configuration de la fermeture
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        firePropertyChange("ACTION_EXIT", false, true);
      }
    });

    // Layout principal
    this.setLayout(new BorderLayout());

    // Création du panneau de contenu
    contentPanel = new JPanel(new CardLayout());
    this.add(contentPanel, BorderLayout.CENTER);

    // Centrer la fenêtre
    this.setLocationRelativeTo(null);
  }

  /**
   * Affiche la boîte de dialogue "A propos"
   */
  private void showAboutDialog() {
    ImageIcon logoIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 100, 100);
    JOptionPane.showMessageDialog(
      this,
      "MessageApp - Application de messagerie\nUBO M2-TIIL\nDépartement Informatique",
      "A propos",
      JOptionPane.INFORMATION_MESSAGE,
      logoIcon
    );
  }

  /**
   * Définit le panneau de contenu principal
   */
  public void setContentPanel(JPanel panel) {
    // Supprimer l'ancien contenu
    this.getContentPane().removeAll();

    // Ajouter le nouveau panneau
    this.add(panel, BorderLayout.CENTER);

    // Mettre à jour l'affichage
    this.revalidate();
    this.repaint();
  }

  /**
   * Met à jour le menu en fonction de l'état de connexion
   */
  public void updateMenuForConnectedUser(boolean isConnected) {
    userMenu.setEnabled(isConnected);
  }
}