package com.ubo.tp.message.components.message.service;

import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import java.util.Set;

/**
 * Interface définissant les opérations de gestion des messages.
 * Fournit des fonctionnalités d’envoi et de recherche.
 */
public interface IMessageService {
  /**
   * Envoie un message depuis l’utilisateur donné.
   *
   * @param sender l’utilisateur émetteur
   * @param text le contenu du message (max 200 caractères)
   * @throws MessageValidationException si la validation du message échoue
   */
  void sendMessage(User sender, String text) throws MessageValidationException;

  /**
   * Recherche des messages en fonction d’une requête.
   * <p>
   * Le comportement de la recherche est le suivant :
   * - Si la requête contient le symbole '@', la recherche retourne les messages émis par
   *   cet utilisateur ou dans lesquels il est mentionné.
   * - Si la requête contient le symbole '#', la recherche retourne les messages contenant
   *   ce tag.
   * - Si aucun symbole n’est présent, la recherche retourne l’union des deux critères.
   * </p>
   *
   * @param query la requête de recherche
   * @return l’ensemble des messages correspondants
   */
  Set<Message> searchMessages(String query);

  /**
   * Ajoute un observateur qui sera notifié lorsqu’un nouveau message est envoyé.
   *
   * @param observer l’observateur à ajouter
   */
  void addObserver(MessageObserver observer);

  /**
   * Supprime un observateur précédemment ajouté.
   *
   * @param observer l’observateur à supprimer
   */
  void removeObserver(MessageObserver observer);
}
