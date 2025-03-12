package com.ubo.tp.message.components.user.details.model;

import com.ubo.tp.message.core.datamodel.User;

/**
 * Modèle pour le profil utilisateur, sans logique métier ni accès à la base de données.
 */
public class UserProfileModel {

  private User displayedUser;

  public UserProfileModel() {
    // Constructeur vide
  }

  /**
   * Définit l'utilisateur à afficher.
   * @param user L'utilisateur à afficher.
   */
  public void setDisplayedUser(User user) {
    this.displayedUser = user;
  }

  /**
   * Récupère l'utilisateur affiché.
   * @return L'utilisateur affiché.
   */
  public User getDisplayedUser() {
    return displayedUser;
  }
}
