package com.ubo.tp.message.ihm.components;

import com.ubo.tp.message.common.ui.IconFactory;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.ihm.session.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.Set;

/**
 * Panneau de connexion
 */
public class LoginPanel extends JPanel {
  /**
   * Base de données
   */
  protected final IDatabase mDatabase;

  /**
   * Gestionnaire de session
   */
  protected final SessionManager mSessionManager;

  /**
   * Champ pour le tag utilisateur
   */
  protected JTextField mTagField;

  /**
   * Champ pour le mot de passe
   */
  protected JPasswordField mPasswordField;

  /**
   * Bouton pour la connexion
   */
  protected JButton mLoginButton;

  /**
   * Bouton pour basculer vers l'inscription
   */
  protected JButton mRegisterButton;

  /**
   * Étiquette pour afficher les messages d'erreur
   */
  protected JLabel mErrorLabel;

  /**
   * Constructeur
   */
  public LoginPanel(IDatabase database, SessionManager sessionManager) {
    this.mDatabase = database;
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
    JLabel titleLabel = new JLabel("Connexion", JLabel.CENTER);
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

    mTagField = new JTextField(15);
    constraints.gridx = 1;
    constraints.gridy = 1;
    this.add(mTagField, constraints);

    // Champ pour le mot de passe
    JLabel passwordLabel = new JLabel("Mot de passe:");
    constraints.gridx = 0;
    constraints.gridy = 2;
    this.add(passwordLabel, constraints);

    mPasswordField = new JPasswordField(15);
    constraints.gridx = 1;
    constraints.gridy = 2;
    this.add(mPasswordField, constraints);

    // Étiquette pour les erreurs
    mErrorLabel = new JLabel("");
    mErrorLabel.setForeground(Color.RED);
    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.gridwidth = 2;
    this.add(mErrorLabel, constraints);

    // Panneau pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // Bouton de connexion
    mLoginButton = new JButton("Connexion");
    mLoginButton.addActionListener(new LoginActionListener());
    buttonPanel.add(mLoginButton);

    // Bouton d'inscription
    mRegisterButton = new JButton("S'inscrire");
    buttonPanel.add(mRegisterButton);

    constraints.gridx = 0;
    constraints.gridy = 4;
    constraints.gridwidth = 2;
    this.add(buttonPanel, constraints);
  }

  /**
   * Récupère le bouton d'inscription
   */
  public JButton getRegisterButton() {
    return mRegisterButton;
  }

  /**
   * Classe interne pour gérer l'action de connexion
   */
  class LoginActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      // Récupérer les valeurs des champs
      String userTag = mTagField.getText().trim();
      String password = new String(mPasswordField.getPassword());

      // Vérifier que les champs ne sont pas vides
      if (userTag.isEmpty() || password.isEmpty()) {
        mErrorLabel.setText("Veuillez remplir tous les champs");
        return;
      }

      // Ajouter le préfixe @ si nécessaire, comme lors de l'inscription
      if (!userTag.startsWith("@")) {
        userTag = "@" + userTag;
      }

      // Chercher l'utilisateur dans la base de données
      User user = findUserByTag(userTag);

      // Vérifier si l'utilisateur existe
      if (user == null) {
        mErrorLabel.setText("Utilisateur inconnu");
        return;
      }

      // Vérifier le mot de passe
      if (!user.getUserPassword().equals(password)) {
        mErrorLabel.setText("Mot de passe incorrect");
        return;
      }

      // Connexion réussie
      mErrorLabel.setText("");
      mSessionManager.login(user);

      // Réinitialiser les champs
      mTagField.setText("");
      mPasswordField.setText("");
    }

    /**
     * Recherche un utilisateur par son tag
     */
    private User findUserByTag(String userTag) {
      Set<User> users = mDatabase.getUsers();

      Optional<User> foundUser = users.stream()
        .filter(user -> user.getUserTag().equals(userTag))
        .findFirst();

      return foundUser.orElse(null);
    }
  }
}