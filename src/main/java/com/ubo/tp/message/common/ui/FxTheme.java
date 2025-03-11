//package com.ubo.tp.message.common.ui;
//
//import java.awt.*;
//
///**
// * Adaptateur pour utiliser le thème EnvUI avec des composants JavaFX.
// */
//public class FxTheme {
//
//  // CONVERSION DES COULEURS
//  /** Couleur principale */
//  public static final Color PRIMARY = rgbToColor(EnvUI.PRIMARY_RGB);
//  /** Couleur secondaire */
//  public static final Color SECONDARY = rgbToColor(EnvUI.SECONDARY_RGB);
//  /** Couleur d'accent */
//  public static final Color ACCENT = rgbToColor(EnvUI.ACCENT_RGB);
//  /** Couleur de danger */
//  public static final Color DANGER = rgbToColor(EnvUI.DANGER_RGB);
//  /** Couleur de succès */
//  public static final Color SUCCESS = rgbToColor(EnvUI.SUCCESS_RGB);
//  /** Couleur d'information */
//  public static final Color INFO = rgbToColor(EnvUI.INFO_RGB);
//  /** Couleur de fond */
//  public static final Color BACKGROUND = rgbToColor(EnvUI.BACKGROUND_RGB);
//  /** Couleur de panneau */
//  public static final Color PANEL = rgbToColor(EnvUI.PANEL_RGB);
//  /** Couleur alternative */
//  public static final Color ALTERNATE = rgbToColor(EnvUI.ALTERNATE_RGB);
//  /** Couleur de texte */
//  public static final Color TEXT = rgbToColor(EnvUI.TEXT_RGB);
//  /** Couleur de texte secondaire */
//  public static final Color TEXT_SECONDARY = rgbToColor(EnvUI.TEXT_SECONDARY_RGB);
//  /** Couleur de texte sur fond coloré */
//  public static final Color TEXT_ON_COLOR = rgbToColor(EnvUI.TEXT_ON_COLOR_RGB);
//
//  // CONVERSION DES POLICES
//  /** Police pour les titres */
//  public static final Font TITLE_FONT = Font.font(EnvUI.FONT_FAMILY, FontWeight.BOLD, EnvUI.TITLE_FONT_SIZE);
//  /** Police pour les sous-titres */
//  public static final Font SUBTITLE_FONT = Font.font(EnvUI.FONT_FAMILY, FontWeight.BOLD, EnvUI.SUBTITLE_FONT_SIZE);
//  /** Police pour le texte normal en gras */
//  public static final Font TEXT_BOLD = Font.font(EnvUI.FONT_FAMILY, FontWeight.BOLD, EnvUI.TEXT_FONT_SIZE);
//  /** Police pour le texte normal */
//  public static final Font TEXT_REGULAR = Font.font(EnvUI.FONT_FAMILY, FontWeight.NORMAL, EnvUI.TEXT_FONT_SIZE);
//  /** Police pour le texte secondaire */
//  public static final Font TEXT_SMALL = Font.font(EnvUI.FONT_FAMILY, FontWeight.NORMAL, EnvUI.SMALL_FONT_SIZE);
//  /** Police pour les infobulles */
//  public static final Font TOOLTIP_FONT = Font.font(EnvUI.FONT_FAMILY, FontPosture.ITALIC, EnvUI.TOOLTIP_FONT_SIZE);
//
//  // DIMENSIONS ET STYLES
//  /** Marge standard pour les panneaux */
//  public static final Insets PANEL_INSETS = new Insets(
//    EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING,
//    EnvUI.PANEL_PADDING, EnvUI.PANEL_PADDING);
//
//  /** Coins arrondis standard */
//  public static final CornerRadii CORNER_RADII = new CornerRadii(EnvUI.CORNER_RADIUS);
//
//  /**
//   * Convertit un tableau RGB en objet Color de JavaFX.
//   */
//  private static Color rgbToColor(int[] rgb) {
//    return Color.rgb(rgb[0], rgb[1], rgb[2]);
//  }
//
//  /**
//   * Applique le style standard à un panneau JavaFX.
//   */
//  public static void stylePane(Pane pane) {
//    pane.setBackground(new Background(new BackgroundFill(PANEL, CORNER_RADII, Insets.EMPTY)));
//    pane.setPadding(PANEL_INSETS);
//
//    // Ajouter une bordure via CSS
//    pane.setStyle("-fx-border-color: " + toRgbCssString(PRIMARY) + ";"
//                    + "-fx-border-radius: " + EnvUI.CORNER_RADIUS + "px;"
//                    + "-fx-border-width: " + EnvUI.BORDER_WIDTH + "px;");
//  }
//
//  /**
//   * Applique le style standard à un champ texte JavaFX.
//   */
//  public static void styleTextField(TextField textField) {
//    textField.setFont(TEXT_REGULAR);
//    textField.setPadding(new Insets(8, 15, 8, 15));
//
//    // Style CSS pour les coins arrondis
//    textField.setStyle(
//      "-fx-background-radius: " + EnvUI.CORNER_RADIUS + "px;"
//        + "-fx-border-radius: " + EnvUI.CORNER_RADIUS + "px;"
//        + "-fx-border-color: #CCCCCC;"
//        + "-fx-border-width: " + EnvUI.BORDER_WIDTH + "px;");
//  }
//
//  /**
//   * Applique le style standard à un champ mot de passe JavaFX.
//   */
//  public static void stylePasswordField(PasswordField passwordField) {
//    styleTextField(passwordField); // Réutilise le style du TextField
//  }
//
//  /**
//   * Applique le style standard à un bouton JavaFX.
//   * @param isPrimary true pour un style principal, false pour un style secondaire
//   */
//  public static void styleButton(Button button, boolean isPrimary) {
//    button.setFont(TEXT_BOLD);
//    button.setPrefSize(EnvUI.BUTTON_WIDTH, EnvUI.BUTTON_HEIGHT);
//
//    if (isPrimary) {
//      button.setTextFill(TEXT_ON_COLOR);
//      button.setStyle(
//        "-fx-background-color: " + toRgbCssString(PRIMARY) + ";"
//          + "-fx-background-radius: " + EnvUI.CORNER_RADIUS + "px;");
//    } else {
//      button.setTextFill(TEXT);
//      button.setStyle(
//        "-fx-background-color: " + toRgbCssString(ALTERNATE) + ";"
//          + "-fx-background-radius: " + EnvUI.CORNER_RADIUS + "px;");
//    }
//  }
//
//  /**
//   * Configure une étiquette avec le style de titre.
//   */
//  public static void setTitleStyle(Label label) {
//    label.setFont(TITLE_FONT);
//    label.setTextFill(PRIMARY);
//  }
//
//  /**
//   * Configure une étiquette avec le style de sous-titre.
//   */
//  public static void setSubtitleStyle(Label label) {
//    label.setFont(SUBTITLE_FONT);
//    label.setTextFill(TEXT);
//  }
//
//  /**
//   * Configure le style d'une table JavaFX.
//   */
//  public static void styleTableView(TableView<?> tableView) {
//    tableView.setFixedCellSize(EnvUI.ROW_HEIGHT);
//
//    // Style de la table via CSS
//    tableView.setStyle(
//      "-fx-selection-bar: " + toRgbCssString(new Color(0.835, 0.961, 1.0, 1.0)) + ";"
//        + "-fx-selection-bar-non-focused: " + toRgbCssString(new Color(0.95, 0.95, 0.95, 1.0)) + ";");
//
//    // Style des en-têtes
//    for (TableColumn<?,?> column : tableView.getColumns()) {
//      column.setStyle("-fx-font-weight: bold;");
//    }
//  }
//
//  /**
//   * Configure le style d'une liste JavaFX.
//   */
//  public static void styleListView(ListView<?> listView) {
//    listView.setFixedCellSize(EnvUI.ROW_HEIGHT);
//    listView.setStyle(
//      "-fx-selection-bar: " + toRgbCssString(new Color(0.835, 0.961, 1.0, 1.0)) + ";"
//        + "-fx-selection-bar-non-focused: " + toRgbCssString(new Color(0.95, 0.95, 0.95, 1.0)) + ";");
//  }
//
//  /**
//   * Convertit une couleur JavaFX en chaîne CSS rgb().
//   */
//  private static String toRgbCssString(Color color) {
//    return String.format("rgb(%d, %d, %d)",
//                         (int)(color.getRed() * 255),
//                         (int)(color.getGreen() * 255),
//                         (int)(color.getBlue() * 255));
//  }
//}