package com.ubo.tp.message.components.message.view;

import com.ubo.tp.message.common.ui.RoundedBorder;
import com.ubo.tp.message.common.ui.RoundedTextArea;
import com.ubo.tp.message.common.ui.SearchBar;
import com.ubo.tp.message.components.message.model.IMessageObserver;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.ISession;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

public class ModernChatView extends JPanel implements IMessageObserver {

  private final ChatPanel chatPanel;
  private final RoundedTextArea messageInputArea;
  private final SearchBar searchBar;
  private final ISession session;

  // Référence vers le modèle observé
  private MessageModel model;

  public ModernChatView(ISession session,
                        ActionListener sendAction,
                        ActionListener searchAction) {
    this.session = session;
    setLayout(new BorderLayout());

    // Barre de recherche en haut
    searchBar = new SearchBar("Recherche: ", 18, "Rechercher");
    searchBar.addSearchAction(searchAction);
    add(searchBar, BorderLayout.NORTH);

    // Zone centrale : ChatPanel
    chatPanel = new ChatPanel();
    add(chatPanel, BorderLayout.CENTER);

    // Panneau d’envoi en bas
    JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
    bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel inputPanel = new JPanel(new BorderLayout());
    messageInputArea = new RoundedTextArea(2, 30);
    messageInputArea.setLineWrap(true);
    messageInputArea.setWrapStyleWord(true);

    Border textAreaRounded = new RoundedBorder(15);
    Border textAreaEmpty = new EmptyBorder(10, 10, 10, 10);
    messageInputArea.setBorder(new CompoundBorder(textAreaRounded, textAreaEmpty));

    JScrollPane scrollPane = new JScrollPane(messageInputArea);
    scrollPane.setBorder(null);
    inputPanel.add(scrollPane, BorderLayout.CENTER);

    JButton sendButton = new JButton("Envoyer");
    sendButton.setBackground(new Color(30, 144, 255));
    sendButton.setFocusPainted(false);
    sendButton.setOpaque(true);

    Border buttonRounded = new RoundedBorder(15);
    Border buttonEmpty = new EmptyBorder(10, 20, 10, 20);
    sendButton.setBorder(new CompoundBorder(buttonRounded, buttonEmpty));

    bottomPanel.add(inputPanel, BorderLayout.CENTER);
    bottomPanel.add(sendButton, BorderLayout.EAST);
    add(bottomPanel, BorderLayout.SOUTH);

    sendButton.addActionListener(sendAction);
  }

  // Méthodes pour le contrôleur

  public String getMessageText() {
    return messageInputArea.getText();
  }

  public void clearMessageInput() {
    messageInputArea.setText("");
  }

  public String getSearchQuery() {
    return searchBar.getSearchQuery();
  }

  public void addMessageToFeed(Message message) {
    User connectedUser = session.getConnectedUser();
    boolean isOutgoing = (connectedUser != null && connectedUser.equals(message.getSender()));
    String senderDisplayName = message.getSender().getName() + " (" + message.getSender().getUserTag() + ")";
    MessageBubble bubble = new MessageBubble(senderDisplayName, message.getText(), isOutgoing);
    chatPanel.addMessageBubble(bubble, isOutgoing);
  }

  public void updateSearchResults(List<Message> messages) {
    chatPanel.clearMessages();
    messages.stream().sorted(Comparator.comparingLong(Message::getEmissionDate)).forEach(this::addMessageToFeed);
    for (Message m : messages) {
      addMessageToFeed(m);
    }
  }

  // Méthodes de l'observateur : lorsqu'un message est ajouté ou retiré dans le modèle,
  // nous rafraîchissons la totalité du chat.
  @Override
  public void notifyMessageAdded() {
    SwingUtilities.invokeLater(() -> {
      chatPanel.clearMessages();
      for (Message m : model.getMessages()) {
        addMessageToFeed(m);
      }
    });
  }

  @Override
  public void notifyMessageRemoved() {
    SwingUtilities.invokeLater(() -> {
      chatPanel.clearMessages();
      for (Message m : model.getMessages()) {
        addMessageToFeed(m);
      }
    });
  }

  // Permet d'associer le modèle à la vue et de s'y inscrire comme observateur
  public void setModel(MessageModel model) {
    this.model = model;
    model.addObserver(this);
  }

  public void clearMessages() {
    this.chatPanel.clearMessages();
  }
}
