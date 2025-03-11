package com.ubo.tp.message.components.user.profil.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.common.ui.RoundedBorder;
import com.ubo.tp.message.core.datamodel.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Vue pour l'interface du profil utilisateur.
 */
public class ProfileView extends JPanel implements IProfileView {
  /**
   * Affichage des informations de l'utilisateur.
   */
  private JLabel tagLabel;
  private JLabel nameLabel;
  private JLabel followersCountLabel;

  /**
   * Champs d'édition.
   */
  private JTextField nameField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;

  /**
   * Bouton de mise à jour du profil.
   */
  private JButton updateButton;

  /**
   * Affichage des messages de l'utilisateur.
   */
  private DefaultListModel<String> messagesListModel;
  private JList<String> messagesList;

  /**
   * Labels pour les messages d'état.
   */
  private JLabel errorLabel;
  private JLabel successLabel;

  /**
   * Constructeur.
   */
  public ProfileView() {
    this.initGUI();
  }

  /**
   * Initialisation de l'interface graphique.
   */
  private void initGUI() {
    // Configuration du panneau principal
    this.setBackground(new Color(245, 248, 250));
    this.setLayout(new BorderLayout(15, 15));
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Panneau d'en-tête avec les informations de profil
    JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
    headerPanel.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(15, 20, 15, 20)
    ));
    headerPanel.setBackground(new Color(255, 255, 255));

    // Panneau gauche pour l'avatar et les boutons
    JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
    leftPanel.setOpaque(false);

    // Avatar utilisateur
    JLabel avatarLabel = new JLabel();
    avatarLabel.setIcon(IconFactory.createUserIcon(IconFactory.ICON_LARGE));
    avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
    avatarLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    leftPanel.add(avatarLabel, BorderLayout.NORTH);

    headerPanel.add(leftPanel, BorderLayout.WEST);

    // Panneau central pour les informations
    JPanel infoPanel = new JPanel(new GridBagLayout());
    infoPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 5, 3, 5);

    // Titre du profil
    JLabel profileTitleLabel = new JLabel("Mon Profil");
    profileTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
    profileTitleLabel.setForeground(new Color(52, 152, 219));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    infoPanel.add(profileTitleLabel, gbc);

    // Tag utilisateur
    JLabel tagTitleLabel = new JLabel("Tag:");
    tagTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    infoPanel.add(tagTitleLabel, gbc);

    tagLabel = new JLabel();
    tagLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 1;
    gbc.gridy = 1;
    infoPanel.add(tagLabel, gbc);

    // Nom d'utilisateur
    JLabel nameTitleLabel = new JLabel("Nom:");
    nameTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 2;
    infoPanel.add(nameTitleLabel, gbc);

    nameLabel = new JLabel();
    nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 1;
    gbc.gridy = 2;
    infoPanel.add(nameLabel, gbc);

    // Followers
    JLabel followersTitleLabel = new JLabel("Followers:");
    followersTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 3;
    infoPanel.add(followersTitleLabel, gbc);

    followersCountLabel = new JLabel();
    followersCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    gbc.gridx = 1;
    gbc.gridy = 3;
    infoPanel.add(followersCountLabel, gbc);

    headerPanel.add(infoPanel, BorderLayout.CENTER);

    // Ajout du panneau d'en-tête au panneau principal
    this.add(headerPanel, BorderLayout.NORTH);

    // Section de modification du profil
    JPanel editPanel = new JPanel(new GridBagLayout());
    editPanel.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));
    editPanel.setBackground(new Color(255, 255, 255));

    GridBagConstraints editGbc = new GridBagConstraints();
    editGbc.fill = GridBagConstraints.HORIZONTAL;
    editGbc.anchor = GridBagConstraints.WEST;
    editGbc.insets = new Insets(5, 5, 5, 5);

    // Titre de la section
    JLabel editTitleLabel = new JLabel("Modifier votre profil");
    editTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    editTitleLabel.setForeground(new Color(52, 73, 94));
    editGbc.gridx = 0;
    editGbc.gridy = 0;
    editGbc.gridwidth = 2;
    editPanel.add(editTitleLabel, editGbc);

    // Nouveau nom
    JLabel newNameLabel = new JLabel("Nouveau nom:");
    newNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    editGbc.gridx = 0;
    editGbc.gridy = 1;
    editGbc.gridwidth = 1;
    editPanel.add(newNameLabel, editGbc);

    nameField = new JTextField(20);
    nameField.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    nameField.setFont(new Font("Arial", Font.PLAIN, 14));
    editGbc.gridx = 1;
    editGbc.gridy = 1;
    editPanel.add(nameField, editGbc);

    // Nouveau mot de passe
    JLabel newPasswordLabel = new JLabel("Nouveau mot de passe:");
    newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    editGbc.gridx = 0;
    editGbc.gridy = 2;
    editPanel.add(newPasswordLabel, editGbc);

    passwordField = new JPasswordField(20);
    passwordField.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    editGbc.gridx = 1;
    editGbc.gridy = 2;
    editPanel.add(passwordField, editGbc);

    // Confirmation du mot de passe
    JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe:");
    confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    editGbc.gridx = 0;
    editGbc.gridy = 3;
    editPanel.add(confirmPasswordLabel, editGbc);

    confirmPasswordField = new JPasswordField(20);
    confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
    editGbc.gridx = 1;
    editGbc.gridy = 3;
    editPanel.add(confirmPasswordField, editGbc);

    // Messages d'état
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

    editGbc.gridx = 0;
    editGbc.gridy = 4;
    editGbc.gridwidth = 2;
    editPanel.add(statusPanel, editGbc);

    // Bouton de mise à jour
    updateButton = new JButton("Mettre à jour");
    updateButton.setFont(new Font("Arial", Font.BOLD, 14));
    updateButton.setBackground(new Color(52, 152, 219));
    updateButton.setForeground(Color.WHITE);
    updateButton.setFocusPainted(false);
    updateButton.setBorder(new RoundedBorder(15));
    updateButton.setPreferredSize(new Dimension(150, 40));

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setOpaque(false);
    buttonPanel.add(updateButton);

    editGbc.gridx = 0;
    editGbc.gridy = 5;
    editGbc.gridwidth = 2;
    editPanel.add(buttonPanel, editGbc);

    // Section centrale du profil
    JPanel centralPanel = new JPanel(new BorderLayout(0, 15));
    centralPanel.setOpaque(false);
    centralPanel.add(editPanel, BorderLayout.NORTH);

    // Section des messages de l'utilisateur
    JPanel messagesPanel = new JPanel(new BorderLayout(10, 10));
    messagesPanel.setBorder(BorderFactory.createCompoundBorder(
      new RoundedBorder(15),
      BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    messagesPanel.setBackground(new Color(255, 255, 255));

    JLabel messagesTitle = new JLabel("Mes messages");
    messagesTitle.setFont(new Font("Arial", Font.BOLD, 18));
    messagesTitle.setForeground(new Color(52, 73, 94));
    messagesPanel.add(messagesTitle, BorderLayout.NORTH);

    messagesListModel = new DefaultListModel<>();
    messagesList = new JList<>(messagesListModel);
    messagesList.setFont(new Font("Arial", Font.PLAIN, 14));
    messagesList.setCellRenderer(new MessageCellRenderer());

    JScrollPane scrollPane = new JScrollPane(messagesList);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setPreferredSize(new Dimension(400, 200));
    messagesPanel.add(scrollPane, BorderLayout.CENTER);

    centralPanel.add(messagesPanel, BorderLayout.CENTER);

    // Ajout du panneau central au panneau principal
    this.add(centralPanel, BorderLayout.CENTER);
  }

  // Classe personnalisée pour le rendu des messages dans la liste
  private class MessageCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if (!isSelected) {
        label.setBackground(index % 2 == 0 ? new Color(245, 248, 250) : Color.WHITE);
      }

      label.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
      return label;
    }
  }

  // Implémentation des méthodes de l'interface IProfileView

  @Override
  public void setUpdateButtonListener(ActionListener listener) {
    updateButton.addActionListener(listener);
  }

  @Override
  public void updateUserInfo(String tag, String name, int followersCount) {
    tagLabel.setText(tag);
    nameLabel.setText(name);
    followersCountLabel.setText(String.valueOf(followersCount));
  }

  @Override
  public void updateUserMessages(Set<Message> messages) {
    messagesListModel.clear();
    for (Message message : messages) {
      String formattedMessage = formatMessage(message);
      messagesListModel.addElement(formattedMessage);
    }
  }

  /**
   * Formate un message pour l'affichage.
   */
  private String formatMessage(Message message) {
    return "[" + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(message.getEmissionDate())) + "] " + message.getText();
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
}