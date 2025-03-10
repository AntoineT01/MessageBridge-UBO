package com.ubo.tp.message.components.directory.controller;

import com.ubo.tp.message.common.constants.Constants;
import com.ubo.tp.message.common.utils.PropertiesManager;
import com.ubo.tp.message.components.directory.view.FileChooserWithLogo;
import com.ubo.tp.message.core.directory.IWatchableDirectory;
import com.ubo.tp.message.core.directory.WatchableDirectory;
import com.ubo.tp.message.core.entity.EntityManager;

import javax.swing.JFrame;
import java.io.File;
import java.util.Properties;

/**
 * Contrôleur gérant le répertoire d'échange de l'application.
 * Gère la sélection, la surveillance et la configuration du répertoire d'échange.
 */
public class DirectoryController {
  /**
   * Répertoire d'échange de l'application.
   */
  protected String exchangeDirectoryPath;

  /**
   * Classe de surveillance de répertoire
   */
  protected IWatchableDirectory watchableDirectory;

  /**
   * Gestionnaire des entités
   */
  protected EntityManager entityManager;

  /**
   * Constructeur
   */
  public DirectoryController(EntityManager entityManager) {
    this.entityManager = entityManager;
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
      if (isValidExchangeDirectory(directory)) {
        // Arrêter la surveillance du répertoire actuel si nécessaire
        if (watchableDirectory != null) {
          watchableDirectory.stopWatching();
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
  protected boolean isValidExchangeDirectory(File directory) {
    return directory != null && directory.exists() && directory.isDirectory() && directory.canRead()
      && directory.canWrite();
  }

  /**
   * Initialisation du répertoire d'échange.
   */
  protected void initDirectory(String directoryPath) {
    exchangeDirectoryPath = directoryPath;
    watchableDirectory = new WatchableDirectory(directoryPath);
    entityManager.setExchangeDirectory(directoryPath);

    watchableDirectory.initWatching();
    watchableDirectory.addObserver(entityManager);
  }

  /**
   * Demande à l'utilisateur de sélectionner un répertoire d'échange
   */
  public boolean selectAndChangeExchangeDirectory(JFrame parent) {
    // Utilisation du sélecteur de dossier
    File selectedDirectory = FileChooserWithLogo.showDirectoryChooser(parent);

    // Vérifier si l'utilisateur a sélectionné un dossier
    if (selectedDirectory != null) {
      // Changer le répertoire
      return changeExchangeDirectory(selectedDirectory.getAbsolutePath());
    }

    return false;
  }

  /**
   * Arrête proprement la surveillance du répertoire
   */
  public void shutdown() {
    if (watchableDirectory != null) {
      watchableDirectory.stopWatching();
    }
  }

}