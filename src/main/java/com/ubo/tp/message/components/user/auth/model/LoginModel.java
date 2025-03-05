package com.ubo.tp.message.ihm.auth.model;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.SessionManager;

import java.util.Optional;
import java.util.Set;

/**
 * Modèle gérant la logique d'authentification
 */
public class LoginModel {
  /**
   * Base de données
   */
  protected final IDatabase database;

  /**
   * Gestionnaire de session
   */
  protected final SessionManager sessionManager;

  /**
   * Constructeur
   */
  public LoginModel(IDatabase database, SessionManager sessionManager) {
    this.database = database;
    this.sessionManager = sessionManager;
  }

  /**
   * Tente d'authentifier un utilisateur avec ses identifiants
   * @param userTag Tag de l'utilisateur
   * @param password Mot de passe
   * @return Un objet LoginResult contenant le résultat de l'opération
   */
  public LoginResult authenticate(String userTag, String password) {
    // Vérifier que les champs ne sont pas vides
    if (userTag.isEmpty() || password.isEmpty()) {
      return new LoginResult(false, "Veuillez remplir tous les champs", null);
    }

    // Ajouter le préfixe @ si nécessaire
    if (!userTag.startsWith("@")) {
      userTag = "@" + userTag;
    }

    // Chercher l'utilisateur dans la base de données
    User user = findUserByTag(userTag);

    // Vérifier si l'utilisateur existe
    if (user == null) {
      return new LoginResult(false, "Utilisateur inconnu", null);
    }

    // Vérifier le mot de passe
    if (!user.getUserPassword().equals(password)) {
      return new LoginResult(false, "Mot de passe incorrect", null);
    }

    // Connexion réussie
    return new LoginResult(true, "", user);
  }

  /**
   * Connecte l'utilisateur dans la session
   * @param user Utilisateur à connecter
   */
  public void login(User user) {
    sessionManager.login(user);
  }

  /**
   * Recherche un utilisateur par son tag
   */
  private User findUserByTag(String userTag) {
    Set<User> users = database.getUsers();

    Optional<User> foundUser = users.stream()
      .filter(user -> user.getUserTag().equals(userTag))
      .findFirst();

    return foundUser.orElse(null);
  }

  /**
   * Classe interne représentant le résultat d'une tentative de connexion
   */
  public static class LoginResult {
    private final boolean success;
    private final String errorMessage;
    private final User user;

    public LoginResult(boolean success, String errorMessage, User user) {
      this.success = success;
      this.errorMessage = errorMessage;
      this.user = user;
    }

    public boolean isSuccess() {
      return success;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    public User getUser() {
      return user;
    }
  }
}