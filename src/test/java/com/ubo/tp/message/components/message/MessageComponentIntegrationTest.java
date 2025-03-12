//package com.ubo.tp.message.components.message;
//
//import com.ubo.tp.message.core.database.IDatabase;
//import com.ubo.tp.message.core.database.IDatabaseObserver;
//import com.ubo.tp.message.core.datamodel.Message;
//import com.ubo.tp.message.core.datamodel.User;
//import com.ubo.tp.message.core.entity.EntityManager;
//import com.ubo.tp.message.core.session.ISession;
//import com.ubo.tp.message.core.session.ISessionObserver;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
///**
// * Tests d’intégration visant à reproduire le bug constaté lorsque database.getMessages() renvoie null.
// */
//public class MessageComponentIntegrationTest {
//
//  // --- FAKE IMPLEMENTATIONS POUR SIMULER LE COMPORTEMENT ---
//
//  /**
//   * FakeDatabase renvoie null pour getMessages().
//   */
//  private static class FakeDatabase implements IDatabase {
//    @Override public Set<User> getUsers() { return new HashSet<>(); }
//    @Override public Set<Message> getMessages() { return null; } // Problème simulé
//    @Override public void addMessage(Message messageToAdd) { }
//    @Override public void removeMessage(Message messageToRemove) { }
//    @Override public void modifiyMessage(Message messageToModify) { }
//    @Override public void addUser(User userToAdd) { }
//    @Override public void removeUser(User userToRemove) { }
//    @Override public void modifiyUser(User userToModify) { }
//    @Override public void clearMessages() { }
//    @Override public void clearUsers() { }
//    @Override public void clear() { }
//    @Override public Set<Message> getMessagesWithTag(String tag) { return new HashSet<>(); }
//    @Override public Set<Message> getMessagesWithUserTag(String userTag) { return new HashSet<>(); }
//    @Override public Set<Message> getUserMessages(User user) { return new HashSet<>(); }
//    @Override public Set<User> getFollowers(User user) { return new HashSet<>(); }
//    @Override public int getFollowersCount(User user) { return 0; }
//    @Override public void addObserver(IDatabaseObserver observer) { }
//    @Override public void deleteObserver(IDatabaseObserver observer) { }
//  }
//
//  /**
//   * FakeSession retourne un utilisateur fictif.
//   */
//  private static class FakeSession implements ISession {
//    private User dummyUser;
//    public FakeSession() {
//      dummyUser = new User(UUID.randomUUID(), "@Dummy", "pwd", "Dummy", new HashSet<>(), "");
//    }
//    @Override public User getConnectedUser() { return dummyUser; }
//    @Override public void addObserver(ISessionObserver observer) { }
//    @Override public void removeObserver(ISessionObserver observer) { }
//    @Override public void connect(User user) { dummyUser = user; }
//    @Override public void disconnect() { dummyUser = null; }
//  }
//
//  /**
//   * FakeEntityManager minimal, qui n’effectue pas d’écriture réelle.
//   */
//  private static class FakeEntityManager extends EntityManager {
//    public FakeEntityManager(IDatabase db) { super(db); }
//    @Override public void writeMessageFile(Message message) {
//      // Pas d'écriture réelle pour le test
//    }
//  }
//
//  // --- TESTS ---
//
//  /**
//   * Vérifie que la création du MessageComponent échoue (NullPointerException)
//   * lorsque database.getMessages() renvoie null.
//   */
//  @Test
//  public void testMessageComponentCreationWithNullMessages() {
//    IDatabase fakeDatabase = new FakeDatabase();
//    ISession fakeSession = new FakeSession();
//    EntityManager fakeEntityManager = new FakeEntityManager(fakeDatabase);
//
//    // On attend une NullPointerException lors de l'appel à
//    // messageModel.setMessages(database.getMessages())
//    assertThrows(NullPointerException.class, () -> {
//      new MessageComponent(fakeDatabase, fakeSession, fakeEntityManager);
//    });
//  }
//}
