package com.ubo.tp.message.components.user.details.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.components.message.view.MessageBubble;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Vue pour l'affichage du profil d'un utilisateur.
 */
public class UserProfileView extends JPanel {

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
  private void initGUI() {
    JLabel userIconLabel;
    this.setLayout(new BorderLayout(10, 10));
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Panneau d'en-tête avec les informations de l'utilisateur
    JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
    headerPanel.setBorder(new TitledBorder("Profil utilisateur"));

    // Panneau pour le bouton de retour
    JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    backButton = new JButton("Retour");
    backPanel.add(backButton);
    headerPanel.add(backPanel, BorderLayout.NORTH);

    // Panneau pour les informations utilisateur
    JPanel userInfoPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    // Icône utilisateur
    userIconLabel = new JLabel(IconFactory.createUserIcon(IconFactory.ICON_LARGE));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    userInfoPanel.add(userIconLabel, gbc);

    // Tag utilisateur
    userTagLabel = new JLabel("");
    userTagLabel.setFont(new Font("Arial", Font.BOLD, 16));
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.WEST;
    userInfoPanel.add(userTagLabel, gbc);

    // Nom utilisateur
    userNameLabel = new JLabel("");
    gbc.gridx = 1;
    gbc.gridy = 1;
    userInfoPanel.add(userNameLabel, gbc);

    // Stats utilisateur
    JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

    // Nombre de followers
    JPanel followersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    followersPanel.add(new JLabel("Followers:"));
    followersCountLabel = new JLabel("0");
    followersCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
    followersPanel.add(followersCountLabel);
    statsPanel.add(followersPanel);

    // Nombre d'utilisateurs suivis
    JPanel followingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    followingPanel.add(new JLabel("Abonnements:"));
    followingCountLabel = new JLabel("0");
    followingCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
    followingPanel.add(followingCountLabel);
    statsPanel.add(followingPanel);

    // Bouton pour suivre/ne plus suivre
    followButton = new JButton("Suivre");
    statsPanel.add(followButton);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    userInfoPanel.add(statsPanel, gbc);

    headerPanel.add(userInfoPanel, BorderLayout.CENTER);

    // Panneau central pour les onglets
    JTabbedPane tabbedPane = new JTabbedPane();

    // Onglet des messages
    messagesPanel = new JPanel();
    messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
    messagesPanel.setBackground(new Color(245, 245, 245));
    messagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    messagesScrollPane = new JScrollPane(messagesPanel);
    messagesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    messagesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    messagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    messagesScrollPane.setPreferredSize(new Dimension(400, 300));

    tabbedPane.addTab("Messages", messagesScrollPane);

    // Onglet des followers
    JPanel followersListPanel = new JPanel(new BorderLayout());
    followersList = new JList<>();
    followersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Écouteur pour le double-clic sur les followers
    followersList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          // Double-clic détecté, récupérer l'utilisateur sélectionné
          int index = followersList.locationToIndex(e.getPoint());
          if (index != -1) {
            // Traiter la sélection (à implémenter dans le contrôleur)
          }
        }
      }
    });

    JScrollPane followersScrollPane = new JScrollPane(followersList);
    followersListPanel.add(followersScrollPane, BorderLayout.CENTER);
    tabbedPane.addTab("Followers", followersListPanel);

    // Onglet des utilisateurs suivis
    JPanel followingListPanel = new JPanel(new BorderLayout());
    followingList = new JList<>();
    followingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Écouteur pour le double-clic sur les utilisateurs suivis
    followingList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          // Double-clic détecté, récupérer l'utilisateur sélectionné
          int index = followingList.locationToIndex(e.getPoint());
          if (index != -1) {
            // Traiter la sélection (à implémenter dans le contrôleur)
          }
        }
      }
    });

    JScrollPane followingScrollPane = new JScrollPane(followingList);
    followingListPanel.add(followingScrollPane, BorderLayout.CENTER);
    tabbedPane.addTab("Abonnements", followingListPanel);

    // Panneau d'état pour les messages d'erreur et de succès
    JPanel statusPanel = new JPanel(new GridLayout(2, 1));

    errorLabel = new JLabel(" ");
    errorLabel.setForeground(Color.RED);
    statusPanel.add(errorLabel);

    successLabel = new JLabel(" ");
    successLabel.setForeground(new Color(0, 128, 0)); // Vert
    statusPanel.add(successLabel);

    // Assemblage final
    this.add(headerPanel, BorderLayout.NORTH);
    this.add(tabbedPane, BorderLayout.CENTER);
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
  public void setBackButtonListener(ActionListener listener) {
    backButton.addActionListener(listener);
  }

  /**
   * Définit l'écouteur pour le bouton de suivi.
   * @param listener L'écouteur à définir.
   */
  public void setFollowButtonListener(ActionListener listener) {
    followButton.addActionListener(listener);
  }

  /**
   * Affiche un message d'erreur.
   * @param message Le message d'erreur à afficher.
   */
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
    successLabel.setText(" ");
  }

  /**
   * Affiche un message de succès.
   * @param message Le message de succès à afficher.
   */
  public void setSuccessMessage(String message) {
    successLabel.setText(message);
    errorLabel.setText(" ");
  }

  /**
   * Réinitialise la vue.
   */
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
}