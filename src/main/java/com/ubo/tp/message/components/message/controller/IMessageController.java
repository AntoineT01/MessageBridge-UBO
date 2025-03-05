package com.ubo.tp.message.components.message.controller;

import com.ubo.tp.message.core.datamodel.Message;

/**
 * Interface définissant les opérations de gestion des messages.
 * Fournit des fonctionnalités d’envoi et de recherche.
 */
public interface IMessageController {
  /**
   * Méthode appelée lorsqu’un nouveau message est envoyé.
   *
   * @param message le nouveau message
   */
  void onMessageSent(Message message);
  /**
   * Envoie un message depuis l’utilisateur donné.
   *
   * @param text le contenu du message (max 200 caractères)
   */
  void sendMessage(String text);

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
   */
  void searchMessages(String query);
}
