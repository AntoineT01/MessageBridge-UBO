package com.ubo.tp.message.ihm;

import com.ubo.tp.message.common.ImageUtils;
import com.ubo.tp.message.core.database.IDatabase;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Classe gérant l'interface graphique de l'application.
 */
public class MessageAppGUI {
  /**
   * Vue principale de l'application.
   */
  protected MessageAppMainView mMainView;

  /**
   * L'instance de MessageApp (partie métier)
   */
  protected MessageApp mMessageApp;

  /**
   * Constructeur.
   *
   * @param messageApp L'instance de MessageApp (partie métier)
   */
  public MessageAppGUI(MessageApp messageApp) {
    this.mMessageApp = messageApp;
  }

  /**
   * Initialisation de l'interface graphique.
   */
  public void init() {
    // Init du look and feel de l'application
    this.initLookAndFeel();

    // Demander à l'utilisateur de sélectionner un répertoire d'échange
    boolean directorySelected = selectAndChangeExchangeDirectory();

    // Si aucun répertoire n'a été sélectionné ou si la sélection a échoué,
    // on peut quitter l'application ou utiliser un répertoire par défaut
    if (!directorySelected) {
      int response = JOptionPane.showConfirmDialog(null,
                                                   "Aucun répertoire d'échange n'a été sélectionné. Voulez-vous utiliser un répertoire temporaire par défaut ?",
                                                   "Sélection de répertoire",
                                                   JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE);

      if (response == JOptionPane.NO_OPTION) {
        System.exit(0); // Quitter l'application si l'utilisateur refuse
      }
      // Sinon, on continue avec le répertoire par défaut qui a été configuré dans MessageApp
    }

    // Initialisation de l'IHM
    this.initGui();
  }

  /**
   * Ferme proprement l'application, en fermant d'abord la partie GUI puis la partie métier.
   */
  public void shutdown() {
    // Fermer proprement les composants GUI
    if (mMainView != null) {
      mMainView.dispose();
    }

    // Fermer proprement la partie métier
    if (mMessageApp != null) {
      mMessageApp.shutdown();
    }

    System.out.println("MessageAppGUI: Arrêt propre de l'interface graphique");
  }


  /**
   * Initialisation du look and feel de l'application.
   */
  protected void initLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Initialisation de l'interface graphique.
   */
  protected void initGui() {
    // On crée la vue principale en lui passant la base de données
    mMainView = new MessageAppMainView(mMessageApp.getDatabase());

    mMainView.addPropertyChangeListener("ACTION_EXIT", evt -> {
      // Demander confirmation à l'utilisateur
      int response = JOptionPane.showConfirmDialog(
        mMainView,
        "Voulez-vous vraiment quitter l'application ?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
      );

      if (response == JOptionPane.YES_OPTION) {
        // Arrêt propre puis fermeture
        shutdown();
        System.exit(0);
      }
    });



    // Configurer l'action pour changer le répertoire d'échange dans le menu
    configureChangeDirectoryMenuItem();
  }

  /**
   * Configure l'élément de menu pour changer le répertoire d'échange.
   */
  private void configureChangeDirectoryMenuItem() {
    for (int i = 0; i < mMainView.getJMenuBar().getMenu(0).getItemCount(); i++) {
      JMenuItem item = mMainView.getJMenuBar().getMenu(0).getItem(i);
      if (item != null && "Changer le répertoire d'échange".equals(item.getText())) {
        item.addActionListener(e -> {
          boolean success = this.selectAndChangeExchangeDirectory();
          if (success) {
            JOptionPane.showMessageDialog(mMainView,
                                          "Le répertoire d'échange a été changé avec succès.",
                                          "Succès", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(mMainView,
                                          "Impossible de changer le répertoire d'échange.",
                                          "Erreur", JOptionPane.ERROR_MESSAGE);
          }
        });
        break;
      }
    }
  }

  /**
   * Ouvre un sélecteur de dossier et change le répertoire d'échange si l'utilisateur en sélectionne un.
   *
   * @return true si le répertoire a été changé avec succès, false sinon
   */
  public boolean selectAndChangeExchangeDirectory() {
    // Utilisation du sélecteur de dossier
    File selectedDirectory = FileChooserWithLogo.showDirectoryChooser(mMainView);

    // Vérifier si l'utilisateur a sélectionné un dossier
    if (selectedDirectory != null) {
      // Demander à la partie métier de changer le répertoire
      return mMessageApp.changeExchangeDirectory(selectedDirectory.getAbsolutePath());
    }

    return false;
  }

  /**
   * Affiche la fenêtre principale de l'application.
   */
  public void show() {
    // On rend la fenêtre visible
    SwingUtilities.invokeLater(() -> {
      if (mMainView != null) {
        mMainView.setVisible(true);
      }
    });
  }
}