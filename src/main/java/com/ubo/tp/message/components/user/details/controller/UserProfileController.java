package com.ubo.tp.message.components.user.details.controller;

import com.ubo.tp.message.components.user.details.model.UserProfileModel;
import com.ubo.tp.message.components.user.details.view.UserProfileView;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Contrôleur pour la gestion du profil d'un utilisateur.
 */
public class UserProfileController {
  /**
   * Vue associée.
   */
  private final UserProfileView view;

  /**
   * Modèle associé.
   */
  private final UserProfileModel model;

  /**
   * Gestionnaire d'entités pour écrire les fichiers utilisateur.
   */
  private final EntityManager entityManager;

  /**
   * Écouteur pour l'action de retour à la liste.
   */
  private ActionListener backToListListener;

  /**
   * Constructeur.
   * @param view La vue à contrôler.
   * @param model Le modèle à utiliser.
   * @param entityManager Le gestionnaire d'entités.
   */
  public UserProfileController(UserProfileView view, UserProfileModel model, EntityManager entityManager) {
    this.view = view;
    this.model = model;
    this.entityManager = entityManager;

    // Initialiser les écouteurs d'événements
    this.initEventListeners();
  }

  /**
   * Initialise les écouteurs d'événements.
   */
  private void initEventListeners() {
    // Écouteur pour le bouton de suivi
    view.setFollowButtonListener(e -> toggleFollow());

    // Écouteur pour le bouton de retour
    view.setBackButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (backToListListener != null) {
          backToListListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "back_to_list"));
        }
      }
    });
  }

  /**
   * Définit l'utilisateur à afficher.
   * @param user L'utilisateur à afficher.
   */
  public void setUserToDisplay(User user) {
    if (user != null) {
      // Mettre à jour le modèle
      model.setDisplayedUser(user);

      // Mettre à jour la vue
      refreshView();
    }
  }

  /**
   * Rafraîchit la vue avec les données actuelles du modèle.
   */
  private void refreshView() {
    User displayedUser = model.getDisplayedUser();

    if (displayedUser != null) {
      // Récupérer les informations nécessaires
      int followersCount = model.getFollowersCount();
      int followingCount = model.getFollowed().size();
      boolean isFollowed = model.isFollowing();

      // Mettre à jour les informations de l'utilisateur
      view.updateUserInfo(displayedUser, followersCount, followingCount, isFollowed);

      // Mettre à jour les messages
      Set<Message> messages = model.getUserMessages();
      view.updateMessages(messages);

      // Mettre à jour les followers
      Set<User> followers = model.getFollowers();
      view.updateFollowers(followers);

      // Mettre à jour les utilisateurs suivis
      Set<User> following = model.getFollowed();
      view.updateFollowing(following);
    }
  }

  /**
   * Suit ou cesse de suivre l'utilisateur affiché.
   */
  private void toggleFollow() {
    // Vérifier si on suit déjà l'utilisateur
    boolean isFollowing = model.isFollowing();

    // Appliquer l'action inverse
    boolean result = model.toggleFollow(!isFollowing);

    if (result) {
      try {
        // Écrire les changements dans le fichier utilisateur
        User connectedUser = model.getConnectedUser();
        entityManager.writeUserFile(connectedUser);

        // Mettre à jour la vue
        view.setSuccessMessage(
          isFollowing ?
            "Vous ne suivez plus " + model.getDisplayedUser().getUserTag() :
            "Vous suivez maintenant " + model.getDisplayedUser().getUserTag()
        );

        // Rafraîchir la vue
        refreshView();
      } catch (Exception e) {
        view.setErrorMessage("Erreur lors de l'enregistrement: " + e.getMessage());
      }
    } else {
      view.setErrorMessage("Impossible de " + (isFollowing ? "ne plus suivre" : "suivre") + " cet utilisateur.");
    }
  }

  /**
   * Définit l'écouteur pour l'action de retour à la liste.
   * @param listener L'écouteur à définir.
   */
  public void setBackToListListener(ActionListener listener) {
    this.backToListListener = listener;
  }

  /**
   * Affiche un message d'erreur dans la vue.
   * @param message Le message d'erreur à afficher.
   */
  public void showError(String message) {
    view.setErrorMessage(message);
  }

  /**
   * Affiche un message de succès dans la vue.
   * @param message Le message de succès à afficher.
   */
  public void showSuccess(String message) {
    view.setSuccessMessage(message);
  }

  /**
   * Réinitialise le contrôleur et la vue.
   */
  public void reset() {
    model.setDisplayedUser(null);
    view.reset();
  }

  /**
   * Active ou désactive la vue.
   * @param enabled true pour activer, false pour désactiver.
   */
  public void setEnabled(boolean enabled) {
    view.setEnabled(enabled);
  }
}