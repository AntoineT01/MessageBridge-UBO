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

/**
 * Composant de navigation principal utilisant des JMenus.
 * Intègre le modèle, les vues et le contrôleur de navigation.
 */
public class NavigationComponent implements IComponent<JPanel> {

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
  private final INavigationViewConnected connectedView;

  /**
   * Vue pour utilisateur déconnecté.
   */
  private final INavigationViewDisconnected disconnectedView;

  /**
   * Contrôleur de navigation.
   */
  private final NavigationController controller;

  /**
   * Frame principale
   */
  private JFrame mainFrame;


  public NavigationComponent(SessionManager sessionManager) {
    // Initialisation du modèle
    model = new NavigationModel(sessionManager);

    // Initialisation des vues concrètes
    NavigationViewConnected concreteConnectedView = new NavigationViewConnected();
    NavigationViewDisconnected concreteDisconnectedView = new NavigationViewDisconnected();

    // Affectation aux interfaces
    this.connectedView = concreteConnectedView;
    this.disconnectedView = concreteDisconnectedView;

    // Initialisation du contrôleur avec les interfaces
    controller = new NavigationController(model, connectedView, disconnectedView);

    // S'abonner aux événements de session
    sessionManager.addSessionObserver(controller);

    // Initialisation du panneau principal
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(concreteDisconnectedView, BorderLayout.CENTER);

    // Affichage de la vue appropriée
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
    // Rien à faire ici
  }

  @Override
  public void reset() {
    // Rien à faire ici
  }

  @Override
  public void setEnabled(boolean enabled) {
    connectedView.setEnabled(enabled);
    disconnectedView.setEnabled(enabled);
    mainPanel.setEnabled(enabled);
  }

  /**
   * Définit la frame principale
   */
  public void setMainFrame(JFrame mainFrame) {
    this.mainFrame = mainFrame;

    // Mettre à jour les vues avec la frame parente
    connectedView.setParentFrame(mainFrame);
    disconnectedView.setParentFrame(mainFrame);

    // Mettre à jour la barre de menu de la frame
    updateMenu();
  }

  /**
   * Définit le contrôleur de répertoire
   */
  public void setDirectoryController(DirectoryController directoryController) {
    // Mettre à jour les vues avec le contrôleur
    connectedView.setDirectoryController(directoryController);
    disconnectedView.setDirectoryController(directoryController);
  }

  /**
   * Met à jour la barre de menu de la frame principale
   */
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

  public void showConnectedView() {
    controller.showConnectedView();
    mainPanel.removeAll();
    // Utilisez un cast explicite
    mainPanel.add((Component) connectedView, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    // Mettre à jour la barre de menu
    updateMenu();
  }

  public void showDisconnectedView() {
    controller.showDisconnectedView();
    mainPanel.removeAll();
    // Utilisez un cast explicite
    mainPanel.add((Component) disconnectedView, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    // Mettre à jour la barre de menu
    updateMenu();
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
   * Définit l'écouteur pour l'action de quitter.
   */
  public void setExitActionListener(ActionListener listener) {
    controller.setExitActionListener(listener);
  }
}