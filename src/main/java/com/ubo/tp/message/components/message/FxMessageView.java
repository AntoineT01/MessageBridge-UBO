package com.ubo.tp.message.components.message;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Vue des messages en JavaFX.
 * Adapte les fonctionnalités de MessageComponent en JavaFX.
 */
public class FxMessageView extends BorderPane implements IDatabaseObserver {

  // Services
  private final IDatabase database;
  private final ISession session;
  private final EntityManager entityManager;

  // Composants de recherche
  private TextField searchField;
  private Button searchButton;

  // Composants de saisie de message
  private TextArea messageTextArea;
  private Button sendButton;

  // Liste des messages
  private ListView<FxMessageBubble> messagesListView;
  private ObservableList<FxMessageBubble> messagesList;

  // Navigation
  private Button profileButton;

  // Messages d'état
  private Text errorText;
  private Text successText;

  // Écouteur pour la navigation vers le profil
  private EventHandler<ActionEvent> showProfileListener;

  // Constantes
  private static final int MAX_MESSAGE_LENGTH = 200;

  /**
   * Constructeur.
   */
  public FxMessageView(IDatabase database, ISession session, EntityManager entityManager) {
    this.database = database;
    this.session = session;
    this.entityManager = entityManager;

    // Initialisation de l'interface
    setPadding(new Insets(20));

    // Création des composants
    initComponents();

    // Configuration des actions
    setupEventHandlers();

    // S'inscrire comme observateur de la base de données
    database.addObserver(this);
  }

  /**
   * Initialise les composants.
   */
  private void initComponents() {
    // Panneau principal avec espacement vertical
    VBox mainBox = new VBox(15);
    mainBox.setPadding(new Insets(10));
    setCenter(mainBox);

    // En-tête - Navigation et recherche
    HBox headerBox = new HBox(15);
    headerBox.setAlignment(Pos.CENTER_LEFT);
    headerBox.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 10px;");

    Text messagesTitle = new Text("Messages");
    messagesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    messagesTitle.setFill(Color.rgb(52, 152, 219));

    profileButton = new Button("Mon Profil");

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    searchField = new TextField();
    searchField.setPromptText("Rechercher des messages (@user ou #tag)");
    searchField.setPrefWidth(250);

    searchButton = new Button("Rechercher");

    headerBox.getChildren().addAll(messagesTitle, profileButton, spacer, searchField, searchButton);

    // Liste des messages
    messagesList = FXCollections.observableArrayList();
    messagesListView = new ListView<>(messagesList);
    messagesListView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(FxMessageBubble item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
          setText(null);
        } else {
          setGraphic(item);
          // Pour éviter que le texte s'affiche en plus de la bulle
          setText(null);
          // Style pour la cellule
          setStyle("-fx-background-color: transparent;");
        }
      }
    });
    messagesListView.setPrefHeight(300);

    // Panneau des messages avec style
    VBox messagesBox = new VBox(10);
    messagesBox.setStyle("-fx-background-color: #f5f8fa; -fx-padding: 15px; -fx-background-radius: 10px;");

    Text feedTitle = new Text("Fil d'actualité");
    feedTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

    messagesBox.getChildren().addAll(feedTitle, messagesListView);

    // Panneau de saisie de nouveau message
    VBox inputBox = new VBox(10);
    inputBox.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 10px;");

    Text newMessageTitle = new Text("Nouveau message");
    newMessageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

    messageTextArea = new TextArea();
    messageTextArea.setPromptText("Écrivez votre message ici...");
    messageTextArea.setPrefRowCount(3);

    HBox messageControls = new HBox(10);
    messageControls.setAlignment(Pos.CENTER_RIGHT);

    errorText = new Text();
    errorText.setFill(Color.RED);

    successText = new Text();
    successText.setFill(Color.rgb(46, 204, 113));

    Region inputSpacer = new Region();
    HBox.setHgrow(inputSpacer, Priority.ALWAYS);

    sendButton = new Button("Envoyer");
    sendButton.setDefaultButton(true);

    messageControls.getChildren().addAll(errorText, successText, inputSpacer, sendButton);

    inputBox.getChildren().addAll(newMessageTitle, messageTextArea, messageControls);

    // Ajout de tous les panneaux
    mainBox.getChildren().addAll(headerBox, messagesBox, inputBox);
  }

  /**
   * Configure les gestionnaires d'événements.
   */
  private void setupEventHandlers() {
    // Envoi de message
    sendButton.setOnAction(e -> sendMessage());

    // Recherche de messages
    searchButton.setOnAction(e -> searchMessages());

    // Navigation vers le profil
    profileButton.setOnAction(e -> {
      if (showProfileListener != null) {
        showProfileListener.handle(new ActionEvent());
      }
    });
  }

  /**
   * Envoie un nouveau message.
   */
  private void sendMessage() {
    // Effacer les messages précédents
    errorText.setText("");
    successText.setText("");

    String text = messageTextArea.getText().trim();

    // Vérification du texte
    if (text.isEmpty()) {
      errorText.setText("Le texte du message ne peut pas être vide");
      return;
    }

    if (text.length() > MAX_MESSAGE_LENGTH) {
      errorText.setText("Le texte du message ne peut pas dépasser " + MAX_MESSAGE_LENGTH + " caractères");
      return;
    }

    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      errorText.setText("Aucun utilisateur connecté");
      return;
    }

    try {
      // Création du message
      Message newMessage = new Message(connectedUser, text);

      // Écriture dans le fichier
      entityManager.writeMessageFile(newMessage);

      // Message envoyé avec succès
      successText.setText("Message envoyé");

      // Effacer le champ de saisie
      messageTextArea.clear();
    } catch (Exception e) {
      errorText.setText("Erreur lors de l'envoi du message: " + e.getMessage());
    }
  }

  /**
   * Recherche des messages.
   */
  private void searchMessages() {
    String query = searchField.getText().trim();

    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      return;
    }

    List<Message> results;

    // Filtrage des messages en fonction des critères
    if (query.isEmpty()) {
      // Sans critère, on affiche tous les messages pertinents
      results = getFilteredMessages();
    } else if (query.contains("@") && !query.contains("#")) {
      // Recherche par tag utilisateur
      String userTag = query.replace("@", "");
      results = getFilteredMessages().stream()
        .filter(m -> m.getSender().getUserTag().equalsIgnoreCase("@" + userTag) ||
          m.containsUserTag(userTag))
        .collect(Collectors.toList());
    } else if (query.contains("#") && !query.contains("@")) {
      // Recherche par mot-clé
      String tag = query.replace("#", "");
      results = getFilteredMessages().stream()
        .filter(m -> m.containsTag(tag))
        .collect(Collectors.toList());
    } else {
      // Recherche mixte
      String cleanedQuery = query.replace("@", "").replace("#", "");
      results = getFilteredMessages().stream()
        .filter(m -> m.getSender().getUserTag().equalsIgnoreCase("@" + cleanedQuery) ||
          m.containsUserTag(cleanedQuery) ||
          m.containsTag(cleanedQuery))
        .collect(Collectors.toList());
    }

    // Mise à jour de l'affichage
    updateMessagesList(results);
  }

  /**
   * Retourne la liste des messages filtrés pour l'utilisateur connecté.
   */
  private List<Message> getFilteredMessages() {
    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      return Collections.emptyList();
    }

    return database.getMessages().stream()
      .filter(message ->
                message.getSender().getUuid().equals(connectedUser.getUuid()) ||
                  connectedUser.getFollows().contains(message.getSender().getUserTag())
      )
      .sorted(Comparator.comparing(Message::getEmissionDate).reversed())
      .collect(Collectors.toList());
  }

  /**
   * Met à jour la liste des messages affichés.
   */
  private void updateMessagesList(List<Message> messages) {
    Platform.runLater(() -> {
      messagesList.clear();

      User connectedUser = session.getConnectedUser();
      if (connectedUser == null) {
        return;
      }

      for (Message message : messages) {
        boolean isOutgoing = message.getSender().getUuid().equals(connectedUser.getUuid());
        FxMessageBubble bubble = new FxMessageBubble(message, isOutgoing);
        messagesList.add(bubble);
      }
    });
  }

  /**
   * Rafraîchit tous les messages.
   */
  public void refreshMessages() {
    // Mise à jour de la liste des messages
    updateMessagesList(getFilteredMessages());
  }

  /**
   * Définit l'écouteur pour l'événement de navigation vers le profil.
   */
  public void setShowProfileListener(EventHandler<ActionEvent> listener) {
    this.showProfileListener = listener;
  }

  // Implémentation des méthodes de l'observateur de base de données

  @Override
  public void notifyMessageAdded(Message addedMessage) {
    refreshMessages();
  }

  @Override
  public void notifyMessageDeleted(Message deletedMessage) {
    refreshMessages();
  }

  @Override
  public void notifyMessageModified(Message modifiedMessage) {
    refreshMessages();
  }

  @Override
  public void notifyUserAdded(User addedUser) {
    // Non utilisé
  }

  @Override
  public void notifyUserDeleted(User deletedUser) {
    // Non utilisé
  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    User connectedUser = session.getConnectedUser();
    if (connectedUser != null && connectedUser.getUuid().equals(modifiedUser.getUuid())) {
      refreshMessages();
    }
  }

  /**
   * Classe interne pour représenter une bulle de message dans l'interface JavaFX.
   */
  public static class FxMessageBubble extends VBox {

    /**
     * Constructeur.
     */
    public FxMessageBubble(Message message, boolean isOutgoing) {
      // Configuration de base
      setMaxWidth(400);
      setPadding(new Insets(10));
      setSpacing(5);

      // Style de la bulle selon l'expéditeur
      if (isOutgoing) {
        setStyle("-fx-background-color: #DCF8C6; -fx-background-radius: 15px;");
        setAlignment(Pos.CENTER_RIGHT);
      } else {
        setStyle("-fx-background-color: white; -fx-background-radius: 15px;");
        setAlignment(Pos.CENTER_LEFT);
      }

      // En-tête avec le nom de l'expéditeur et la date
      HBox header = new HBox(10);

      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      String formattedDate = dateFormat.format(new Date(message.getEmissionDate()));

      Label senderLabel = new Label(message.getSender().getName() + " (" + message.getSender().getUserTag() + ")");
      senderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

      Label dateLabel = new Label(formattedDate);
      dateLabel.setFont(Font.font("Arial", 10));
      dateLabel.setTextFill(Color.GRAY);

      header.getChildren().addAll(senderLabel, dateLabel);

      // Corps du message
      Label textLabel = new Label(message.getText());
      textLabel.setWrapText(true);

      // Ajout des composants
      getChildren().addAll(header, textLabel);

      // Pour que la bulle s'adapte au contenu
      setMinHeight(USE_PREF_SIZE);
    }
  }
}