package com.ubo.tp.message.components.user.search.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.common.ui.SearchBar;
import com.ubo.tp.message.common.ui.SwingTheme;
import com.ubo.tp.message.core.datamodel.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

/**
 * Vue pour l'interface de recherche d'utilisateurs.
 */
public class SearchView extends JPanel implements ISearchView {
  /**
   * Barre de recherche.
   */
  private SearchBar searchBar;

  /**
   * Modèle de la table des utilisateurs.
   */
  private DefaultTableModel userTableModel;

  /**
   * Table des utilisateurs.
   */
  private JTable userTable;

  /**
   * Bouton pour voir le profil.
   */
  private JButton viewProfileButton;

  /**
   * Bouton pour suivre/ne plus suivre.
   */
  private JButton followButton;

  /**
   * Étiquette pour les messages d'erreur.
   */
  private JLabel errorLabel;

  /**
   * Étiquette pour les messages de succès.
   */
  private JLabel successLabel;

  /**
   * Constructeur.
   */
  public SearchView() {
    this.initGUI();
  }

  /**
   * Initialisation de l'interface graphique.
   */
  private void initGUI() {
    // Configuration du panneau principal
    this.setBackground(SwingTheme.BACKGROUND);
    this.setLayout(new BorderLayout(15, 15));
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Panneau de recherche
    JPanel searchPanel = SwingTheme.createStyledPanel("Rechercher des utilisateurs");

    // Barre de recherche personnalisée
    JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
    searchBarPanel.setOpaque(false);

    searchBar = new SearchBar("", 25, "Rechercher");
    // Styliser la barre de recherche (ajouter une méthode dans SearchBar si nécessaire)
    searchBar.setBackground(SwingTheme.BACKGROUND);
    searchBarPanel.add(searchBar, BorderLayout.CENTER);

    searchPanel.add(searchBarPanel, BorderLayout.CENTER);
    this.add(searchPanel, BorderLayout.NORTH);

    // Panneau de résultats
    JPanel resultsPanel = SwingTheme.createStyledPanel("Résultats de la recherche");

    // Table des résultats avec style amélioré
    String[] columnNames = {"", "Tag", "Nom", "Abonnés"};
    userTableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
          return Icon.class;
        }
        return String.class;
      }
    };

    userTable = new JTable(userTableModel);
    SwingTheme.styleTable(userTable);
    userTable.getColumnModel().getColumn(0).setMaxWidth(40);
    userTable.getColumnModel().getColumn(1).setPreferredWidth(120);
    userTable.getColumnModel().getColumn(2).setPreferredWidth(180);
    userTable.getColumnModel().getColumn(3).setPreferredWidth(80);

    // Double-clic pour voir le profil
    userTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          viewProfile();
        }
      }
    });

    JScrollPane scrollPane = new JScrollPane(userTable);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setPreferredSize(new Dimension(400, 300));
    resultsPanel.add(scrollPane, BorderLayout.CENTER);

    // Panneau d'actions
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    actionsPanel.setOpaque(false);

    viewProfileButton = new JButton("Voir profil");
    viewProfileButton.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    SwingTheme.styleButton(viewProfileButton, true);
    viewProfileButton.setEnabled(false);
    actionsPanel.add(viewProfileButton);

    followButton = new JButton("Suivre");
    SwingTheme.styleButton(followButton, false);
    followButton.setBackground(SwingTheme.SECONDARY); // Vert pour "Suivre"
    followButton.setEnabled(false);
    actionsPanel.add(followButton);

    resultsPanel.add(actionsPanel, BorderLayout.SOUTH);
    this.add(resultsPanel, BorderLayout.CENTER);

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

    // Ajout d'un listener de sélection pour activer/désactiver les boutons
    userTable.getSelectionModel().addListSelectionListener(e -> {
      boolean hasSelection = userTable.getSelectedRow() != -1;
      viewProfileButton.setEnabled(hasSelection);
      followButton.setEnabled(hasSelection);

      // Mise à jour du texte du bouton suivre si sélection
      if (hasSelection) {
        int selectedRow = userTable.getSelectedRow();
        boolean isFollowed = (boolean) userTable.getClientProperty("followed_" + selectedRow);
        followButton.setText(isFollowed ? "Ne plus suivre" : "Suivre");

        // Ajustement de la couleur en fonction de l'état
        if (isFollowed) {
          followButton.setBackground(SwingTheme.ALTERNATE);
          followButton.setForeground(SwingTheme.TEXT);
        } else {
          followButton.setBackground(SwingTheme.SECONDARY);
          followButton.setForeground(SwingTheme.TEXT_ON_COLOR);
        }
      }
    });
  }

  /**
   * Action de visualisation du profil de l'utilisateur sélectionné.
   */
  private void viewProfile() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow != -1) {
      viewProfileButton.doClick();
    }
  }

  /**
   * Définit l'écouteur d'événements pour la barre de recherche.
   * @param listener L'écouteur à définir.
   */
  public void setSearchActionListener(ActionListener listener) {
    searchBar.addSearchAction(listener);
  }

  /**
   * Définit l'écouteur d'événements pour le bouton de visualisation du profil.
   * @param listener L'écouteur à définir.
   */
  public void setViewProfileButtonListener(ActionListener listener) {
    viewProfileButton.addActionListener(listener);
  }

  /**
   * Définit l'écouteur d'événements pour le bouton de suivi.
   * @param listener L'écouteur à définir.
   */
  public void setFollowButtonListener(ActionListener listener) {
    followButton.addActionListener(listener);
  }

  /**
   * Récupère la requête de recherche saisie.
   * @return La requête de recherche.
   */
  public String getSearchQuery() {
    return searchBar.getSearchQuery();
  }

  /**
   * Met à jour la liste des utilisateurs affichés.
   * @param users La liste des utilisateurs à afficher.
   */
  public void updateUsersList(Set<User> users, Set<String> followedTags) {
    // Vider la table
    userTableModel.setRowCount(0);

    // Remplir la table avec les utilisateurs
    for (User user : users) {
      boolean isFollowed = followedTags.contains(user.getUserTag());

      Object[] rowData = {
        IconFactory.createUserIcon(IconFactory.ICON_SMALL),
        user.getUserTag(),
        user.getName(),
        "-" // Le nombre de followers sera rempli par le contrôleur
      };

      userTableModel.addRow(rowData);

      // Stocker l'objet User dans les données de la table pour pouvoir le récupérer plus tard
      int rowIndex = userTableModel.getRowCount() - 1;
      userTable.putClientProperty("user_" + rowIndex, user);
      userTable.putClientProperty("followed_" + rowIndex, isFollowed);
    }

    // Mettre à jour l'apparence du tableau
    userTable.repaint();
  }

  /**
   * Met à jour le nombre de followers pour un utilisateur.
   * @param rowIndex L'indice de la ligne de l'utilisateur.
   * @param followersCount Le nombre de followers.
   */
  public void updateFollowersCount(int rowIndex, int followersCount) {
    if (rowIndex >= 0 && rowIndex < userTableModel.getRowCount()) {
      userTableModel.setValueAt(String.valueOf(followersCount), rowIndex, 3);
    }
  }

  /**
   * Met à jour l'état du bouton de suivi pour l'utilisateur sélectionné.
   */
  public void updateFollowButton() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow != -1) {
      boolean isFollowed = (boolean) userTable.getClientProperty("followed_" + selectedRow);
      followButton.setText(isFollowed ? "Ne plus suivre" : "Suivre");
    }
  }

  /**
   * Récupère l'utilisateur sélectionné dans la table.
   * @return L'utilisateur sélectionné ou null si aucun utilisateur n'est sélectionné.
   */
  public User getSelectedUser() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow != -1) {
      return (User) userTable.getClientProperty("user_" + selectedRow);
    }
    return null;
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
   * Réinitialise les messages d'état.
   */
  public void clearMessages() {
    errorLabel.setText(" ");
    successLabel.setText(" ");
  }

  /**
   * Réinitialise la vue.
   */
  public void reset() {
    searchBar.clearSearch();
    userTableModel.setRowCount(0);
    clearMessages();
  }
}