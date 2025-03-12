package com.ubo.tp.message.components.user.profil.view;

import com.ubo.tp.message.common.ui.RoundedBorder;
import com.ubo.tp.message.common.ui.UserAvatarEditor;
import com.ubo.tp.message.core.datamodel.Message;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Set;

public class ProfileView extends JPanel implements IProfileView {
  private JLabel tagLabel;
  private JLabel nameLabel;
  private JLabel followersCountLabel;
  private JTextField nameField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  private JButton updateButton;
  private DefaultListModel<String> messagesListModel;
  private JList<String> messagesList;
  private JLabel errorLabel;
  private JLabel successLabel;
  private UserAvatarEditor avatarEditor;

  public ProfileView() {
    initGUI();
  }

  private void initGUI() {
    setBackground(new Color(245, 248, 250));
    setLayout(new BorderLayout(15, 15));
    setBorder(new EmptyBorder(20, 20, 20, 20));

    // Header avec avatar éditable et informations
    JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
    headerPanel.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(15, 20, 15, 20)
    ));
    headerPanel.setBackground(Color.WHITE);

    // Avatar éditable
    avatarEditor = new UserAvatarEditor(80);
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setOpaque(false);
    leftPanel.add(avatarEditor, BorderLayout.NORTH);
    headerPanel.add(leftPanel, BorderLayout.WEST);

    // Infos du profil
    JPanel infoPanel = new JPanel(new GridBagLayout());
    infoPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 5, 3, 5);

    JLabel profileTitleLabel = new JLabel("Mon Profil");
    profileTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
    profileTitleLabel.setForeground(new Color(52, 152, 219));
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    infoPanel.add(profileTitleLabel, gbc);

    JLabel tagTitleLabel = new JLabel("Tag:");
    tagTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
    infoPanel.add(tagTitleLabel, gbc);

    tagLabel = new JLabel();
    tagLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 1; gbc.gridy = 1;
    infoPanel.add(tagLabel, gbc);

    JLabel nameTitleLabel = new JLabel("Nom:");
    nameTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0; gbc.gridy = 2;
    infoPanel.add(nameTitleLabel, gbc);

    nameLabel = new JLabel();
    nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 1; gbc.gridy = 2;
    infoPanel.add(nameLabel, gbc);

    JLabel followersTitleLabel = new JLabel("Followers:");
    followersTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0; gbc.gridy = 3;
    infoPanel.add(followersTitleLabel, gbc);

    followersCountLabel = new JLabel();
    followersCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 1; gbc.gridy = 3;
    infoPanel.add(followersCountLabel, gbc);

    headerPanel.add(infoPanel, BorderLayout.CENTER);
    add(headerPanel, BorderLayout.NORTH);

    // Partie édition
    JPanel editPanel = new JPanel(new GridBagLayout());
    editPanel.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));
    editPanel.setBackground(Color.WHITE);
    GridBagConstraints editGbc = new GridBagConstraints();
    editGbc.fill = GridBagConstraints.HORIZONTAL;
    editGbc.anchor = GridBagConstraints.WEST;
    editGbc.insets = new Insets(5, 5, 5, 5);

    JLabel editTitleLabel = new JLabel("Modifier votre profil");
    editTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    editTitleLabel.setForeground(new Color(52, 73, 94));
    editGbc.gridx = 0; editGbc.gridy = 0; editGbc.gridwidth = 2;
    editPanel.add(editTitleLabel, editGbc);

    JLabel newNameLabel = new JLabel("Nouveau nom:");
    newNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    editGbc.gridx = 0; editGbc.gridy = 1; editGbc.gridwidth = 1;
    editPanel.add(newNameLabel, editGbc);

    nameField = new JTextField(20);
    nameField.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    nameField.setFont(new Font("Arial", Font.PLAIN, 14));
    editGbc.gridx = 1; editGbc.gridy = 1;
    editPanel.add(nameField, editGbc);

    JLabel newPasswordLabel = new JLabel("Nouveau mot de passe:");
    newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    editGbc.gridx = 0; editGbc.gridy = 2;
    editPanel.add(newPasswordLabel, editGbc);

    passwordField = new JPasswordField(20);
    passwordField.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    editGbc.gridx = 1; editGbc.gridy = 2;
    editPanel.add(passwordField, editGbc);

    JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe:");
    confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    editGbc.gridx = 0; editGbc.gridy = 3;
    editPanel.add(confirmPasswordLabel, editGbc);

    confirmPasswordField = new JPasswordField(20);
    confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
    editGbc.gridx = 1; editGbc.gridy = 3;
    editPanel.add(confirmPasswordField, editGbc);

    JPanel statusPanel = new JPanel(new GridLayout(2, 1, 5, 5));
    statusPanel.setOpaque(false);
    errorLabel = new JLabel(" ");
    errorLabel.setForeground(new Color(231, 76, 60));
    errorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    statusPanel.add(errorLabel);
    successLabel = new JLabel(" ");
    successLabel.setForeground(new Color(46, 204, 113));
    successLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    statusPanel.add(successLabel);
    editGbc.gridx = 0; editGbc.gridy = 4; editGbc.gridwidth = 2;
    editPanel.add(statusPanel, editGbc);

    updateButton = new JButton("Mettre à jour");
    updateButton.setFont(new Font("Arial", Font.BOLD, 14));
    updateButton.setBackground(new Color(52, 152, 219));
    updateButton.setFocusPainted(false);
    updateButton.setBorder(new RoundedBorder(15));
    updateButton.setPreferredSize(new Dimension(150, 40));
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setOpaque(false);
    buttonPanel.add(updateButton);
    editGbc.gridx = 0; editGbc.gridy = 5; editGbc.gridwidth = 2;
    editPanel.add(buttonPanel, editGbc);

    JPanel centralPanel = new JPanel(new BorderLayout(0, 15));
    centralPanel.setOpaque(false);
    centralPanel.add(editPanel, BorderLayout.NORTH);

    // Zone des messages
    JPanel messagesPanel = new JPanel(new BorderLayout(10, 10));
    messagesPanel.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    messagesPanel.setBackground(Color.WHITE);
    JLabel messagesTitle = new JLabel("Mes messages");
    messagesTitle.setFont(new Font("Arial", Font.BOLD, 18));
    messagesTitle.setForeground(new Color(52, 73, 94));
    messagesPanel.add(messagesTitle, BorderLayout.NORTH);
    messagesListModel = new DefaultListModel<>();
    messagesList = new JList<>(messagesListModel);
    messagesList.setFont(new Font("Arial", Font.PLAIN, 14));
    messagesList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value,
                                                    int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return label;
      }
    });
    JScrollPane scrollPane = new JScrollPane(messagesList);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setPreferredSize(new Dimension(400, 200));
    messagesPanel.add(scrollPane, BorderLayout.CENTER);
    centralPanel.add(messagesPanel, BorderLayout.CENTER);

    add(centralPanel, BorderLayout.CENTER);
  }

  @Override
  public void setUpdateButtonListener(ActionListener listener) {
    updateButton.addActionListener(listener);
  }

  @Override
  public void updateUserInfo(String tag, String name, int followersCount, String avatarPath) {
    tagLabel.setText(tag);
    nameLabel.setText(name);
    followersCountLabel.setText(String.valueOf(followersCount));
    this.avatarEditor.setAvatarPath(avatarPath);
  }

  @Override
  public void updateUserMessages(Set<Message> messages) {
    messagesListModel.clear();
    for (Message message : messages) {
      String formattedMessage = "[" + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
        .format(new java.util.Date(message.getEmissionDate())) + "] " + message.getText();
      messagesListModel.addElement(formattedMessage);
    }
  }

  @Override
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
    successLabel.setText(" ");
  }

  @Override
  public void setSuccessMessage(String message) {
    successLabel.setText(message);
    errorLabel.setText(" ");
  }

  @Override
  public String getNewName() {
    return nameField.getText().trim();
  }

  @Override
  public String getNewPassword() {
    return new String(passwordField.getPassword());
  }

  @Override
  public String getConfirmPassword() {
    return new String(confirmPasswordField.getPassword());
  }

  @Override
  public String getAvatarPath() {
    return avatarEditor.getAvatarPath();
  }

  @Override
  public void resetFields() {
    nameField.setText("");
    passwordField.setText("");
    confirmPasswordField.setText("");
    errorLabel.setText(" ");
    successLabel.setText(" ");
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    nameField.setEnabled(enabled);
    passwordField.setEnabled(enabled);
    confirmPasswordField.setEnabled(enabled);
    updateButton.setEnabled(enabled);
    messagesList.setEnabled(enabled);
  }

  // Méthode d'actualisation de l'avatar dans la vue
  public void updateAvatar(String avatarPath) {
    avatarEditor.setAvatarPath(avatarPath);
  }
}
