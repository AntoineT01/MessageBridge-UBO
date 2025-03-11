package com.ubo.tp.message.components.message.view;

import com.ubo.tp.message.components.message.model.IMessageObserver;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.core.datamodel.Message;

import java.awt.Component;
import java.util.List;

/**
 * Interface pour la vue de chat moderne.
 */
public interface IChatView extends IMessageObserver {

  /**
   * Récupère le texte du message à envoyer.
   */
  String getMessageText();

  /**
   * Vide le champ de saisie du message.
   */
  void clearMessageInput();

  /**
   * Récupère la requête de recherche.
   */
  String getSearchQuery();

  /**
   * Ajoute un message au fil de discussion.
   */
  void addMessageToFeed(Message message);

  /**
   * Met à jour les résultats de la recherche.
   */
  void updateSearchResults(List<Message> messages);

  /**
   * Définit le modèle pour cette vue.
   */
  void setModel(MessageModel model);

  /**
   * Vide l'affichage des messages.
   */
  void clearMessages();

  /**
   * Active ou désactive la vue.
   */
  void setEnabled(boolean enabled);

  /**
   * Récupère le composant Swing associé à cette vue.
   */
  Component getComponent();

}