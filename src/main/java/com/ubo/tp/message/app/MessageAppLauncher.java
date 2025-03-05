package com.ubo.tp.message.app;

import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.ihm.MessageApp;
import com.ubo.tp.message.ihm.MessageAppGUI;
import com.ubo.tp.message.mock.MessageAppMock;
import com.ubo.tp.message.test.ConsoleDatabaseObserver;

import java.nio.charset.Charset;

/**
 * Classe de lancement de l'application.
 */
public class MessageAppLauncher {

	/**
	 * Indique si le mode bouchoné est activé.
	 */
	protected static boolean IS_MOCK_ENABLED = true;

	/**
	 * Launcher.
	 */
	public static void main(String[] args) {
		IDatabase database = new Database();
		database.addObserver(new ConsoleDatabaseObserver());
		EntityManager entityManager = new EntityManager(database);

		if (IS_MOCK_ENABLED) {
			MessageAppMock mock = new MessageAppMock(database, entityManager);
			mock.showGUI();
		}

		// Création de la partie métier
		MessageApp messageApp = new MessageApp(database, entityManager);
		messageApp.init();

		// Création, initialisation et affichage de la partie graphique
		MessageAppGUI gui = new MessageAppGUI(messageApp);
		gui.init(); // Cette méthode ouvre maintenant automatiquement le sélecteur de répertoire
		gui.show();
	}
}