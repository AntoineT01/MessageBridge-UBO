package com.ubo.tp.message.components.user.auth.register.view;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.core.database.IDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vue pour l'interface d'inscription
 */
public class RegisterView extends JPanel {
  /**
   * Base de données (pour l'accès par le contrôleur)
   */
  private final IDatabase database;

  /**
   * Champ pour le tag utilisateur
   */
  private JTextField tagField;

  /**
   * Champ pour le nom d'utilisateur
   */
  private JTextField nameField;

  /**
   * Champ pour le mot de passe
   */
  private JPasswordField passwordField;

  /**
   * Champ pour la confirmation du mot de passe
   */
  private JPasswordField confirmPasswordField;

  /**
   * Bouton pour l'inscription
   */
  private JButton registerButton;

  /**
   * Bouton pour basculer vers la connexion
   */
  private JButton loginButton;

  /**
   * Étiquette pour afficher les messages d'erreur
   */
  private JLabel errorLabel;

  /**
   * Constructeur
   * @param database Base de données pour les vérifications
   */
  public RegisterView(IDatabase database) {
    this.database = database;
    this.initGUI();
  }

  /**
   * Initialisation de l'interface graphique
   */
  private void initGUI() {
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(5, 5, 5, 5);

    // Titre du panneau
    JLabel titleLabel = new JLabel("Inscription", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setIcon(IconFactory.createUserIcon(IconFactory.ICON_MEDIUM));
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    this.add(titleLabel, constraints);

    // Champ pour le tag utilisateur
    JLabel tagLabel = new JLabel("Tag utilisateur:");
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 1;
    this.add(tagLabel, constraints);

    tagField = new JTextField(15);
    constraints.gridx = 1;
    constraints.gridy = 1;
    this.add(tagField, constraints);

    // Champ pour le nom d'utilisateur
    JLabel nameLabel = new JLabel("Nom d'utilisateur:");
    constraints.gridx = 0;
    constraints.gridy = 2;
    this.add(nameLabel, constraints);

    nameField = new JTextField(15);
    constraints.gridx = 1;
    constraints.gridy = 2;
    this.add(nameField, constraints);

    // Champ pour le mot de passe
    JLabel passwordLabel = new JLabel("Mot de passe:");
    constraints.gridx = 0;
    constraints.gridy = 3;
    this.add(passwordLabel, constraints);

    passwordField = new JPasswordField(15);
    constraints.gridx = 1;
    constraints.gridy = 3;
    this.add(passwordField, constraints);

    // Champ pour la confirmation du mot de passe
    JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe:");
    constraints.gridx = 0;
    constraints.gridy = 4;
    this.add(confirmPasswordLabel, constraints);

    confirmPasswordField = new JPasswordField(15);
    constraints.gridx = 1;
    constraints.gridy = 4;
    this.add(confirmPasswordField, constraints);

    // Étiquette pour les erreurs
    errorLabel = new JLabel("");
    errorLabel.setForeground(Color.RED);
    constraints.gridx = 0;
    constraints.gridy = 5;
    constraints.gridwidth = 2;
    this.add(errorLabel, constraints);

    // Panneau pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // Bouton d'inscription
    registerButton = new JButton("S'inscrire");
    buttonPanel.add(registerButton);

    // Bouton de connexion
    loginButton = new JButton("Se connecter");
    buttonPanel.add(loginButton);

    constraints.gridx = 0;
    constraints.gridy = 6;
    constraints.gridwidth = 2;
    this.add(buttonPanel, constraints);
  }

  /**
   * Définit l'écouteur d'événements pour le bouton d'inscription
   */
  public void setRegisterButtonListener(ActionListener listener) {
    registerButton.addActionListener(listener);
  }

  /**
   * Définit l'écouteur d'événements pour le bouton de connexion
   */
  public void setLoginButtonListener(ActionListener listener) {
    loginButton.addActionListener(listener);
  }

  /**
   * Récupère le tag utilisateur saisi
   */
  public String getUserTag() {
    return tagField.getText().trim();
  }

  /**
   * Récupère le nom d'utilisateur saisi
   */
  public String getUserName() {
    return nameField.getText().trim();
  }

  /**
   * Récupère le mot de passe saisi
   */
  public String getUserPassword() {
    return new String(passwordField.getPassword());
  }

  /**
   * Récupère la confirmation du mot de passe
   */
  public String getConfirmPassword() {
    return new String(confirmPasswordField.getPassword());
  }

  /**
   * Affiche un message d'erreur
   */
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
  }

  /**
   * Réinitialise les champs de formulaire
   */
  public void resetFields() {
    tagField.setText("");
    nameField.setText("");
    passwordField.setText("");
    confirmPasswordField.setText("");
    errorLabel.setText("");
  }

  /**
   * Active ou désactive tous les composants de la vue
   */
  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    tagField.setEnabled(enabled);
    nameField.setEnabled(enabled);
    passwordField.setEnabled(enabled);
    confirmPasswordField.setEnabled(enabled);
    registerButton.setEnabled(enabled);
    loginButton.setEnabled(enabled);
  }

  /**
   * Récupère le bouton de connexion (pour la compatibilité)
   */
  public JButton getLoginButton() {
    return loginButton;
  }

  /**
   * Récupère la base de données (pour le contrôleur)
   */
  public IDatabase getDatabase() {
    return database;
  }
}