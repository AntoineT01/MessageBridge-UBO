package com.ubo.tp.message.common.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

public class ToastNotification extends JWindow {

  public ToastNotification(String message, int duration) {
    // Configuration de la fenêtre sans bordure et transparente
    setBackground(new Color(0, 0, 0, 0));

    // Panneau personnalisé pour dessiner un fond arrondi semi-transparent
    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        int arc = 20;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(50, 50, 50, 220)); // fond sombre et semi-transparent
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2d.dispose();
        super.paintComponent(g);
      }
    };
    panel.setOpaque(false);
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    // Le message
    JLabel label = new JLabel(message);
    label.setForeground(Color.WHITE);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(label, BorderLayout.CENTER);
    getContentPane().add(panel);

    pack();
    // Définir une taille minimale
    setSize(300, 50);

    // Positionner en bas au centre de l'écran
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - getWidth()) / 2;
    int y = screenSize.height - getHeight() - 75;
    setLocation(x, y);

    // Lancer un thread pour fermer la notification après "duration" millisecondes
    new Thread(() -> {
      try {
        Thread.sleep(duration);
        SwingUtilities.invokeLater(this::dispose);
      } catch (InterruptedException e) {
        // gestion de l'interruption si nécessaire
      }
    }).start();
  }

  public static void showToast(String message, int duration) {
    ToastNotification toast = new ToastNotification(message, duration);
    toast.setVisible(true);
  }
}
