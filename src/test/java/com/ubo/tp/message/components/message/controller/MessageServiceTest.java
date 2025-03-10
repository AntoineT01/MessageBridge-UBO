package com.ubo.tp.message.components.message.controller;

import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.ubo.tp.message.components.message.controller.MessageController.searchMessages;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Implémentation minimale du service utilisant la méthode à tester.
public class MessageServiceTest {

  // Création de messages de test avec différents scénarios
  private List<Message> createTestMessages() {
    List<Message> messages = new ArrayList<>();

    User userJohn = new User(UUID.randomUUID(), "john", "password", "John Doe", new HashSet<>(), "avatar1.png");
    User userAlice = new User(UUID.randomUUID(), "alice", "password", "Alice Smith", new HashSet<>(), "avatar2.png");
    User userBob = new User(UUID.randomUUID(), "bob", "password", "Bob Builder", new HashSet<>(), "avatar3.png");
    User userCharlie = new User(UUID.randomUUID(), "charlie", "password", "Charlie Chaplin", new HashSet<>(), "avatar4.png");

    // Message 1 : émis par john, aucun tag mentionné
    Message m1 = new Message(userJohn, "Hello, this is a test message.");

    // Message 2 : émis par alice, mentionne @john dans le texte
    Message m2 = new Message(userAlice, "Hey, check this out: @john, you should see this!");

    // Message 3 : émis par bob, contient le tag #tag1
    Message m3 = new Message(userBob, "This is a message with #tag1 included.");

    // Message 4 : émis par john, mentionne @alice et contient le tag #tag1
    Message m4 = new Message(userJohn, "A message from john with @alice and #tag1 in it.");

    // Message 5 : émis par charlie, message sans contenu particulier
    Message m5 = new Message(userCharlie, "Just a random message without tags.");

    messages.add(m1);
    messages.add(m2);
    messages.add(m3);
    messages.add(m4);
    messages.add(m5);

    return messages;
  }

  @Test
  public void testWithNullQuery() {
    List<Message> messages = createTestMessages();
    List<Message> results = searchMessages("", messages);
    assertEquals(messages.size(), results.size(), "Une requête nulle doit retourner tous les messages");
  }

  @Test
  public void testWithEmptyQuery() {
    List<Message> messages = createTestMessages();
    List<Message> results = searchMessages("   ", messages);
    assertEquals(messages.size(), results.size(), "Une requête vide doit retourner tous les messages");
  }

  @Test
  public void testWithAtSymbol() {
    List<Message> messages = createTestMessages();
    // La requête "@john" doit retourner les messages dont l'émetteur est john ou qui mentionnent @john.
    List<Message> results = searchMessages("@john", messages);
    // Messages attendus : m1 (sender john), m2 (mentionne @john), m4 (sender john)
    assertTrue(results.contains(messages.get(0)), "Le message 1 devrait être présent");
    assertTrue(results.contains(messages.get(1)), "Le message 2 devrait être présent");
    assertTrue(results.contains(messages.get(3)), "Le message 4 devrait être présent");
    assertEquals(3, results.size(), "Le nombre de messages retournés doit être 3");
  }

  @Test
  public void testWithHashtag() {
    List<Message> messages = createTestMessages();
    // La requête "#tag1" doit retourner les messages contenant le tag #tag1.
    List<Message> results = searchMessages("#tag1", messages);
    // Messages attendus : m3 et m4
    assertTrue(results.contains(messages.get(2)), "Le message 3 devrait être présent");
    assertTrue(results.contains(messages.get(3)), "Le message 4 devrait être présent");
    assertEquals(2, results.size(), "Le nombre de messages retournés doit être 2");
  }

  @Test
  public void testWithoutSymbols() {
    List<Message> messages = createTestMessages();
    // La requête "john" (sans symbole) doit retourner l'union : émetteur égal à john ou mentionnant john ou contenant le tag "john"
    List<Message> results = searchMessages("john", messages);
    // Messages attendus : m1 (sender john), m2 (mentionne @john), m4 (sender john)
    assertTrue(results.contains(messages.get(0)), "Le message 1 devrait être présent");
    assertTrue(results.contains(messages.get(1)), "Le message 2 devrait être présent");
    assertTrue(results.contains(messages.get(3)), "Le message 4 devrait être présent");
    assertEquals(3, results.size(), "Le nombre de messages retournés doit être 3");
  }

  @Test
  public void testWithBothSymbols() {
    List<Message> messages = createTestMessages();
    // La requête combinée "@john#tag1" est traitée en retirant les symboles (résultant en "johntag1") et ne devrait correspondre à aucun message.
    List<Message> results = searchMessages("@john#tag1", messages);
    assertEquals(0, results.size(), "Aucun message ne doit correspondre à la requête combinée 'johntag1'");
  }
}
