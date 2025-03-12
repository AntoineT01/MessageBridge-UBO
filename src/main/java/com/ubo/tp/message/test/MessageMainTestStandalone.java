package com.ubo.tp.message.test;

import com.ubo.tp.message.components.directory.controller.DirectoryController;
import com.ubo.tp.message.components.message.MessageComponent;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.core.session.Session;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.UUID;

public class MessageMainTestStandalone {
  public static void main(String[] args) {
    // Base de données partagée
    IDatabase database = new Database();
    // Exemple dans le main de test

    // Création de deux sessions distinctes
    ISession sessionA = new Session();
    ISession sessionB = new Session();

    // Création de deux utilisateurs de test
    User userA = new User(UUID.randomUUID(), "Alice", "pwd", "Alice", new HashSet<>(), "");
    User userB = new User(UUID.randomUUID(), "Bob", "pwd", "Bob", new HashSet<>(), "");

    // Faire en sorte que chaque utilisateur suive l'autre pour déclencher les notifications
    userA.addFollowing(userB.getUserTag());
    userB.addFollowing(userA.getUserTag());

    // Ajout des utilisateurs dans la base
    database.addUser(userA);
    database.addUser(userB);

    // Connexion des sessions
    sessionA.connect(userA);
    sessionB.connect(userB);

    EntityManager entityManager = new EntityManager(database);
    DirectoryController directoryController = new DirectoryController(entityManager);
    directoryController.changeExchangeDirectory("C:\\Users\\darwh\\Documents\\Test");

    // Création de deux intégrations MessageComponent en passant le MessageService partagé
    MessageComponent integrationA = new MessageComponent(database, sessionA, entityManager);
    MessageComponent integrationB = new MessageComponent(database, sessionB, entityManager);

    // Création d'une fenêtre principale affichant les deux panneaux côte à côte
    JFrame frame = new JFrame("Test Multi-User Messaging");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(1, 2));
//    frame.add(integrationA.getMessagePanel());
//    frame.add(integrationB.getMessagePanel());
    frame.setSize(900, 600);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
