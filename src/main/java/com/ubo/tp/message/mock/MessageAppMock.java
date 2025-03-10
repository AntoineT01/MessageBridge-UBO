package com.ubo.tp.message.mock;

import com.ubo.tp.message.common.utils.ImageUtils;
import com.ubo.tp.message.common.utils.RandomUtils;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class MessageAppMock {

  protected JFrame mFrame;
  protected IDatabase mDatabase;
  protected EntityManager mEntityManager;

  public MessageAppMock(IDatabase database, EntityManager entityManager) {
    this.mDatabase = database;
    this.mEntityManager = entityManager;
  }

  public void showGUI() {
    if (mFrame == null) {
      initGUI();
    }

    SwingUtilities.invokeLater(() -> {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      mFrame.setLocation((screenSize.width - mFrame.getWidth()) / 6,
        (screenSize.height - mFrame.getHeight()) / 4);
      mFrame.setVisible(true);
      mFrame.pack();
    });
  }

  protected void initGUI() {
    // Création de la fenêtre principale du mock
    mFrame = new JFrame("MOCK");
    // Utilisation de l'icône dans la fenêtre
    ImageIcon windowIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 32, 32);
    if (windowIcon != null) {
      mFrame.setIconImage(windowIcon.getImage());
    }

    mFrame.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    // Section base de données
    JLabel dbLabel = new JLabel("Database");
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
    mFrame.add(dbLabel, gbc);

    Button addUserButton = new Button("Add User");
    addUserButton.setPreferredSize(new Dimension(100, 50));
    addUserButton.addActionListener(_ -> addUserInDatabase());
    gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
    mFrame.add(addUserButton, gbc);

    Button addMessageButton = new Button("Add Message");
    addMessageButton.setPreferredSize(new Dimension(100, 50));
    addMessageButton.addActionListener(_ -> addMessageInDatabase());
    gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
    mFrame.add(addMessageButton, gbc);

    // Section fichiers
    JLabel fileLabel = new JLabel("Files");
    gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
    mFrame.add(fileLabel, gbc);

    Button sendUserButton = new Button("Write User");
    sendUserButton.setPreferredSize(new Dimension(100, 50));
    sendUserButton.addActionListener(_ -> writeUser());
    gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
    mFrame.add(sendUserButton, gbc);

    Button sendMessageButton = new Button("Write Message");
    sendMessageButton.setPreferredSize(new Dimension(100, 50));
    sendMessageButton.addActionListener(_ -> writeMessage());
    gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
    mFrame.add(sendMessageButton, gbc);
  }

  protected void addUserInDatabase() {
    User newUser = generateUser();
    mDatabase.addUser(newUser);
  }

  protected void writeUser() {
    User newUser = generateUser();
    mEntityManager.writeUserFile(newUser);
  }

  protected User generateUser() {
    int randomInt = RandomUtils.randomInt();
    String userName = "MockUser" + randomInt;
    return new User(UUID.randomUUID(), userName, "This_Is_Not_A_Password", userName, new HashSet<>(), "");
  }

  protected void addMessageInDatabase() {
    Message newMessage = generateMessage();
    mDatabase.addMessage(newMessage);
  }

  protected void writeMessage() {
    Message newMessage = generateMessage();
    mEntityManager.writeMessageFile(newMessage);
  }

  protected Message generateMessage() {
    if (mDatabase.getUsers().isEmpty()) {
      addUserInDatabase();
    }
    List<User> users = new ArrayList<>(mDatabase.getUsers());
    int userIndex = RandomUtils.randomInt();
    User randomUser = users.get(userIndex);
    return new Message(randomUser, "Message fictif!! #Mock #test");
  }
}
