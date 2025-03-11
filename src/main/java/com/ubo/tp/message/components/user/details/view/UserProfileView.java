package com.ubo.tp.message.components.user.details.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.common.ui.SwingTheme;
import com.ubo.tp.message.components.message.view.MessageBubble;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Vue pour l'affichage du profil d'un utilisateur.
 */
public class UserProfileView extends JPanel implements IUserProfileView {

  /**
   * Tag de l'utilisateur.
   */
  private JLabel userTagLabel;

  /**
   * Nom de l'utilisateur.
   */
  private JLabel userNameLabel;

  /**
   * Nombre de followers.
   */
  private JLabel followersCountLabel;

  /**
   * Nombre d'utilisateurs suivis.
   */
  private JLabel followingCountLabel;

  /**
   * Bouton pour suivre/ne plus suivre l'utilisateur.
   */
  private JButton followButton;

  /**
   * Bouton pour revenir à la liste des utilisateurs.
   */
  private JButton backButton;

  /**
   * Panneau pour les messages de l'utilisateur.
   */
  private JPanel messagesPanel;

  /**
   * Panneau avec défilement pour les messages.
   */
  private JScrollPane messagesScrollPane;

  /**
   * Liste des followers.
   */
  private JList<String> followersList;

  /**
   * Liste des utilisateurs suivis.
   */
  private JList<String> followingList;

  /**
   * Label pour les messages d'erreur.
   */
  private JLabel errorLabel;

  /**
   * Label pour les messages de succès.
   */
  private JLabel successLabel;

  /**
   * Constructeur.
   */
  public UserProfileView() {
    this.initGUI();
  }

  /**
   * Initialisation de l'interface graphique.
   */
  // Dans la méthode initGUI() de UserProfileView
  private void initGUI() {
    // Configuration du panneau principal
    this.setBackground(SwingTheme.BACKGROUND);
    this.setLayout(new BorderLayout(15, 15));
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Panneau d'en-tête avec les informations de l'utilisateur
    JPanel headerPanel = SwingTheme.createStyledPanel(null);

    // Panneau pour le bouton de retour
    JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    backPanel.setOpaque(false);
    backButton = new JButton("Retour");
    backButton.setFont(SwingTheme.TEXT_REGULAR);
    backButton.setBackground(SwingTheme.ALTERNATE);
    backButton.setForeground(SwingTheme.TEXT);
    backButton.setFocusPainted(false);
    backButton.setBorder(SwingTheme.createRoundedBorder());
    backPanel.add(backButton);
    headerPanel.add(backPanel, BorderLayout.NORTH);

    // Panneau d'informations utilisateur
    JPanel userInfoPanel = new JPanel(new BorderLayout(20, 10));
    userInfoPanel.setOpaque(false);

    // Avatar et informations de base
    JPanel avatarPanel = new JPanel(new BorderLayout(10, 10));
    avatarPanel.setOpaque(false);

    JLabel userIconLabel = new JLabel(IconFactory.createUserIcon(IconFactory.ICON_LARGE));
    userIconLabel.setPreferredSize(new Dimension(80, 80));
    avatarPanel.add(userIconLabel, BorderLayout.CENTER);

    userInfoPanel.add(avatarPanel, BorderLayout.WEST);

    // Détails de l'utilisateur
    JPanel detailsPanel = new JPanel(new GridBagLayout());
    detailsPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 5, 3, 5);

    // Tag utilisateur
    userTagLabel = new JLabel();
    userTagLabel.setFont(SwingTheme.TITLE_FONT);
    userTagLabel.setForeground(SwingTheme.PRIMARY);
    gbc.gridx = 0;
    gbc.gridy = 0;
    detailsPanel.add(userTagLabel, gbc);

    // Nom utilisateur
    userNameLabel = new JLabel();
    userNameLabel.setFont(SwingTheme.TEXT_REGULAR);
    userNameLabel.setForeground(SwingTheme.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 1;
    detailsPanel.add(userNameLabel, gbc);

    // Statistiques
    JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
    statsPanel.setOpaque(false);

    JPanel followersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    followersPanel.setOpaque(false);
    followersPanel.add(new JLabel("Followers:"));
    followersCountLabel = new JLabel("0");
    followersCountLabel.setFont(SwingTheme.TEXT_BOLD);
    followersPanel.add(followersCountLabel);
    statsPanel.add(followersPanel);

    JPanel followingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    followingPanel.setOpaque(false);
    followingPanel.add(new JLabel("Abonnements:"));
    followingCountLabel = new JLabel("0");
    followingCountLabel.setFont(SwingTheme.TEXT_BOLD);
    followingPanel.add(followingCountLabel);
    statsPanel.add(followingPanel);

    gbc.gridx = 0;
    gbc.gridy = 2;
    detailsPanel.add(statsPanel, gbc);

    userInfoPanel.add(detailsPanel, BorderLayout.CENTER);

    // Bouton suivre/ne plus suivre
    followButton = new JButton("Suivre");
    SwingTheme.styleButton(followButton, false);
    followButton.setBackground(SwingTheme.SECONDARY);

    JPanel followButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    followButtonPanel.setOpaque(false);
    followButtonPanel.add(followButton);
    userInfoPanel.add(followButtonPanel, BorderLayout.EAST);

    headerPanel.add(userInfoPanel, BorderLayout.CENTER);
    this.add(headerPanel, BorderLayout.NORTH);

    // Panneau central avec onglets
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setFont(SwingTheme.TEXT_REGULAR);
    tabbedPane.setBackground(SwingTheme.PANEL);

    // Onglet des messages
    messagesPanel = new JPanel();
    messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
    messagesPanel.setBackground(SwingTheme.BACKGROUND);
    messagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    messagesScrollPane = new JScrollPane(messagesPanel);
    messagesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    messagesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    messagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    messagesScrollPane.setBorder(BorderFactory.createEmptyBorder());
    messagesScrollPane.setPreferredSize(new Dimension(400, 300));

    tabbedPane.addTab("Messages", messagesScrollPane);

    // Onglet des followers
    JPanel followersListPanel = new JPanel(new BorderLayout());
    followersListPanel.setBackground(SwingTheme.PANEL);

    followersList = new JList<>();
    SwingTheme.styleList(followersList);

    // Style pour les éléments de la liste
    followersList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value,
                                                    int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(
          list, value, index, isSelected, cellHasFocus);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        label.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
        if (!isSelected) {
          label.setBackground(index % 2 == 0 ? SwingTheme.BACKGROUND : SwingTheme.PANEL);
        }
        return label;
      }
    });

    JScrollPane followersScrollPane = new JScrollPane(followersList);
    followersScrollPane.setBorder(BorderFactory.createEmptyBorder());
    followersListPanel.add(followersScrollPane, BorderLayout.CENTER);
    tabbedPane.addTab("Followers", followersListPanel);

    // Onglet des abonnements
    JPanel followingListPanel = new JPanel(new BorderLayout());
    followingListPanel.setBackground(SwingTheme.PANEL);

    followingList = new JList<>();
    SwingTheme.styleList(followingList);

    // Même style pour les abonnements
    followingList.setCellRenderer(followersList.getCellRenderer());

    JScrollPane followingScrollPane = new JScrollPane(followingList);
    followingScrollPane.setBorder(BorderFactory.createEmptyBorder());
    followingListPanel.add(followingScrollPane, BorderLayout.CENTER);
    tabbedPane.addTab("Abonnements", followingListPanel);

    this.add(tabbedPane, BorderLayout.CENTER);

    // Panneau d'état
    JPanel statusPanel = new JPanel(new GridLayout(2, 1, 5, 5));
    statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
    statusPanel.setOpaque(false);

    errorLabel = new JLabel(" ");
    errorLabel.setForeground(SwingTheme.DANGER);
    errorLabel.setFont(SwingTheme.TOOLTIP_FONT);
    statusPanel.add(errorLabel);

    successLabel = new JLabel(" ");
    successLabel.setForeground(SwingTheme.SUCCESS);
    successLabel.setFont(SwingTheme.TOOLTIP_FONT);
    statusPanel.add(successLabel);

    this.add(statusPanel, BorderLayout.SOUTH);
  }

  /**
   * Met à jour les informations de l'utilisateur affiché.
   * @param user L'utilisateur à afficher.
   * @param followersCount Le nombre de followers.
   * @param followingCount Le nombre d'utilisateurs suivis.
   * @param isFollowed Indique si l'utilisateur est suivi par l'utilisateur connecté.
   */
  public void updateUserInfo(User user, int followersCount, int followingCount, boolean isFollowed) {
    if (user != null) {
      userTagLabel.setText(user.getUserTag());
      userNameLabel.setText(user.getName());
      followersCountLabel.setText(String.valueOf(followersCount));
      followingCountLabel.setText(String.valueOf(followingCount));

      // Mise à jour du bouton de suivi
      followButton.setText(isFollowed ? "Ne plus suivre" : "Suivre");

      // Activer le bouton de suivi uniquement si on n'est pas sur son propre profil
      User connectedUser = (User) getClientProperty("connectedUser");
      followButton.setEnabled(connectedUser == null || !connectedUser.equals(user));
    }
  }

  /**
   * Met à jour la liste des messages de l'utilisateur.
   * @param messages La liste des messages à afficher.
   */
  @Override
  public void updateMessages(Set<Message> messages) {
    // Vider le panneau des messages
    messagesPanel.removeAll();

    // Trier les messages par date (du plus récent au plus ancien)
    List<Message> sortedMessages = messages.stream()
      .sorted((m1, m2) -> Long.compare(m2.getEmissionDate(), m1.getEmissionDate()))
      .toList();

    // Ajouter les messages au panneau
    for (Message message : sortedMessages) {
      String senderDisplayName = message.getSender().getName() + " (" + message.getSender().getUserTag() + ")";
      String timeString = new SimpleDateFormat("HH:mm").format(new Date(message.getEmissionDate()));
      MessageBubble bubble = new MessageBubble(senderDisplayName, message.getText(), false, timeString);

      // Créer un panneau wrapper pour aligner le message à gauche
      JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
      wrapper.setOpaque(false);
      wrapper.add(bubble);

      // Éviter que le wrapper ne s'étende sur toute la hauteur
      wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                           bubble.getPreferredSize().height + 10));

      // Ajouter le wrapper au panneau des messages
      messagesPanel.add(wrapper);

      // Petit espace vertical après chaque message
      messagesPanel.add(Box.createVerticalStrut(5));
    }

    // Rafraîchir le panneau
    messagesPanel.revalidate();
    messagesPanel.repaint();

    // Défiler vers le haut pour voir les messages les plus récents
    SwingUtilities.invokeLater(() -> {
      JScrollBar vertical = messagesScrollPane.getVerticalScrollBar();
      vertical.setValue(0);
    });
  }

  /**
   * Met à jour la liste des followers.
   * @param followers La liste des followers à afficher.
   */
  @Override
  public void updateFollowers(Set<User> followers) {
    DefaultListModel<String> model = new DefaultListModel<>();

    // Ajouter chaque follower à la liste
    for (User follower : followers) {
      model.addElement(follower.getName() + " (" + follower.getUserTag() + ")");

      // Stocker l'objet User dans les propriétés client de la liste
      int index = model.getSize() - 1;
      followersList.putClientProperty("follower_" + index, follower);
    }

    followersList.setModel(model);
  }

  /**
   * Met à jour la liste des utilisateurs suivis.
   * @param following La liste des utilisateurs suivis à afficher.
   */
  @Override
  public void updateFollowing(Set<User> following) {
    DefaultListModel<String> model = new DefaultListModel<>();

    // Ajouter chaque utilisateur suivi à la liste
    for (User followed : following) {
      model.addElement(followed.getName() + " (" + followed.getUserTag() + ")");

      // Stocker l'objet User dans les propriétés client de la liste
      int index = model.getSize() - 1;
      followingList.putClientProperty("following_" + index, followed);
    }

    followingList.setModel(model);
  }

  /**
   * Définit l'écouteur pour le bouton de retour.
   * @param listener L'écouteur à définir.
   */
  @Override
  public void setBackButtonListener(ActionListener listener) {
    backButton.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton de suivi.
   * @param listener L'écouteur à définir.
   */
  @Override
  public void setFollowButtonListener(ActionListener listener) {
    followButton.addActionListener(listener);
  }

  /**
   * Affiche un message d'erreur.
   * @param message Le message d'erreur à afficher.
   */
  @Override
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
    successLabel.setText(" ");
  }

  /**
   * Affiche un message de succès.
   * @param message Le message de succès à afficher.
   */
  @Override
  public void setSuccessMessage(String message) {
    successLabel.setText(message);
    errorLabel.setText(" ");
  }

  /**
   * Réinitialise la vue.
   */
  @Override
  public void reset() {
    userTagLabel.setText("");
    userNameLabel.setText("");
    followersCountLabel.setText("0");
    followingCountLabel.setText("0");

    messagesPanel.removeAll();
    messagesPanel.revalidate();
    messagesPanel.repaint();

    DefaultListModel<String> emptyModel = new DefaultListModel<>();
    followersList.setModel(emptyModel);
    followingList.setModel(emptyModel);

    errorLabel.setText(" ");
    successLabel.setText(" ");
  }

  /**
   * Active ou désactive tous les composants de la vue.
   */
  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    backButton.setEnabled(enabled);
    followButton.setEnabled(enabled);
    followersList.setEnabled(enabled);
    followingList.setEnabled(enabled);
  }
}