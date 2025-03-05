package com.ubo.tp.message.components.navigation;

import com.ubo.tp.message.app.MessageAppView;
import com.ubo.tp.message.components.navigation.controller.NavigationController;
import com.ubo.tp.message.components.navigation.model.NavigationModel;
import com.ubo.tp.message.components.navigation.view.NavigationViewConnected;
import com.ubo.tp.message.components.navigation.view.NavigationViewDisconnected;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Composant de navigation principal utilisant des JMenus.
 * Intègre le modèle, les vues et le contrôleur de navigation.
 */
public class NavigationComponent {

  /**
   * Panneau principal.
   */
  private final JPanel mainPanel;

  /**
   * Modèle de navigation.
   */
  private final NavigationModel model;

  /**
   * Vue pour utilisateur connecté.
   */
  private final NavigationViewConnected connectedView;

  /**
   * Vue pour utilisateur déconnecté.
   */
  private final NavigationViewDisconnected disconnectedView;

  /**
   * Contrôleur de navigation.
   */
  private final NavigationController controller;

  /**
   * Vue principale de l'application
   */
  private MessageAppView mainView;

  /**
   * Constructeur.
   * @param sessionManager Le gestionnaire de session
   */
  public NavigationComponent(SessionManager sessionManager) {
    // Initialisation du modèle
    model = new NavigationModel(sessionManager);

    // Initialisation des vues
    connectedView = new NavigationViewConnected();
    disconnectedView = new NavigationViewDisconnected();

    // Initialisation du contrôleur
    controller = new NavigationController(model, connectedView, disconnectedView);

    // S'abonner aux événements de session
    sessionManager.addSessionObserver(controller);

    // Initialisation du panneau principal
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(disconnectedView, BorderLayout.CENTER);

    // Affichage de la vue appropriée
    if (model.isUserConnected()) {
      showConnectedView();
    } else {
      showDisconnectedView();
    }
  }

  /**
   * Définit la vue principale de l'application
   */
  public void setMainView(MessageAppView mainView) {
    this.mainView = mainView;
  }

  /**
   * Récupère le composant Swing principal.
   */
  public JComponent getComponent() {
    return mainPanel;
  }

  /**
   * Récupère la barre de menu appropriée (connectée ou déconnectée)
   */
  public JMenuBar getMenuBar() {
    if (controller.isShowingConnectedView()) {
      return connectedView.getMenuBar();
    } else {
      return disconnectedView.getMenuBar();
    }
  }

  /**
   * Initialise le composant.
   */
  public void initialize() {
    // Déjà fait dans le constructeur
  }

  /**
   * Réinitialise l'état du composant.
   */
  public void reset() {
    // Rien à réinitialiser
  }

  /**
   * Active ou désactive le composant.
   */
  public void setEnabled(boolean enabled) {
    connectedView.setEnabled(enabled);
    disconnectedView.setEnabled(enabled);
    mainPanel.setEnabled(enabled);
  }

  /**
   * Affiche la vue pour un utilisateur connecté.
   */
  public void showConnectedView() {
    controller.showConnectedView();
    mainPanel.removeAll();
    mainPanel.add(connectedView, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    // Si la vue principale est définie, mettre à jour sa barre de menu
    if (mainView != null) {
      mainView.setJMenuBar(connectedView.getMenuBar());
      mainView.revalidate();
    }
  }

  /**
   * Affiche la vue pour un utilisateur déconnecté.
   */
  public void showDisconnectedView() {
    controller.showDisconnectedView();
    mainPanel.removeAll();
    mainPanel.add(disconnectedView, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    // Si la vue principale est définie, mettre à jour sa barre de menu
    if (mainView != null) {
      mainView.setJMenuBar(disconnectedView.getMenuBar());
      mainView.revalidate();
    }
  }

  /**
   * Définit l'écouteur pour l'action de profil.
   */
  public void setProfileActionListener(ActionListener listener) {
    controller.setProfileActionListener(listener);
  }

  /**
   * Définit l'écouteur pour l'action de messages.
   */
  public void setMessagesActionListener(ActionListener listener) {
    controller.setMessagesActionListener(listener);
  }

  /**
   * Définit l'écouteur pour l'action de recherche.
   */
  public void setSearchActionListener(ActionListener listener) {
    controller.setSearchActionListener(listener);
  }

  /**
   * Définit l'écouteur pour l'action de déconnexion.
   */
  public void setLogoutActionListener(ActionListener listener) {
    controller.setLogoutActionListener(listener);
  }

  /**
   * Définit l'écouteur pour l'action de connexion.
   */
  public void setLoginActionListener(ActionListener listener) {
    controller.setLoginActionListener(listener);
  }

  /**
   * Définit l'écouteur pour l'action d'inscription.
   */
  public void setRegisterActionListener(ActionListener listener) {
    controller.setRegisterActionListener(listener);
  }

  /**
   * Définit l'écouteur pour l'action "À propos".
   */
  public void setAboutActionListener(ActionListener listener) {
    controller.setAboutActionListener(listener);
  }

  /**
   * Définit l'écouteur pour l'action de changement de répertoire.
   */
  public void setChangeDirectoryActionListener(ActionListener listener) {
    controller.setChangeDirectoryActionListener(listener);
  }
}