package com.ubo.tp.message.common.ui;

/**
 * Classe utilitaire centralisant les paramètres esthétiques de l'application.
 * Contient uniquement des constantes et valeurs indépendantes de la plateforme.
 */
public class EnvUI {
  // COULEURS PRINCIPALES (définies en RGB pour compatibilité cross-platform)
  /** Couleur principale de l'application - utilisée pour les éléments principaux */
  public static final int[] PRIMARY_RGB = {52, 152, 219};       // Bleu
  /** Couleur secondaire - utilisée pour certains boutons et accents */
  public static final int[] SECONDARY_RGB = {46, 204, 113};     // Vert
  /** Couleur d'accent/alerte - utilisée pour les actions importantes */
  public static final int[] ACCENT_RGB = {230, 126, 34};        // Orange
  /** Couleur pour les éléments de danger/suppression */
  public static final int[] DANGER_RGB = {231, 76, 60};         // Rouge
  /** Couleur pour les messages de succès */
  public static final int[] SUCCESS_RGB = {46, 204, 113};       // Vert
  /** Couleur pour les messages d'information */
  public static final int[] INFO_RGB = {52, 152, 219};          // Bleu

  // COULEURS DE FOND
  /** Couleur de fond principale de l'application */
  public static final int[] BACKGROUND_RGB = {245, 248, 250};   // Gris très clair
  /** Couleur de fond des panneaux */
  public static final int[] PANEL_RGB = {255, 255, 255};        // Blanc
  /** Couleur de fond des éléments alternés (lignes de tableaux, listes) */
  public static final int[] ALTERNATE_RGB = {236, 240, 241};    // Gris clair

  // COULEURS DE TEXTE
  /** Couleur de texte principale */
  public static final int[] TEXT_RGB = {44, 62, 80};            // Gris foncé
  /** Couleur de texte secondaire (sous-titres, descriptions) */
  public static final int[] TEXT_SECONDARY_RGB = {127, 140, 141}; // Gris moyen
  /** Couleur de texte sur un fond coloré */
  public static final int[] TEXT_ON_COLOR_RGB = {10, 10, 10}; // Noir presque noir

  // POLICES
  /** Famille de police principale */
  public static final String FONT_FAMILY = "Arial";
  /** Taille de police pour les titres */
  public static final int TITLE_FONT_SIZE = 22;
  /** Taille de police pour les sous-titres */
  public static final int SUBTITLE_FONT_SIZE = 18;
  /** Taille de police pour le texte normal */
  public static final int TEXT_FONT_SIZE = 14;
  /** Taille de police pour les petits textes */
  public static final int SMALL_FONT_SIZE = 12;
  /** Taille de police pour les infobulles */
  public static final int TOOLTIP_FONT_SIZE = 11;

  // DIMENSIONS
  /** Largeur des boutons principaux */
  public static final int BUTTON_WIDTH = 150;
  /** Hauteur des boutons principaux */
  public static final int BUTTON_HEIGHT = 40;
  /** Largeur des petits boutons */
  public static final int SMALL_BUTTON_WIDTH = 100;
  /** Hauteur des petits boutons */
  public static final int SMALL_BUTTON_HEIGHT = 30;
  /** Marge intérieure standard des panneaux */
  public static final int PANEL_PADDING = 15;
  /** Rayon des coins arrondis */
  public static final int CORNER_RADIUS = 15;
  /** Hauteur des lignes dans les tableaux et listes */
  public static final int ROW_HEIGHT = 40;
  /** Épaisseur standard des bordures */
  public static final int BORDER_WIDTH = 1;

  // ESPACEMENT
  /** Espacement standard entre les composants */
  public static final int SPACING_STANDARD = 10;
  /** Petit espacement entre les composants */
  public static final int SPACING_SMALL = 5;
  /** Grand espacement entre les composants */
  public static final int SPACING_LARGE = 20;
}