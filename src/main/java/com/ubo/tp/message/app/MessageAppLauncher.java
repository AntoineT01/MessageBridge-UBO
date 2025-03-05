package com.ubo.tp.message.app;

import com.ubo.tp.message.common.utils.UserDatabaseCleaner;
import com.ubo.tp.message.components.ComponentsController;
import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.mock.MessageAppMock;
import com.ubo.tp.message.test.ConsoleDatabaseObserver;

import javax.swing.*;
import java.nio.charset.Charset;

/**
 * Classe de lancement de l'application.
 * Version modifiée pour intégrer la navigation par menus.
 */
public class MessageAppLauncher {

	/**
	 * Indique si le mode bouchonné est activé.
	 */
	protected static boolean IS_MOCK_ENABLED = true;

	/**
	 * Launcher.
	 */
	public static void main(String[] args) {
		IDatabase database = new Database();
		database.addObserver(new ConsoleDatabaseObserver());
		EntityManager entityManager = new EntityManager(database);

		// Initialisation du look and feel
		initLookAndFeel();

		// Afficher le mock si activé
		if (IS_MOCK_ENABLED) {
			MessageAppMock mock = new MessageAppMock(database, entityManager);
			mock.showGUI();
		}

		// Lancement de l'application en Swing
		SwingUtilities.invokeLater(() -> {
			// Création de la vue principale
			MessageAppView mainView = new MessageAppView(database);

			// Création du contrôleur principal des composants
			ComponentsController controller = new ComponentsController(mainView, database, entityManager);

			// Création du contrôleur de répertoire
			DirectoryController directoryController = new DirectoryController(entityManager);

			// Connecter le contrôleur de répertoire au contrôleur principal
			controller.setDirectoryController(directoryController);

			// Demander à l'utilisateur de sélectionner un répertoire d'échange
			boolean directorySelected = directoryController.selectAndChangeExchangeDirectory(mainView);

			// Si aucun répertoire n'a été sélectionné
			if (!directorySelected) {
				int response = JOptionPane.showConfirmDialog(null,
				                                             "Aucun répertoire d'échange n'a été sélectionné. Voulez-vous utiliser un répertoire temporaire par défaut ?",
				                                             "Sélection de répertoire",
				                                             JOptionPane.YES_NO_OPTION,
				                                             JOptionPane.QUESTION_MESSAGE);

				if (response == JOptionPane.NO_OPTION) {
					System.exit(0); // Quitter l'application si l'utilisateur refuse
				}
				// Sinon, on continue avec un répertoire par défaut (tmp system)
			}

			// Nettoyer les utilisateurs en double
			int removedUsers = UserDatabaseCleaner.cleanDuplicateUsers(
				database,
				entityManager
			);

			if (removedUsers > 0) {
				System.out.println("Nettoyage de la base de données : " + removedUsers + " utilisateurs en double supprimés.");
			}

			// Afficher la fenêtre principale
			mainView.setVisible(true);
		});
	}

	/**
	 * Initialise le look and feel de l'application
	 */
	private static void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}