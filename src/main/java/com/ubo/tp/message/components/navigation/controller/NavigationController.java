package com.ubo.tp.message.components.navigation.controller;

import com.ubo.tp.message.components.navigation.model.NavigationModel;
import com.ubo.tp.message.components.navigation.view.INavigationViewConnected;
import com.ubo.tp.message.components.navigation.view.INavigationViewDisconnected;
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
  private final INavigationViewConnected connectedView;

  /**
   * La vue pour un utilisateur déconnecté.
   */
  private final INavigationViewDisconnected disconnectedView;

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
                              INavigationViewConnected connectedView,
                              INavigationViewDisconnected disconnectedView) {
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

  public void setProfileActionListener(ActionListener listener) {
    this.profileActionListener = listener;
  }

  public void setMessagesActionListener(ActionListener listener) {
    this.messagesActionListener = listener;
  }

  public void setSearchActionListener(ActionListener listener) {
    this.searchActionListener = listener;
  }

  public void setLogoutActionListener(ActionListener listener) {
    this.logoutActionListener = listener;
  }

  public void setLoginActionListener(ActionListener listener) {
    this.loginActionListener = listener;
  }

  public void setRegisterActionListener(ActionListener listener) {
    this.registerActionListener = listener;
  }

  public void setAboutActionListener(ActionListener listener) {
    this.aboutActionListener = listener;
  }
  /**
   * Indique si la vue connectée est affichée.
   * @return true si la vue connectée est affichée, false sinon
   */
  public boolean isShowingConnectedView() {
    return showingConnectedView;
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