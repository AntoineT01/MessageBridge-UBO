package com.ubo.tp.message.components.navigation.controller;

import com.ubo.tp.message.components.navigation.model.NavigationModel;

import com.ubo.tp.message.components.navigation.view.NavigationViewConnected;
import com.ubo.tp.message.components.navigation.view.NavigationViewDisconnected;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.ISessionObserver;

import java.awt.event.ActionListener;

/**
 * Contrôleur pour la navigation.
 * Gère les transitions entre les vues connectées et déconnectées.
 */
public class NavigationController implements ISessionObserver {

  /**
   * Le modèle de navigation.
   */
  private final NavigationModel model;

  /**
   * La vue pour un utilisateur connecté.
   */
  private final NavigationViewConnected connectedView;

  /**
   * La vue pour un utilisateur déconnecté.
   */
  private final NavigationViewDisconnected disconnectedView;

  /**
   * La vue actuellement affichée.
   */
  private boolean showingConnectedView;

  /**
   * Les différents écouteurs pour les actions de navigation.
   */
  private ActionListener profileActionListener;
  private ActionListener messagesActionListener;
  private ActionListener searchActionListener;
  private ActionListener logoutActionListener;
  private ActionListener loginActionListener;
  private ActionListener registerActionListener;
  private ActionListener aboutActionListener;
  private ActionListener changeDirectoryActionListener;

  /**
   * Constructeur.
   * @param model Le modèle de navigation
   * @param connectedView La vue pour un utilisateur connecté
   * @param disconnectedView La vue pour un utilisateur déconnecté
   */
  public NavigationController(NavigationModel model,
                              NavigationViewConnected connectedView,
                              NavigationViewDisconnected disconnectedView) {
    this.model = model;
    this.connectedView = connectedView;
    this.disconnectedView = disconnectedView;

    // Initialisation des vues
    setupViews();

    // Affichage de la vue appropriée selon l'état de connexion
    updateViewBasedOnConnectionStatus();
  }

  /**
   * Configure les vues.
   */
  private void setupViews() {
    // Configuration de la vue connectée
    connectedView.setProfileButtonListener(e -> {
      if (profileActionListener != null) {
        profileActionListener.actionPerformed(e);
      }
    });

    connectedView.setMessagesButtonListener(e -> {
      if (messagesActionListener != null) {
        messagesActionListener.actionPerformed(e);
      }
    });

    connectedView.setSearchButtonListener(e -> {
      if (searchActionListener != null) {
        searchActionListener.actionPerformed(e);
      }
    });

    connectedView.setLogoutButtonListener(e -> {
      if (logoutActionListener != null) {
        logoutActionListener.actionPerformed(e);
      }
      model.logout();
    });

    connectedView.setAboutButtonListener(e -> {
      if (aboutActionListener != null) {
        aboutActionListener.actionPerformed(e);
      }
    });

    connectedView.setChangeDirectoryButtonListener(e -> {
      if (changeDirectoryActionListener != null) {
        changeDirectoryActionListener.actionPerformed(e);
      }
    });

    // Configuration de la vue déconnectée
    disconnectedView.setLoginButtonListener(e -> {
      if (loginActionListener != null) {
        loginActionListener.actionPerformed(e);
      }
    });

    disconnectedView.setRegisterButtonListener(e -> {
      if (registerActionListener != null) {
        registerActionListener.actionPerformed(e);
      }
    });

    disconnectedView.setAboutButtonListener(e -> {
      if (aboutActionListener != null) {
        aboutActionListener.actionPerformed(e);
      }
    });
  }

  /**
   * Met à jour la vue en fonction de l'état de connexion.
   */
  private void updateViewBasedOnConnectionStatus() {
    if (model.isUserConnected()) {
      showConnectedView();
    } else {
      showDisconnectedView();
    }
  }
  // Ajouter cette nouvelle méthode:
  /**
   * Définit l'écouteur pour l'action de quitter.
   * @param listener L'écouteur à définir
   */
  public void setExitActionListener(ActionListener listener) {
    connectedView.setExitButtonListener(listener);
    disconnectedView.setExitButtonListener(listener);
  }

  /**
   * Affiche la vue pour un utilisateur connecté.
   */
  public void showConnectedView() {
    if (!showingConnectedView) {
      connectedView.updateUserInfo(model.getConnectedUserName(), model.getConnectedUserTag());
      showingConnectedView = true;
    }
  }

  /**
   * Affiche la vue pour un utilisateur déconnecté.
   */
  public void showDisconnectedView() {
    if (showingConnectedView) {
      showingConnectedView = false;
    }
  }

  /**
   * Définit l'écouteur pour l'action de profil.
   * @param listener L'écouteur à définir
   */
  public void setProfileActionListener(ActionListener listener) {
    this.profileActionListener = listener;
  }

  /**
   * Définit l'écouteur pour l'action de messages.
   * @param listener L'écouteur à définir
   */
  public void setMessagesActionListener(ActionListener listener) {
    this.messagesActionListener = listener;
  }

  /**
   * Définit l'écouteur pour l'action de recherche.
   * @param listener L'écouteur à définir
   */
  public void setSearchActionListener(ActionListener listener) {
    this.searchActionListener = listener;
  }

  /**
   * Définit l'écouteur pour l'action de déconnexion.
   * @param listener L'écouteur à définir
   */
  public void setLogoutActionListener(ActionListener listener) {
    this.logoutActionListener = listener;
  }

  /**
   * Définit l'écouteur pour l'action de connexion.
   * @param listener L'écouteur à définir
   */
  public void setLoginActionListener(ActionListener listener) {
    this.loginActionListener = listener;
  }

  /**
   * Définit l'écouteur pour l'action d'inscription.
   * @param listener L'écouteur à définir
   */
  public void setRegisterActionListener(ActionListener listener) {
    this.registerActionListener = listener;
  }

  /**
   * Définit l'écouteur pour l'action "À propos".
   * @param listener L'écouteur à définir
   */
  public void setAboutActionListener(ActionListener listener) {
    this.aboutActionListener = listener;
  }

  /**
   * Définit l'écouteur pour l'action de changement de répertoire.
   * @param listener L'écouteur à définir
   */
  public void setChangeDirectoryActionListener(ActionListener listener) {
    this.changeDirectoryActionListener = listener;
  }

  /**
   * Indique si la vue connectée est affichée.
   * @return true si la vue connectée est affichée, false sinon
   */
  public boolean isShowingConnectedView() {
    return showingConnectedView;
  }

  /**
   * Récupère la vue connectée.
   * @return La vue connectée
   */
  public NavigationViewConnected getConnectedView() {
    return connectedView;
  }

  /**
   * Récupère la vue déconnectée.
   * @return La vue déconnectée
   */
  public NavigationViewDisconnected getDisconnectedView() {
    return disconnectedView;
  }

  @Override
  public void notifyLogin(User connectedUser) {
    showConnectedView();
    // Mettre à jour les informations de l'utilisateur dans la vue
    connectedView.updateUserInfo(connectedUser.getName(), connectedUser.getUserTag());
  }

  @Override
  public void notifyLogout() {
    showDisconnectedView();
  }
}