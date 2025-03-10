package com.ubo.tp.message.common.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Classe utilitaire pour la création et la gestion des icônes de l'application.
 */
public class IconFactory {

  // Tailles standards pour les icônes
  public static final int ICON_SMALL = 16;
  public static final int ICON_MEDIUM = 24;
  public static final int ICON_LARGE = 32;

  /**
   * Crée une icône standard de fermeture (X rouge)
   * @param size Taille de l'icône en pixels
   * @return L'icône de fermeture
   */
  public static Icon createCloseIcon(int size) {
    return new Icon() {
      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cercle rouge
        g2.setColor(new Color(231, 76, 60)); // Rouge plus agréable
        g2.fill(new Ellipse2D.Float(x, y, size, size));

        // Croix blanche
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(size / 8.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int margin = size / 4;
        g2.drawLine(x + margin, y + margin, x + size - margin, y + size - margin);
        g2.drawLine(x + size - margin, y + margin, x + margin, y + size - margin);

        g2.dispose();
      }

      @Override
      public int getIconWidth() {
        return size;
      }

      @Override
      public int getIconHeight() {
        return size;
      }
    };
  }

  /**
   * Crée une icône d'information (i dans un cercle bleu)
   * @param size Taille de l'icône en pixels
   * @return L'icône d'information
   */
  public static Icon createInfoIcon(int size) {
    return new Icon() {
      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cercle bleu
        g2.setColor(new Color(52, 152, 219)); // Bleu agréable
        g2.fill(new Ellipse2D.Float(x, y, size, size));

        // "i" blanc
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, size * 3/4));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth("i");
        int textHeight = fm.getHeight();
        g2.drawString("i", x + (size - textWidth) / 2, y + (size + textHeight) / 2 - fm.getDescent());

        g2.dispose();
      }

      @Override
      public int getIconWidth() {
        return size;
      }

      @Override
      public int getIconHeight() {
        return size;
      }
    };
  }

  /**
   * Crée une icône utilisateur (silhouette dans un cercle)
   * @param size Taille de l'icône en pixels
   * @return L'icône utilisateur
   */
  public static Icon createUserIcon(int size) {
    return new Icon() {
      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cercle gris
        g2.setColor(new Color(189, 195, 199));
        g2.fill(new Ellipse2D.Float(x, y, size, size));

        // Tête
        g2.setColor(Color.WHITE);
        int headSize = size / 3;
        g2.fill(new Ellipse2D.Float(x + (float) (size - headSize) / 2, y + (float) size / 5, headSize, headSize));

        // Corps
        g2.fillOval(x + size / 4, y + size / 2, size / 2, size / 2);

        g2.dispose();
      }

      @Override
      public int getIconWidth() {
        return size;
      }

      @Override
      public int getIconHeight() {
        return size;
      }
    };
  }

  /**
   * Crée une icône de message (bulle de dialogue)
   * @param size Taille de l'icône en pixels
   * @return L'icône message
   */
  public static Icon createMessageIcon(int size) {
    return new Icon() {
      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Bulle de dialogue
        int margin = size / 8;
        int bubbleSize = size - 2 * margin;
        g2.setColor(new Color(41, 128, 185));
        g2.fillRoundRect(x + margin, y + margin, bubbleSize, bubbleSize * 3/4, 10, 10);

        // Queue de la bulle
        int[] xPoints = {x + size/2, x + size/3, x + size/3};
        int[] yPoints = {y + size - margin, y + size*3/4, y + size*2/3};
        g2.fillPolygon(xPoints, yPoints, 3);

        // Lignes de texte
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(size / 16.0f));
        int textY = y + size/4;
        int textMargin = size / 4;
        for (int i = 0; i < 3; i++) {
          g2.drawLine(x + textMargin, textY, x + size - textMargin, textY);
          textY += size / 8;
        }

        g2.dispose();
      }

      @Override
      public int getIconWidth() {
        return size;
      }

      @Override
      public int getIconHeight() {
        return size;
      }
    };
  }

  /**
   * Combine l'usage d'une icône système si disponible, sinon utilise une icône personnalisée
   * @param uiManagerKey Clé UIManager pour l'icône système
   * @param size Taille souhaitée pour l'icône personnalisée
   * @param fallbackCreator Fonction qui crée l'icône personnalisée si nécessaire
   * @return L'icône (système ou personnalisée)
   */
  public static Icon getSystemIconOrCustom(String uiManagerKey, int size, java.util.function.Function<Integer, Icon> fallbackCreator) {
    Icon systemIcon = UIManager.getIcon(uiManagerKey);
    if (systemIcon != null) {
      return systemIcon;
    } else {
      return fallbackCreator.apply(size);
    }
  }

  /**
   * Essaie de charger une icône depuis une ressource, avec une icône de secours
   * @param resourcePath Chemin de la ressource
   * @param size Taille souhaitée en cas d'échec de chargement
   * @param fallbackCreator Fonction qui crée l'icône de secours
   * @return L'icône (chargée ou personnalisée)
   */
  public static Icon getResourceOrCustom(String resourcePath, int size, java.util.function.Function<Integer, Icon> fallbackCreator) {
    ImageIcon resourceIcon = null;
    try {
      resourceIcon = new ImageIcon(IconFactory.class.getResource(resourcePath));
      if (resourceIcon.getIconWidth() <= 0) {
        resourceIcon = null;
      }
    } catch (Exception e) {
      resourceIcon = null;
    }

    return (resourceIcon != null) ? resourceIcon : fallbackCreator.apply(size);
  }
}