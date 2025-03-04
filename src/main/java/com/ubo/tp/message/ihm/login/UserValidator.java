package com.ubo.tp.message.ihm.login;

import com.ubo.tp.message.datamodel.User;

/**
 * Classe utilitaire pour la validation des données utilisateur
 */
public class UserValidator {

  /**
   * Valide un tag utilisateur
   * @param tag Tag à valider
   * @return Message d'erreur ou null si valide
   */
  public static String validateUserTag(String tag) {
    if (tag == null || tag.trim().isEmpty()) {
      return "Le tag utilisateur ne peut pas être vide";
    }

    if (tag.contains(" ")) {
      return "Le tag utilisateur ne peut pas contenir d'espaces";
    }

    if (tag.length() < 3) {
      return "Le tag utilisateur doit contenir au moins 3 caractères";
    }

    if (tag.length() > 15) {
      return "Le tag utilisateur ne peut pas dépasser 15 caractères";
    }

    if (!tag.matches("[a-zA-Z0-9_]+")) {
      return "Le tag utilisateur ne peut contenir que des lettres, chiffres et underscores";
    }

    return null;
  }

  /**
   * Valide un mot de passe
   * @param password Mot de passe à valider
   * @return Message d'erreur ou null si valide
   */
  public static String validatePassword(String password) {
    if (password == null || password.isEmpty()) {
      return "Le mot de passe ne peut pas être vide";
    }

    if (password.length() < 4) {
      return "Le mot de passe doit contenir au moins 4 caractères";
    }

    return null;
  }

  /**
   * Valide un nom d'utilisateur
   * @param name Nom à valider
   * @return Message d'erreur ou null si valide
   */
  public static String validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      return "Le nom ne peut pas être vide";
    }

    if (name.length() > 30) {
      return "Le nom ne peut pas dépasser 30 caractères";
    }

    return null;
  }

  /**
   * Valide un chemin d'avatar
   * @param avatarPath Chemin à valider
   * @return Message d'erreur ou null si valide
   */
  public static String validateAvatarPath(String avatarPath) {
    // L'avatar est facultatif
    if (avatarPath == null || avatarPath.trim().isEmpty()) {
      return null;
    }

    // Vérification de l'extension
    String lowerPath = avatarPath.toLowerCase();
    if (!lowerPath.endsWith(".jpg") &&
      !lowerPath.endsWith(".jpeg") &&
      !lowerPath.endsWith(".png") &&
      !lowerPath.endsWith(".gif")) {
      return "L'avatar doit être une image (.jpg, .png ou .gif)";
    }

    return null;
  }
}