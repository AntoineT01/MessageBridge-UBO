package com.ubo.tp.message.components.user.search.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.common.ui.SearchBar;
import com.ubo.tp.message.core.datamodel.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

/**
 * Vue pour l'interface de recherche d'utilisateurs.
 */
public class SearchView extends JPanel {
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
    this.setLayout(new BorderLayout(10, 10));
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Panneau de recherche
    JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
    searchPanel.setBorder(new TitledBorder("Rechercher des utilisateurs"));

    searchBar = new SearchBar("Recherche:", 25, "Rechercher");
    searchPanel.add(searchBar, BorderLayout.NORTH);

    // Panneau de résultats
    JPanel resultsPanel = new JPanel(new BorderLayout(5, 5));

    // Création du modèle de table
    String[] columnNames = {"", "Tag", "Nom", "Abonnés"};
    userTableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Rendre toutes les cellules non éditables
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
          return Icon.class; // La première colonne contient des icônes
        }
        return String.class;
      }
    };

    userTable = new JTable(userTableModel);
    userTable.setRowHeight(30);
    userTable.getColumnModel().getColumn(0).setMaxWidth(30);
    userTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    userTable.getColumnModel().getColumn(2).setPreferredWidth(150);
    userTable.getColumnModel().getColumn(3).setPreferredWidth(60);

    // Ajout d'un écouteur pour le double clic
    userTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          viewProfile();
        }
      }
    });

    JScrollPane scrollPane = new JScrollPane(userTable);
    scrollPane.setPreferredSize(new Dimension(400, 300));
    resultsPanel.add(scrollPane, BorderLayout.CENTER);

    // Panneau d'actions
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    viewProfileButton = new JButton("Voir profil");
    viewProfileButton.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    viewProfileButton.setEnabled(false);
    actionsPanel.add(viewProfileButton);

    followButton = new JButton("Suivre");
    followButton.setEnabled(false);
    actionsPanel.add(followButton);

    // Messages d'état
    JPanel statusPanel = new JPanel(new GridLayout(2, 1));

    errorLabel = new JLabel(" ");
    errorLabel.setForeground(Color.RED);
    statusPanel.add(errorLabel);

    successLabel = new JLabel(" ");
    successLabel.setForeground(new Color(0, 128, 0)); // Vert
    statusPanel.add(successLabel);

    // Assemblage final
    this.add(searchPanel, BorderLayout.NORTH);
    this.add(resultsPanel, BorderLayout.CENTER);
    this.add(actionsPanel, BorderLayout.SOUTH);
    this.add(statusPanel, BorderLayout.SOUTH);

    // Ajout d'un listenr de sélection pour activer/désactiver les boutons
    userTable.getSelectionModel().addListSelectionListener(e -> {
      boolean hasSelection = userTable.getSelectedRow() != -1;
      viewProfileButton.setEnabled(hasSelection);
      followButton.setEnabled(hasSelection);
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
   * @param followedUsers La liste des utilisateurs suivis par l'utilisateur connecté.
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