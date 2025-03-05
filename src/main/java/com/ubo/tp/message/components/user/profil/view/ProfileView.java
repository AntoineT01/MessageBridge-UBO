package com.ubo.tp.message.components.user.profil.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.core.datamodel.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Vue pour l'interface du profil utilisateur.
 */
public class ProfileView extends JPanel {
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
    this.setLayout(new BorderLayout(10, 10));
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Panneau d'informations
    JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
    infoPanel.setBorder(new TitledBorder("Informations utilisateur"));

    // Sous-panneau pour les données utilisateur
    JPanel userDataPanel = new JPanel(new GridLayout(3, 2, 5, 5));

    userDataPanel.add(new JLabel("Tag:"));
    tagLabel = new JLabel();
    tagLabel.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    userDataPanel.add(tagLabel);

    userDataPanel.add(new JLabel("Nom:"));
    nameLabel = new JLabel();
    userDataPanel.add(nameLabel);

    userDataPanel.add(new JLabel("Followers:"));
    followersCountLabel = new JLabel();
    userDataPanel.add(followersCountLabel);

    infoPanel.add(userDataPanel, BorderLayout.NORTH);

    // Panneau d'édition
    JPanel editPanel = new JPanel(new GridBagLayout());
    editPanel.setBorder(new TitledBorder("Modifier votre profil"));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(5, 5, 5, 5);

    // Champ nom
    constraints.gridx = 0;
    constraints.gridy = 0;
    editPanel.add(new JLabel("Nouveau nom:"), constraints);

    constraints.gridx = 1;
    constraints.gridy = 0;
    nameField = new JTextField(15);
    editPanel.add(nameField, constraints);

    // Champ mot de passe
    constraints.gridx = 0;
    constraints.gridy = 1;
    editPanel.add(new JLabel("Nouveau mot de passe:"), constraints);

    constraints.gridx = 1;
    constraints.gridy = 1;
    passwordField = new JPasswordField(15);
    editPanel.add(passwordField, constraints);

    // Confirmation mot de passe
    constraints.gridx = 0;
    constraints.gridy = 2;
    editPanel.add(new JLabel("Confirmer mot de passe:"), constraints);

    constraints.gridx = 1;
    constraints.gridy = 2;
    confirmPasswordField = new JPasswordField(15);
    editPanel.add(confirmPasswordField, constraints);

    // Messages d'état
    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.gridwidth = 2;
    errorLabel = new JLabel(" ");
    errorLabel.setForeground(Color.RED);
    editPanel.add(errorLabel, constraints);

    constraints.gridx = 0;
    constraints.gridy = 4;
    successLabel = new JLabel(" ");
    successLabel.setForeground(new Color(0, 128, 0)); // Vert
    editPanel.add(successLabel, constraints);

    // Bouton de mise à jour
    constraints.gridx = 0;
    constraints.gridy = 5;
    constraints.gridwidth = 2;
    constraints.anchor = GridBagConstraints.CENTER;
    updateButton = new JButton("Mettre à jour");
    editPanel.add(updateButton, constraints);

    infoPanel.add(editPanel, BorderLayout.CENTER);

    // Panneau des messages de l'utilisateur
    JPanel messagesPanel = new JPanel(new BorderLayout(5, 5));
    messagesPanel.setBorder(new TitledBorder("Mes messages"));

    messagesListModel = new DefaultListModel<>();
    messagesList = new JList<>(messagesListModel);
    JScrollPane scrollPane = new JScrollPane(messagesList);
    scrollPane.setPreferredSize(new Dimension(400, 200));
    messagesPanel.add(scrollPane, BorderLayout.CENTER);

    // Ajout des panneaux au panneau principal
    this.add(infoPanel, BorderLayout.NORTH);
    this.add(messagesPanel, BorderLayout.CENTER);
  }

  /**
   * Définit l'écouteur d'événements pour le bouton de mise à jour.
   */
  public void setUpdateButtonListener(ActionListener listener) {
    updateButton.addActionListener(listener);
  }

  /**
   * Met à jour les informations affichées de l'utilisateur.
   */
  public void updateUserInfo(String tag, String name, int followersCount) {
    tagLabel.setText(tag);
    nameLabel.setText(name);
    followersCountLabel.setText(String.valueOf(followersCount));
  }

  /**
   * Met à jour la liste des messages de l'utilisateur.
   */
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

  /**
   * Affiche un message d'erreur.
   */
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
    successLabel.setText(" ");
  }

  /**
   * Affiche un message de succès.
   */
  public void setSuccessMessage(String message) {
    successLabel.setText(message);
    errorLabel.setText(" ");
  }

  /**
   * Récupère le nouveau nom saisi.
   */
  public String getNewName() {
    return nameField.getText().trim();
  }

  /**
   * Récupère le nouveau mot de passe saisi.
   */
  public String getNewPassword() {
    return new String(passwordField.getPassword());
  }

  /**
   * Récupère la confirmation du mot de passe saisi.
   */
  public String getConfirmPassword() {
    return new String(confirmPasswordField.getPassword());
  }

  /**
   * Réinitialise les champs de formulaire.
   */
  public void resetFields() {
    nameField.setText("");
    passwordField.setText("");
    confirmPasswordField.setText("");
    errorLabel.setText(" ");
    successLabel.setText(" ");
  }

  /**
   * Active ou désactive tous les composants de la vue.
   */
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