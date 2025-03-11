package com.ubo.tp.message.components.user.profil.view;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Vue de profil utilisateur en JavaFX.
 * Cette classe adapte les fonctionnalités de IProfileComponent en JavaFX.
 */
public class FxProfileView extends BorderPane {

  // Services
  private final IDatabase database;
  private final EntityManager entityManager;
  private final SessionManager sessionManager;

  // Informations utilisateur
  private Text userTagText;
  private Text userNameText;
  private Text followersCountText;

  // Formulaire de mise à jour
  private TextField nameField;
  private PasswordField passwordField;
  private PasswordField confirmPasswordField;
  private Button updateButton;

  // Liste des messages
  private ListView<String> messagesList;

  // Navigation
  private Button messagesButton;

  // Messages d'état
  private Text errorText;
  private Text successText;

  // Écouteur pour la navigation vers les messages
  private EventHandler<ActionEvent> showMessagesListener;

  /**
   * Constructeur.
   */
  public FxProfileView(IDatabase database, EntityManager entityManager, SessionManager sessionManager) {
    this.database = database;
    this.entityManager = entityManager;
    this.sessionManager = sessionManager;

    // Initialisation de l'interface
    setPadding(new Insets(20));

    // Création des composants
    initComponents();

    // Configuration des actions
    setupEventHandlers();
  }

  /**
   * Initialise les composants.
   */
  private void initComponents() {
    // Panneau principal avec espacement vertical
    VBox mainBox = new VBox(15);
    mainBox.setPadding(new Insets(10));
    setCenter(mainBox);

    // En-tête - Informations utilisateur
    VBox headerBox = new VBox(10);
    headerBox.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 10px;");

    Text profileTitle = new Text("Mon Profil");
    profileTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    profileTitle.setFill(Color.rgb(52, 152, 219));

    // GridPane pour les informations utilisateur
    GridPane infoGrid = new GridPane();
    infoGrid.setHgap(10);
    infoGrid.setVgap(10);

    // Tag utilisateur
    Label tagLabel = new Label("Tag:");
    tagLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    infoGrid.add(tagLabel, 0, 0);

    userTagText = new Text();
    infoGrid.add(userTagText, 1, 0);

    // Nom
    Label nameLabel = new Label("Nom:");
    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    infoGrid.add(nameLabel, 0, 1);

    userNameText = new Text();
    infoGrid.add(userNameText, 1, 1);

    // Followers
    Label followersLabel = new Label("Followers:");
    followersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    infoGrid.add(followersLabel, 0, 2);

    followersCountText = new Text();
    infoGrid.add(followersCountText, 1, 2);

    headerBox.getChildren().addAll(profileTitle, infoGrid);

    // Panneau de mise à jour du profil
    VBox updateBox = new VBox(10);
    updateBox.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 10px;");

    Text updateTitle = new Text("Modifier votre profil");
    updateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
    updateTitle.setFill(Color.rgb(52, 73, 94));

    // GridPane pour le formulaire
    GridPane updateGrid = new GridPane();
    updateGrid.setHgap(10);
    updateGrid.setVgap(10);

    // Nouveau nom
    Label newNameLabel = new Label("Nouveau nom:");
    newNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    updateGrid.add(newNameLabel, 0, 0);

    nameField = new TextField();
    nameField.setPromptText("Laissez vide pour ne pas modifier");
    updateGrid.add(nameField, 1, 0);

    // Nouveau mot de passe
    Label newPasswordLabel = new Label("Nouveau mot de passe:");
    newPasswordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    updateGrid.add(newPasswordLabel, 0, 1);

    passwordField = new PasswordField();
    passwordField.setPromptText("Laissez vide pour ne pas modifier");
    updateGrid.add(passwordField, 1, 1);

    // Confirmation
    Label confirmLabel = new Label("Confirmer mot de passe:");
    confirmLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    updateGrid.add(confirmLabel, 0, 2);

    confirmPasswordField = new PasswordField();
    confirmPasswordField.setPromptText("Confirmez le nouveau mot de passe");
    updateGrid.add(confirmPasswordField, 1, 2);

    // Messages d'état
    HBox statusBox = new HBox(20);

    errorText = new Text();
    errorText.setFill(Color.RED);

    successText = new Text();
    successText.setFill(Color.rgb(46, 204, 113));

    statusBox.getChildren().addAll(errorText, successText);

    // Bouton de mise à jour
    updateButton = new Button("Mettre à jour");
    updateButton.setPrefSize(150, 40);

    updateBox.getChildren().addAll(updateTitle, updateGrid, statusBox, updateButton);

    // Panneau des messages
    VBox messagesBox = new VBox(10);
    messagesBox.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 10px;");

    HBox messagesHeader = new HBox(20);
    messagesHeader.setAlignment(Pos.CENTER_LEFT);

    Text messagesTitle = new Text("Mes messages");
    messagesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
    messagesTitle.setFill(Color.rgb(52, 73, 94));

    messagesButton = new Button("Voir tous les messages");

    messagesHeader.getChildren().addAll(messagesTitle, messagesButton);

    messagesList = new ListView<>();
    messagesList.setPrefHeight(200);

    messagesBox.getChildren().addAll(messagesHeader, messagesList);

    // Ajout de tous les panneaux
    mainBox.getChildren().addAll(headerBox, updateBox, messagesBox);
  }

  /**
   * Configure les gestionnaires d'événements.
   */
  private void setupEventHandlers() {
    // Mise à jour du profil
    updateButton.setOnAction(e -> updateProfile());

    // Navigation vers les messages
    messagesButton.setOnAction(e -> {
      if (showMessagesListener != null) {
        showMessagesListener.handle(new ActionEvent());
      }
    });
  }

  /**
   * Met à jour le profil de l'utilisateur.
   */
  private void updateProfile() {
    // Effacer les messages précédents
    errorText.setText("");
    successText.setText("");

    String newName = nameField.getText().trim();
    String newPassword = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    // Vérification si un des champs est rempli
    if (newName.isEmpty() && newPassword.isEmpty()) {
      errorText.setText("Veuillez remplir au moins un champ pour effectuer une mise à jour");
      return;
    }

    // Vérification que les mots de passe correspondent
    if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
      errorText.setText("Les mots de passe ne correspondent pas");
      return;
    }

    User connectedUser = sessionManager.getConnectedUser();
    if (connectedUser == null) {
      errorText.setText("Aucun utilisateur connecté");
      return;
    }

    // Mise à jour du nom si fourni
    if (!newName.isEmpty()) {
      connectedUser.setName(newName);
    }

    // Mise à jour du mot de passe si fourni
    if (!newPassword.isEmpty()) {
      connectedUser.setUserPassword(newPassword);
    }

    try {
      // Notification de la base de données
      database.modifiyUser(connectedUser);

      // Écriture dans le fichier
      entityManager.writeUserFile(connectedUser);

      // Mise à jour réussie
      successText.setText("Profil mis à jour avec succès");

      // Effacer les champs
      nameField.clear();
      passwordField.clear();
      confirmPasswordField.clear();

      // Rafraîchir les données
      refreshProfileData();
    } catch (Exception e) {
      errorText.setText("Erreur lors de la mise à jour: " + e.getMessage());
    }
  }

  /**
   * Rafraîchit les données du profil.
   */
  public void refreshProfileData() {
    User connectedUser = sessionManager.getConnectedUser();
    if (connectedUser != null) {
      // Mise à jour des informations utilisateur
      userTagText.setText(connectedUser.getUserTag());
      userNameText.setText(connectedUser.getName());

      // Calcul du nombre de followers
      int followersCount = database.getFollowersCount(connectedUser);
      followersCountText.setText(String.valueOf(followersCount));

      // Mise à jour de la liste des messages
      updateMessagesList(connectedUser);
    }
  }

  /**
   * Met à jour la liste des messages de l'utilisateur.
   */
  private void updateMessagesList(User user) {
    messagesList.getItems().clear();

    // Récupération des messages de l'utilisateur
    Set<Message> userMessages = database.getUserMessages(user);

    // Formatage des messages pour l'affichage
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    for (Message message : userMessages) {
      String formattedDate = dateFormat.format(new Date(message.getEmissionDate()));
      String displayText = "[" + formattedDate + "] " + message.getText();
      messagesList.getItems().add(displayText);
    }
  }

  /**
   * Définit l'écouteur pour l'événement de navigation vers les messages.
   */
  public void setShowMessagesListener(EventHandler<ActionEvent> listener) {
    this.showMessagesListener = listener;
  }
}