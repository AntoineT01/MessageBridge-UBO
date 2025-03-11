package com.ubo.tp.message.components.user.auth.login.view;

import com.ubo.tp.message.common.ui.EnvUI;
import com.ubo.tp.message.common.ui.SwingTheme;
import com.ubo.tp.message.common.utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
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
    // Configuration du panneau principal
    this.setBackground(SwingTheme.BACKGROUND);
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(40, 40, 40, 40));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(EnvUI.SPACING_STANDARD, EnvUI.SPACING_STANDARD,
                                    EnvUI.SPACING_STANDARD, EnvUI.SPACING_STANDARD);

    // Logo plus visible
    JLabel logoLabel = new JLabel();
    ImageIcon logo = ImageUtils.loadScaledIcon("/tux_logo.png", 80, 80);
    logoLabel.setIcon(logo);
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    this.add(logoLabel, constraints);

    // Titre stylisé
    JLabel titleLabel = new JLabel("Bienvenue sur MessageApp", SwingConstants.CENTER);
    SwingTheme.setTitleStyle(titleLabel);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 2;
    this.add(titleLabel, constraints);

    // Sous-titre
    JLabel subtitleLabel = new JLabel("Connectez-vous pour continuer", SwingConstants.CENTER);
    subtitleLabel.setFont(SwingTheme.TEXT_REGULAR);
    subtitleLabel.setForeground(SwingTheme.TEXT);
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.gridwidth = 2;
    this.add(subtitleLabel, constraints);

    // Espaceur
    constraints.gridy = 3;
    this.add(Box.createVerticalStrut(20), constraints);

    // Champ de tag utilisateur
    JLabel tagLabel = new JLabel("Tag utilisateur:");
    tagLabel.setFont(SwingTheme.TEXT_BOLD);
    constraints.gridx = 0;
    constraints.gridy = 4;
    constraints.gridwidth = 2;
    this.add(tagLabel, constraints);

    tagField = new JTextField(20);
    SwingTheme.styleTextComponent(tagField);
    constraints.gridx = 0;
    constraints.gridy = 5;
    this.add(tagField, constraints);

    // Champ de mot de passe
    JLabel passwordLabel = new JLabel("Mot de passe:");
    passwordLabel.setFont(SwingTheme.TEXT_BOLD);
    constraints.gridx = 0;
    constraints.gridy = 6;
    this.add(passwordLabel, constraints);

    passwordField = new JPasswordField(20);
    SwingTheme.styleTextComponent(passwordField);
    constraints.gridx = 0;
    constraints.gridy = 7;
    this.add(passwordField, constraints);

    // Message d'erreur stylisé
    errorLabel = new JLabel(" ");
    errorLabel.setForeground(SwingTheme.DANGER);
    errorLabel.setFont(SwingTheme.TOOLTIP_FONT);
    constraints.gridx = 0;
    constraints.gridy = 8;
    constraints.gridwidth = 2;
    this.add(errorLabel, constraints);

    // Panneau pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    buttonPanel.setOpaque(false);

    // Bouton de connexion attractif
    loginButton = new JButton("Connexion");
    SwingTheme.styleButton(loginButton, true);
    buttonPanel.add(loginButton);

    // Bouton d'inscription secondaire
    registerButton = new JButton("S'inscrire");
    SwingTheme.styleButton(registerButton, false);
    buttonPanel.add(registerButton);

    constraints.gridx = 0;
    constraints.gridy = 9;
    constraints.gridwidth = 2;
    constraints.insets = new Insets(20, 10, 10, 10);
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