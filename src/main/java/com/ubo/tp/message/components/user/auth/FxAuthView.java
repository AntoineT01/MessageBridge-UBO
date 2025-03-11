package com.ubo.tp.message.components.user.auth;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.HashSet;
import java.util.UUID;

/**
 * Vue d'authentification JavaFX.
 * Cette classe adapte les fonctionnalités de IAuthComponent en JavaFX.
 */
public class FxAuthView extends BorderPane {

  // Panneaux de contenu
  private VBox loginPane;
  private VBox registerPane;

  // Composants du formulaire de connexion
  private TextField loginTagField;
  private PasswordField loginPasswordField;
  private Button loginButton;
  private Button switchToRegisterButton;
  private Text loginErrorText;

  // Composants du formulaire d'inscription
  private TextField registerTagField;
  private TextField registerNameField;
  private PasswordField registerPasswordField;
  private PasswordField confirmPasswordField;
  private Button registerButton;
  private Button switchToLoginButton;
  private Text registerErrorText;

  // Services
  private final IDatabase database;
  private final EntityManager entityManager;
  private final SessionManager sessionManager;

  // Écouteur pour la connexion réussie
  private EventHandler<ActionEvent> authSuccessListener;

  /**
   * Constructeur.
   */
  public FxAuthView(IDatabase database, EntityManager entityManager, SessionManager sessionManager) {
    this.database = database;
    this.entityManager = entityManager;
    this.sessionManager = sessionManager;

    // Initialisation de l'interface
    setPadding(new Insets(20));
    initLoginPane();
    initRegisterPane();

    // Par défaut, on affiche le login
    setCenter(loginPane);

    // Configuration des actions
    setupEventHandlers();
  }

  /**
   * Initialise le panneau de connexion.
   */
  private void initLoginPane() {
    loginPane = new VBox(15);
    loginPane.setAlignment(Pos.CENTER);
    loginPane.setPadding(new Insets(20));

    // Logo
    ImageView logoView = createLogoImageView();

    // Titre
    Text titleText = new Text("Bienvenue sur MessageApp");
    titleText.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    titleText.setFill(Color.rgb(52, 152, 219));

    // Sous-titre
    Text subtitleText = new Text("Connectez-vous pour continuer");
    subtitleText.setFont(Font.font("Arial", 14));

    // Grille de formulaire
    GridPane formGrid = new GridPane();
    formGrid.setHgap(10);
    formGrid.setVgap(10);
    formGrid.setAlignment(Pos.CENTER);

    // Tag utilisateur
    Label tagLabel = new Label("Tag utilisateur :");
    tagLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    formGrid.add(tagLabel, 0, 0);

    loginTagField = new TextField();
    loginTagField.setPromptText("Entrez votre tag (ex: @user)");
    formGrid.add(loginTagField, 1, 0);

    // Mot de passe
    Label passwordLabel = new Label("Mot de passe :");
    passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    formGrid.add(passwordLabel, 0, 1);

    loginPasswordField = new PasswordField();
    loginPasswordField.setPromptText("Entrez votre mot de passe");
    formGrid.add(loginPasswordField, 1, 1);

    // Message d'erreur
    loginErrorText = new Text();
    loginErrorText.setFill(Color.RED);

    // Boutons
    HBox buttonBox = new HBox(20);
    buttonBox.setAlignment(Pos.CENTER);

    loginButton = new Button("Connexion");
    loginButton.setDefaultButton(true);
    loginButton.setPrefSize(150, 40);

    switchToRegisterButton = new Button("S'inscrire");
    switchToRegisterButton.setPrefSize(150, 40);

    buttonBox.getChildren().addAll(loginButton, switchToRegisterButton);

    // Ajout des éléments
    loginPane.getChildren().addAll(
      logoView, titleText, subtitleText, formGrid, loginErrorText, buttonBox
    );
  }

  /**
   * Initialise le panneau d'inscription.
   */
  private void initRegisterPane() {
    registerPane = new VBox(15);
    registerPane.setAlignment(Pos.CENTER);
    registerPane.setPadding(new Insets(20));

    // Logo
    ImageView logoView = createLogoImageView();

    // Titre
    Text titleText = new Text("Créer un compte");
    titleText.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    titleText.setFill(Color.rgb(46, 204, 113));

    // Sous-titre
    Text subtitleText = new Text("Rejoignez la communauté MessageApp");
    subtitleText.setFont(Font.font("Arial", 14));

    // Grille de formulaire
    GridPane formGrid = new GridPane();
    formGrid.setHgap(10);
    formGrid.setVgap(10);
    formGrid.setAlignment(Pos.CENTER);

    // Tag utilisateur
    Label tagLabel = new Label("Tag utilisateur :");
    tagLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    formGrid.add(tagLabel, 0, 0);

    registerTagField = new TextField();
    registerTagField.setPromptText("Entrez votre tag (ex: @user)");
    formGrid.add(registerTagField, 1, 0);

    // Nom
    Label nameLabel = new Label("Nom d'utilisateur :");
    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    formGrid.add(nameLabel, 0, 1);

    registerNameField = new TextField();
    registerNameField.setPromptText("Entrez votre nom");
    formGrid.add(registerNameField, 1, 1);

    // Mot de passe
    Label passwordLabel = new Label("Mot de passe :");
    passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    formGrid.add(passwordLabel, 0, 2);

    registerPasswordField = new PasswordField();
    registerPasswordField.setPromptText("Choisissez un mot de passe");
    formGrid.add(registerPasswordField, 1, 2);

    // Confirmation du mot de passe
    Label confirmLabel = new Label("Confirmer mot de passe :");
    confirmLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    formGrid.add(confirmLabel, 0, 3);

    confirmPasswordField = new PasswordField();
    confirmPasswordField.setPromptText("Confirmez le mot de passe");
    formGrid.add(confirmPasswordField, 1, 3);

    // Message d'erreur
    registerErrorText = new Text();
    registerErrorText.setFill(Color.RED);

    // Boutons
    HBox buttonBox = new HBox(20);
    buttonBox.setAlignment(Pos.CENTER);

    registerButton = new Button("S'inscrire");
    registerButton.setDefaultButton(true);
    registerButton.setPrefSize(150, 40);

    switchToLoginButton = new Button("Se connecter");
    switchToLoginButton.setPrefSize(150, 40);

    buttonBox.getChildren().addAll(registerButton, switchToLoginButton);

    // Ajout des éléments
    registerPane.getChildren().addAll(
      logoView, titleText, subtitleText, formGrid, registerErrorText, buttonBox
    );
  }

  /**
   * Configure les gestionnaires d'événements.
   */
  private void setupEventHandlers() {
    // Basculer entre connexion et inscription
    switchToRegisterButton.setOnAction(e -> showRegisterView());
    switchToLoginButton.setOnAction(e -> showLoginView());

    // Connexion
    loginButton.setOnAction(e -> attemptLogin());

    // Inscription
    registerButton.setOnAction(e -> attemptRegistration());
  }

  /**
   * Tente une connexion avec les identifiants saisis.
   */
  private void attemptLogin() {
    String userTag = loginTagField.getText().trim();
    String password = loginPasswordField.getText();

    // Validation des champs
    if (userTag.isEmpty() || password.isEmpty()) {
      loginErrorText.setText("Veuillez remplir tous les champs");
      return;
    }

    // Ajouter le préfixe @ si nécessaire
    if (!userTag.startsWith("@")) {
      userTag = "@" + userTag;
    }

    // Recherche de l'utilisateur
    User user = findUserByTag(userTag);

    if (user == null) {
      loginErrorText.setText("Utilisateur inconnu");
    } else if (!user.getUserPassword().equals(password)) {
      loginErrorText.setText("Mot de passe incorrect");
    } else {
      // Connexion réussie
      sessionManager.login(user);
      loginTagField.clear();
      loginPasswordField.clear();
      loginErrorText.setText("");

      // Notification du succès
      if (authSuccessListener != null) {
        authSuccessListener.handle(new ActionEvent());
      }
    }
  }

  /**
   * Tente une inscription avec les données saisies.
   */
  private void attemptRegistration() {
    String userTag = registerTagField.getText().trim();
    String userName = registerNameField.getText().trim();
    String password = registerPasswordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    // Validation des champs
    if (userTag.isEmpty() || userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      registerErrorText.setText("Veuillez remplir tous les champs");
      return;
    }

    // Vérification des mots de passe
    if (!password.equals(confirmPassword)) {
      registerErrorText.setText("Les mots de passe ne correspondent pas");
      return;
    }

    // Normalisation du tag
    if (!userTag.startsWith("@")) {
      userTag = "@" + userTag;
    }

    // Vérification de l'unicité du tag
    if (isUserTagTaken(userTag)) {
      registerErrorText.setText("Ce tag utilisateur est déjà utilisé");
      return;
    }

    try {
      // Création du nouvel utilisateur
      User newUser = new User(UUID.randomUUID(), userTag, password, userName, new HashSet<>(), "");

      // Ajout à la base de données
      database.addUser(newUser);

      // Écriture du fichier
      entityManager.writeUserFile(newUser);

      // Réinitialisation des champs
      registerTagField.clear();
      registerNameField.clear();
      registerPasswordField.clear();
      confirmPasswordField.clear();
      registerErrorText.setText("");

      // Connexion automatique
      sessionManager.login(newUser);

      // Notification du succès
      if (authSuccessListener != null) {
        authSuccessListener.handle(new ActionEvent());
      }
    } catch (Exception e) {
      registerErrorText.setText("Erreur lors de l'inscription: " + e.getMessage());
    }
  }

  /**
   * Vérifie si un tag utilisateur est déjà pris.
   */
  private boolean isUserTagTaken(String userTag) {
    return findUserByTag(userTag) != null;
  }

  /**
   * Recherche un utilisateur par son tag.
   */
  private User findUserByTag(String userTag) {
    for (User user : database.getUsers()) {
      if (user.getUserTag().equals(userTag)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Crée une vue d'image pour le logo.
   */
  private ImageView createLogoImageView() {
    try {
      InputStream logoStream = getClass().getResourceAsStream("/tux_logo.png");
      if (logoStream != null) {
        Image logoImage = new Image(logoStream, 80, 80, true, true);
        return new ImageView(logoImage);
      }
    } catch (Exception e) {
      System.err.println("Impossible de charger le logo: " + e.getMessage());
    }

    // Image par défaut si échec
    return new ImageView();
  }

  /**
   * Affiche la vue de connexion.
   */
  public void showLoginView() {
    setCenter(loginPane);
  }

  /**
   * Affiche la vue d'inscription.
   */
  public void showRegisterView() {
    setCenter(registerPane);
  }

  /**
   * Définit l'écouteur pour les événements d'authentification réussie.
   */
  public void setAuthSuccessListener(EventHandler<ActionEvent> listener) {
    this.authSuccessListener = listener;
  }
}