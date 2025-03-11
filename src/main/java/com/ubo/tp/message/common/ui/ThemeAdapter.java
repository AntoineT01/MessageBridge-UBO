package com.ubo.tp.message.common.ui;

/**
 * Interface pour les adaptateurs de thème.
 * Permet de définir une API commune qui pourra être implémentée
 * pour différentes plateformes (Swing, JavaFX, etc.)
 */
public interface ThemeAdapter {
  /**
   * Obtient la couleur primaire pour la plateforme cible.
   * @return Objet représentant la couleur dans la plateforme cible
   */
  Object getPrimaryColor();

  /**
   * Obtient la couleur secondaire pour la plateforme cible.
   * @return Objet représentant la couleur dans la plateforme cible
   */
  Object getSecondaryColor();

  /**
   * Obtient la couleur de fond pour la plateforme cible.
   * @return Objet représentant la couleur dans la plateforme cible
   */
  Object getBackgroundColor();

  /**
   * Obtient la police pour les titres.
   * @return Objet représentant la police dans la plateforme cible
   */
  Object getTitleFont();

  /**
   * Obtient la police pour le texte normal.
   * @return Objet représentant la police dans la plateforme cible
   */
  Object getTextFont();

  /**
   * Applique le style au bouton principal.
   * @param button Le bouton à styler (de la plateforme cible)
   */
  void styleButton(Object button, boolean isPrimary);

  /**
   * Applique le style au champ de texte.
   * @param textField Le champ de texte à styler (de la plateforme cible)
   */
  void styleTextField(Object textField);

  /**
   * Applique le style au panneau.
   * @param panel Le panneau à styler (de la plateforme cible)
   */
  void stylePanel(Object panel);
}