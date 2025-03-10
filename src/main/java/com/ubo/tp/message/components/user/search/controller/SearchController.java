package com.ubo.tp.message.components.user.search.controller;

import com.ubo.tp.message.components.user.search.model.SearchModel;
import com.ubo.tp.message.components.user.search.view.SearchView;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Contrôleur pour la gestion de la recherche d'utilisateurs.
 */
public class SearchController {
  /**
   * Vue associée.
   */
  private final SearchView view;

  /**
   * Modèle associé.
   */
  private final SearchModel model;

  /**
   * Gestionnaire d'entités pour écrire les fichiers utilisateur.
   */
  private final EntityManager entityManager;

  /**
   * Écouteur pour l'action de visualisation du profil.
   */
  private ActionListener viewProfileListener;

  /**
   * Constructeur.
   * @param view La vue à contrôler.
   * @param model Le modèle à utiliser.
   * @param entityManager Le gestionnaire d'entités.
   */
  public SearchController(SearchView view, SearchModel model, EntityManager entityManager) {
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
    // Écouteur pour la recherche
    view.setSearchActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String query = view.getSearchQuery();
        searchUsers(query);
      }
    });

    // Écouteur pour le suivi
    view.setFollowButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        toggleFollow();
      }
    });

    // Par défaut, le bouton de visualisation du profil ne fait rien
    // Il sera configuré par le composant parent
    view.setViewProfileButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (viewProfileListener != null) {
          viewProfileListener.actionPerformed(new ActionEvent(
            view.getSelectedUser(), ActionEvent.ACTION_PERFORMED, "view_profile"));
        }
      }
    });
  }

  /**
   * Recherche des utilisateurs en fonction d'un critère.
   * @param criteria Le critère de recherche.
   */
  public void searchUsers(String criteria) {
    // Récupérer les utilisateurs correspondant au critère
    Set<User> users = model.searchUsers(criteria);

    // Récupérer l'utilisateur connecté
    User connectedUser = model.getConnectedUser();

    // Mettre à jour la vue
    if (connectedUser != null) {
      view.updateUsersList(users, connectedUser.getFollows());
    } else {
      view.updateUsersList(users, Set.of());
    }

    // Mettre à jour le nombre de followers pour chaque utilisateur
    updateFollowersCounts(users);

    // Mettre à jour l'état du bouton de suivi
    view.updateFollowButton();
  }

  /**
   * Met à jour le nombre de followers pour chaque utilisateur dans la vue.
   * @param users Les utilisateurs à mettre à jour.
   */
  private void updateFollowersCounts(Set<User> users) {
    int rowIndex = 0;
    for (User user : users) {
      int followersCount = model.getFollowersCount(user);
      view.updateFollowersCount(rowIndex++, followersCount);
    }
  }

  /**
   * Suit ou cesse de suivre l'utilisateur sélectionné.
   */
  private void toggleFollow() {
    User selectedUser = view.getSelectedUser();
    User connectedUser = model.getConnectedUser();

    if (selectedUser != null && connectedUser != null) {
      // Vérifier si l'utilisateur est déjà suivi
      boolean isFollowed = model.isFollowing(selectedUser);

      // Suivre ou ne plus suivre
      boolean result = model.toggleFollow(selectedUser, !isFollowed);

      if (result) {
        try {
          // Écrire les changements dans le fichier utilisateur
          entityManager.writeUserFile(connectedUser);

          // Mettre à jour la vue
          view.setSuccessMessage(
            isFollowed ?
              "Vous ne suivez plus " + selectedUser.getUserTag() :
              "Vous suivez maintenant " + selectedUser.getUserTag()
          );

          // Rafraîchir la liste des utilisateurs
          searchUsers(view.getSearchQuery());
        } catch (Exception e) {
          view.setErrorMessage("Erreur lors de l'enregistrement: " + e.getMessage());
        }
      } else {
        view.setErrorMessage("Impossible de " + (isFollowed ? "ne plus suivre" : "suivre") + " l'utilisateur.");
      }
    }
  }

  /**
   * Définit l'écouteur pour l'action de visualisation du profil.
   * @param listener L'écouteur à définir.
   */
  public void setViewProfileListener(ActionListener listener) {
    this.viewProfileListener = listener;
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