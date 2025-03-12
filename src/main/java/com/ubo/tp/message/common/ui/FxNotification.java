package com.ubo.tp.message.common.ui;

import com.ubo.tp.message.common.utils.NotificationConfig;
import com.ubo.tp.message.common.utils.SoundPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Classe permettant d'afficher des notifications JavaFX modernes et interactives
 */
public class FxNotification {

  private static final int NOTIFICATION_WIDTH = 350;
  private static final int NOTIFICATION_HEIGHT = 100;
  private static int DISPLAY_TIME; // Initialisé à partir de la configuration

  // Initialiser depuis la configuration
  static {
    updateDisplayTimeFromConfig();
  }

  // Mettre à jour le temps d'affichage depuis la configuration
  private static void updateDisplayTimeFromConfig() {
    DISPLAY_TIME = NotificationConfig.getInstance().getNotificationDuration();
  }

  private final Stage notificationStage;
  private final Timeline fadeInTimeline;
  private final Timeline fadeOutTimeline;
  private final Runnable onClickAction;

  /**
   * Crée une nouvelle notification
   *
   * @param title Le titre de la notification
   * @param message Le message à afficher
   * @param onClickAction L'action à exécuter lorsque l'utilisateur clique sur la notification
   */
  public FxNotification(String title, String message, Image icon, Runnable onClickAction) {
    this.onClickAction = onClickAction;

    // Création de la scène
    BorderPane root = createNotificationContent(title, message, icon);

    // Configuration du stage
    notificationStage = new Stage();
    notificationStage.initStyle(StageStyle.TRANSPARENT);
    notificationStage.setAlwaysOnTop(true);
    notificationStage.initModality(Modality.NONE);

    // Création de la scène
    Scene scene = new Scene(root, NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
    scene.setFill(Color.TRANSPARENT);
    notificationStage.setScene(scene);

    // Positionnement dans le coin inférieur droit
    positionStage();

    // Gestion des animations
    fadeInTimeline = new Timeline(
      new KeyFrame(Duration.ZERO, new KeyValue(notificationStage.opacityProperty(), 0.0)),
      new KeyFrame(Duration.millis(500), new KeyValue(notificationStage.opacityProperty(), 1.0))
    );

    fadeOutTimeline = new Timeline(
      new KeyFrame(Duration.ZERO, new KeyValue(notificationStage.opacityProperty(), 1.0)),
      new KeyFrame(Duration.millis(500), new KeyValue(notificationStage.opacityProperty(), 0.0))
    );

    fadeOutTimeline.setOnFinished(event -> notificationStage.close());

    // Ajout de la gestion du clic
    root.setOnMouseClicked(this::handleClick);

    // Ajout de la capacité à déplacer la notification
    addDragSupport(root);
  }

  private BorderPane createNotificationContent(String title, String message, Image icon) {
    BorderPane root = new BorderPane();
    root.getStyleClass().add("notification");
    root.setStyle("-fx-background-color: #3498db; -fx-background-radius: 10;");

    // Effet d'ombre
    DropShadow dropShadow = new DropShadow();
    dropShadow.setColor(Color.color(0, 0, 0, 0.4));
    dropShadow.setRadius(10);
    dropShadow.setOffsetX(0);
    dropShadow.setOffsetY(5);
    root.setEffect(dropShadow);

    // Contenu principal
    VBox content = new VBox(5);
    content.setPadding(new Insets(15, 15, 15, 15));

    // Titre
    Label titleLabel = new Label(title);
    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
    titleLabel.setTextFill(Color.WHITE);

    // Message
    Label messageLabel = new Label(message);
    messageLabel.setFont(Font.font("System", 12));
    messageLabel.setTextFill(Color.WHITE);
    messageLabel.setWrapText(true);

    content.getChildren().addAll(titleLabel, messageLabel);

    // Icône si présente
    if (icon != null) {
      ImageView imageView = new ImageView(icon);
      imageView.setFitHeight(32);
      imageView.setFitWidth(32);
      imageView.setPreserveRatio(true);

      HBox leftBox = new HBox(10);
      leftBox.setAlignment(Pos.CENTER);
      leftBox.setPadding(new Insets(0, 0, 0, 10));
      leftBox.getChildren().add(imageView);

      root.setLeft(leftBox);
    }

    // Bouton de fermeture
    Button closeButton = new Button("×");
    closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");
    closeButton.setOnAction(e -> {
      fadeOutAndClose();
    });

    HBox rightBox = new HBox();
    rightBox.setAlignment(Pos.TOP_RIGHT);
    rightBox.setPadding(new Insets(5, 10, 0, 0));
    rightBox.getChildren().add(closeButton);

    root.setRight(rightBox);
    root.setCenter(content);

    // Pied de page
    Label footerLabel = new Label("Cliquez pour afficher");
    footerLabel.setFont(Font.font("System", FontWeight.LIGHT, 10));
    footerLabel.setTextFill(Color.rgb(255, 255, 255, 0.7));
    footerLabel.setAlignment(Pos.CENTER);

    BorderPane footerPane = new BorderPane();
    footerPane.setCenter(footerLabel);
    footerPane.setPadding(new Insets(0, 0, 5, 0));

    root.setBottom(footerPane);

    // Rendre les coins arrondis
    Rectangle clip = new Rectangle(NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
    clip.setArcWidth(20);
    clip.setArcHeight(20);
    root.setClip(clip);

    return root;
  }

  private void positionStage() {
    javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    // Récupérer la position depuis la configuration
    String position = NotificationConfig.getInstance().getNotificationPosition();

    double x, y;
    switch (position) {
      case "bottom-right":
        x = screenBounds.getMaxX() - NOTIFICATION_WIDTH - 20;
        y = screenBounds.getMaxY() - NOTIFICATION_HEIGHT - 40;
        break;
      case "bottom-left":
        x = screenBounds.getMinX() + 20;
        y = screenBounds.getMaxY() - NOTIFICATION_HEIGHT - 40;
        break;
      case "top-right":
        x = screenBounds.getMaxX() - NOTIFICATION_WIDTH - 20;
        y = screenBounds.getMinY() + 40;
        break;
      case "top-left":
        x = screenBounds.getMinX() + 20;
        y = screenBounds.getMinY() + 40;
        break;
      default:
        // Position par défaut : bas-droite
        x = screenBounds.getMaxX() - NOTIFICATION_WIDTH - 20;
        y = screenBounds.getMaxY() - NOTIFICATION_HEIGHT - 40;
    }

    notificationStage.setX(x);
    notificationStage.setY(y);
  }

  private void addDragSupport(BorderPane root) {
    AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
    AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

    root.setOnMousePressed(event -> {
      xOffset.set(event.getSceneX());
      yOffset.set(event.getSceneY());
    });

    root.setOnMouseDragged(event -> {
      notificationStage.setX(event.getScreenX() - xOffset.get());
      notificationStage.setY(event.getScreenY() - yOffset.get());
    });
  }

  private void handleClick(MouseEvent event) {
    // On vérifie que ce n'est pas un clic sur le bouton de fermeture
    if (event.getTarget() instanceof Button && "×".equals(((Button) event.getTarget()).getText())) {
      return;
    }

    if (onClickAction != null) {
      onClickAction.run();
    }

    fadeOutAndClose();
  }

  public void show() {
    notificationStage.show();
    fadeInTimeline.play();

    // Programmer la fermeture automatique
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(DISPLAY_TIME);
        Platform.runLater(this::fadeOutAndClose);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    thread.setDaemon(true);
    thread.start();
  }

  private void fadeOutAndClose() {
    if (notificationStage.isShowing()) {
      fadeOutTimeline.play();
    }
  }

  /**
   * Affiche une notification JavaFX
   *
   * @param title Le titre de la notification
   * @param message Le message à afficher
   * @param icon L'icône à afficher (peut être null)
   */
  public static void showNotification(String title, String message, Image icon) {
    showNotification(title, message, icon, null);
  }

  /**
   * Affiche une notification JavaFX avec une action lors du clic
   *
   * @param title Le titre de la notification
   * @param message Le message à afficher
   * @param icon L'icône à afficher (peut être null)
   * @param onClickAction L'action à exécuter lorsque l'utilisateur clique sur la notification
   */
  public static void showNotification(String title, String message, Image icon, Runnable onClickAction) {
    // Vérifier si les notifications sont activées
    NotificationConfig config = NotificationConfig.getInstance();
    if (!config.isNotificationsEnabled()) {
      return;
    }

    // Mettre à jour le temps d'affichage
    updateDisplayTimeFromConfig();

    Platform.runLater(() -> {
      FxNotification notification = new FxNotification(title, message, icon, onClickAction);
      notification.show();

      // Jouer le son de notification
      SoundPlayer.playNotificationSound();
    });
  }
}