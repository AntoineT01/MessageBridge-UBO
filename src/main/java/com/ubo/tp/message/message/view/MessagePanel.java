package com.ubo.tp.message.message.view;

import com.ubo.tp.message.core.datamodel.Message;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Set;

/**
 * Panneau Swing pour l’interaction avec les messages.
 * Propose une zone d’envoi, un champ de recherche et un affichage du fil de messages.
 */
public class MessagePanel extends JPanel {

  private final JTextArea messageInputArea;
  private final JButton sendButton;
  private final JTextField searchField;
  private final JButton searchButton;
  private final DefaultListModel<String> messageListModel;
  private final JList<String> messageList;

  /**
   * Construit le MessagePanel et initialise l’interface.
   */
  public MessagePanel() {
    setLayout(new BorderLayout());
    setBorder(new TitledBorder("Messages"));

    // Panneau d’envoi
    JPanel sendPanel = new JPanel(new BorderLayout());
    messageInputArea = new JTextArea(3, 40);
    messageInputArea.setLineWrap(true);
    messageInputArea.setWrapStyleWord(true);
    JScrollPane inputScrollPane = new JScrollPane(messageInputArea);
    sendButton = new JButton("Envoyer");
    sendPanel.add(inputScrollPane, BorderLayout.CENTER);
    sendPanel.add(sendButton, BorderLayout.EAST);

    // Panneau de recherche
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchField = new JTextField(20);
    searchButton = new JButton("Rechercher");
    searchPanel.add(new JLabel("Recherche: "));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);

    // Liste pour afficher les messages
    messageListModel = new DefaultListModel<>();
    messageList = new JList<>(messageListModel);
    JScrollPane listScrollPane = new JScrollPane(messageList);

    add(sendPanel, BorderLayout.NORTH);
    add(searchPanel, BorderLayout.CENTER);
    add(listScrollPane, BorderLayout.SOUTH);
  }

  public JButton getSendButton() {
    return sendButton;
  }

  public JTextField getSearchField() {
    return searchField;
  }

  public JButton getSearchButton() {
    return searchButton;
  }

  /**
   * Efface le contenu de la zone d’envoi.
   */
  public void clearMessageInput() {
    messageInputArea.setText("");
  }

  /**
   * Met à jour l’affichage du fil de messages avec l’ensemble des messages recherchés.
   *
   * @param messages l’ensemble des messages à afficher
   */
  public void updateSearchResults(Set<Message> messages) {
    messageListModel.clear();
    for (Message message : messages) {
      messageListModel.addElement(formatMessage(message));
    }
    this.repaint();
  }

  /**
   * Ajoute un message au fil d’actualité.
   *
   * @param message le message à ajouter
   */
  public void addMessageToFeed(Message message) {
    messageListModel.addElement(formatMessage(message));
  }

  /**
   * Formate un message pour l’affichage.
   *
   * @param message le message à formater
   * @return la chaîne formatée
   */
  private String formatMessage(Message message) {
    return "[" + message.getSender().getUserTag() + "] " + message.getText();
  }

  public String getMessageText() {
    return messageInputArea.getText();
  }
}
