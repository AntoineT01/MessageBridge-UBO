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
public class LoginView extends JPanel implements ILoginView {
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
    // Code existant inchangé
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

  // Implémentation des méthodes de l'interface ILoginView

  @Override
  public void setLoginButtonListener(ActionListener listener) {
    loginButton.addActionListener(listener);
  }

  @Override
  public void setRegisterButtonListener(ActionListener listener) {
    registerButton.addActionListener(listener);
  }

  @Override
  public String getUserTag() {
    return tagField.getText().trim();
  }

  @Override
  public String getUserPassword() {
    return new String(passwordField.getPassword());
  }

  @Override
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
  }

  @Override
  public void resetFields() {
    tagField.setText("");
    passwordField.setText("");
    errorLabel.setText("");
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    tagField.setEnabled(enabled);
    passwordField.setEnabled(enabled);
    loginButton.setEnabled(enabled);
    registerButton.setEnabled(enabled);
  }
}