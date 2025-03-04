package com.ubo.tp.message.ihm.login;

import com.ubo.tp.message.common.IconFactory;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.UUID;

public class SignupView extends JPanel {
  protected final JTextField tagField;
  protected final JTextField nameField;
  protected final JPasswordField passwordField;
  protected final JPasswordField confirmPasswordField;
  protected final JButton chooseAvatarButton;
  protected final JButton signupButton;
  protected final JButton cancelButton;
  protected final JLabel avatarPathLabel;

  protected String selectedAvatarPath = "";
  protected LoginController controller;

  public SignupView() {
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Création des composants
    tagField = new JTextField(20);
    nameField = new JTextField(20);
    passwordField = new JPasswordField(20);
    confirmPasswordField = new JPasswordField(20);
    chooseAvatarButton = new JButton("Choisir un avatar");
    signupButton = new JButton("Créer le compte");
    cancelButton = new JButton("Annuler");
    avatarPathLabel = new JLabel("Aucun avatar sélectionné");

    // Configurer les boutons
    signupButton.setIcon(IconFactory.getResourceOrCustom("/icons/signup.png",
                                                         IconFactory.ICON_SMALL, size -> IconFactory.createUserIcon(size)));
    cancelButton.setIcon(IconFactory.createCloseIcon(IconFactory.ICON_SMALL));

    // Placement des composants avec GridBagLayout
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Titre
    JLabel titleLabel = new JLabel("Création d'un compte");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setHorizontalAlignment(JLabel.CENTER);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    this.add(titleLabel, gbc);

    // Champ tag
    JLabel tagLabel = new JLabel("Tag utilisateur (@) :");
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    this.add(tagLabel, gbc);

    gbc.gridx = 1;
    this.add(tagField, gbc);

    // Champ nom
    JLabel nameLabel = new JLabel("Nom complet :");
    gbc.gridx = 0;
    gbc.gridy = 2;
    this.add(nameLabel, gbc);

    gbc.gridx = 1;
    this.add(nameField, gbc);

    // Champ mot de passe
    JLabel passwordLabel = new JLabel("Mot de passe :");
    gbc.gridx = 0;
    gbc.gridy = 3;
    this.add(passwordLabel, gbc);

    gbc.gridx = 1;
    this.add(passwordField, gbc);

    // Champ confirmation mot de passe
    JLabel confirmPasswordLabel = new JLabel("Confirmer le mot de passe :");
    gbc.gridx = 0;
    gbc.gridy = 4;
    this.add(confirmPasswordLabel, gbc);

    gbc.gridx = 1;
    this.add(confirmPasswordField, gbc);

    // Sélection d'avatar
    JPanel avatarPanel = new JPanel(new BorderLayout());
    avatarPanel.add(chooseAvatarButton, BorderLayout.WEST);
    avatarPanel.add(avatarPathLabel, BorderLayout.CENTER);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    this.add(avatarPanel, gbc);

    // Boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(signupButton);
    buttonPanel.add(cancelButton);

    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    this.add(buttonPanel, gbc);

    // Configuration du bouton de sélection d'avatar
    chooseAvatarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choisir un avatar");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(SignupView.this);
        if (result == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          selectedAvatarPath = selectedFile.getAbsolutePath();
          avatarPathLabel.setText(selectedFile.getName());
        }
      }
    });
  }

  public void setController(LoginController controller) {
    this.controller = controller;

    // Configurer les actions des boutons
    signupButton.addActionListener(e -> {
      if (validateFields()) {
        String tag = tagField.getText();
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());

        User newUser = new User(
          UUID.randomUUID(),
          tag,
          password,
          name,
          new HashSet<>(),
          selectedAvatarPath
        );

        controller.signup(newUser);
      }
    });

    cancelButton.addActionListener(e -> {
      controller.cancelSignup();
    });
  }

  private boolean validateFields() {
    String tag = tagField.getText();
    String name = nameField.getText();
    String password = new String(passwordField.getPassword());
    String confirmPassword = new String(confirmPasswordField.getPassword());

    // Vérification du tag
    if (tag == null || tag.trim().isEmpty()) {
      showError("Le tag utilisateur ne peut pas être vide");
      return false;
    }

    // Vérification du nom
    if (name == null || name.trim().isEmpty()) {
      showError("Le nom ne peut pas être vide");
      return false;
    }

    // Vérification du mot de passe
    if (password == null || password.isEmpty()) {
      showError("Le mot de passe ne peut pas être vide");
      return false;
    }

    // Vérification de la confirmation du mot de passe
    if (!password.equals(confirmPassword)) {
      showError("Les mots de passe ne correspondent pas");
      return false;
    }

    return true;
  }

  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
  }

  public void clearFields() {
    tagField.setText("");
    nameField.setText("");
    passwordField.setText("");
    confirmPasswordField.setText("");
    selectedAvatarPath = "";
    avatarPathLabel.setText("Aucun avatar sélectionné");
  }
}