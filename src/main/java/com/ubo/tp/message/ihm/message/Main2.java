package com.ubo.tp.message.ihm.message;

import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.core.session.Session;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.service.IMessageService;
import com.ubo.tp.message.service.MessageService;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.UUID;

public class Main2 {
  public static void main(String[] args) {
    // Base de données partagée
    IDatabase database = new Database();

    // Création d'une instance partagée de MessageService
    IMessageService sharedMessageService = new MessageService(database);

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

    // Création de deux intégrations MessageIntegration en passant le MessageService partagé
    MessageIntegration integrationA = new MessageIntegration(database, sessionA, sharedMessageService);
    MessageIntegration integrationB = new MessageIntegration(database, sessionB, sharedMessageService);

    // Création d'une fenêtre principale affichant les deux panneaux côte à côte
    JFrame frame = new JFrame("Test Multi-User Messaging");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(1, 2));
    frame.add(integrationA.getMessagePanel());
    frame.add(integrationB.getMessagePanel());
    frame.setSize(900, 600);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
