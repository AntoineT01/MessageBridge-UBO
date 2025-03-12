package com.ubo.tp.message.components.message;

import com.ubo.tp.message.common.ui.FxNotification;
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
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class FxMessageView extends BorderPane implements IDatabaseObserver {

  // Attributs existants
  private final IDatabase database;
  private final ISession session;
  private final EntityManager entityManager;

  // Champs pour l'interface
  private TextField searchField;
  private Button searchButton;

  // Champs pour la saisie de messages
  private TextArea messageTextArea;
  private Button sendButton;

  // Liste de messages
  private ListView<FxMessageBubble> messagesListView;
  private ObservableList<FxMessageBubble> messagesList;

  // Bouton pour accéder au profil
  private Button profileButton;

  // Champs pour les messages d'erreur et de succès
  private Text errorText;
  private Text successText;

  // Écouteur pour l'affichage du profil
  private EventHandler<ActionEvent> showProfileListener;

  // Constantes
  private static final int MAX_MESSAGE_LENGTH = 200;
  private static final Image USER_ICON;

  // Initialisation statique pour l'icône
  static {
    Image icon = null;
    try {
      InputStream iconStream = FxMessageView.class.getResourceAsStream("/tux_logo.png");
      if (iconStream != null) {
        icon = new Image(iconStream);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    USER_ICON = icon;
  }

  // Constructeur
  public FxMessageView(IDatabase database, ISession session, EntityManager entityManager) {
    this.database = database;
    this.session = session;
    this.entityManager = entityManager;

    // Définir le padding
    setPadding(new Insets(20));

    // Initialiser les composants
    initComponents();

    // Configurer les gestionnaires d'événements
    setupEventHandlers();

    // S'abonner aux changements de la base de données
    database.addObserver(this);
  }

  // Initialisation des composants
  private void initComponents() {
    // Conteneur principal
    VBox mainBox = new VBox(15);
    mainBox.setPadding(new Insets(10));
    setCenter(mainBox);

    // En-tête
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

    // Liste de messages
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
          // Pas de texte
          setText(null);
          // Fond transparent
          setStyle("-fx-background-color: transparent;");
        }
      }
    });
    messagesListView.setPrefHeight(300);

    // Boîte de messages
    VBox messagesBox = new VBox(10);
    messagesBox.setStyle("-fx-background-color: #f5f8fa; -fx-padding: 15px; -fx-background-radius: 10px;");

    Text feedTitle = new Text("Fil d'actualité");
    feedTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

    messagesBox.getChildren().addAll(feedTitle, messagesListView);

    // Boîte de saisie
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

    // Assembler le tout
    mainBox.getChildren().addAll(headerBox, messagesBox, inputBox);
  }

  // Configurer les gestionnaires d'événements
  private void setupEventHandlers() {
    // Envoi de message
    sendButton.setOnAction(e -> sendMessage());

    // Recherche
    searchButton.setOnAction(e -> searchMessages());

    // Affichage du profil
    profileButton.setOnAction(e -> {
      if (showProfileListener != null) {
        showProfileListener.handle(new ActionEvent());
      }
    });
  }

  // Envoi d'un message
  private void sendMessage() {
    // Réinitialiser les messages
    errorText.setText("");
    successText.setText("");

    String text = messageTextArea.getText().trim();

    // Vérifier si le message est vide
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
      // Créer le message
      Message newMessage = new Message(connectedUser, text);

      // Enregistrer le message
      entityManager.writeMessageFile(newMessage);

      // Afficher un message de succès
      successText.setText("Message envoyé");

      // Vider le champ de texte
      messageTextArea.clear();
    } catch (Exception e) {
      errorText.setText("Erreur lors de l'envoi du message: " + e.getMessage());
    }
  }

  // Recherche de messages
  private void searchMessages() {
    String query = searchField.getText().trim();

    User connectedUser = session.getConnectedUser();
    if (connectedUser == null) {
      return;
    }

    List<Message> results;

    // Déterminer les critères de recherche
    if (query.isEmpty()) {
      // Pas de critère - afficher tous les messages pertinents
      results = getFilteredMessages();
    } else if (query.contains("@") && !query.contains("#")) {
      // Recherche par utilisateur
      String userTag = query.replace("@", "");
      results = getFilteredMessages().stream()
        .filter(m -> m.getSender().getUserTag().equalsIgnoreCase("@" + userTag) ||
          m.containsUserTag(userTag))
        .collect(Collectors.toList());
    } else if (query.contains("#") && !query.contains("@")) {
      // Recherche par tag
      String tag = query.replace("#", "");
      results = getFilteredMessages().stream()
        .filter(m -> m.containsTag(tag))
        .collect(Collectors.toList());
    } else {
      // Recherche générale
      String cleanedQuery = query.replace("@", "").replace("#", "");
      results = getFilteredMessages().stream()
        .filter(m -> m.getSender().getUserTag().equalsIgnoreCase("@" + cleanedQuery) ||
          m.containsUserTag(cleanedQuery) ||
          m.containsTag(cleanedQuery))
        .collect(Collectors.toList());
    }

    // Mettre à jour l'affichage
    updateMessagesList(results);
  }

  // Obtenir les messages filtrés
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

  // Mettre à jour la liste des messages
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

  // Rafraîchir la liste des messages
  public void refreshMessages() {
    // Mettre à jour la liste avec les messages filtrés
    updateMessagesList(getFilteredMessages());
  }

  // Définir l'écouteur pour l'affichage du profil
  public void setShowProfileListener(EventHandler<ActionEvent> listener) {
    this.showProfileListener = listener;
  }

  // Méthodes de l'interface IDatabaseObserver

  @Override
  public void notifyMessageAdded(Message addedMessage) {
    Platform.runLater(() -> {
      User connectedUser = session.getConnectedUser();

      // Vérifier si l'utilisateur connecté suit l'expéditeur du message
      if (connectedUser != null
        && !addedMessage.getSender().getUuid().equals(connectedUser.getUuid())
        && connectedUser.getFollows().contains(addedMessage.getSender().getUserTag())) {

        // Préparer le contenu du message pour l'aperçu
        String messagePreview = addedMessage.getText();
        if (messagePreview.length() > 50) {
          messagePreview = messagePreview.substring(0, 50) + "...";
        }

        // Afficher une notification
        FxNotification.showNotification(
          "Nouvelle publication de " + addedMessage.getSender().getName(),
          messagePreview,
          USER_ICON,
          // Action lors du clic sur la notification - mettre en évidence le message
          () -> {
            List<Message> singleMessage = new ArrayList<>();
            singleMessage.add(addedMessage);
            updateMessagesList(singleMessage);
          }
        );
      }

      // Rafraîchir la liste de messages
      refreshMessages();
    });
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
    // Pas d'action nécessaire
  }

  @Override
  public void notifyUserDeleted(User deletedUser) {
    // Pas d'action nécessaire
  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    User connectedUser = session.getConnectedUser();
    if (connectedUser != null && connectedUser.getUuid().equals(modifiedUser.getUuid())) {
      refreshMessages();
    }
  }

  // Classe interne pour les bulles de message
  public static class FxMessageBubble extends VBox {

    // Constructeur
    public FxMessageBubble(Message message, boolean isOutgoing) {
      // Définir la largeur maximale
      setMaxWidth(400);
      setPadding(new Insets(10));
      setSpacing(5);

      // Styler la bulle selon l'expéditeur
      if (isOutgoing) {
        setStyle("-fx-background-color: #DCF8C6; -fx-background-radius: 15px;");
        setAlignment(Pos.CENTER_RIGHT);
      } else {
        setStyle("-fx-background-color: white; -fx-background-radius: 15px;");
        setAlignment(Pos.CENTER_LEFT);
      }

      // En-tête du message
      HBox header = new HBox(10);

      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      String formattedDate = dateFormat.format(new Date(message.getEmissionDate()));

      Label senderLabel = new Label(message.getSender().getName() + " (" + message.getSender().getUserTag() + ")");
      senderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

      Label dateLabel = new Label(formattedDate);
      dateLabel.setFont(Font.font("Arial", 10));
      dateLabel.setTextFill(Color.GRAY);

      header.getChildren().addAll(senderLabel, dateLabel);

      // Contenu du message
      Label textLabel = new Label(message.getText());
      textLabel.setWrapText(true);

      // Assemblage
      getChildren().addAll(header, textLabel);

      // Hauteur minimale
      setMinHeight(USE_PREF_SIZE);
    }
  }
}