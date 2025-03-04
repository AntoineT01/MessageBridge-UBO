package com.ubo.tp.message.ihm.login;

import com.ubo.tp.message.common.IconFactory;
import com.ubo.tp.message.common.ImageUtils;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashSet;

public class LoginView extends JPanel {
  protected final JTextField usernameField;
  protected final JPasswordField passwordField;
  protected final JButton loginButton;
  protected final JButton signupButton;
  protected LoginController controller;

  public LoginView() {
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Création des composants
    usernameField = new JTextField(20);
    passwordField = new JPasswordField(20);
    loginButton = new JButton("Se connecter");
    signupButton = new JButton("Créer un compte");

    // Configurer les boutons
    loginButton.setIcon(IconFactory.getResourceOrCustom("/icons/login.png",
                                                        IconFactory.ICON_SMALL, size -> IconFactory.createUserIcon(size)));
    signupButton.setIcon(IconFactory.getResourceOrCustom("/icons/signup.png",
                                                         IconFactory.ICON_SMALL, size -> IconFactory.createUserIcon(size)));

    // Ajouter un logo en haut
    ImageIcon logoIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 100, 100);
    JLabel logoLabel = new JLabel(logoIcon);
    logoLabel.setHorizontalAlignment(JLabel.CENTER);

    // Placement des composants avec GridBagLayout
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Logo
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    this.add(logoLabel, gbc);

    // Titre
    JLabel titleLabel = new JLabel("Bienvenue dans MessageApp");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    gbc.gridy = 1;
    this.add(titleLabel, gbc);

    // Champ username
    JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    this.add(usernameLabel, gbc);

    gbc.gridx = 1;
    this.add(usernameField, gbc);

    // Champ password
    JLabel passwordLabel = new JLabel("Mot de passe :");
    gbc.gridx = 0;
    gbc.gridy = 3;
    this.add(passwordLabel, gbc);

    gbc.gridx = 1;
    this.add(passwordField, gbc);

    // Boutons de connexion et création de compte
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(loginButton);
    buttonPanel.add(signupButton);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    this.add(buttonPanel, gbc);
  }

  public void setController(LoginController controller) {
    this.controller = controller;

    // Configurer les actions des boutons
    loginButton.addActionListener(e -> {
      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());
      controller.login(username, password);
    });

    signupButton.addActionListener(e -> {
      controller.openSignupView();
    });
  }

  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
  }

  public void clearFields() {
    usernameField.setText("");
    passwordField.setText("");
  }
}