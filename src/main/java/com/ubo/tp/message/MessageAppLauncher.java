package com.ubo.tp.message;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.ihm.MessageApp;
import com.ubo.tp.message.mock.MessageAppMock;
import com.ubo.tp.message.observers.ConsoleDatabaseObserver;

import java.nio.charset.Charset;

/**
 * Classe de lancement de l'application.
 *
 * @author S.Lucas
 */
public class MessageAppLauncher {

	/**
	 * Indique si le mode bouchoné est activé.
	 */
	protected static boolean IS_MOCK_ENABLED = true;

	/**
	 * Launcher.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("JVM file.encoding = " + System.getProperty("file.encoding"));
		System.out.println("JVM file.encoding = " + Charset.defaultCharset().displayName());

		IDatabase database = new Database();

		database.addObserver(new ConsoleDatabaseObserver());

		EntityManager entityManager = new EntityManager(database);

		if (IS_MOCK_ENABLED) {
			MessageAppMock mock = new MessageAppMock(database, entityManager);
			mock.showGUI();
		}

		MessageApp messageApp = new MessageApp(database, entityManager);
		messageApp.init();
		messageApp.show();

	}
}
