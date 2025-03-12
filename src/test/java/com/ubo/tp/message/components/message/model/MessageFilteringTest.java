//package com.ubo.tp.message.components.message.model;
//
//import com.ubo.tp.message.components.message.controller.MessageController;
//import com.ubo.tp.message.components.message.view.ModernChatView;
//import com.ubo.tp.message.core.database.Database;
//import com.ubo.tp.message.core.database.IDatabase;
//import com.ubo.tp.message.core.datamodel.Message;
//import com.ubo.tp.message.core.datamodel.User;
//import com.ubo.tp.message.core.entity.EntityManager;
//import com.ubo.tp.message.core.session.ISession;
//import com.ubo.tp.message.core.session.ISessionObserver;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//import static com.ubo.tp.message.components.message.controller.MessageController.searchMessages;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
///**
// * Tests visant à isoler le filtrage et la recherche dans le MessageModel et MessageController.
// */
//public class MessageFilteringTest {
//
//  // --- FAKE IMPLEMENTATIONS ---
//
//  /**
//   * Fake session qui permet de contrôler l’utilisateur connecté.
//   */
//  public static class FakeSession implements ISession {
//    private User connectedUser;
//    public FakeSession(User user) { connectedUser = user; }
//    @Override public User getConnectedUser() { return connectedUser; }
//    @Override public void addObserver(ISessionObserver observer) { }
//    @Override public void removeObserver(ISessionObserver observer) { }
//    @Override public void connect(User user) { connectedUser = user; }
//    @Override public void disconnect() { connectedUser = null; }
//  }
//
//  /**
//   * Fake vue de chat qui capte le résultat de la recherche.
//   */
//  public static class FakeMessageView extends ModernChatView {
//    public Set<Message> lastSearchResults = new HashSet<>();
//    public FakeMessageView(ISession session) {
//      // On passe des ActionListener vides puisque nous n’utilisons pas ces actions dans le test
////      super(session, _ -> { }, _ -> { });
//    }
//    public void updateSearchResults(Set<Message> messages) {
//      lastSearchResults = messages;
//    }
//    // Les notifications de mise à jour (notifyMessageAdded/Removed) ne font rien ici.
//    @Override public void notifyMessageAdded() { }
//    @Override public void notifyMessageRemoved() { }
//  }
//
//  // --- TESTS ---
//
//  @Test
//  void testFilteringRelevantMessages() {
//    // Création de 3 utilisateurs fictifs.
//    User userA = new User(UUID.randomUUID(), "@Alice", "pwd", "Alice", new HashSet<>(), "");
//    User userB = new User(UUID.randomUUID(), "@Bob", "pwd", "Bob", new HashSet<>(), "");
//    User userC = new User(UUID.randomUUID(), "@Charlie", "pwd", "Charlie", new HashSet<>(), "");
//    // userA suit Bob
//    userA.addFollowing(userB.getUserTag());
//
//    FakeSession session = new FakeSession(userA);
//    MessageModel model = new MessageModel(session);
//
//    // Création de 3 messages provenant de chacun.
//    Message msgFromAlice = new Message(userA, "Hello from Alice");
//    Message msgFromBob = new Message(userB, "Hello from Bob");
//    Message msgFromCharlie = new Message(userC, "Hello from Charlie");
//
//    model.addMessage(msgFromAlice);
//    model.addMessage(msgFromBob);
//    model.addMessage(msgFromCharlie);
//
//    Set<Message> filtered = new HashSet<>(model.getMessages());
//    // On s'attend à avoir les messages de Alice (lui-même) et de Bob (qu'elle suit)
//    assertTrue(filtered.contains(msgFromAlice), "Le message d'Alice doit être pertinent");
//    assertTrue(filtered.contains(msgFromBob), "Le message de Bob doit être pertinent (Alice suit Bob)");
//    assertFalse(filtered.contains(msgFromCharlie), "Le message de Charlie ne doit pas être pertinent");
//  }
//
//  @Test
//  void testAddingNewMessageDoesNotRemovePrevious() {
//    User userA = new User(UUID.randomUUID(), "@Alice", "pwd", "Alice", new HashSet<>(), "");
//    FakeSession session = new FakeSession(userA);
//    MessageModel model = new MessageModel(session);
//
//    Message msg1 = new Message(userA, "Premier message");
//    model.addMessage(msg1);
//    Set<Message> messagesAfterFirst = new HashSet<>(model.getMessages());
//    assertTrue(messagesAfterFirst.contains(msg1), "Le premier message doit être présent");
//
//    Message msg2 = new Message(userA, "Deuxième message");
//    model.addMessage(msg2);
//    Set<Message> messagesAfterSecond = new HashSet<>(model.getMessages());
//    // Les deux messages doivent être présents
//    assertTrue(messagesAfterSecond.contains(msg1), "Le premier message doit être conservé");
//    assertTrue(messagesAfterSecond.contains(msg2), "Le deuxième message doit être présent");
//  }
//
//  @Test
//  void testSearchMessagesByUserTag() {
//    // Création de 2 utilisateurs : userA et userB
//    User userA = new User(UUID.randomUUID(), "@Alice", "pwd", "Alice", new HashSet<>(), "");
//    User userB = new User(UUID.randomUUID(), "@Bob", "pwd", "Bob", new HashSet<>(), "");
//    // userA suit Bob
//    userA.addFollowing(userB.getUserTag());
//
//    FakeSession session = new FakeSession(userA);
//    MessageModel model = new MessageModel(session);
//    // Ajout de messages de Alice et Bob
//    Message msgFromAlice = new Message(userA, "Message d'Alice");
//    Message msgFromBob = new Message(userB, "Message de Bob avec mention @Bob");
//    List<Message> messages = Arrays.asList(msgFromAlice, msgFromBob);
//    model.addMessage(msgFromAlice);
//    model.addMessage(msgFromBob);
//
//    // Création d'une fausse vue pour capter le résultat de la recherche.
//    FakeMessageView fakeView = new FakeMessageView(session);
//
//    // Pour la recherche, on crée un MessageController en passant un IDatabase et EntityManager fictifs.
//    IDatabase dummyDatabase = new Database();
//    EntityManager dummyEntityManager = new EntityManager(dummyDatabase);
//    MessageController controller = new MessageController(session, fakeView, dummyDatabase, model, dummyEntityManager);
//
//    // Rechercher par tag "@Bob" devrait retourner le message de Bob uniquement.
//    controller.searchMessages("@Bob", messages);
//    Set<Message> searchResult = fakeView.lastSearchResults;
//    assertTrue(searchResult.contains(msgFromBob), "La recherche avec @Bob doit retourner le message de Bob");
//    assertFalse(searchResult.contains(msgFromAlice), "La recherche avec @Bob ne doit pas retourner le message d'Alice");
//
//    // Une recherche vide doit retourner l'ensemble des messages filtrés (Alice et Bob)
//    controller.searchMessages("", messages);
//    searchResult = fakeView.lastSearchResults;
//    assertTrue(searchResult.contains(msgFromAlice), "La recherche vide doit retourner le message d'Alice");
//    assertTrue(searchResult.contains(msgFromBob), "La recherche vide doit retourner le message de Bob");
//  }
//
//  @Test
//  void testFollowListChangedRefreshesFilteredMessages() {
//    // Création de userA et userC (userA ne suit initialement pas userC)
//    User userA = new User(UUID.randomUUID(), "@Alice", "pwd", "Alice", new HashSet<>(), "");
//    User userC = new User(UUID.randomUUID(), "@Charlie", "pwd", "Charlie", new HashSet<>(), "");
//    FakeSession session = new FakeSession(userA);
//    MessageModel model = new MessageModel(session);
//
//    Message msgFromCharlie = new Message(userC, "Message de Charlie");
//    model.addMessage(msgFromCharlie);
//    // Initialement, userA ne suit pas Charlie, donc le message ne doit pas figurer
//    Set<Message> filteredInitial = new HashSet<>(model.getMessages());
//    assertFalse(filteredInitial.contains(msgFromCharlie),
//      "Au départ, le message de Charlie ne doit pas être visible");
//
//    // Maintenant, userA suit Charlie : le mécanisme d'observation doit déclencher le rafraîchissement
//    userA.addFollowing(userC.getUserTag());
//    // Après notification, le message devrait apparaître
//    Set<Message> filteredAfter = new HashSet<>(model.getMessages());
//    assertTrue(filteredAfter.contains(msgFromCharlie),
//      "Après avoir suivi Charlie, le message de celui-ci doit apparaître");
//  }
//}
