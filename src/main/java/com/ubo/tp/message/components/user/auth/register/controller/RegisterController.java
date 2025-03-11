package com.ubo.tp.message.components.user.auth.register.controller;

import com.ubo.tp.message.components.user.auth.register.model.RegisterModel;
import com.ubo.tp.message.components.user.auth.register.view.IRegisterView;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.UUID;

/**
 * Contrôleur pour la gestion de l'inscription
 */
public class RegisterController {
  /**
   * Vue associée
   */
  private final IRegisterView view; // Utilisation de l'interface au lieu de la classe concrète

  /**
   * Modèle associé
   */
  private final RegisterModel model;

  /**
   * Gestionnaire d'entités pour écrire les fichiers utilisateur
   */
  private final EntityManager entityManager;

  /**
   * Gestionnaire de session pour connecter l'utilisateur après inscription
   */
  private final SessionManager sessionManager;

  /**
   * Écouteur pour le changement vers l'écran de connexion
   */
  private ActionListener loginScreenListener;

  /**
   * Écouteur pour les événements d'inscription réussie
   */
  private ActionListener registerSuccessListener;

  /**
   * Constructeur
   */
  public RegisterController(IRegisterView view, RegisterModel model,
                            EntityManager entityManager, SessionManager sessionManager) {
    this.view = view;
    this.model = model;
    this.entityManager = entityManager;
    this.sessionManager = sessionManager;

    // Initialiser les écouteurs d'événements
    this.initEventListeners();
  }

  /**
   * Initialise les écouteurs d'événements
   */
  private void initEventListeners() {
    // Écouteur pour le bouton d'inscription
    view.setRegisterButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        attemptRegistration();
      }
    });
  }

  /**
   * Tente une inscription avec les données saisies
   */
  private void attemptRegistration() {
    // Récupérer les valeurs des champs
    String userTag = view.getUserTag();
    String userName = view.getUserName();
    String password = view.getUserPassword();
    String confirmPassword = view.getConfirmPassword();

    // Valider les données avec le modèle
    String errorMessage = model.validateFields(userTag, userName, password, confirmPassword);

    if (errorMessage != null) {
      // Afficher le message d'erreur
      view.setErrorMessage(errorMessage);
      return;
    }

    // Normaliser le tag utilisateur
    String normalizedTag = model.normalizeUserTag(userTag);

    try {
      // Créer le nouvel utilisateur
      User newUser = new User(UUID.randomUUID(), normalizedTag, password, userName, new HashSet<>(), "");

      // Ajouter l'utilisateur à la base de données
      view.getDatabase().addUser(newUser);

      // Générer le fichier utilisateur
      entityManager.writeUserFile(newUser);

      // Réinitialiser les champs du formulaire
      view.resetFields();

      // Connecter automatiquement l'utilisateur
      sessionManager.login(newUser);

      // Notifier les écouteurs d'inscription réussie
      if (registerSuccessListener != null) {
        registerSuccessListener.actionPerformed(
          new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "register_success"));
      }
    } catch (Exception e) {
      // Gérer les erreurs d'inscription
      view.setErrorMessage("Erreur lors de l'inscription: " + e.getMessage());
    }
  }

  /**
   * Définit l'écouteur pour le changement vers l'écran de connexion
   */
  public void setLoginScreenListener(ActionListener listener) {
    this.loginScreenListener = listener;
    view.setLoginButtonListener(listener);
  }

  /**
   * Définit l'écouteur pour les événements d'inscription réussie
   */
  public void setRegisterSuccessListener(ActionListener listener) {
    this.registerSuccessListener = listener;
  }

  /**
   * Réinitialise le contrôleur et la vue
   */
  public void reset() {
    view.resetFields();
  }

  /**
   * Affiche un message d'erreur dans la vue
   */
  public void showError(String message) {
    view.setErrorMessage(message);
  }

  /**
   * Active ou désactive la vue
   */
  public void setEnabled(boolean enabled) {
    view.setEnabled(enabled);
  }
}