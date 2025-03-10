package com.ubo.tp.message.common.ui;

import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

/**
 * Simple bordure arrondie pour donner un aspect "bubble" aux champs/boutons.
 */
public class RoundedBorder implements Border {
  private final int radius;

  public RoundedBorder(int radius) {
    this.radius = radius;
  }

  @Override
  public Insets getBorderInsets(Component c) {
    // L'épaisseur interne dépend du rayon
    return new Insets(radius+2, radius+2, radius+2, radius+2);
  }

  @Override
  public boolean isBorderOpaque() {
    return false;
  }

  @Override
  public void paintBorder(Component c, Graphics g,
                          int x, int y, int width, int height) {
    g.setColor(Color.LIGHT_GRAY);
    g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
  }
}
