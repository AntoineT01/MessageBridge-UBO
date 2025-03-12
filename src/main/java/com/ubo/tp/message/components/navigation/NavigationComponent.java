package com.ubo.tp.message.components.navigation;

import com.ubo.tp.message.components.IComponent;
import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.components.navigation.controller.NavigationController;
import com.ubo.tp.message.components.navigation.model.NavigationModel;
import com.ubo.tp.message.components.navigation.view.INavigationViewConnected;
import com.ubo.tp.message.components.navigation.view.INavigationViewDisconnected;
import com.ubo.tp.message.components.navigation.view.NavigationViewConnected;
import com.ubo.tp.message.components.navigation.view.NavigationViewDisconnected;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;


public class NavigationComponent implements IComponent<JPanel> {

  // Panel principal
  private final JPanel mainPanel;

  // Modèle
  private final NavigationModel model;

  // Vues
  private final INavigationViewConnected connectedView;
  private final INavigationViewDisconnected disconnectedView;

  // Contrôleur
  private final NavigationController controller;

  // Frame principale
  private JFrame mainFrame;


  public NavigationComponent(SessionManager sessionManager) {
    // Initialiser le modèle
    model = new NavigationModel(sessionManager);

    // Initialiser les vues
    NavigationViewConnected concreteConnectedView = new NavigationViewConnected();
    NavigationViewDisconnected concreteDisconnectedView = new NavigationViewDisconnected();

    // Affecter les vues aux interfaces
    this.connectedView = concreteConnectedView;
    this.disconnectedView = concreteDisconnectedView;

    // Initialiser le contrôleur
    controller = new NavigationController(model, connectedView, disconnectedView);

    // S'abonner aux notifications de session
    sessionManager.addSessionObserver(controller);

    // Initialiser le panel principal
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(concreteDisconnectedView, BorderLayout.CENTER);

    // Afficher la vue appropriée
    if (model.isUserConnected()) {
      showConnectedView();
    } else {
      showDisconnectedView();
    }
  }

  @Override
  public JPanel getComponent() {
    return mainPanel;
  }

  @Override
  public void initialize() {
    // Rien à faire
  }

  @Override
  public void reset() {
    // Rien à faire
  }

  @Override
  public void setEnabled(boolean enabled) {
    connectedView.setEnabled(enabled);
    disconnectedView.setEnabled(enabled);
    mainPanel.setEnabled(enabled);
  }

  // Définir la frame principale
  public void setMainFrame(JFrame mainFrame) {
    this.mainFrame = mainFrame;

    // Propager à chaque vue
    connectedView.setParentFrame(mainFrame);
    disconnectedView.setParentFrame(mainFrame);

    // Mettre à jour le menu
    updateMenu();
  }

  // Définir le contrôleur de répertoire
  public void setDirectoryController(DirectoryController directoryController) {
    // Propager à chaque vue
    connectedView.setDirectoryController(directoryController);
    disconnectedView.setDirectoryController(directoryController);
  }

  // Mettre à jour le menu
  private void updateMenu() {
    if (mainFrame != null) {
      if (controller.isShowingConnectedView()) {
        mainFrame.setJMenuBar(connectedView.getMenuBar());
      } else {
        mainFrame.setJMenuBar(disconnectedView.getMenuBar());
      }
      mainFrame.revalidate();
    }
  }

  // Afficher la vue connectée
  public void showConnectedView() {
    controller.showConnectedView();
    mainPanel.removeAll();
    // Ajouter la vue connectée au panel principal
    mainPanel.add((Component) connectedView, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    // Mettre à jour le menu
    updateMenu();
  }

  // Afficher la vue déconnectée
  public void showDisconnectedView() {
    controller.showDisconnectedView();
    mainPanel.removeAll();
    // Ajouter la vue déconnectée au panel principal
    mainPanel.add((Component) disconnectedView, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    // Mettre à jour le menu
    updateMenu();
  }

  // Méthodes pour définir les écouteurs
  public void setProfileActionListener(ActionListener listener) {
    controller.setProfileActionListener(listener);
  }

  public void setMessagesActionListener(ActionListener listener) {
    controller.setMessagesActionListener(listener);
  }

  public void setSearchActionListener(ActionListener listener) {
    controller.setSearchActionListener(listener);
  }

  // Nouvelle méthode pour l'écouteur des paramètres
  public void setSettingsActionListener(ActionListener listener) {
    controller.setSettingsActionListener(listener);
  }

  public void setLogoutActionListener(ActionListener listener) {
    controller.setLogoutActionListener(listener);
  }

  public void setLoginActionListener(ActionListener listener) {
    controller.setLoginActionListener(listener);
  }

  public void setRegisterActionListener(ActionListener listener) {
    controller.setRegisterActionListener(listener);
  }

  public void setAboutActionListener(ActionListener listener) {
    controller.setAboutActionListener(listener);
  }

  public void setExitActionListener(ActionListener listener) {
    controller.setExitActionListener(listener);
  }
}