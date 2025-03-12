package com.ubo.tp.message.common.ui;

import com.ubo.tp.message.common.utils.NotificationConfig;
import com.ubo.tp.message.common.utils.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Une notification améliorée qui s'affiche dans le coin inférieur droit de l'écran
 * pour informer l'utilisateur des nouvelles publications des personnes qu'il suit.
 */
public class EnhancedNotification extends JWindow {

  private static int DISPLAY_DURATION; // Durée d'affichage en millisecondes
  private static final int FADE_DURATION = 500; // Durée du fondu en millisecondes
  private static final int MAX_WIDTH = 350; // Largeur maximale de la notification
  private static final int MIN_HEIGHT = 80; // Hauteur minimale de la notification

  // Mettre à jour la durée à partir des paramètres
  static {
    updateDurationFromConfig();
  }

  // Méthode pour mettre à jour la durée d'affichage depuis la configuration
  private static void updateDurationFromConfig() {
    DISPLAY_DURATION = NotificationConfig.getInstance().getNotificationDuration();
  }

  private final Color backgroundColor = new Color(52, 152, 219); // Bleu primaire
  private final Color textColor = Color.WHITE;
  private final Color borderColor = new Color(41, 128, 185); // Bleu plus foncé

  private JPanel mainPanel;
  private Timer fadeTimer;
  private float opacity = 1.0f;
  private Runnable clickAction;

  public EnhancedNotification(String title, String message, Icon icon) {
    this(title, message, icon, null);
  }

  public EnhancedNotification(String title, String message, Icon icon, Runnable clickAction) {
    this.clickAction = clickAction;
    setOpacity(0.0f); // Commencer invisible pour l'effet de fondu

    mainPanel = createMainPanel(title, message, icon);
    add(mainPanel);

    pack();
    setSize(Math.min(getWidth(), MAX_WIDTH), Math.max(getHeight(), MIN_HEIGHT));

    // Rendre la fenêtre arrondie
    setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

    // Positionner la notification
    positionWindow();

    // Ajouter un écouteur pour le clic sur la notification
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (clickAction != null) {
          clickAction.run();
        }
        closeNotification();
      }
    });

    // Créer les timers pour l'affichage et le fondu
    setupTimers();
  }

  private JPanel createMainPanel(String title, String message, Icon icon) {
    JPanel panel = new JPanel(new BorderLayout(10, 5)) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Bordure
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 18, 18);

        g2d.dispose();
      }
    };

    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    // Panel d'icône
    if (icon != null) {
      JLabel iconLabel = new JLabel(icon);
      panel.add(iconLabel, BorderLayout.WEST);
    }

    // Panel de contenu
    JPanel contentPanel = new JPanel(new BorderLayout(0, 5));
    contentPanel.setOpaque(false);

    // Titre
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    titleLabel.setForeground(textColor);
    contentPanel.add(titleLabel, BorderLayout.NORTH);

    // Message
    JTextArea messageArea = new JTextArea(message);
    messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
    messageArea.setForeground(textColor);
    messageArea.setWrapStyleWord(true);
    messageArea.setLineWrap(true);
    messageArea.setRows(2);
    messageArea.setOpaque(false);
    messageArea.setEditable(false);
    messageArea.setBorder(null);
    contentPanel.add(messageArea, BorderLayout.CENTER);

    // Bouton de fermeture
    JLabel closeButton = new JLabel("×");
    closeButton.setFont(new Font("Arial", Font.BOLD, 16));
    closeButton.setForeground(textColor);
    closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    closeButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        closeNotification();
      }
    });
    panel.add(closeButton, BorderLayout.EAST);

    panel.add(contentPanel, BorderLayout.CENTER);

    // Ajouter un message de bas de page
    JLabel footerLabel = new JLabel("Cliquez pour afficher");
    footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
    footerLabel.setForeground(new Color(255, 255, 255, 180));
    footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(footerLabel, BorderLayout.SOUTH);

    return panel;
  }

  private void positionWindow() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Récupérer la position depuis la configuration
    String position = NotificationConfig.getInstance().getNotificationPosition();

    int x, y;
    switch (position) {
      case "bottom-right":
        x = screenSize.width - getWidth() - 20;
        y = screenSize.height - getHeight() - 40;
        break;
      case "bottom-left":
        x = 20;
        y = screenSize.height - getHeight() - 40;
        break;
      case "top-right":
        x = screenSize.width - getWidth() - 20;
        y = 40;
        break;
      case "top-left":
        x = 20;
        y = 40;
        break;
      default:
        // Position par défaut : bas-droite
        x = screenSize.width - getWidth() - 20;
        y = screenSize.height - getHeight() - 40;
    }

    setLocation(x, y);
  }

  private void setupTimers() {
    // Timer pour le fondu d'entrée
    Timer fadeInTimer = new Timer(20, e -> {
      opacity += 0.05f;
      if (opacity >= 1.0f) {
        opacity = 1.0f;
        ((Timer) e.getSource()).stop();

        // Programmer la fermeture
        Timer closeTimer = new Timer(DISPLAY_DURATION, event -> startFadeOut());
        closeTimer.setRepeats(false);
        closeTimer.start();
      }
      setOpacity(opacity);
    });
    fadeInTimer.start();
  }

  private void startFadeOut() {
    fadeTimer = new Timer(20, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        opacity -= 0.05f;
        if (opacity <= 0.0f) {
          opacity = 0.0f;
          setOpacity(0.0f);
          dispose();
          ((Timer) e.getSource()).stop();
        } else {
          setOpacity(opacity);
        }
      }
    });
    fadeTimer.start();
  }

  private void closeNotification() {
    if (fadeTimer != null && fadeTimer.isRunning()) {
      fadeTimer.stop();
    }
    startFadeOut();
  }

  /**
   * Affiche une notification enrichie.
   *
   * @param title Le titre de la notification
   * @param message Le contenu du message
   * @param icon L'icône à afficher (peut être null)
   */
  public static void showNotification(String title, String message, Icon icon) {
    // Vérifier si les notifications sont activées
    NotificationConfig config = NotificationConfig.getInstance();
    if (!config.isNotificationsEnabled()) {
      return;
    }

    // Mettre à jour la durée d'affichage
    updateDurationFromConfig();

    SwingUtilities.invokeLater(() -> {
      EnhancedNotification notification = new EnhancedNotification(title, message, icon);
      notification.setVisible(true);

      // Jouer le son de notification
      SoundPlayer.playNotificationSound();
    });
  }

  /**
   * Affiche une notification enrichie avec une action de clic.
   *
   * @param title Le titre de la notification
   * @param message Le contenu du message
   * @param icon L'icône à afficher (peut être null)
   * @param clickAction L'action à exécuter lorsque l'utilisateur clique sur la notification
   */
  public static void showNotification(String title, String message, Icon icon, Runnable clickAction) {
    // Vérifier si les notifications sont activées
    NotificationConfig config = NotificationConfig.getInstance();
    if (!config.isNotificationsEnabled()) {
      return;
    }

    // Mettre à jour la durée d'affichage
    updateDurationFromConfig();

    SwingUtilities.invokeLater(() -> {
      EnhancedNotification notification = new EnhancedNotification(title, message, icon, clickAction);
      notification.setVisible(true);

      // Jouer le son de notification
      SoundPlayer.playNotificationSound();
    });
  }
}