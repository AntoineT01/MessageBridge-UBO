package com.ubo.tp.message.ihm.components;

import com.ubo.tp.message.common.IconFactory;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.ihm.session.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Panneau affichant la liste des utilisateurs
 */
public class UserListPanel extends JPanel implements IDatabaseObserver {
  /**
   * Base de données
   */
  protected final IDatabase mDatabase;

  /**
   * Gestionnaire de session
   */
  protected final SessionManager mSessionManager;

  /**
   * Modèle de données pour la table
   */
  protected DefaultTableModel mTableModel;

  /**
   * Table des utilisateurs
   */
  protected JTable mUserTable;

  /**
   * Champ de recherche
   */
  protected JTextField mSearchField;

  /**
   * Consumer appelé lorsqu'un utilisateur est sélectionné
   */
  protected Consumer<User> mUserSelectedCallback;

  /**
   * Liste des utilisateurs actuellement affichés
   */
  protected List<User> mDisplayedUsers;

  /**
   * Constructeur
   */
  public UserListPanel(IDatabase database, SessionManager sessionManager) {
    this.mDatabase = database;
    this.mSessionManager = sessionManager;
    this.mDisplayedUsers = new ArrayList<>();

    // S'enregistrer comme observateur de la base de données
    this.mDatabase.addObserver(this);

    this.initGUI();
    this.updateUserList();
  }

  /**
   * Initialisation de l'interface graphique
   */
  protected void initGUI() {
    this.setLayout(new BorderLayout(10, 10));
    this.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Panel pour la recherche
    JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
    searchPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

    JLabel searchLabel = new JLabel("Rechercher un utilisateur: ");
    searchPanel.add(searchLabel, BorderLayout.WEST);

    mSearchField = new JTextField();
    mSearchField.addActionListener(e -> filterUsers(mSearchField.getText()));
    searchPanel.add(mSearchField, BorderLayout.CENTER);

    JButton searchButton = new JButton("Rechercher");
    searchButton.addActionListener(e -> filterUsers(mSearchField.getText()));
    searchPanel.add(searchButton, BorderLayout.EAST);

    this.add(searchPanel, BorderLayout.NORTH);

    // Modèle de table non éditable
    mTableModel = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    // Colonnes de la table
    mTableModel.addColumn("Avatar");
    mTableModel.addColumn("Nom");
    mTableModel.addColumn("Tag");
    mTableModel.addColumn("Abonnés");

    // Table des utilisateurs
    mUserTable = new JTable(mTableModel);
    mUserTable.setRowHeight(40);

    // Renderer personnalisé pour les avatars
    mUserTable.getColumnModel().getColumn(0).setCellRenderer(new AvatarCellRenderer());

    // Centrer le texte dans certaines colonnes
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    mUserTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

    // Ajuster les largeurs des colonnes
    mUserTable.getColumnModel().getColumn(0).setPreferredWidth(50);
    mUserTable.getColumnModel().getColumn(1).setPreferredWidth(150);
    mUserTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    mUserTable.getColumnModel().getColumn(3).setPreferredWidth(80);

    // Gérer les clics sur la table
    mUserTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = mUserTable.getSelectedRow();
        if (row >= 0 && row < mDisplayedUsers.size() && mUserSelectedCallback != null) {
          User selectedUser = mDisplayedUsers.get(row);
          mUserSelectedCallback.accept(selectedUser);
        }
      }
    });

    // Scroll pane pour la table
    JScrollPane scrollPane = new JScrollPane(mUserTable);
    scrollPane.setPreferredSize(new Dimension(400, 300));

    this.add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Définit le callback appelé lorsqu'un utilisateur est sélectionné
   */
  public void setUserSelectedCallback(Consumer<User> callback) {
    this.mUserSelectedCallback = callback;
  }

  /**
   * Met à jour la liste des utilisateurs
   */
  protected void updateUserList() {
    // Vider la table
    mTableModel.setRowCount(0);
    mDisplayedUsers.clear();

    // Récupérer tous les utilisateurs
    Set<User> allUsers = mDatabase.getUsers();

    // Map pour éviter les doublons (par tag utilisateur)
    Map<String, User> uniqueUsers = new HashMap<>();

    // Filtrer les doublons
    for (User user : allUsers) {
      // Ignorer l'utilisateur inconnu
      if (user.getUuid().equals(com.ubo.tp.message.common.Constants.UNKNONWN_USER_UUID)) {
        continue;
      }

      // Ne conserver que le premier utilisateur pour chaque tag
      if (!uniqueUsers.containsKey(user.getUserTag())) {
        uniqueUsers.put(user.getUserTag(), user);
      }
    }

    // Ajouter les utilisateurs uniques à la table
    for (User user : uniqueUsers.values()) {
      addUserToTable(user);
    }
  }

  /**
   * Filtre les utilisateurs selon un critère de recherche
   */
  protected void filterUsers(String searchText) {
    if (searchText == null || searchText.trim().isEmpty()) {
      updateUserList();
      return;
    }

    // Convertir en minuscules pour une recherche insensible à la casse
    searchText = searchText.toLowerCase().trim();

    // Vider la table
    mTableModel.setRowCount(0);
    mDisplayedUsers.clear();

    // Map pour éviter les doublons (par tag utilisateur)
    Map<String, User> uniqueFilteredUsers = new HashMap<>();

    // Récupérer tous les utilisateurs et filtrer
    for (User user : mDatabase.getUsers()) {
      // Ignorer l'utilisateur inconnu
      if (user.getUuid().equals(com.ubo.tp.message.common.Constants.UNKNONWN_USER_UUID)) {
        continue;
      }

      // Vérifier si le nom ou le tag contient le texte recherché
      if (user.getName().toLowerCase().contains(searchText) ||
        user.getUserTag().toLowerCase().contains(searchText)) {

        // Ne conserver que le premier utilisateur pour chaque tag
        if (!uniqueFilteredUsers.containsKey(user.getUserTag())) {
          uniqueFilteredUsers.put(user.getUserTag(), user);
        }
      }
    }

    // Ajouter les utilisateurs filtrés à la table
    for (User user : uniqueFilteredUsers.values()) {
      addUserToTable(user);
    }
  }

  /**
   * Ajoute un utilisateur à la table
   */
  protected void addUserToTable(User user) {
    // Ignorer l'utilisateur inconnu
    if (user.getUuid().equals(com.ubo.tp.message.common.Constants.UNKNONWN_USER_UUID)) {
      return;
    }

    // Récupérer le nombre d'abonnés
    int followersCount = mDatabase.getFollowersCount(user);

    // Créer une icône pour l'avatar (sera gérée par le renderer)
    Icon avatarIcon = IconFactory.createUserIcon(IconFactory.ICON_SMALL);

    // Ajouter l'utilisateur au modèle
    mTableModel.addRow(new Object[] {
      user,  // On stocke l'utilisateur directement, le renderer s'occupera de l'affichage
      user.getName(),
      user.getUserTag(),
      followersCount
    });

    // Stocker l'utilisateur dans la liste
    mDisplayedUsers.add(user);
  }

  /**
   * Classe pour le rendu des avatars dans la table
   */
  class AvatarCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
      // Utiliser le rendu par défaut pour le fond et la sélection
      super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);

      // Centrer le contenu
      setHorizontalAlignment(JLabel.CENTER);

      // L'objet value est l'utilisateur
      if (value instanceof User) {
        User user = (User) value;
        // Vérifier si l'utilisateur a un avatar
        if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
          File avatarFile = new File(user.getAvatarPath());
          if (avatarFile.exists()) {
            try {
              // Charger et redimensionner l'avatar
              ImageIcon avatarIcon = new ImageIcon(user.getAvatarPath());
              Image scaledImage = avatarIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
              setIcon(new ImageIcon(scaledImage));
              setText("");
              return this;
            } catch (Exception e) {
              // Si l'image ne peut pas être chargée, utiliser l'icône par défaut
            }
          }
        }

        // Utiliser l'icône par défaut si aucun avatar n'est défini ou s'il ne peut pas être chargé
        setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
        setText("");
      }

      return this;
    }
  }

  @Override
  public void notifyUserAdded(User addedUser) {
    SwingUtilities.invokeLater(() -> {
      // Mettre à jour la liste si l'utilisateur correspond aux critères de recherche
      String searchText = mSearchField.getText().toLowerCase().trim();
      if (searchText.isEmpty() ||
        addedUser.getName().toLowerCase().contains(searchText) ||
        addedUser.getUserTag().toLowerCase().contains(searchText)) {
        addUserToTable(addedUser);
      }
    });
  }

  @Override
  public void notifyUserDeleted(User deletedUser) {
    SwingUtilities.invokeLater(() -> {
      // Rechercher l'utilisateur dans la liste affichée
      int index = mDisplayedUsers.indexOf(deletedUser);
      if (index >= 0) {
        // Supprimer l'utilisateur de la table et de la liste
        mTableModel.removeRow(index);
        mDisplayedUsers.remove(index);
      }
    });
  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    SwingUtilities.invokeLater(() -> {
      // Rechercher l'utilisateur dans la liste affichée
      int index = mDisplayedUsers.indexOf(modifiedUser);
      if (index >= 0) {
        // Mettre à jour les informations dans la table
        mTableModel.setValueAt(modifiedUser, index, 0); // Avatar (utilisateur)
        mTableModel.setValueAt(modifiedUser.getName(), index, 1); // Nom
        mTableModel.setValueAt(modifiedUser.getUserTag(), index, 2); // Tag
        mTableModel.setValueAt(mDatabase.getFollowersCount(modifiedUser), index, 3); // Abonnés
      }
    });
  }

  // Méthodes non utilisées de l'interface IDatabaseObserver
  @Override
  public void notifyMessageAdded(Message addedMessage) { }

  @Override
  public void notifyMessageDeleted(Message deletedMessage) { }

  @Override
  public void notifyMessageModified(Message modifiedMessage) { }
}