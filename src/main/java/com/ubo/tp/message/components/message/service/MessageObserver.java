package com.ubo.tp.message.components.message.service;

import com.ubo.tp.message.core.datamodel.Message;

/**
 * Interface d’observation pour la réception de notifications lors de l’envoi d’un nouveau message.
 */
public interface MessageObserver {
  /**
   * Méthode appelée lorsqu’un nouveau message est envoyé.
   *
   * @param message le nouveau message
   */
  void onMessageSent(Message message);
}
