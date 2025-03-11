package com.ubo.tp.message.app;

import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.mock.MessageAppMock;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Lanceur permettant de choisir entre l'interface Swing et JavaFX.
 */
public class DualInterfaceLauncher {

  /**
   * Indique si le mode bouchonné est activé.
   */
  protected static boolean IS_MOCK_ENABLED = false;

  public static void main(String[] args) {
    // Options disponibles
    Object[] options = {"Interface Swing", "Interface JavaFX", "Les deux", "Annuler"};

    // Boîte de dialogue pour choisir l'interface
    int choice = JOptionPane.showOptionDialog(
      null,
      "Choisissez l'interface graphique à lancer :",
      "MessageApp - Sélection d'interface",
      JOptionPane.DEFAULT_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null,
      options,
      options[0]
    );

    // Création des objets partagés pour les deux interfaces
    IDatabase database = new Database();
    database.addObserver(new com.ubo.tp.message.test.ConsoleDatabaseObserver());
    EntityManager entityManager = new EntityManager(database);

    // Initialisation du répertoire d'échange
    DirectoryController directoryController = new DirectoryController(entityManager);

    // Traitement du choix
    switch (choice) {
      case 0: // Swing uniquement
        launchSwingInterface(database, entityManager, directoryController);
        break;
      case 1: // JavaFX uniquement
        launchJavaFxInterface(database, entityManager, directoryController);
        break;
      case 2: // Les deux
        launchSwingInterface(database, entityManager, directoryController);
        launchJavaFxInterface(database, entityManager, directoryController);
        break;
      default: // Annuler ou fermeture de la fenêtre
        System.exit(0);
    }
  }

  /**
   * Lance l'interface Swing.
   */
  private static void launchSwingInterface(IDatabase database, EntityManager entityManager, DirectoryController directoryController) {
    try {
      // Initialisation du look and feel
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Afficher le mock si activé
    if (IS_MOCK_ENABLED) {
      MessageAppMock mock = new MessageAppMock(database, entityManager);
      mock.showGUI();
    }

    // On lance dans un thread Swing pour ne pas bloquer le lancement de JavaFX
    SwingUtilities.invokeLater(() -> {
      // Réutilisation du lanceur Swing existant avec la même base de données et entity manager
      MessageAppLauncher.startSwingApp(database, entityManager, directoryController);
    });
  }

  /**
   * Lance l'interface JavaFX.
   */
  private static void launchJavaFxInterface(IDatabase database, EntityManager entityManager, DirectoryController directoryController) {
    // On utilise un thread séparé pour lancer JavaFX
    new Thread(() -> {
      // Lancement de la vue JavaFX avec les mêmes composants partagés
      MessageAppFxView.launchFx(database, entityManager, directoryController);
    }).start();
  }
}