package com.ubo.tp.message.ihm.oldlogin;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * Panneau permettant de rechercher un utilisateur et d'afficher son profil
 */
public class SearchUserPanel extends JPanel implements ISessionObserver {
  /**
   * Base de données
   */
  protected final IDatabase mDatabase;

  /**
   * Gestionnaire de session
   */
  protected final SessionManager mSessionManager;

  /**
   * Panel pour la liste des utilisateurs
   */
  protected UserListPanel mUserListPanel;

  /**
   * Panel pour le profil de l'utilisateur sélectionné
   */
  protected UserProfilePanel mUserProfilePanel;

  /**
   * Panel pour le contenu principal (liste ou profil)
   */
  protected JPanel mContentPanel;

  /**
   * Bouton pour retourner à la liste
   */
  protected JButton mBackButton;

  /**
   * Consumer appelé lorsque l'utilisateur clique sur "Retour"
   */
  protected Consumer<Void> mBackCallback;

  /**
   * Constructeur
   */
  public SearchUserPanel(IDatabase database, SessionManager sessionManager) {
    this.mDatabase = database;
    this.mSessionManager = sessionManager;

    // S'enregistrer comme observateur de session
    this.mSessionManager.addSessionObserver(this);

    this.initGUI();

    // Afficher la liste des utilisateurs par défaut
    this.showUserList();
  }

  /**
   * Initialisation de l'interface graphique
   */
  protected void initGUI() {
    this.setLayout(new BorderLayout(10, 10));
    this.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Création des composants
    mUserListPanel = new UserListPanel(mDatabase, mSessionManager);
    mUserListPanel.setUserSelectedCallback(this::showUserProfile);

    mContentPanel = new JPanel(new BorderLayout());

    // Bouton de retour (initialement masqué)
    mBackButton = new JButton("Retour à la liste");
    mBackButton.addActionListener(new BackActionListener());
    mBackButton.setVisible(false);

    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.add(mBackButton, BorderLayout.WEST);

    // Assemblage
    this.add(headerPanel, BorderLayout.NORTH);
    this.add(mContentPanel, BorderLayout.CENTER);
  }

  /**
   * Définit le callback appelé lorsque l'utilisateur clique sur "Retour"
   */
  public void setBackCallback(Consumer<Void> callback) {
    this.mBackCallback = callback;
  }

  /**
   * Affiche la liste des utilisateurs
   */
  public void showUserList() {
    // Vider le panel de contenu
    mContentPanel.removeAll();

    // Ajouter la liste des utilisateurs
    mContentPanel.add(mUserListPanel, BorderLayout.CENTER);

    // Masquer le bouton de retour
    mBackButton.setVisible(false);

    // Redessiner le panel
    mContentPanel.revalidate();
    mContentPanel.repaint();
  }

  /**
   * Affiche le profil d'un utilisateur
   */
  public void showUserProfile(User user) {
    if (user == null) {
      return;
    }

    // Créer le panel de profil
    mUserProfilePanel = new UserProfilePanel(mDatabase, mSessionManager, user);

    // Vider le panel de contenu
    mContentPanel.removeAll();

    // Ajouter le profil
    mContentPanel.add(mUserProfilePanel, BorderLayout.CENTER);

    // Afficher le bouton de retour
    mBackButton.setVisible(true);

    // Redessiner le panel
    mContentPanel.revalidate();
    mContentPanel.repaint();
  }

  @Override
  public void notifyLogin(User connectedUser) {
    // Actualiser l'affichage
    if (mUserProfilePanel != null) {
      mUserProfilePanel.updateButtons();
    }
  }

  @Override
  public void notifyLogout() {
    // Revenir à la liste des utilisateurs
    showUserList();
  }

  /**
   * Classe interne pour gérer le retour à la liste
   */
  class BackActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      showUserList();

      // Appeler le callback si défini
      if (mBackCallback != null) {
        mBackCallback.accept(null);
      }
    }
  }
}