package com.ubo.tp.message.ihm;

import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.directory.IWatchableDirectory;
import com.ubo.tp.message.core.directory.WatchableDirectory;
import com.ubo.tp.message.common.constants.Constants;
import com.ubo.tp.message.common.utils.PropertiesManager;

import java.io.File;
import java.util.Properties;

/**
 * Classe principale métier de l'application.
 */
public class MessageApp {
  /**
   * Base de données.
   */
  protected IDatabase mDatabase;

  /**
   * Gestionnaire des entités contenu de la base de données.
   */
  protected EntityManager mEntityManager;

  /**
   * Classe de surveillance de répertoire
   */
  protected IWatchableDirectory mWatchableDirectory;

  /**
   * Répertoire d'échange de l'application.
   */
  protected String mExchangeDirectoryPath;

  /**
   * Constructeur.
   */
  public MessageApp(IDatabase database, EntityManager entityManager) {
    this.mDatabase = database;
    this.mEntityManager = entityManager;
  }

  /**
   * Initialisation de l'application.
   * Cette méthode n'initialise plus automatiquement le répertoire d'échange
   */
  public void init() {
    // L'initialisation du répertoire d'échange est maintenant gérée par l'interface graphique
  }

  /**
   * Arrête proprement la partie métier de l'application.
   */
  public void shutdown() {
    // Arrêter la surveillance du répertoire
    if (mWatchableDirectory != null) {
      mWatchableDirectory.stopWatching();
    }

    // Vous pourriez ajouter ici d'autres nettoyages nécessaires
    // Par exemple, fermer les connexions à la base de données, etc.

    System.out.println("MessageApp: Arrêt propre de la partie métier");
  }



  /**
   * Change le répertoire d'échange.
   *
   * @param directoryPath Le chemin du nouveau répertoire
   * @return true si le changement a réussi, false sinon
   */
  public boolean changeExchangeDirectory(String directoryPath) {
    if (directoryPath != null && !directoryPath.isEmpty()) {
      File directory = new File(directoryPath);
      if (isValideExchangeDirectory(directory)) {
        // Arrêter la surveillance du répertoire actuel si nécessaire
        if (mWatchableDirectory != null) {
          mWatchableDirectory.stopWatching();
        }

        // Initialiser avec le nouveau répertoire
        initDirectory(directory.getAbsolutePath());

        // Sauvegarder dans la configuration
        Properties props = PropertiesManager.loadProperties(Constants.CONFIGURATION_FILE);
        props.setProperty(Constants.CONFIGURATION_KEY_EXCHANGE_DIRECTORY, directoryPath);
        PropertiesManager.writeProperties(props, Constants.CONFIGURATION_FILE);

        return true;
      }
    }
    return false;
  }

  /**
   * Indique si le fichier donné est valide pour servir de répertoire d'échange
   */
  protected boolean isValideExchangeDirectory(File directory) {
    return directory != null && directory.exists() && directory.isDirectory() && directory.canRead()
      && directory.canWrite();
  }

  /**
   * Initialisation du répertoire d'échange.
   */
  protected void initDirectory(String directoryPath) {
    mExchangeDirectoryPath = directoryPath;
    mWatchableDirectory = new WatchableDirectory(directoryPath);
    mEntityManager.setExchangeDirectory(directoryPath);

    mWatchableDirectory.initWatching();
    mWatchableDirectory.addObserver(mEntityManager);
  }

  /**
   * Getter pour la base de données
   */
  public IDatabase getDatabase() {
    return mDatabase;
  }

  /**
   * Getter pour le gestionnaire d'entités
   */
  public EntityManager getEntityManager() {
    return mEntityManager;
  }

  /**
   * Getter pour le répertoire d'échange actuel
   */
  public String getExchangeDirectoryPath() {
    return mExchangeDirectoryPath;
  }
}