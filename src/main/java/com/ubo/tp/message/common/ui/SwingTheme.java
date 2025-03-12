package com.ubo.tp.message.common.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

/**
 * Adaptateur pour utiliser le thème EnvUI avec des composants Swing.
 */
public class SwingTheme {

  // CONVERSION DES COULEURS
  /** Couleur principale */
  public static final Color PRIMARY = new Color(EnvUI.PRIMARY_RGB[0], EnvUI.PRIMARY_RGB[1], EnvUI.PRIMARY_RGB[2]);
  /** Couleur secondaire */
  public static final Color SECONDARY = new Color(EnvUI.SECONDARY_RGB[0], EnvUI.SECONDARY_RGB[1], EnvUI.SECONDARY_RGB[2]);
  /** Couleur d'accent */
  public static final Color ACCENT = new Color(EnvUI.ACCENT_RGB[0], EnvUI.ACCENT_RGB[1], EnvUI.ACCENT_RGB[2]);
  /** Couleur de danger */
  public static final Color DANGER = new Color(EnvUI.DANGER_RGB[0], EnvUI.DANGER_RGB[1], EnvUI.DANGER_RGB[2]);
  /** Couleur de succès */
  public static final Color SUCCESS = new Color(EnvUI.SUCCESS_RGB[0], EnvUI.SUCCESS_RGB[1], EnvUI.SUCCESS_RGB[2]);
  /** Couleur d'information */
  public static final Color INFO = new Color(EnvUI.INFO_RGB[0], EnvUI.INFO_RGB[1], EnvUI.INFO_RGB[2]);
  /** Couleur de fond */
  public static final Color BACKGROUND = new Color(EnvUI.BACKGROUND_RGB[0], EnvUI.BACKGROUND_RGB[1], EnvUI.BACKGROUND_RGB[2]);
  /** Couleur de panneau */
  public static final Color PANEL = new Color(EnvUI.PANEL_RGB[0], EnvUI.PANEL_RGB[1], EnvUI.PANEL_RGB[2]);
  /** Couleur alternative */
  public static final Color ALTERNATE = new Color(EnvUI.ALTERNATE_RGB[0], EnvUI.ALTERNATE_RGB[1], EnvUI.ALTERNATE_RGB[2]);
  /** Couleur de texte */
  public static final Color TEXT = new Color(EnvUI.TEXT_RGB[0], EnvUI.TEXT_RGB[1], EnvUI.TEXT_RGB[2]);
  /** Couleur de texte secondaire */
  public static final Color TEXT_SECONDARY = new Color(EnvUI.TEXT_SECONDARY_RGB[0], EnvUI.TEXT_SECONDARY_RGB[1], EnvUI.TEXT_SECONDARY_RGB[2]);
  /** Couleur de texte sur fond coloré */
  public static final Color TEXT_ON_COLOR = new Color(EnvUI.TEXT_ON_COLOR_RGB[0], EnvUI.TEXT_ON_COLOR_RGB[1], EnvUI.TEXT_ON_COLOR_RGB[2]);

  // CONVERSION DES POLICES
  /** Police pour les titres */
  public static final Font TITLE_FONT = new Font(EnvUI.FONT_FAMILY, Font.BOLD, EnvUI.TITLE_FONT_SIZE);
  /** Police pour les sous-titres */
  public static final Font SUBTITLE_FONT = new Font(EnvUI.FONT_FAMILY, Font.BOLD, EnvUI.SUBTITLE_FONT_SIZE);
  /** Police pour le texte normal en gras */
  public static final Font TEXT_BOLD = new Font(EnvUI.FONT_FAMILY, Font.BOLD, EnvUI.TEXT_FONT_SIZE);
  /** Police pour le texte normal */
  public static final Font TEXT_REGULAR = new Font(EnvUI.FONT_FAMILY, Font.PLAIN, EnvUI.TEXT_FONT_SIZE);
  /** Police pour le texte secondaire */
  public static final Font TEXT_SMALL = new Font(EnvUI.FONT_FAMILY, Font.PLAIN, EnvUI.SMALL_FONT_SIZE);
  /** Police pour les infobulles */
  public static final Font TOOLTIP_FONT = new Font(EnvUI.FONT_FAMILY, Font.ITALIC, EnvUI.TOOLTIP_FONT_SIZE);

  // DIMENSIONS
  /** Dimension des boutons principaux */
  public static final Dimension BUTTON_SIZE = new Dimension(EnvUI.BUTTON_WIDTH, EnvUI.BUTTON_HEIGHT);
  /** Dimension des petits boutons */
  public static final Dimension SMALL_BUTTON_SIZE = new Dimension(EnvUI.SMALL_BUTTON_WIDTH, EnvUI.SMALL_BUTTON_HEIGHT);
  /** Marge standard pour les panneaux */
  public static final Insets PANEL_INSETS = new Insets(
    EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING,
    EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING);

  /**
   * Crée une bordure arrondie simple.
   */
  public static Border createRoundedBorder() {
    return new RoundedBorder(EnvUI.CORNER_RADIUS);
  }

  /**
   * Applique le style standard à un panneau Swing.
   */
  public static void stylePanel(JPanel panel) {
    panel.setBackground(PANEL);
    panel.setBorder(BorderFactory.createCompoundBorder(
      createRoundedBorder(),
      BorderFactory.createEmptyBorder(
        EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING,
        EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING)
    ));
  }

  /**
   * Applique le style standard à un composant de texte Swing.
   */
  public static void styleTextComponent(JComponent textComponent) {
    textComponent.setFont(TEXT_REGULAR);
    textComponent.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(EnvUI.CORNER_RADIUS),
      BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
  }

  /**
   * Applique le style standard à un bouton Swing.
   * @param isPrimary true pour un style principal, false pour un style secondaire
   */
  public static void styleButton(JButton button, boolean isPrimary) {
    button.setFont(TEXT_BOLD);
    button.setFocusPainted(false);

    if (isPrimary) {
      button.setBackground(PRIMARY);
      button.setForeground(TEXT_ON_COLOR);
    } else {
      button.setBackground(ALTERNATE);
      button.setForeground(TEXT);
    }

    button.setBorder(new RoundedBorder(EnvUI.CORNER_RADIUS));
    button.setPreferredSize(BUTTON_SIZE);
  }

  /**
   * Configure une étiquette avec le style de titre.
   */
  public static void setTitleStyle(JLabel label) {
    label.setFont(TITLE_FONT);
    label.setForeground(PRIMARY);
  }

  /**
   * Configure une étiquette avec le style de sous-titre.
   */
  public static void setSubtitleStyle(JLabel label) {
    label.setFont(SUBTITLE_FONT);
    label.setForeground(TEXT);
  }

  /**
   * Configure le style d'une table Swing.
   */
  public static void styleTable(JTable table) {
    table.setRowHeight(EnvUI.ROW_HEIGHT);
    table.setShowGrid(false);
    table.setIntercellSpacing(new Dimension(0, EnvUI.SPACING_SMALL));
    table.setSelectionBackground(new Color(213, 245, 255));
    table.setSelectionForeground(TEXT);

    // Personnalisation des en-têtes de colonnes
    table.getTableHeader().setFont(TEXT_BOLD);
    table.getTableHeader().setBackground(BACKGROUND);
    table.getTableHeader().setForeground(TEXT);
  }

  /**
   * Configure le style d'une liste Swing.
   */
  public static void styleList(JList<?> list) {
    list.setFont(TEXT_REGULAR);
    list.setSelectionBackground(new Color(213, 245, 255));
    list.setFixedCellHeight(EnvUI.ROW_HEIGHT);
  }

  /**
   * Crée un panneau stylisé avec une bordure et un titre.
   */
  public static JPanel createStyledPanel(String title) {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createCompoundBorder(
      createRoundedBorder(),
      BorderFactory.createEmptyBorder(
        EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING,
        EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING)
    ));
    panel.setBackground(PANEL);

    if (title != null && !title.isEmpty()) {
      JLabel titleLabel = new JLabel(title);
      setSubtitleStyle(titleLabel);
      titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
      panel.add(titleLabel, BorderLayout.NORTH);
    }

    return panel;
  }
}