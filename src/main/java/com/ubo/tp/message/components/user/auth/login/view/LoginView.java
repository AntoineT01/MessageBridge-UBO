package com.ubo.tp.message.components.user.auth.login.view;

import com.ubo.tp.message.common.ui.ShadowBorder;
import com.ubo.tp.message.common.utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginView extends JPanel implements ILoginView {

  private JTextField tagField;
  private JPasswordField passwordField;
  private JButton loginButton;
  private JButton registerButton;
  private JLabel errorLabel;

  public LoginView() {
    this.initGUI();
  }

  private void initGUI() {
    // Configuration du panneau principal
    this.setBackground(new Color(245, 248, 250));
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(40, 40, 40, 40));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(10, 10, 10, 10);

    // Panneau central avec effet d'ombre
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(Color.WHITE);
    centerPanel.setBorder(BorderFactory.createCompoundBorder(
      new ShadowBorder(5, Color.BLACK, 0.1f),
      BorderFactory.createEmptyBorder(25, 30, 25, 30)
    ));

    GridBagConstraints centerConstraints = new GridBagConstraints();
    centerConstraints.fill = GridBagConstraints.HORIZONTAL;
    centerConstraints.insets = new Insets(8, 8, 8, 8);

    // Logo
    JLabel logoLabel = new JLabel();
    ImageIcon logo = ImageUtils.loadScaledIcon("/tux_logo.png", 100, 100);
    logoLabel.setIcon(logo);
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 0;
    centerConstraints.gridwidth = 1;
    centerPanel.add(logoLabel, centerConstraints);

    // Titre
    JLabel titleLabel = new JLabel("Bienvenue sur MessageApp", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(new Color(52, 152, 219));
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 1;
    centerPanel.add(titleLabel, centerConstraints);

    // Sous-titre
    JLabel subtitleLabel = new JLabel("Connectez-vous pour continuer", SwingConstants.CENTER);
    subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    subtitleLabel.setForeground(new Color(100, 100, 100));
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 2;
    centerPanel.add(subtitleLabel, centerConstraints);

    // Espacement
    centerConstraints.gridy = 3;
    centerPanel.add(Box.createVerticalStrut(20), centerConstraints);

    // Champ de tag
    JPanel tagPanel = new JPanel(new BorderLayout(5, 5));
    tagPanel.setOpaque(false);
    JLabel tagLabel = new JLabel("Tag utilisateur");
    tagLabel.setFont(new Font("Arial", Font.BOLD, 14));
    tagPanel.add(tagLabel, BorderLayout.NORTH);

    tagField = new JTextField(20);
    tagField.setFont(new Font("Arial", Font.PLAIN, 14));
    tagField.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    tagField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        tagField.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true),
          BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
      }

      @Override
      public void focusLost(FocusEvent e) {
        tagField.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
          BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
      }
    });
    tagPanel.add(tagField, BorderLayout.CENTER);

    centerConstraints.gridx = 0;
    centerConstraints.gridy = 4;
    centerPanel.add(tagPanel, centerConstraints);

    // Champ de mot de passe
    JPanel passwordPanel = new JPanel(new BorderLayout(5, 5));
    passwordPanel.setOpaque(false);
    JLabel passwordLabel = new JLabel("Mot de passe");
    passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    passwordPanel.add(passwordLabel, BorderLayout.NORTH);

    passwordField = new JPasswordField(20);
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    passwordField.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    passwordField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        passwordField.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true),
          BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
      }

      @Override
      public void focusLost(FocusEvent e) {
        passwordField.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
          BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
      }
    });
    passwordPanel.add(passwordField, BorderLayout.CENTER);

    centerConstraints.gridx = 0;
    centerConstraints.gridy = 5;
    centerPanel.add(passwordPanel, centerConstraints);

    // Message d'erreur
    errorLabel = new JLabel(" ");
    errorLabel.setForeground(new Color(231, 76, 60));
    errorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 6;
    centerPanel.add(errorLabel, centerConstraints);

    // Panneau de boutons
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
    buttonPanel.setOpaque(false);

    // Bouton de connexion
    loginButton = new JButton("Connexion");
    loginButton.setFont(new Font("Arial", Font.BOLD, 14));
    loginButton.setBackground(new Color(52, 152, 219));
    loginButton.setForeground(Color.WHITE);
    loginButton.setFocusPainted(false);
    loginButton.setBorderPainted(false);
    loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loginButton.setPreferredSize(new Dimension(150, 45));
    buttonPanel.add(loginButton);

    // Bouton d'inscription
    registerButton = new JButton("S'inscrire");
    registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
    registerButton.setBackground(new Color(236, 240, 241));
    registerButton.setForeground(new Color(44, 62, 80));
    registerButton.setFocusPainted(false);
    registerButton.setBorderPainted(false);
    registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    registerButton.setPreferredSize(new Dimension(150, 45));
    buttonPanel.add(registerButton);

    centerConstraints.gridx = 0;
    centerConstraints.gridy = 7;
    centerConstraints.insets = new Insets(20, 8, 8, 8);
    centerPanel.add(buttonPanel, centerConstraints);

    // Ajouter le panneau central au panneau principal
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    this.add(centerPanel, constraints);
  }

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