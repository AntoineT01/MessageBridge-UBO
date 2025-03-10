package com.ubo.tp.message.app;

import com.ubo.tp.message.common.utils.ImageUtils;
import com.ubo.tp.message.core.database.IDatabase;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Vue principale de l'application MessageApp.
 * Version extrêmement simplifiée pour éviter les problèmes de PropertyChangeSupport.
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
   * Écouteur pour l'action de sortie
   */
  private ActionListener exitListener;

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

    // Configuration de la fermeture
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        if (exitListener != null) {
          exitListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "exit"));
        }
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
  public void showAboutDialog() {
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
   * Définit l'écouteur pour l'action de sortie
   */
  public void setExitListener(ActionListener exitListener) {
    this.exitListener = exitListener;
  }

  /**
   * Met à jour le menu en fonction de l'état de connexion
   * Cette méthode est maintenant vide car la gestion des menus
   * est entièrement déléguée au composant de navigation
   */
  public void updateMenuForConnectedUser(boolean isConnected) {
    // Ne rien faire - la mise à jour est gérée par NavigationComponent
  }
}