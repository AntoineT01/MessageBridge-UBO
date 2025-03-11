package com.ubo.tp.message.app;

import com.ubo.tp.message.common.utils.FxComponentsAdapter;
import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.entity.EntityManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

/**
 * Vue principale de l'application MessageApp en JavaFX.
 * Équivalent JavaFX de MessageAppView.
 */
public class MessageAppFxView extends Application {

  // Références statiques pour partage entre les threads
  private static IDatabase sharedDatabase;
  private static EntityManager sharedEntityManager;
  private static DirectoryController sharedDirectoryController;

  // Éléments de l'interface
  private BorderPane mainPane;
  private BorderPane contentPane;

  @Override
  public void start(Stage primaryStage) {
    // Configuration de la fenêtre principale
    primaryStage.setTitle("MessageApp - JavaFX");

    // Initialisation des composants UI
    initUI(primaryStage);

    // Création de la scène
    Scene scene = new Scene(mainPane, 800, 600);
    primaryStage.setScene(scene);

    // Chargement de l'icône de l'application
    try {
      InputStream iconStream = getClass().getResourceAsStream("/tux_logo.png");
      if (iconStream != null) {
        primaryStage.getIcons().add(new Image(iconStream));
      }
    } catch (Exception e) {
      System.err.println("Impossible de charger l'icône: " + e.getMessage());
    }

    // Gestion de la fermeture de l'application
    primaryStage.setOnCloseRequest(event -> {
      if (confirmExit()) {
        if (sharedDirectoryController != null) {
          sharedDirectoryController.shutdown();
        }
        Platform.exit();
      } else {
        event.consume(); // Annuler la fermeture
      }
    });

    // Création du contrôleur principal (adapté pour JavaFX)
    FxComponentsAdapter controllerAdapter = new FxComponentsAdapter(
      this, sharedDatabase, sharedEntityManager, sharedDirectoryController
    );

    // Affichage de la fenêtre
    primaryStage.show();
  }

  /**
   * Initialise l'interface utilisateur.
   */
  private void initUI(Stage primaryStage) {
    mainPane = new BorderPane();

    // Création de la barre de menu
    MenuBar menuBar = createMenuBar(primaryStage);
    mainPane.setTop(menuBar);

    // Création du panneau de contenu central
    contentPane = new BorderPane();
    mainPane.setCenter(contentPane);
  }

  /**
   * Crée la barre de menu.
   */
  private MenuBar createMenuBar(Stage primaryStage) {
    MenuBar menuBar = new MenuBar();

    // Menu Fichier
    Menu fileMenu = new Menu("Fichier");
    MenuItem changeDirectoryItem = new MenuItem("Changer le répertoire d'échange");
    changeDirectoryItem.setOnAction(e -> changeDirectory(primaryStage));

    MenuItem exitItem = new MenuItem("Quitter");
    exitItem.setOnAction(e -> {
      if (confirmExit()) {
        if (sharedDirectoryController != null) {
          sharedDirectoryController.shutdown();
        }
        Platform.exit();
      }
    });

    fileMenu.getItems().addAll(changeDirectoryItem, new SeparatorMenuItem(), exitItem);

    // Menu Aide
    Menu helpMenu = new Menu("?");
    MenuItem aboutItem = new MenuItem("À propos");
    aboutItem.setOnAction(e -> showAboutDialog());
    helpMenu.getItems().add(aboutItem);

    // Ajout des menus à la barre
    menuBar.getMenus().addAll(fileMenu, helpMenu);

    return menuBar;
  }

  /**
   * Affiche la boîte de dialogue de confirmation de sortie.
   */
  private boolean confirmExit() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Quitter l'application");
    alert.setContentText("Voulez-vous vraiment quitter l'application ?");

    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
  }

  /**
   * Affiche la boîte de dialogue "À propos".
   */
  public void showAboutDialog() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("À propos");
    alert.setHeaderText("MessageApp - Application de messagerie");
    alert.setContentText("UBO M2-TIIL\nDépartement Informatique");

    // Chargement de l'icône
    try {
      InputStream iconStream = getClass().getResourceAsStream("/tux_logo.png");
      if (iconStream != null) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(iconStream));
      }
    } catch (Exception e) {
      System.err.println("Impossible de charger l'icône: " + e.getMessage());
    }

    alert.showAndWait();
  }

  /**
   * Change le répertoire d'échange.
   */
  private void changeDirectory(Stage primaryStage) {
    if (sharedDirectoryController != null) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Sélectionner le répertoire d'échange");
      File selectedDirectory = directoryChooser.showDialog(primaryStage);

      if (selectedDirectory != null) {
        boolean success = sharedDirectoryController.changeExchangeDirectory(selectedDirectory.getAbsolutePath());

        if (success) {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Succès");
          alert.setHeaderText("Répertoire d'échange modifié");
          alert.setContentText("Le répertoire d'échange a été changé avec succès.");
          alert.showAndWait();
        } else {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Erreur");
          alert.setHeaderText("Échec de modification du répertoire");
          alert.setContentText("Impossible de changer le répertoire d'échange.");
          alert.showAndWait();
        }
      }
    }
  }

  /**
   * Définit le contenu principal de l'application.
   */
  public void setContent(javafx.scene.Node content) {
    contentPane.setCenter(content);
  }

  /**
   * Méthode statique pour lancer l'application JavaFX avec les objets partagés.
   */
  public static void launchFx(IDatabase database, EntityManager entityManager, DirectoryController directoryController) {
    // Stockage des références pour la méthode start()
    sharedDatabase = database;
    sharedEntityManager = entityManager;
    sharedDirectoryController = directoryController;

    // Lancement de l'application JavaFX
    launch();
  }
}