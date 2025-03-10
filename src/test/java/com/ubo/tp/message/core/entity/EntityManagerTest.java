package com.ubo.tp.message.core.entity;

import com.ubo.tp.message.common.constants.Constants;
import com.ubo.tp.message.common.utils.DataFilesManager;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EntityManagerTest {

  @TempDir
  Path tempDir;

  /**
   * Crée un fichier utilisateur temporaire avec les propriétés données.
   */
  private File createUserFile(UUID userUuid, String userTag, String name) throws Exception {
    Properties props = new Properties();
    props.setProperty(DataFilesManager.PROPERTY_KEY_USER_UUID, userUuid.toString());
    props.setProperty(DataFilesManager.PROPERTY_KEY_USER_TAG, userTag);
    props.setProperty(DataFilesManager.PROPERTY_KEY_USER_PASSWORD, DataFilesManager.encrypt("password"));
    props.setProperty(DataFilesManager.PROPERTY_KEY_USER_NAME, name);
    props.setProperty(DataFilesManager.PROPERTY_KEY_USER_FOLLOWS, "");
    props.setProperty(DataFilesManager.PROPERTY_KEY_USER_AVATAR, "");
    File file = tempDir.resolve(userUuid + "." + Constants.USER_FILE_EXTENSION).toFile();
    try (FileOutputStream out = new FileOutputStream(file)) {
      props.store(out, "Test user file");
    }
    return file;
  }

  /**
   * Crée un fichier message temporaire avec les propriétés données.
   */
  private File createMessageFile(UUID messageUuid, UUID senderUuid, String text) throws Exception {
    Properties props = new Properties();
    props.setProperty(DataFilesManager.PROPERTY_KEY_MESSAGE_UUID, messageUuid.toString());
    props.setProperty(DataFilesManager.PROPERTY_KEY_MESSAGE_SENDER, senderUuid.toString());
    props.setProperty(DataFilesManager.PROPERTY_KEY_MESSAGE_DATE, "123456789");
    props.setProperty(DataFilesManager.PROPERTY_KEY_MESSAGE_TEXT, text);
    File file = tempDir.resolve(messageUuid + "." + Constants.MESSAGE_FILE_EXTENSION).toFile();
    try (FileOutputStream out = new FileOutputStream(file)) {
      props.store(out, "Test message file");
    }
    return file;
  }

  /**
   * Teste que lorsque les fichiers utilisateur et message sont chargés simultanément,
   * le message est correctement associé à l’utilisateur.
   */
  @Test
  void testLoadingUserBeforeMessage() throws Exception {
    // Création des fichiers utilisateur et message
    UUID userUuid = UUID.randomUUID();
    String userTag = "@Alice";
    String userName = "Alice";
    File userFile = createUserFile(userUuid, userTag, userName);

    UUID messageUuid = UUID.randomUUID();
    String messageText = "Hello world! #test";
    File messageFile = createMessageFile(messageUuid, userUuid, messageText);

    // Création d'un ensemble contenant les deux fichiers (l'ordre dans un Set n'est pas garanti)
    Set<File> files = new HashSet<>();
    files.add(userFile);
    files.add(messageFile);

    IDatabase database = new Database();
    EntityManager entityManager = new EntityManager(database);

    // On simule le chargement initial en passant l'ensemble des fichiers
    entityManager.notifyNewFiles(files);

    // Vérification que la base contient bien l'utilisateur et le message
    Set<User> users = database.getUsers();
    Set<Message> messages = database.getMessages();

    assertFalse(users.isEmpty(), "L'utilisateur doit être chargé");
    assertFalse(messages.isEmpty(), "Le message doit être chargé");

    // Vérification de l'association : le message doit pointer vers l'utilisateur créé
    Message loadedMessage = messages.iterator().next();
    assertEquals(userTag, loadedMessage.getSender().getUserTag(), "L'expéditeur du message doit être l'utilisateur chargé");
  }

  /**
   * Teste le cas où le fichier message est chargé avant le fichier utilisateur.
   * Le message sera alors associé à l'utilisateur inconnu, ce qui indique le bug.
   */
  @Test
  void testLoadingMessageBeforeUser() throws Exception {
    // Création des fichiers
    UUID userUuid = UUID.randomUUID();
    String userTag = "@Bob";
    String userName = "Bob";
    File userFile = createUserFile(userUuid, userTag, userName);

    UUID messageUuid = UUID.randomUUID();
    String messageText = "Message de test sans utilisateur chargé";
    File messageFile = createMessageFile(messageUuid, userUuid, messageText);

    IDatabase database = new Database();
    EntityManager entityManager = new EntityManager(database);

    // D'abord, on charge uniquement le fichier message
    Set<File> messageOnly = new HashSet<>();
    messageOnly.add(messageFile);
    entityManager.notifyNewFiles(messageOnly);

    // Vérification : le message est chargé, mais l'expéditeur n'est pas trouvé
    Set<Message> messagesAfterMsg = database.getMessages();
    assertFalse(messagesAfterMsg.isEmpty(), "Le message doit être chargé même sans utilisateur");
    Message loadedMessage1 = messagesAfterMsg.iterator().next();
    // On s'attend à ce que l'expéditeur soit l'utilisateur inconnu
    assertEquals(Constants.UNKNONWN_USER_UUID, loadedMessage1.getSender().getUuid(),
      "Bug attendu : l'expéditeur doit être l'utilisateur inconnu car le fichier utilisateur n'est pas encore chargé");

    // Ensuite, on charge le fichier utilisateur
    Set<File> userOnly = new HashSet<>();
    userOnly.add(userFile);
    entityManager.notifyNewFiles(userOnly);

    // Vérification : le message ne se met pas à jour et reste associé à l'utilisateur inconnu
    Set<Message> messagesAfterUser = database.getMessages();
    Message loadedMessage2 = messagesAfterUser.iterator().next();
    assertEquals(Constants.UNKNONWN_USER_UUID, loadedMessage2.getSender().getUuid(),
      "Bug : même après chargement du fichier utilisateur, l'expéditeur du message reste l'utilisateur inconnu");
  }
}
