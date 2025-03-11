package com.ubo.tp.message.components.user.auth.register.view;

import com.ubo.tp.message.common.ui.EnvUI;
import com.ubo.tp.message.common.ui.SwingTheme;
import com.ubo.tp.message.common.utils.ImageUtils;
import com.ubo.tp.message.core.database.IDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

/**
 * Vue pour l'interface d'inscription
 */
public class RegisterView extends JPanel implements IRegisterView {
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
    // Configuration du panneau principal
    this.setBackground(SwingTheme.BACKGROUND);
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(30, 40, 30, 40));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(EnvUI.SPACING_SMALL, EnvUI.SPACING_STANDARD,
                                    EnvUI.SPACING_SMALL, EnvUI.SPACING_STANDARD);

    // Logo et titre
    JLabel logoLabel = new JLabel();
    ImageIcon logo = ImageUtils.loadScaledIcon("/tux_logo.png", 70, 70);
    logoLabel.setIcon(logo);
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    this.add(logoLabel, constraints);

    // Titre stylisé - utilisation de la couleur secondaire pour différencier de la connexion
    JLabel titleLabel = new JLabel("Créer un compte", SwingConstants.CENTER);
    titleLabel.setFont(SwingTheme.TITLE_FONT);
    titleLabel.setForeground(SwingTheme.SECONDARY);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 2;
    this.add(titleLabel, constraints);

    // Sous-titre
    JLabel subtitleLabel = new JLabel("Rejoignez la communauté MessageApp", SwingConstants.CENTER);
    subtitleLabel.setFont(SwingTheme.TEXT_REGULAR);
    subtitleLabel.setForeground(SwingTheme.TEXT);
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.gridwidth = 2;
    this.add(subtitleLabel, constraints);

    // Espaceur
    constraints.gridy = 3;
    this.add(Box.createVerticalStrut(15), constraints);

    // Champ de tag utilisateur
    JLabel tagLabel = new JLabel("Tag utilisateur:");
    tagLabel.setFont(SwingTheme.TEXT_BOLD);
    constraints.gridx = 0;
    constraints.gridy = 4;
    constraints.gridwidth = 1;
    this.add(tagLabel, constraints);

    tagField = new JTextField(15);
    SwingTheme.styleTextComponent(tagField);
    constraints.gridx = 1;
    constraints.gridy = 4;
    this.add(tagField, constraints);

    // Info bulle pour le tag
    JLabel tagInfoLabel = new JLabel("Votre identifiant unique (ex: @pseudo)");
    tagInfoLabel.setFont(SwingTheme.TOOLTIP_FONT);
    tagInfoLabel.setForeground(SwingTheme.TEXT_SECONDARY);
    constraints.gridx = 1;
    constraints.gridy = 5;
    this.add(tagInfoLabel, constraints);

    // Champ de nom d'utilisateur
    JLabel nameLabel = new JLabel("Nom d'utilisateur:");
    nameLabel.setFont(SwingTheme.TEXT_BOLD);
    constraints.gridx = 0;
    constraints.gridy = 6;
    this.add(nameLabel, constraints);

    nameField = new JTextField(15);
    SwingTheme.styleTextComponent(nameField);
    constraints.gridx = 1;
    constraints.gridy = 6;
    this.add(nameField, constraints);

    // Champ de mot de passe
    JLabel passwordLabel = new JLabel("Mot de passe:");
    passwordLabel.setFont(SwingTheme.TEXT_BOLD);
    constraints.gridx = 0;
    constraints.gridy = 7;
    this.add(passwordLabel, constraints);

    passwordField = new JPasswordField(15);
    SwingTheme.styleTextComponent(passwordField);
    constraints.gridx = 1;
    constraints.gridy = 7;
    this.add(passwordField, constraints);

    // Info bulle pour le mot de passe
    JLabel passwordInfoLabel = new JLabel("Choisissez un mot de passe sécurisé");
    passwordInfoLabel.setFont(SwingTheme.TOOLTIP_FONT);
    passwordInfoLabel.setForeground(SwingTheme.TEXT_SECONDARY);
    constraints.gridx = 1;
    constraints.gridy = 8;
    this.add(passwordInfoLabel, constraints);

    // Champ de confirmation du mot de passe
    JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe:");
    confirmPasswordLabel.setFont(SwingTheme.TEXT_BOLD);
    constraints.gridx = 0;
    constraints.gridy = 9;
    this.add(confirmPasswordLabel, constraints);

    confirmPasswordField = new JPasswordField(15);
    SwingTheme.styleTextComponent(confirmPasswordField);
    constraints.gridx = 1;
    constraints.gridy = 9;
    this.add(confirmPasswordField, constraints);

    // Message d'erreur stylisé
    errorLabel = new JLabel(" ");
    errorLabel.setForeground(SwingTheme.DANGER);
    errorLabel.setFont(SwingTheme.TOOLTIP_FONT);
    constraints.gridx = 0;
    constraints.gridy = 10;
    constraints.gridwidth = 2;
    this.add(errorLabel, constraints);

    // Panneau pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    buttonPanel.setOpaque(false);

    // Bouton d'inscription principal - utilise la couleur secondaire (verte)
    registerButton = new JButton("S'inscrire");
    registerButton.setFont(SwingTheme.TEXT_BOLD);
    registerButton.setBackground(SwingTheme.SECONDARY);
    registerButton.setForeground(SwingTheme.TEXT_ON_COLOR);
    registerButton.setFocusPainted(false);
    registerButton.setBorder(SwingTheme.createRoundedBorder());
    registerButton.setPreferredSize(SwingTheme.BUTTON_SIZE);
    buttonPanel.add(registerButton);

    // Bouton de connexion secondaire
    loginButton = new JButton("Se connecter");
    SwingTheme.styleButton(loginButton, false);
    buttonPanel.add(loginButton);

    constraints.gridx = 0;
    constraints.gridy = 11;
    constraints.gridwidth = 2;
    constraints.insets = new Insets(20, 10, 10, 10);
    this.add(buttonPanel, constraints);

    // Note de confidentialité
    JLabel privacyLabel = new JLabel("En vous inscrivant, vous acceptez nos conditions d'utilisation", SwingConstants.CENTER);
    privacyLabel.setFont(SwingTheme.TOOLTIP_FONT);
    privacyLabel.setForeground(SwingTheme.TEXT_SECONDARY);
    constraints.gridx = 0;
    constraints.gridy = 12;
    constraints.gridwidth = 2;
    constraints.insets = new Insets(5, 10, 10, 10);
    this.add(privacyLabel, constraints);
  }

  // Implémentation des méthodes de l'interface IRegisterView

  @Override
  public void setRegisterButtonListener(ActionListener listener) {
    registerButton.addActionListener(listener);
  }

  @Override
  public void setLoginButtonListener(ActionListener listener) {
    loginButton.addActionListener(listener);
  }

  @Override
  public String getUserTag() {
    return tagField.getText().trim();
  }

  @Override
  public String getUserName() {
    return nameField.getText().trim();
  }

  @Override
  public String getUserPassword() {
    return new String(passwordField.getPassword());
  }

  @Override
  public String getConfirmPassword() {
    return new String(confirmPasswordField.getPassword());
  }

  @Override
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
  }

  @Override
  public void resetFields() {
    tagField.setText("");
    nameField.setText("");
    passwordField.setText("");
    confirmPasswordField.setText("");
    errorLabel.setText("");
  }

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

  @Override
  public IDatabase getDatabase() {
    return database;
  }
}