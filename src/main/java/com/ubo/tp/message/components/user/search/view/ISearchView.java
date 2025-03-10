package com.ubo.tp.message.components.user.search.view;

import com.ubo.tp.message.core.datamodel.User;

import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Interface pour la vue de recherche d'utilisateurs.
 */
public interface ISearchView {

  /**
   * Définit l'écouteur d'événements pour la barre de recherche.
   */
  void setSearchActionListener(ActionListener listener);

  /**
   * Définit l'écouteur d'événements pour le bouton de visualisation du profil.
   */
  void setViewProfileButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur d'événements pour le bouton de suivi.
   */
  void setFollowButtonListener(ActionListener listener);

  /**
   * Récupère la requête de recherche saisie.
   */
  String getSearchQuery();

  /**
   * Met à jour la liste des utilisateurs affichés.
   */
  void updateUsersList(Set<User> users, Set<String> followedTags);

  /**
   * Met à jour le nombre de followers pour un utilisateur.
   */
  void updateFollowersCount(int rowIndex, int followersCount);

  /**
   * Met à jour l'état du bouton de suivi pour l'utilisateur sélectionné.
   */
  void updateFollowButton();

  /**
   * Récupère l'utilisateur sélectionné dans la table.
   */
  User getSelectedUser();

  /**
   * Affiche un message d'erreur.
   */
  void setErrorMessage(String message);

  /**
   * Affiche un message de succès.
   */
  void setSuccessMessage(String message);

  /**
   * Réinitialise les messages d'état.
   */
  void clearMessages();

  /**
   * Réinitialise la vue.
   */
  void reset();

  /**
   * Active ou désactive la vue.
   */
  void setEnabled(boolean enabled);
}