package com.ubo.tp.message.components.message.service;

import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.database.IDatabase;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implémentation de l’interface IMessageService.
 * Gère la logique d’envoi et de recherche des messages.
 */
public class MessageService implements IMessageService {

  private static final int MAX_MESSAGE_LENGTH = 200;
  private final IDatabase database;
  private final Set<MessageObserver> observers;

  /**
   * Construit un MessageService avec la base de données donnée.
   *
   * @param database l’instance de la base de données
   */
  public MessageService(IDatabase database) {
    this.database = database;
    this.observers = new HashSet<>();
  }

  @Override
  public void sendMessage(User sender, String text) throws MessageValidationException {
    if (text == null || text.trim().isEmpty()) {
      throw new MessageValidationException("Le texte du message ne peut pas être vide.");
    }
    if (text.length() > MAX_MESSAGE_LENGTH) {
      throw new MessageValidationException("Le texte du message ne peut pas dépasser " + MAX_MESSAGE_LENGTH + " caractères.");
    }

    // Création d’un nouvel objet Message
    Message newMessage = new Message(sender, text);

    // Ajout du message dans la base de données
    database.addMessage(newMessage);

    // Notification des observateurs
    notifyObservers(newMessage);
  }

  @Override
  public Set<Message> searchMessages(String query) {
    Set<Message> allMessages = database.getMessages();
    if (query == null || query.trim().isEmpty()) {
      return allMessages;
    }
    String trimmedQuery = query.trim();

    // Si la requête contient uniquement '@'
    if (trimmedQuery.contains("@") && !trimmedQuery.contains("#")) {
      String userTag = trimmedQuery.replace("@", "");
      return allMessages.stream()
        .filter(m -> m.getSender().getUserTag().equalsIgnoreCase(userTag)
                     || m.containsUserTag(userTag))
        .collect(Collectors.toSet());
    }
    // Si la requête contient uniquement '#'
    if (trimmedQuery.contains("#") && !trimmedQuery.contains("@")) {
      String tag = trimmedQuery.replace("#", "");
      return allMessages.stream()
        .filter(m -> m.containsTag(tag))
        .collect(Collectors.toSet());
    }
    // Si la requête contient les deux symboles ou aucun, recherche par union des critères
    String cleanedQuery = trimmedQuery.replace("@", "").replace("#", "");
    return allMessages.stream()
      .filter(m -> m.getSender().getUserTag().equalsIgnoreCase(cleanedQuery)
                   || m.containsUserTag(cleanedQuery)
                   || m.containsTag(cleanedQuery))
      .collect(Collectors.toSet());
  }

  @Override
  public void addObserver(MessageObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(MessageObserver observer) {
    observers.remove(observer);
  }

  /**
   * Notifie tous les observateurs enregistrés du nouveau message.
   *
   * @param message le message envoyé
   */
  private void notifyObservers(Message message) {
    for (MessageObserver observer : observers) {
      observer.onMessageSent(message);
    }
  }
}
