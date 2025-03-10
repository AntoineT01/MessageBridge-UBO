package com.ubo.tp.message.components.user.details.view;

import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;

import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Interface pour la vue de profil utilisateur détaillé.
 */
public interface IUserProfileView {

  /**
   * Met à jour les informations de l'utilisateur affiché.
   */
  void updateUserInfo(User user, int followersCount, int followingCount, boolean isFollowed);

  /**
   * Met à jour la liste des messages de l'utilisateur.
   */
  void updateMessages(Set<Message> messages);

  /**
   * Met à jour la liste des followers.
   */
  void updateFollowers(Set<User> followers);

  /**
   * Met à jour la liste des utilisateurs suivis.
   */
  void updateFollowing(Set<User> following);

  /**
   * Définit l'écouteur pour le bouton de retour.
   */
  void setBackButtonListener(ActionListener listener);

  /**
   * Définit l'écouteur pour le bouton de suivi.
   */
  void setFollowButtonListener(ActionListener listener);

  /**
   * Affiche un message d'erreur.
   */
  void setErrorMessage(String message);

  /**
   * Affiche un message de succès.
   */
  void setSuccessMessage(String message);

  /**
   * Réinitialise la vue.
   */
  void reset();

  /**
   * Active ou désactive la vue.
   */
  void setEnabled(boolean enabled);
}