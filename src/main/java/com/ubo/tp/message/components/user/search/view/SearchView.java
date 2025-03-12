package com.ubo.tp.message.components.user.search.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.common.ui.SearchBar;
import com.ubo.tp.message.common.ui.SwingTheme;
import com.ubo.tp.message.core.datamodel.User;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Set;

public class SearchView extends JPanel implements ISearchView {
  private SearchBar searchBar;
  private DefaultTableModel userTableModel;
  private JTable userTable;
  private JButton viewProfileButton;
  private JButton followButton;
  private JLabel errorLabel;
  private JLabel successLabel;

  public SearchView() {
    this.initGUI();
  }

  private void initGUI() {
    this.setBackground(SwingTheme.BACKGROUND);
    this.setLayout(new BorderLayout(15, 15));
    this.setBorder(new EmptyBorder(20, 20, 20, 20));
    JPanel searchPanel = SwingTheme.createStyledPanel("Rechercher des utilisateurs");
    JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
    searchBarPanel.setOpaque(false);
    searchBar = new SearchBar("", 25, "Rechercher");
    searchBar.setBackground(SwingTheme.BACKGROUND);
    searchBarPanel.add(searchBar, BorderLayout.CENTER);
    searchPanel.add(searchBarPanel, BorderLayout.CENTER);
    this.add(searchPanel, BorderLayout.NORTH);
    JPanel resultsPanel = SwingTheme.createStyledPanel("R\u00E9sultats de la recherche");
    String[] columnNames = {"", "Tag", "Nom", "Abonn\u00E9s"};
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
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    actionsPanel.setOpaque(false);
    viewProfileButton = new JButton("Voir profil");
    viewProfileButton.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    SwingTheme.styleButton(viewProfileButton, true);
    viewProfileButton.setEnabled(false);
    actionsPanel.add(viewProfileButton);
    followButton = new JButton("Suivre");
    SwingTheme.styleButton(followButton, false);
    followButton.setBackground(SwingTheme.SECONDARY);
    followButton.setEnabled(false);
    actionsPanel.add(followButton);
    resultsPanel.add(actionsPanel, BorderLayout.SOUTH);
    this.add(resultsPanel, BorderLayout.CENTER);
    JPanel statusPanel = new JPanel(new GridLayout(2, 1, 5, 5));
    statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    statusPanel.setOpaque(false);
    errorLabel = new JLabel(" ");
    errorLabel.setFont(SwingTheme.TOOLTIP_FONT);
    errorLabel.setForeground(SwingTheme.DANGER);
    statusPanel.add(errorLabel);
    successLabel = new JLabel(" ");
    successLabel.setFont(SwingTheme.TOOLTIP_FONT);
    successLabel.setForeground(SwingTheme.SUCCESS);
    statusPanel.add(successLabel);
    this.add(statusPanel, BorderLayout.SOUTH);
    userTable.getSelectionModel().addListSelectionListener(e -> {
      boolean hasSelection = userTable.getSelectedRow() != -1;
      viewProfileButton.setEnabled(hasSelection);
      followButton.setEnabled(hasSelection);
      if (hasSelection) {
        int selectedRow = userTable.getSelectedRow();
        boolean isFollowed = (boolean) userTable.getClientProperty("followed_" + selectedRow);
        followButton.setText(isFollowed ? "Ne plus suivre" : "Suivre");
      }
    });
  }

  private void viewProfile() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow != -1) {
      viewProfileButton.doClick();
    }
  }

  public void setSearchActionListener(ActionListener listener) {
    searchBar.addSearchAction(listener);
  }

  public void setViewProfileButtonListener(ActionListener listener) {
    viewProfileButton.addActionListener(listener);
  }

  public void setFollowButtonListener(ActionListener listener) {
    followButton.addActionListener(listener);
  }

  public String getSearchQuery() {
    return searchBar.getSearchQuery();
  }

  public void updateUsersList(Set<User> users, Set<String> followedTags) {
    userTableModel.setRowCount(0);
    for (User user : users) {
      boolean isFollowed = followedTags.contains(user.getUserTag());
      Icon userIcon;
      if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
        File avatarFile = new File(user.getAvatarPath());
        if (avatarFile.exists()) {
          userIcon = new ImageIcon(new ImageIcon(user.getAvatarPath()).getImage().getScaledInstance(IconFactory.ICON_SMALL, IconFactory.ICON_SMALL, Image.SCALE_SMOOTH));
        } else {
          userIcon = IconFactory.createUserIcon(IconFactory.ICON_SMALL);
        }
      } else {
        userIcon = IconFactory.createUserIcon(IconFactory.ICON_SMALL);
      }
      Object[] rowData = { userIcon, user.getUserTag(), user.getName(), "-" };
      userTableModel.addRow(rowData);
      int rowIndex = userTableModel.getRowCount() - 1;
      userTable.putClientProperty("user_" + rowIndex, user);
      userTable.putClientProperty("followed_" + rowIndex, isFollowed);
    }
    userTable.repaint();
  }

  public void updateFollowersCount(int rowIndex, int followersCount) {
    if (rowIndex >= 0 && rowIndex < userTableModel.getRowCount()) {
      userTableModel.setValueAt(String.valueOf(followersCount), rowIndex, 3);
    }
  }

  public void updateFollowButton() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow != -1) {
      boolean isFollowed = (boolean) userTable.getClientProperty("followed_" + selectedRow);
      followButton.setText(isFollowed ? "Ne plus suivre" : "Suivre");
    }
  }

  public User getSelectedUser() {
    int selectedRow = userTable.getSelectedRow();
    if (selectedRow != -1) {
      return (User) userTable.getClientProperty("user_" + selectedRow);
    }
    return null;
  }

  public void setErrorMessage(String message) {
    errorLabel.setText(message);
    successLabel.setText(" ");
  }

  public void setSuccessMessage(String message) {
    successLabel.setText(message);
    errorLabel.setText(" ");
  }

  public void clearMessages() {
    errorLabel.setText(" ");
    successLabel.setText(" ");
  }

  public void reset() {
    searchBar.clearSearch();
    userTableModel.setRowCount(0);
    clearMessages();
  }

  public void setEnabled(boolean enabled) {
    searchBar.setEnabled(enabled);
    userTable.setEnabled(enabled);
    viewProfileButton.setEnabled(enabled);
    followButton.setEnabled(enabled);
  }
}
