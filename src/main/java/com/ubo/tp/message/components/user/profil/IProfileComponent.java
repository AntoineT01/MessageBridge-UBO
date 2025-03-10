package com.ubo.tp.message.components.user.profil;
import com.ubo.tp.message.components.IComponent;

/**
 * Interface pour le composant de profil utilisateur.
 * Définit les fonctionnalités spécifiques au composant de profil.
 */
public interface IProfileComponent extends IComponent {

  /**
   * Rafraîchit les données du profil avec les informations les plus récentes.
   */
  void refreshProfileData();

  /**
   * Affiche un message d'erreur dans le composant.
   * @param message Le message d'erreur à afficher.
   */
  void showError(String message);

  /**
   * Affiche un message de succès dans le composant.
   * @param message Le message de succès à afficher.
   */
  void showSuccess(String message);
}