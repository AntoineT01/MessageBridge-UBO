package com.ubo.tp.message.components.message.model;

import com.ubo.tp.message.core.datamodel.IUserObserver;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.ISession;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageModel implements IUserObserver {
  private final List<Message> messages = new ArrayList<>();
  private final List<IMessageObserver> observers = new ArrayList<>();
  private final ISession session;

  public MessageModel(ISession session) {
    this.session = session;
  }

  public MessageModel() {
    this.session = null;
  }

  public void addObserver(IMessageObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(IMessageObserver observer) {
    observers.remove(observer);
  }

  /**
   * Retourne la liste des messages filtrés pour l’utilisateur connecté.
   * On affiche uniquement les messages envoyés par l’utilisateur lui-même
   * ou par des utilisateurs que celui-ci suit.
   */
  public List<Message> getMessages() {
    if (session == null || session.getConnectedUser() == null) {
      return new ArrayList<>();
    }
    var connectedUser = session.getConnectedUser();
    return messages.stream()
      .filter(message ->
        message.getSender().getUuid().equals(connectedUser.getUuid()) ||
        connectedUser.getFollows().contains(message.getSender().getUserTag())
      )
      .collect(Collectors.toList());
  }

  public void addMessage(Message message) {
    messages.add(message);
    // Notifier tous les observateurs qu’un message a été ajouté
    for (IMessageObserver observer : observers) {
      observer.notifyMessageAdded();
    }
  }

  public void removeMessage(Message message) {
    messages.remove(message);
    // Notifier tous les observateurs qu’un message a été retiré
    for (IMessageObserver observer : observers) {
      observer.notifyMessageRemoved();
    }
  }

  public void clearMessages() {
    messages.clear();
    // Pour simplifier, notifier que la liste a changé
    for (IMessageObserver observer : observers) {
      observer.notifyMessageRemoved();
    }
  }

  /**
   * Force le rafraîchissement des messages filtrés en notifiant les observateurs.
   * À appeler lors d’un changement d’état (par exemple, après la connexion).
   */
  public void refresh() {
    for (IMessageObserver observer : observers) {
      observer.notifyMessageAdded();
    }
  }

  @Override
  public void followListChanged(User user) {
    System.out.println("MessageModel.followListChanged");
    refresh();
  }
}
