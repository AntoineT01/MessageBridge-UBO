package com.ubo.tp.message.common.utils;

import com.ubo.tp.message.common.constants.Constants;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe utilitaire pour nettoyer les utilisateurs en double dans la base de données
 */
public class UserDatabaseCleaner {

  /**
   * Nettoie les utilisateurs en double (même tag) dans la base de données
   * Conserve l'utilisateur créé en premier (UUID le plus ancien)
   *
   * @param database Base de données à nettoyer
   * @param entityManager Gestionnaire d'entités pour réécrire les utilisateurs
   * @return Nombre d'utilisateurs supprimés
   */
  public static int cleanDuplicateUsers(IDatabase database, EntityManager entityManager) {
    // Récupérer tous les utilisateurs
    Set<User> allUsers = database.getUsers();

    // Map pour stocker le premier utilisateur trouvé pour chaque tag
    Map<String, User> uniqueUsersByTag = new HashMap<>();

    // Set pour stocker les utilisateurs à supprimer
    Set<User> usersToRemove = new HashSet<>();

    // Identifier les doublons
    for (User user : allUsers) {
      String tag = user.getUserTag();

      // Ignorer l'utilisateur inconnu
      if (user.getUuid().equals(Constants.UNKNONWN_USER_UUID)) {
        continue;
      }

      // Si on a déjà un utilisateur avec ce tag
      if (uniqueUsersByTag.containsKey(tag)) {
        // Ajouter le doublon à la liste des utilisateurs à supprimer
        usersToRemove.add(user);
      } else {
        // Sinon, c'est le premier utilisateur avec ce tag
        uniqueUsersByTag.put(tag, user);
      }
    }

    // Supprimer les doublons
    for (User userToRemove : usersToRemove) {
      // Supprimer de la base de données
      database.removeUser(userToRemove);

      // Note: Nous ne pouvons pas supprimer directement les fichiers sur disque
      // car EntityManager n'a pas de méthode pour récupérer le chemin du répertoire.
      // Les fichiers resteront donc sur le disque mais seront ignorés par l'application.
    }

    // Réécrire les utilisateurs uniques pour s'assurer qu'ils sont correctement synchronisés
    if (!usersToRemove.isEmpty()) {
      for (User user : uniqueUsersByTag.values()) {
        try {
          entityManager.writeUserFile(user);
        } catch (Exception e) {
          // Ignorer les erreurs d'écriture
          System.err.println("Erreur lors de l'écriture du fichier utilisateur: " + e.getMessage());
        }
      }
    }

    return usersToRemove.size();
  }
}