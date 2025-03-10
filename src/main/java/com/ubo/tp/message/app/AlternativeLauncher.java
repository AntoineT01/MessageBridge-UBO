package com.ubo.tp.message.app;

import com.ubo.tp.message.components.ComponentsController;
import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.entity.EntityManager;

import javax.swing.SwingUtilities;

public class AlternativeLauncher {
  public static void main(String[] args) {
    // Créer la base de données et l'EntityManager
    IDatabase database = new Database();
    EntityManager entityManager = new EntityManager(database);

    // Définir le répertoire d'échange pré-sélectionné (adapté à votre environnement)
    String preselectedDirectory = "C:\\Users\\darwh\\Documents\\Test";
    DirectoryController directoryController = new DirectoryController(entityManager);
    if (!directoryController.changeExchangeDirectory(preselectedDirectory)) {
      System.err.println("Erreur lors du changement du répertoire d'échange.");
      System.exit(1);
    }

    // Créer un utilisateur de test "test" (tag, nom et mot de passe)

    // Créer le gestionnaire de session et connecter l'utilisateur de test
//    SessionManager sessionManager = new SessionManager();
//    sessionManager.login(testUser);

    // Initialiser la vue principale
    MessageAppView mainView = new MessageAppView(database);

    // Créer le contrôleur principal avec la base, l'EntityManager et la session déjà connectée
    ComponentsController controller = new ComponentsController(mainView, database, entityManager);
    controller.setDirectoryController(directoryController);

    // (Optionnel) Force le rafraîchissement de l'affichage des messages
    // Si le composant de messages est accessible, vous pouvez l'actualiser ici.
    // Par exemple : messageComponent.refreshMessages();

    // Afficher la fenêtre principale dans le thread Swing
    SwingUtilities.invokeLater(() -> {
      mainView.setVisible(true);
    });
  }
}
