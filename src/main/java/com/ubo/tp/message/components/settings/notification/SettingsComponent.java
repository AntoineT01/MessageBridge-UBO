package com.ubo.tp.message.components.settings.notification;

import com.ubo.tp.message.components.IComponent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

/**
 * Composant de paramètres regroupant différents écrans de configuration
 */
public class SettingsComponent implements IComponent<JPanel> {

  private final JPanel mainPanel;
  private final JTabbedPane tabbedPane;
  private final com.ubo.tp.message.components.settings.NotificationSettingsView notificationSettingsView;

  /**
   * Constructeur
   */
  public SettingsComponent() {
    // Initialiser le panel principal
    mainPanel = new JPanel(new BorderLayout());

    // Créer les différentes vues de paramètres
    notificationSettingsView = new com.ubo.tp.message.components.settings.NotificationSettingsView();

    // Initialiser l'onglet
    tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Notifications", notificationSettingsView);

    // Ajouter des onglets pour d'autres paramètres potentiels
    // tabbedPane.addTab("Affichage", new AppearanceSettingsView());
    // tabbedPane.addTab("Confidentialité", new PrivacySettingsView());

    mainPanel.add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  public JPanel getComponent() {
    return mainPanel;
  }

  @Override
  public void initialize() {
    // Rien à faire
  }

  @Override
  public void reset() {
    // Rien à faire
  }

  @Override
  public void setEnabled(boolean enabled) {
    mainPanel.setEnabled(enabled);
    tabbedPane.setEnabled(enabled);
    notificationSettingsView.setEnabled(enabled);
  }
}