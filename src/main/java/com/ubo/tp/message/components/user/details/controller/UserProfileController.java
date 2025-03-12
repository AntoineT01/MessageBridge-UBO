package com.ubo.tp.message.components.user.details.controller;

import com.ubo.tp.message.components.user.details.model.UserProfileModel;
import com.ubo.tp.message.components.user.details.view.IUserProfileView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion du profil d'un utilisateur.
 * Contient la logique métier et les accès aux données (lecture via database et écriture via entityManager).
 */
public class UserProfileController implements IDatabaseObserver {
  private final IUserProfileView view;
  private final UserProfileModel model;
  private final IDatabase database;         // Lecture
  private final EntityManager entityManager; // Écriture
  private final SessionManager sessionManager;
  private ActionListener backToListListener;

  /**
   * Constructeur.
   *
   * @param view           La vue à contrôler.
   * @param model          Le modèle de données.
   * @param database       La source de lecture.
   * @param sessionManager Le gestionnaire de session.
   * @param entityManager  Le gestionnaire d'entités pour l'écriture.
   */
  public UserProfileController(IUserProfileView view, UserProfileModel model,
                               IDatabase database, SessionManager sessionManager,
                               EntityManager entityManager) {
    this.view = view;
    this.model = model;
    this.database = database;
    this.sessionManager = sessionManager;
    this.entityManager = entityManager;
    this.database.addObserver(this);
    initEventListeners();
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
   *
   * @param user L'utilisateur à afficher.
   */
  public void setUserToDisplay(User user) {
    if (user != null) {
      model.setDisplayedUser(user);
      refreshView();
    }
  }

  /**
   * Rafraîchit la vue avec les données actuelles.
   */
  private void refreshView() {
    User displayedUser = model.getDisplayedUser();
    if (displayedUser != null) {
      // Récupération des données en lecture via database
      int followersCount = database.getFollowersCount(displayedUser);
      Set<Message> messages = database.getUserMessages(displayedUser);
      Set<User> followers = database.getFollowers(displayedUser);
      Set<User> following = database.getUsers().stream()
        .filter(u -> displayedUser.getFollows().contains(u.getUserTag()))
        .collect(Collectors.toSet());

      // Vérification si l'utilisateur connecté suit l'utilisateur affiché
      User connectedUser = sessionManager.getConnectedUser();
      boolean isFollowing = connectedUser != null && connectedUser.isFollowing(displayedUser);

      // Mise à jour de la vue
      view.updateUserInfo(displayedUser, followersCount, following.size(), isFollowing);
      view.updateMessages(messages);
      view.updateFollowers(followers);
      view.updateFollowing(following);
    }
  }

  /**
   * Suit ou cesse de suivre l'utilisateur affiché.
   */
  private void toggleFollow() {
    User displayedUser = model.getDisplayedUser();
    User connectedUser = sessionManager.getConnectedUser();
    if (connectedUser != null && displayedUser != null) {
      boolean isFollowing = connectedUser.isFollowing(displayedUser);
      if (isFollowing) {
        connectedUser.removeFollowing(displayedUser.getUserTag());
      } else {
        connectedUser.addFollowing(displayedUser.getUserTag());
      }
      try {
        // Enregistrement des changements via entityManager (écriture)
        entityManager.writeUserFile(connectedUser);
        view.setSuccessMessage(isFollowing ?
          "Vous ne suivez plus " + displayedUser.getUserTag() :
          "Vous suivez maintenant " + displayedUser.getUserTag());
        refreshView();
      } catch (Exception e) {
        view.setErrorMessage("Erreur lors de l'enregistrement: " + e.getMessage());
      }
    } else {
      view.setErrorMessage("Impossible de modifier le suivi de cet utilisateur.");
    }
  }

  /**
   * Définit l'écouteur pour l'action de retour à la liste.
   *
   * @param listener L'écouteur à définir.
   */
  public void setBackToListListener(ActionListener listener) {
    this.backToListListener = listener;
  }

  /**
   * Affiche un message d'erreur dans la vue.
   *
   * @param message Le message d'erreur à afficher.
   */
  public void showError(String message) {
    view.setErrorMessage(message);
  }

  /**
   * Affiche un message de succès dans la vue.
   *
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
   *
   * @param enabled true pour activer, false pour désactiver.
   */
  public void setEnabled(boolean enabled) {
    view.setEnabled(enabled);
  }

  @Override
  public void notifyMessageAdded(Message addedMessage) {

  }

  @Override
  public void notifyMessageDeleted(Message deletedMessage) {

  }

  @Override
  public void notifyMessageModified(Message modifiedMessage) {

  }

  @Override
  public void notifyUserAdded(User addedUser) {

  }

  @Override
  public void notifyUserDeleted(User deletedUser) {

  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    if (sessionManager.getConnectedUser() == null) {
      return;
    }
    System.out.println("MODIFIE DANS USER PROFILE CONTROLLER ET USER CONNECTE : " + sessionManager.getConnectedUser().getUserTag());
    if (sessionManager.getConnectedUser().getUserTag().equals(modifiedUser.getUserTag())) {
      this.model.setDisplayedUser(modifiedUser);
      refreshView();
    }
  }
}
