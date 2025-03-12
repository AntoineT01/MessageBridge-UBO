package com.ubo.tp.message.components.navigation.controller;

import com.ubo.tp.message.components.navigation.model.NavigationModel;
import com.ubo.tp.message.components.navigation.view.INavigationViewConnected;
import com.ubo.tp.message.components.navigation.view.INavigationViewDisconnected;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.ISessionObserver;

import java.awt.event.ActionListener;


public class NavigationController implements ISessionObserver {

  // Attributs existants
  private final NavigationModel model;
  private final INavigationViewConnected connectedView;
  private final INavigationViewDisconnected disconnectedView;
  private boolean showingConnectedView;

  // Écouteurs d'événements
  private ActionListener profileActionListener;
  private ActionListener messagesActionListener;
  private ActionListener searchActionListener;
  private ActionListener settingsActionListener; // Nouvel écouteur pour les paramètres
  private ActionListener logoutActionListener;
  private ActionListener loginActionListener;
  private ActionListener registerActionListener;
  private ActionListener aboutActionListener;
  private ActionListener changeDirectoryActionListener;

  // Constructeur
  public NavigationController(NavigationModel model,
                              INavigationViewConnected connectedView,
                              INavigationViewDisconnected disconnectedView) {
    this.model = model;
    this.connectedView = connectedView;
    this.disconnectedView = disconnectedView;

    // Configurer les vues
    setupViews();

    // Mettre à jour l'affichage selon l'état de connexion
    updateViewBasedOnConnectionStatus();
  }

  // Configuration des vues
  private void setupViews() {
    // Vue connectée
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

    connectedView.setSettingsButtonListener(e -> {
      if (settingsActionListener != null) {
        settingsActionListener.actionPerformed(e);
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

    // Vue déconnectée
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

  // Mise à jour de la vue en fonction de l'état de connexion
  private void updateViewBasedOnConnectionStatus() {
    if (model.isUserConnected()) {
      showConnectedView();
    } else {
      showDisconnectedView();
    }
  }

  // Définir l'écouteur du bouton de quitter
  public void setExitActionListener(ActionListener listener) {
    connectedView.setExitButtonListener(listener);
    disconnectedView.setExitButtonListener(listener);
  }

  // Afficher la vue connectée
  public void showConnectedView() {
    if (!showingConnectedView) {
      connectedView.updateUserInfo(model.getConnectedUserName(), model.getConnectedUserTag());
      showingConnectedView = true;
    }
  }

  // Afficher la vue déconnectée
  public void showDisconnectedView() {
    if (showingConnectedView) {
      showingConnectedView = false;
    }
  }

  // Définitions des écouteurs (méthodes existantes)
  public void setProfileActionListener(ActionListener listener) {
    this.profileActionListener = listener;
  }

  public void setMessagesActionListener(ActionListener listener) {
    this.messagesActionListener = listener;
  }

  public void setSearchActionListener(ActionListener listener) {
    this.searchActionListener = listener;
  }

  // Nouvelle méthode pour l'écouteur des paramètres
  public void setSettingsActionListener(ActionListener listener) {
    this.settingsActionListener = listener;
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

  public boolean isShowingConnectedView() {
    return showingConnectedView;
  }

  // Méthodes de l'interface ISessionObserver
  @Override
  public void notifyLogin(User connectedUser) {
    showConnectedView();
    // Mettre à jour les informations de l'utilisateur
    connectedView.updateUserInfo(connectedUser.getName(), connectedUser.getUserTag());
  }

  @Override
  public void notifyLogout() {
    showDisconnectedView();
  }
}