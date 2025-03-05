package com.ubo.tp.message.components.message.model;

import com.ubo.tp.message.core.datamodel.Message;
import java.util.ArrayList;
import java.util.List;

// TODO: FAire en sorte qu'il soit instancié et rempli avec les messages filtrés par ceux qui follow l'utilisateur connecté
//  Attention : quand les follow changent, les messages aussi mais USER n'est pas observable DONC rendre User observable
public class MessageModel {
  private final List<Message> messages = new ArrayList<>();
  private final List<IMessageObserver> observers = new ArrayList<>();

  public void addObserver(IMessageObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(IMessageObserver observer) {
    observers.remove(observer);
  }

  public List<Message> getMessages() {
    return new ArrayList<>(messages);
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
    // Pour simplifier, on notifie que la liste a changé
    for (IMessageObserver observer : observers) {
      observer.notifyMessageRemoved();
    }
  }
}
