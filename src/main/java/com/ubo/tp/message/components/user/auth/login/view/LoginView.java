package com.ubo.tp.message.components.user.auth.login.view;

import com.ubo.tp.message.common.ui.IconFactory;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

/**
 * Vue pour l'interface de connexion
 */
public class LoginView extends JPanel {
  /**
   * Champ pour le tag utilisateur
   */
  private JTextField tagField;

  /**
   * Champ pour le mot de passe
   */
  private JPasswordField passwordField;

  /**
   * Bouton pour la connexion
   */
  private JButton loginButton;

  /**
   * Bouton pour basculer vers l'inscription
   */
  private JButton registerButton;

  /**
   * Étiquette pour afficher les messages d'erreur
   */
  private JLabel errorLabel;

  /**
   * Constructeur
   */
  public LoginView() {
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
    JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
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

    // Champ pour le mot de passe
    JLabel passwordLabel = new JLabel("Mot de passe:");
    constraints.gridx = 0;
    constraints.gridy = 2;
    this.add(passwordLabel, constraints);

    passwordField = new JPasswordField(15);
    constraints.gridx = 1;
    constraints.gridy = 2;
    this.add(passwordField, constraints);

    // Étiquette pour les erreurs
    errorLabel = new JLabel("");
    errorLabel.setForeground(Color.RED);
    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.gridwidth = 2;
    this.add(errorLabel, constraints);

    // Panneau pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // Bouton de connexion
    loginButton = new JButton("Connexion");
    buttonPanel.add(loginButton);

    // Bouton d'inscription
    registerButton = new JButton("S'inscrire");
    buttonPanel.add(registerButton);

    constraints.gridx = 0;
    constraints.gridy = 4;
    constraints.gridwidth = 2;
    this.add(buttonPanel, constraints);
  }

  /**
   * Définit l'écouteur d'événements pour le bouton de connexion
   */
  public void setLoginButtonListener(ActionListener listener) {
    loginButton.addActionListener(listener);
  }

  /**
   * Définit l'écouteur d'événements pour le bouton d'inscription
   */
  public void setRegisterButtonListener(ActionListener listener) {
    registerButton.addActionListener(listener);
  }

  /**
   * Récupère le tag utilisateur saisi
   */
  public String getUserTag() {
    return tagField.getText().trim();
  }

  /**
   * Récupère le mot de passe saisi
   */
  public String getUserPassword() {
    return new String(passwordField.getPassword());
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
    passwordField.setText("");
    errorLabel.setText("");
  }

  /**
   * Active ou désactive tous les composants de la vue
   */
  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    tagField.setEnabled(enabled);
    passwordField.setEnabled(enabled);
    loginButton.setEnabled(enabled);
    registerButton.setEnabled(enabled);
  }

}