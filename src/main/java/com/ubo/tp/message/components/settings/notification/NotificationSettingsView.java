package com.ubo.tp.message.components.settings;

import com.ubo.tp.message.common.ui.SwingTheme;
import com.ubo.tp.message.common.utils.NotificationConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Panel permettant de gérer les paramètres de notification
 */
public class NotificationSettingsView extends JPanel {

  private final NotificationConfig config;

  // Composants
  private JCheckBox enableNotificationsCheckbox;
  private JSlider durationSlider;
  private JCheckBox enableSoundCheckbox;
  private JComboBox<String> positionComboBox;
  private JLabel statusLabel;

  /**
   * Constructeur
   */
  public NotificationSettingsView() {
    config = NotificationConfig.getInstance();
    initGUI();
    loadSettings();
  }

  /**
   * Initialiser l'interface
   */
  private void initGUI() {
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(15, 15, 15, 15));
    setBackground(SwingTheme.BACKGROUND);

    // Titre
    JLabel titleLabel = new JLabel("Paramètres de notification");
    titleLabel.setFont(SwingTheme.TITLE_FONT);
    titleLabel.setForeground(SwingTheme.PRIMARY);
    titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
    add(titleLabel, BorderLayout.NORTH);

    // Panel principal
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(SwingTheme.PANEL);
    mainPanel.setBorder(BorderFactory.createCompoundBorder(
      SwingTheme.createRoundedBorder(),
      new EmptyBorder(15, 15, 15, 15)
    ));

    // Activer les notifications
    JPanel enablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    enablePanel.setOpaque(false);
    enableNotificationsCheckbox = new JCheckBox("Activer les notifications");
    enableNotificationsCheckbox.setFont(SwingTheme.TEXT_REGULAR);
    enableNotificationsCheckbox.addActionListener(e -> updateComponentStates());
    enablePanel.add(enableNotificationsCheckbox);
    mainPanel.add(enablePanel);
    mainPanel.add(Box.createVerticalStrut(10));

    // Durée d'affichage
    JPanel durationPanel = new JPanel(new BorderLayout());
    durationPanel.setOpaque(false);
    durationPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
    JLabel durationLabel = new JLabel("Durée d'affichage : ");
    durationLabel.setFont(SwingTheme.TEXT_REGULAR);

    durationSlider = new JSlider(JSlider.HORIZONTAL, 1000, 10000, 5000);
    durationSlider.setMajorTickSpacing(3000);
    durationSlider.setMinorTickSpacing(1000);
    durationSlider.setPaintTicks(true);
    durationSlider.setPaintLabels(true);
    durationSlider.setOpaque(false);

    // Labels personnalisés pour le slider
    createDictionaryLabels(durationSlider);

    durationPanel.add(durationLabel, BorderLayout.NORTH);
    durationPanel.add(durationSlider, BorderLayout.CENTER);
    mainPanel.add(durationPanel);
    mainPanel.add(Box.createVerticalStrut(10));

    // Son des notifications
    JPanel soundPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    soundPanel.setOpaque(false);
    enableSoundCheckbox = new JCheckBox("Activer le son des notifications");
    enableSoundCheckbox.setFont(SwingTheme.TEXT_REGULAR);
    soundPanel.add(enableSoundCheckbox);
    mainPanel.add(soundPanel);
    mainPanel.add(Box.createVerticalStrut(10));

    // Position des notifications
    JPanel positionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    positionPanel.setOpaque(false);
    JLabel positionLabel = new JLabel("Position des notifications : ");
    positionLabel.setFont(SwingTheme.TEXT_REGULAR);
    positionComboBox = new JComboBox<>(new String[] {
      "En bas à droite", "En bas à gauche", "En haut à droite", "En haut à gauche"
    });
    positionComboBox.setFont(SwingTheme.TEXT_REGULAR);
    positionPanel.add(positionLabel);
    positionPanel.add(positionComboBox);
    mainPanel.add(positionPanel);
    mainPanel.add(Box.createVerticalStrut(20));

    // Boutons d'action
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setOpaque(false);
    JButton saveButton = new JButton("Enregistrer");
    SwingTheme.styleButton(saveButton, true);
    saveButton.addActionListener(e -> saveSettings());

    JButton resetButton = new JButton("Réinitialiser");
    SwingTheme.styleButton(resetButton, false);
    resetButton.addActionListener(e -> resetSettings());

    buttonPanel.add(resetButton);
    buttonPanel.add(saveButton);
    mainPanel.add(buttonPanel);

    // Label de statut
    statusLabel = new JLabel(" ");
    statusLabel.setFont(SwingTheme.TEXT_SMALL);
    statusLabel.setForeground(SwingTheme.SUCCESS);
    statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

    add(mainPanel, BorderLayout.CENTER);
    add(statusLabel, BorderLayout.SOUTH);
  }

  /**
   * Créer des étiquettes personnalisées pour le slider
   */
  private void createDictionaryLabels(JSlider slider) {
    Dictionary<Integer, JLabel> labels = new Hashtable<>();
    labels.put(1000, new JLabel("1s"));
    labels.put(4000, new JLabel("4s"));
    labels.put(7000, new JLabel("7s"));
    labels.put(10000, new JLabel("10s"));
    slider.setLabelTable(labels);
  }

  /**
   * Charger les paramètres depuis la configuration
   */
  private void loadSettings() {
    enableNotificationsCheckbox.setSelected(config.isNotificationsEnabled());
    durationSlider.setValue(config.getNotificationDuration());
    enableSoundCheckbox.setSelected(config.isNotificationSoundEnabled());

    // Définir la position
    String position = config.getNotificationPosition();
    int positionIndex = 0;
    switch (position) {
      case "bottom-right":
        positionIndex = 0;
        break;
      case "bottom-left":
        positionIndex = 1;
        break;
      case "top-right":
        positionIndex = 2;
        break;
      case "top-left":
        positionIndex = 3;
        break;
    }
    positionComboBox.setSelectedIndex(positionIndex);

    // Mettre à jour l'état des composants
    updateComponentStates();
  }

  /**
   * Enregistrer les paramètres dans la configuration
   */
  private void saveSettings() {
    config.setNotificationsEnabled(enableNotificationsCheckbox.isSelected());
    config.setNotificationDuration(durationSlider.getValue());
    config.setNotificationSoundEnabled(enableSoundCheckbox.isSelected());

    // Enregistrer la position
    String position = "bottom-right";
    switch (positionComboBox.getSelectedIndex()) {
      case 0:
        position = "bottom-right";
        break;
      case 1:
        position = "bottom-left";
        break;
      case 2:
        position = "top-right";
        break;
      case 3:
        position = "top-left";
        break;
    }
    config.setNotificationPosition(position);

    // Force la sauvegarde
    config.saveConfig();

    // Afficher un message de confirmation
    statusLabel.setText("Paramètres enregistrés avec succès");
    statusLabel.setForeground(SwingTheme.SUCCESS);

    // Timer pour effacer le message après 3 secondes
    Timer timer = new Timer(3000, e -> statusLabel.setText(" "));
    timer.setRepeats(false);
    timer.start();
  }

  /**
   * Réinitialiser les paramètres aux valeurs par défaut
   */
  private void resetSettings() {
    enableNotificationsCheckbox.setSelected(NotificationConfig.DEFAULT_NOTIFICATIONS_ENABLED);
    durationSlider.setValue(NotificationConfig.DEFAULT_NOTIFICATION_DURATION);
    enableSoundCheckbox.setSelected(NotificationConfig.DEFAULT_NOTIFICATION_SOUND_ENABLED);

    // Position par défaut
    String position = NotificationConfig.DEFAULT_NOTIFICATION_POSITION;
    int positionIndex = 0;
    switch (position) {
      case "bottom-right":
        positionIndex = 0;
        break;
      case "bottom-left":
        positionIndex = 1;
        break;
      case "top-right":
        positionIndex = 2;
        break;
      case "top-left":
        positionIndex = 3;
        break;
    }
    positionComboBox.setSelectedIndex(positionIndex);

    // Mettre à jour l'état des composants
    updateComponentStates();

    // Afficher un message de confirmation
    statusLabel.setText("Paramètres réinitialisés");
    statusLabel.setForeground(SwingTheme.INFO);
  }

  /**
   * Mettre à jour l'état des composants en fonction de l'état de la case à cocher
   */
  private void updateComponentStates() {
    boolean enabled = enableNotificationsCheckbox.isSelected();
    durationSlider.setEnabled(enabled);
    enableSoundCheckbox.setEnabled(enabled);
    positionComboBox.setEnabled(enabled);
  }
}