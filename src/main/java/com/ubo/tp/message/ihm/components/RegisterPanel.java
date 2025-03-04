package com.ubo.tp.message.ihm.components;

import com.ubo.tp.message.common.IconFactory;
import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.ihm.session.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Panneau d'inscription d'un nouvel utilisateur
 */
public class RegisterPanel extends JPanel {
  /**
   * Base de données
   */
  protected final IDatabase mDatabase;

  /**
   * Gestionnaire d'entités
   */
  protected final EntityManager mEntityManager;

  /**
   * Gestionnaire de session
   */
  protected final SessionManager mSessionManager;

  /**
   * Champ pour le nom de l'utilisateur
   */
  protected JTextField mNameField;

  /**
   * Champ pour le tag utilisateur
   */
  protected JTextField mTagField;

  /**
   * Champ pour le mot de passe
   */
  protected JPasswordField mPasswordField;

  /**
   * Champ pour confirmer le mot de passe
   */
  protected JPasswordField mConfirmPasswordField;

  /**
   * Bouton pour sélectionner un avatar
   */
  protected JButton mAvatarButton;

  /**
   * Label pour afficher le nom du fichier avatar sélectionné
   */
  protected JLabel mAvatarFileLabel;

  /**
   * Chemin vers le fichier avatar
   */
  protected String mAvatarPath = "";

  /**
   * Bouton pour créer le compte
   */
  protected JButton mRegisterButton;

  /**
   * Bouton pour revenir à la connexion
   */
  protected JButton mLoginButton;

  /**
   * Étiquette pour afficher les messages d'erreur
   */
  protected JLabel mErrorLabel;

  /**
   * Constructeur
   */
  public RegisterPanel(IDatabase database, EntityManager entityManager, SessionManager sessionManager) {
    this.mDatabase = database;
    this.mEntityManager = entityManager;
    this.mSessionManager = sessionManager;

    this.initGUI();
  }

  /**
   * Initialisation de l'interface graphique
   */
  protected void initGUI() {
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(5, 5, 5, 5);

    // Titre du panneau
    JLabel titleLabel = new JLabel("Créer un compte", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setIcon(IconFactory.createUserIcon(IconFactory.ICON_MEDIUM));
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    this.add(titleLabel, constraints);

    // Champ pour le nom
    JLabel nameLabel = new JLabel("Nom:");
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 1;
    this.add(nameLabel, constraints);

    mNameField = new JTextField(15);
    constraints.gridx = 1;
    constraints.gridy = 1;
    this.add(mNameField, constraints);

    // Champ pour le tag utilisateur
    JLabel tagLabel = new JLabel("Tag utilisateur:");
    constraints.gridx = 0;
    constraints.gridy = 2;
    this.add(tagLabel, constraints);

    mTagField = new JTextField(15);
    constraints.gridx = 1;
    constraints.gridy = 2;
    this.add(mTagField, constraints);

    // Champ pour le mot de passe
    JLabel passwordLabel = new JLabel("Mot de passe:");
    constraints.gridx = 0;
    constraints.gridy = 3;
    this.add(passwordLabel, constraints);

    mPasswordField = new JPasswordField(15);
    constraints.gridx = 1;
    constraints.gridy = 3;
    this.add(mPasswordField, constraints);

    // Champ pour confirmer le mot de passe
    JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe:");
    constraints.gridx = 0;
    constraints.gridy = 4;
    this.add(confirmPasswordLabel, constraints);

    mConfirmPasswordField = new JPasswordField(15);
    constraints.gridx = 1;
    constraints.gridy = 4;
    this.add(mConfirmPasswordField, constraints);

    // Sélection d'avatar
    JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    mAvatarButton = new JButton("Choisir un avatar");
    mAvatarButton.addActionListener(new SelectAvatarActionListener());
    avatarPanel.add(mAvatarButton);

    mAvatarFileLabel = new JLabel("Aucun fichier sélectionné");
    avatarPanel.add(mAvatarFileLabel);

    constraints.gridx = 0;
    constraints.gridy = 5;
    constraints.gridwidth = 2;
    this.add(avatarPanel, constraints);

    // Étiquette pour les erreurs
    mErrorLabel = new JLabel("");
    mErrorLabel.setForeground(Color.RED);
    constraints.gridx = 0;
    constraints.gridy = 6;
    constraints.gridwidth = 2;
    this.add(mErrorLabel, constraints);

    // Panneau pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // Bouton d'inscription
    mRegisterButton = new JButton("Créer le compte");
    mRegisterButton.addActionListener(new RegisterActionListener());
    buttonPanel.add(mRegisterButton);

    // Bouton de connexion
    mLoginButton = new JButton("J'ai déjà un compte");
    buttonPanel.add(mLoginButton);

    constraints.gridx = 0;
    constraints.gridy = 7;
    constraints.gridwidth = 2;
    this.add(buttonPanel, constraints);
  }

  /**
   * Récupère le bouton de connexion
   */
  public JButton getLoginButton() {
    return mLoginButton;
  }

  /**
   * Classe interne pour gérer la sélection d'un avatar
   */
  class SelectAvatarActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Sélectionner un avatar");
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

      // Filtrer pour n'afficher que les images
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Images (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
      fileChooser.setFileFilter(filter);

      int result = fileChooser.showOpenDialog(RegisterPanel.this);

      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        mAvatarPath = selectedFile.getAbsolutePath();
        mAvatarFileLabel.setText(selectedFile.getName());
      }
    }
  }

  /**
   * Classe interne pour gérer l'action d'inscription
   */
  class RegisterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      // Récupérer les valeurs des champs
      String name = mNameField.getText().trim();
      String tag = mTagField.getText().trim();
      String password = new String(mPasswordField.getPassword());
      String confirmPassword = new String(mConfirmPasswordField.getPassword());

      // Vérifier que les champs obligatoires ne sont pas vides
      if (name.isEmpty() || tag.isEmpty()) {
        mErrorLabel.setText("Le nom et le tag sont obligatoires");
        return;
      }

      // Vérifier que le tag commence par @
      if (!tag.startsWith("@")) {
        tag = "@" + tag;
        mTagField.setText(tag);
      }

      // Vérifier que les mots de passe correspondent
      if (!password.equals(confirmPassword)) {
        mErrorLabel.setText("Les mots de passe ne correspondent pas");
        return;
      }

      // Vérifier que le tag est unique
      if (isTagAlreadyUsed(tag)) {
        mErrorLabel.setText("Ce tag est déjà utilisé");
        return;
      }

      // Créer le nouvel utilisateur
      User newUser = new User(UUID.randomUUID(), tag, password, name, new HashSet<>(), mAvatarPath);

      // Ajouter l'utilisateur à la base de données
      mDatabase.addUser(newUser);

      // Écrire le fichier utilisateur
      mEntityManager.writeUserFile(newUser);

      // Connecter l'utilisateur
      mSessionManager.login(newUser);

      // Réinitialiser les champs
      mNameField.setText("");
      mTagField.setText("");
      mPasswordField.setText("");
      mConfirmPasswordField.setText("");
      mAvatarPath = "";
      mAvatarFileLabel.setText("Aucun fichier sélectionné");
      mErrorLabel.setText("");
    }

    /**
     * Vérifie si un tag est déjà utilisé
     */
    private boolean isTagAlreadyUsed(String tag) {
      Set<User> users = mDatabase.getUsers();

      for (User user : users) {
        if (user.getUserTag().equals(tag)) {
          return true;
        }
      }

      return false;
    }
  }
}