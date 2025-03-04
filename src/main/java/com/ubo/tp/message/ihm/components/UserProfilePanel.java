package com.ubo.tp.message.ihm.components;

import com.ubo.tp.message.common.IconFactory;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.ihm.session.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

/**
 * Panneau affichant le profil d'un utilisateur
 */
public class UserProfilePanel extends JPanel implements ISessionObserver {
  /**
   * Base de données
   */
  protected final IDatabase mDatabase;

  /**
   * Gestionnaire de session
   */
  protected final SessionManager mSessionManager;

  /**
   * Utilisateur dont le profil est affiché
   */
  protected User mDisplayedUser;

  /**
   * Panel pour l'avatar
   */
  protected JPanel mAvatarPanel;

  /**
   * Label pour le nom
   */
  protected JLabel mNameLabel;

  /**
   * Label pour le tag
   */
  protected JLabel mTagLabel;

  /**
   * Label pour le nombre d'abonnés
   */
  protected JLabel mFollowersLabel;

  /**
   * Label pour le nombre d'abonnements
   */
  protected JLabel mFollowingLabel;

  /**
   * Label pour le nombre de messages
   */
  protected JLabel mMessagesLabel;

  /**
   * Bouton pour suivre/ne plus suivre l'utilisateur
   */
  protected JButton mFollowButton;

  /**
   * Bouton pour se déconnecter
   */
  protected JButton mLogoutButton;

  /**
   * Constructeur pour afficher le profil de l'utilisateur connecté
   */
  public UserProfilePanel(IDatabase database, SessionManager sessionManager) {
    this.mDatabase = database;
    this.mSessionManager = sessionManager;

    // Initialiser l'interface AVANT de s'enregistrer comme observateur
    this.initGUI();

    // S'enregistrer comme observateur de session
    this.mSessionManager.addSessionObserver(this);

    // Afficher le profil de l'utilisateur connecté
    if (this.mSessionManager.isUserConnected()) {
      this.displayUserProfile(this.mSessionManager.getConnectedUser());
    }
  }

  /**
   * Constructeur pour afficher le profil d'un utilisateur spécifique
   */
  public UserProfilePanel(IDatabase database, SessionManager sessionManager, User user) {
    this.mDatabase = database;
    this.mSessionManager = sessionManager;

    // Initialiser l'interface AVANT de s'enregistrer comme observateur
    this.initGUI();

    // S'enregistrer comme observateur de session
    this.mSessionManager.addSessionObserver(this);

    // Afficher le profil de l'utilisateur spécifié
    this.displayUserProfile(user);
  }

  /**
   * Initialisation de l'interface graphique
   */
  protected void initGUI() {
    this.setLayout(new BorderLayout(10, 10));
    this.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Panel pour l'en-tête du profil
    JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
    headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

    // Panel pour l'avatar
    mAvatarPanel = new JPanel();
    mAvatarPanel.setPreferredSize(new Dimension(100, 100));
    mAvatarPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    headerPanel.add(mAvatarPanel, BorderLayout.WEST);

    // Panel pour les informations utilisateur
    JPanel infoPanel = new JPanel(new GridLayout(3, 1));

    mNameLabel = new JLabel("Nom: ");
    mNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
    infoPanel.add(mNameLabel);

    mTagLabel = new JLabel("Tag: ");
    infoPanel.add(mTagLabel);

    JPanel followPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
    mFollowersLabel = new JLabel("Abonnés: 0");
    mFollowingLabel = new JLabel("Abonnements: 0");
    mMessagesLabel = new JLabel("Messages: 0");
    followPanel.add(mFollowersLabel);
    followPanel.add(mFollowingLabel);
    followPanel.add(mMessagesLabel);
    infoPanel.add(followPanel);

    headerPanel.add(infoPanel, BorderLayout.CENTER);

    // Panel pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    mFollowButton = new JButton("Suivre");
    mFollowButton.addActionListener(new FollowActionListener());
    buttonPanel.add(mFollowButton);

    mLogoutButton = new JButton("Se déconnecter");
    mLogoutButton.addActionListener(e -> mSessionManager.logout());
    buttonPanel.add(mLogoutButton);

    headerPanel.add(buttonPanel, BorderLayout.EAST);

    this.add(headerPanel, BorderLayout.NORTH);

    // Panel pour la liste des messages
    JPanel messagesPanel = new JPanel(new BorderLayout());
    messagesPanel.setBorder(new TitledBorder("Messages"));

    // Placeholder pour la liste des messages
    messagesPanel.add(new JLabel("Aucun message à afficher", JLabel.CENTER), BorderLayout.CENTER);

    this.add(messagesPanel, BorderLayout.CENTER);
  }

  /**
   * Affiche le profil d'un utilisateur
   */
  public void displayUserProfile(User user) {
    if (user == null) {
      return;
    }

    this.mDisplayedUser = user;

    // Vérifier que les composants ont été initialisés
    if (mNameLabel == null || mTagLabel == null || mFollowersLabel == null ||
      mFollowingLabel == null || mMessagesLabel == null) {
      System.err.println("Erreur: Les composants d'interface n'ont pas été initialisés correctement");
      return;
    }

    // Mettre à jour les informations
    mNameLabel.setText("Nom: " + user.getName());
    mTagLabel.setText("Tag: " + user.getUserTag());

    // Mettre à jour les compteurs
    int followersCount = mDatabase.getFollowersCount(user);
    mFollowersLabel.setText("Abonnés: " + followersCount);

    int followingCount = user.getFollows().size();
    mFollowingLabel.setText("Abonnements: " + followingCount);

    Set<Message> userMessages = mDatabase.getUserMessages(user);
    mMessagesLabel.setText("Messages: " + userMessages.size());

    // Mettre à jour l'avatar
    mAvatarPanel.removeAll();
    if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
      File avatarFile = new File(user.getAvatarPath());
      if (avatarFile.exists()) {
        try {
          ImageIcon avatarIcon = new ImageIcon(user.getAvatarPath());
          Image scaledImage = avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
          JLabel avatarLabel = new JLabel(new ImageIcon(scaledImage));
          mAvatarPanel.add(avatarLabel);
        } catch (Exception e) {
          // Si l'image ne peut pas être chargée, afficher une icône par défaut
          mAvatarPanel.add(new JLabel(IconFactory.createUserIcon(IconFactory.ICON_LARGE)));
        }
      } else {
        // Si le fichier n'existe pas, afficher une icône par défaut
        mAvatarPanel.add(new JLabel(IconFactory.createUserIcon(IconFactory.ICON_LARGE)));
      }
    } else {
      // Si aucun avatar n'est défini, afficher une icône par défaut
      mAvatarPanel.add(new JLabel(IconFactory.createUserIcon(IconFactory.ICON_LARGE)));
    }

    // Mettre à jour les boutons
    updateButtons();

    // Redessiner le panel
    mAvatarPanel.revalidate();
    mAvatarPanel.repaint();
    this.revalidate();
    this.repaint();
  }

  /**
   * Met à jour l'état des boutons en fonction de l'utilisateur affiché et de l'utilisateur connecté
   */
  protected void updateButtons() {
    User connectedUser = mSessionManager.getConnectedUser();

    // Si aucun utilisateur n'est connecté, désactiver les boutons
    if (connectedUser == null) {
      mFollowButton.setEnabled(false);
      mLogoutButton.setEnabled(false);
      return;
    }

    // Activer le bouton de déconnexion
    mLogoutButton.setEnabled(true);

    // Si l'utilisateur affiché est l'utilisateur connecté, masquer le bouton suivre
    if (mDisplayedUser.equals(connectedUser)) {
      mFollowButton.setVisible(false);
      return;
    }

    // Sinon, afficher le bouton suivre
    mFollowButton.setVisible(true);
    mFollowButton.setEnabled(true);

    // Vérifier si l'utilisateur connecté suit déjà l'utilisateur affiché
    if (connectedUser.isFollowing(mDisplayedUser)) {
      mFollowButton.setText("Ne plus suivre");
    } else {
      mFollowButton.setText("Suivre");
    }
  }

  @Override
  public void notifyLogin(User connectedUser) {
    // Si l'utilisateur affiché n'est pas défini, afficher l'utilisateur connecté
    if (mDisplayedUser == null) {
      displayUserProfile(connectedUser);
    } else {
      // Sinon, juste mettre à jour les boutons
      updateButtons();
    }
  }

  @Override
  public void notifyLogout() {
    // Désactiver les boutons
    mFollowButton.setEnabled(false);
    mLogoutButton.setEnabled(false);
  }

  /**
   * Classe interne pour gérer l'action de suivre/ne plus suivre
   */
  class FollowActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      User connectedUser = mSessionManager.getConnectedUser();

      if (connectedUser == null || mDisplayedUser == null) {
        return;
      }

      // Vérifier si l'utilisateur connecté suit déjà l'utilisateur affiché
      if (connectedUser.isFollowing(mDisplayedUser)) {
        // Ne plus suivre
        connectedUser.removeFollowing(mDisplayedUser.getUserTag());
        mFollowButton.setText("Suivre");
      } else {
        // Suivre
        connectedUser.addFollowing(mDisplayedUser.getUserTag());
        mFollowButton.setText("Ne plus suivre");
      }

      // Mettre à jour l'utilisateur dans la base de données
      mDatabase.modifiyUser(connectedUser);

      // Mettre à jour les compteurs
      int followersCount = mDatabase.getFollowersCount(mDisplayedUser);
      mFollowersLabel.setText("Abonnés: " + followersCount);
    }
  }
}