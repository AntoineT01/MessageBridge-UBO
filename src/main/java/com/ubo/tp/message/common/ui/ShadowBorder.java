package com.ubo.tp.message.common.ui;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * Bordure personnalisée qui ajoute un effet d'ombre portée aux composants Swing.
 */
public class ShadowBorder extends AbstractBorder {
  private final int shadowSize;
  private final Color shadowColor;
  private final float shadowOpacity;
  private final int cornerRadius;

  /**
   * Crée une bordure avec effet d'ombre.
   *
   * @param shadowSize     Taille de l'ombre en pixels
   * @param shadowColor    Couleur de l'ombre
   * @param shadowOpacity  Opacité de l'ombre (0.0 à 1.0)
   * @param cornerRadius   Rayon des coins arrondis
   */
  public ShadowBorder(int shadowSize, Color shadowColor, float shadowOpacity, int cornerRadius) {
    this.shadowSize = shadowSize;
    this.shadowColor = shadowColor;
    this.shadowOpacity = shadowOpacity;
    this.cornerRadius = cornerRadius;
  }

  /**
   * Constructeur simplifié avec un rayon de coin par défaut.
   */
  public ShadowBorder(int shadowSize, Color shadowColor, float shadowOpacity) {
    this(shadowSize, shadowColor, shadowOpacity, 10);
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int i = 0; i < shadowSize; i++) {
      g2.setColor(new Color(
        shadowColor.getRed(),
        shadowColor.getGreen(),
        shadowColor.getBlue(),
        (int)(shadowOpacity * 255 / (i + 1))
      ));
      g2.drawRoundRect(x + i, y + i, width - i * 2, height - i * 2, cornerRadius, cornerRadius);
    }

    g2.dispose();
  }

  @Override
  public Insets getBorderInsets(Component c) {
    return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
  }

  @Override
  public boolean isBorderOpaque() {
    return false;
  }
}