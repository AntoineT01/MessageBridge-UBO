package com.ubo.tp.message.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Classe utilitaire pour gérer les configurations de notification
 */
public class NotificationConfig {

  private static final String CONFIG_FILE = "notification_config.properties";

  // Clés de configuration
  public static final String KEY_NOTIFICATIONS_ENABLED = "notifications.enabled";
  public static final String KEY_NOTIFICATION_DURATION = "notifications.duration";
  public static final String KEY_NOTIFICATION_SOUND_ENABLED = "notifications.sound.enabled";
  public static final String KEY_NOTIFICATION_POSITION = "notifications.position";

  // Valeurs par défaut
  public static final boolean DEFAULT_NOTIFICATIONS_ENABLED = true;
  public static final int DEFAULT_NOTIFICATION_DURATION = 5000; // 5 secondes
  public static final boolean DEFAULT_NOTIFICATION_SOUND_ENABLED = true;
  public static final String DEFAULT_NOTIFICATION_POSITION = "bottom-right";

  // Instance singleton
  private static NotificationConfig instance;

  // Propriétés
  private final Properties properties;

  /**
   * Constructeur privé (pattern Singleton)
   */
  private NotificationConfig() {
    properties = new Properties();
    loadConfig();
  }

  /**
   * Obtenir l'instance unique
   *
   * @return l'instance de NotificationConfig
   */
  public static synchronized NotificationConfig getInstance() {
    if (instance == null) {
      instance = new NotificationConfig();
    }
    return instance;
  }

  /**
   * Charger la configuration depuis le fichier
   */
  private void loadConfig() {
    File configFile = new File(CONFIG_FILE);

    if (configFile.exists()) {
      try (FileInputStream in = new FileInputStream(configFile)) {
        properties.load(in);
        System.out.println("Configuration de notification chargée depuis: " + configFile.getAbsolutePath());
      } catch (Exception e) {
        System.err.println("Erreur lors du chargement des préférences de notification: " + e.getMessage());
        initializeDefaultProperties();
      }
    } else {
      System.out.println("Fichier de configuration non trouvé, création des valeurs par défaut");
      initializeDefaultProperties();
      saveConfig();
    }
  }

  /**
   * Initialiser les propriétés par défaut
   */
  private void initializeDefaultProperties() {
    properties.setProperty(KEY_NOTIFICATIONS_ENABLED, String.valueOf(DEFAULT_NOTIFICATIONS_ENABLED));
    properties.setProperty(KEY_NOTIFICATION_DURATION, String.valueOf(DEFAULT_NOTIFICATION_DURATION));
    properties.setProperty(KEY_NOTIFICATION_SOUND_ENABLED, String.valueOf(DEFAULT_NOTIFICATION_SOUND_ENABLED));
    properties.setProperty(KEY_NOTIFICATION_POSITION, DEFAULT_NOTIFICATION_POSITION);
  }

  /**
   * Sauvegarder la configuration dans le fichier
   */
  public void saveConfig() {
    try {
      File configFile = new File(CONFIG_FILE);
      // Créer le répertoire parent si nécessaire
      File parent = configFile.getParentFile();
      if (parent != null && !parent.exists()) {
        parent.mkdirs();
      }

      try (FileOutputStream out = new FileOutputStream(configFile)) {
        properties.store(out, "MessageApp Notification Settings");
        System.out.println("Configuration de notification enregistrée dans: " + configFile.getAbsolutePath());
      }
    } catch (Exception e) {
      System.err.println("Erreur lors de l'enregistrement des préférences de notification: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Vérifier si les notifications sont activées
   *
   * @return true si les notifications sont activées, false sinon
   */
  public boolean isNotificationsEnabled() {
    return Boolean.parseBoolean(properties.getProperty(KEY_NOTIFICATIONS_ENABLED, String.valueOf(DEFAULT_NOTIFICATIONS_ENABLED)));
  }

  /**
   * Définir si les notifications sont activées
   *
   * @param enabled true pour activer les notifications, false pour les désactiver
   */
  public void setNotificationsEnabled(boolean enabled) {
    properties.setProperty(KEY_NOTIFICATIONS_ENABLED, String.valueOf(enabled));
    saveConfig();
  }

  /**
   * Obtenir la durée d'affichage des notifications
   *
   * @return la durée en millisecondes
   */
  public int getNotificationDuration() {
    try {
      return Integer.parseInt(properties.getProperty(KEY_NOTIFICATION_DURATION, String.valueOf(DEFAULT_NOTIFICATION_DURATION)));
    } catch (NumberFormatException e) {
      return DEFAULT_NOTIFICATION_DURATION;
    }
  }

  /**
   * Définir la durée d'affichage des notifications
   *
   * @param duration la durée en millisecondes
   */
  public void setNotificationDuration(int duration) {
    properties.setProperty(KEY_NOTIFICATION_DURATION, String.valueOf(duration));
    saveConfig();
  }

  /**
   * Vérifier si le son des notifications est activé
   *
   * @return true si le son est activé, false sinon
   */
  public boolean isNotificationSoundEnabled() {
    return Boolean.parseBoolean(properties.getProperty(KEY_NOTIFICATION_SOUND_ENABLED, String.valueOf(DEFAULT_NOTIFICATION_SOUND_ENABLED)));
  }

  /**
   * Définir si le son des notifications est activé
   *
   * @param enabled true pour activer le son, false pour le désactiver
   */
  public void setNotificationSoundEnabled(boolean enabled) {
    properties.setProperty(KEY_NOTIFICATION_SOUND_ENABLED, String.valueOf(enabled));
    saveConfig();
  }

  /**
   * Obtenir la position des notifications
   *
   * @return la position (bottom-right, bottom-left, top-right, top-left)
   */
  public String getNotificationPosition() {
    return properties.getProperty(KEY_NOTIFICATION_POSITION, DEFAULT_NOTIFICATION_POSITION);
  }

  /**
   * Définir la position des notifications
   *
   * @param position la position (bottom-right, bottom-left, top-right, top-left)
   */
  public void setNotificationPosition(String position) {
    if (isValidPosition(position)) {
      properties.setProperty(KEY_NOTIFICATION_POSITION, position);
      saveConfig();
    }
  }

  /**
   * Vérifier si la position est valide
   *
   * @param position la position à vérifier
   * @return true si la position est valide, false sinon
   */
  private boolean isValidPosition(String position) {
    return "bottom-right".equals(position) ||
      "bottom-left".equals(position) ||
      "top-right".equals(position) ||
      "top-left".equals(position);
  }
}