package com.ubo.tp.message.components.user.auth.register.model;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;

import java.util.Set;

/**
 * Modèle gérant la vérification des données d'inscription
 */
public class RegisterModel {
  /**
   * Base de données
   */
  protected final IDatabase database;

  /**
   * Constructeur
   */
  public RegisterModel(IDatabase database) {
    this.database = database;
  }

  /**
   * Vérifie si les champs sont valides
   * @param userTag Tag de l'utilisateur
   * @param userName Nom de l'utilisateur
   * @param password Mot de passe
   * @param confirmPassword Confirmation du mot de passe
   * @return Un message d'erreur ou null si tout est valide
   */
  public String validateFields(String userTag, String userName, String password, String confirmPassword) {
    // Vérifier que les champs ne sont pas vides
    if (userTag.isEmpty() || userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      return "Veuillez remplir tous les champs";
    }

    // Vérifier que les mots de passe correspondent
    if (!password.equals(confirmPassword)) {
      return "Les mots de passe ne correspondent pas";
    }

    // Si le tag ne commence pas par @, on le normalise
    String normalizedTag = userTag.startsWith("@") ? userTag : "@" + userTag;

    // Vérifier si le tag est déjà utilisé
    if (isUserTagTaken(normalizedTag)) {
      return "Ce tag utilisateur est déjà utilisé";
    }

    // Tout est valide
    return null;
  }

  /**
   * Vérifie si un tag utilisateur est déjà utilisé
   * @param userTag Tag à vérifier
   * @return true si le tag est déjà utilisé
   */
  public boolean isUserTagTaken(String userTag) {
    Set<User> users = database.getUsers();

    for (User user : users) {
      if (user.getUserTag().equals(userTag)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Normalise un tag utilisateur en ajoutant @ si nécessaire
   * @param userTag Tag à normaliser
   * @return Tag normalisé
   */
  public String normalizeUserTag(String userTag) {
    return userTag.startsWith("@") ? userTag : "@" + userTag;
  }
}