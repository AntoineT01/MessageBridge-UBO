package com.ubo.tp.message.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ubo.tp.message.message.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MessageServiceTest {

  private MessageService messageService;
  private Message message1;
  private Message message2;

  @BeforeEach
  public void setUp() {
    // Instanciation de la base de données et du service
    Database database = new Database();
    messageService = new MessageService(database);

    // Création de quelques utilisateurs
    User alice = new User(UUID.randomUUID(), "Alice", "pwd", "Alice", new HashSet<>(), "");
    User bob = new User(UUID.randomUUID(), "Bob", "pwd", "Bob", new HashSet<>(), "");
    User charlie = new User(UUID.randomUUID(), "Charlie", "pwd", "Charlie", new HashSet<>(), "");

    // Ajout des utilisateurs dans la base
    database.addUser(alice);
    database.addUser(bob);
    database.addUser(charlie);

    // Création de messages avec des tags intégrés dans le texte.
    // Pour éviter les problèmes de ponctuation dans l'extraction des tags,
    // on s'assure ici que le tag est suivi d'un espace.
    message1 = new Message(alice, "Hello world @Bob #greeting");
    message2 = new Message(bob, "Hi @Alice how are you? #question");
    Message message3 = new Message(charlie, "This is a random message #random");

    // Ajout des messages dans la base
    database.addMessage(message1);
    database.addMessage(message2);
    database.addMessage(message3);
  }

  @Test
  public void testSearchNullQuery() {
    // Une requête nulle doit retourner tous les messages
    Set<Message> results = messageService.searchMessages(null);
    assertEquals(3, results.size(), "Une recherche avec query null doit retourner tous les messages.");
  }

  @Test
  public void testSearchEmptyQuery() {
    // Une requête vide (ou contenant seulement des espaces) retourne également tous les messages
    Set<Message> results = messageService.searchMessages("   ");
    assertEquals(3, results.size(), "Une recherche avec query vide doit retourner tous les messages.");
  }

  @Test
  public void testSearchWithUserTagOnly() {
    // Requête avec uniquement le symbole '@'
    // Ici, la requête "@Alice" doit retourner les messages dont l'expéditeur est Alice
    // ou dont le texte contient la mention "@Alice".
    Set<Message> results = messageService.searchMessages("@Alice");
    // On s'attend à retrouver message2 (dont le texte contient "@Alice")
    // et message1 ne sera pas sélectionné, car son expéditeur est Alice (ce qui est aussi accepté)
    // En fait, message1 est envoyé par Alice donc il doit également correspondre.
    // Ainsi, les messages concernés sont message1 (expéditeur = Alice) et message2 (contient @Alice).
    assertEquals(2, results.size(), "La recherche '@Alice' doit retourner 2 messages.");
    assertTrue(results.contains(message1), "Les messages envoyés par Alice doivent être inclus.");
    assertTrue(results.contains(message2), "Les messages contenant '@Alice' doivent être inclus.");
  }

  @Test
  public void testSearchWithWordTagOnly() {
    // Requête avec uniquement le symbole '#' : on recherche par tag de mot.
    Set<Message> results = messageService.searchMessages("#greeting");
    // Seul message1 contient le tag "#greeting"
    assertEquals(1, results.size(), "La recherche '#greeting' doit retourner 1 message.");
    assertTrue(results.contains(message1), "Le message avec le tag 'greeting' doit être retourné.");
  }

  @Test
  public void testSearchWithUnionCriteria() {
    // Requête sans symbole ou avec les deux symboles doit rechercher par union des critères.
    // Par exemple, la requête "Alice" doit retourner les messages dont l'expéditeur est "Alice"
    // ou ceux qui contiennent "@Alice" ou "#Alice" dans le texte.
    Set<Message> results = messageService.searchMessages("Alice");
    // Dans notre jeu de test, message1 est envoyé par Alice et message2 contient la mention "@Alice".
    assertEquals(2, results.size(), "La recherche 'Alice' doit retourner 2 messages.");
    assertTrue(results.contains(message1), "Le message envoyé par Alice doit être retourné.");
    assertTrue(results.contains(message2), "Le message contenant '@Alice' doit être retourné.");
  }

  @Test
  public void testSearchForNonExistingTag() {
    // Requête pour un tag inexistant doit retourner un ensemble vide.
    Set<Message> results = messageService.searchMessages("#nonexistent");
    assertEquals(0, results.size(), "La recherche d'un tag inexistant doit retourner 0 message.");
  }
}
