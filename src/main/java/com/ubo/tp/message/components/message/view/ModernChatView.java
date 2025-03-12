package com.ubo.tp.message.components.message.view;

import com.ubo.tp.message.common.ui.RoundedBorder;
import com.ubo.tp.message.common.ui.RoundedTextArea;
import com.ubo.tp.message.common.ui.SearchBar;
import com.ubo.tp.message.components.message.autocompletion.AutoCompletionController;
import com.ubo.tp.message.components.message.autocompletion.AutoCompletionModel;
import com.ubo.tp.message.components.message.autocompletion.AutoCompletionView;
import com.ubo.tp.message.components.message.autocompletion.SuggestionProvider;
import com.ubo.tp.message.components.message.autocompletion.UserSuggestionProviderFX;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.ISession;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ModernChatView extends JPanel implements IChatView {

  private final ChatPanel chatPanel;
  private final RoundedTextArea messageInputArea;
  private final SearchBar searchBar;
  private final ISession session;
  private final JButton sendButton;

  private MessageModel model;

  // Intégration de l'auto-complétion
  private AutoCompletionController autoCompletionController;

  public ModernChatView(ISession session, ActionListener sendAction, ActionListener searchAction, IDatabase database) {
    this.session = session;
    setLayout(new BorderLayout());

    searchBar = new SearchBar("Recherche: ", 18, "Rechercher");
    if (searchAction != null) {
      searchBar.addSearchAction(searchAction);
    }
    add(searchBar, BorderLayout.NORTH);

    chatPanel = new ChatPanel();
    add(chatPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
    bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel inputPanel = new JPanel(new BorderLayout());
    messageInputArea = new RoundedTextArea(2, 30);
    messageInputArea.setLineWrap(true);
    messageInputArea.setWrapStyleWord(true);

    CompoundBorder textAreaBorder = new CompoundBorder(new RoundedBorder(15), new EmptyBorder(10, 10, 10, 10));
    messageInputArea.setBorder(textAreaBorder);

    JScrollPane scrollPane = new JScrollPane(messageInputArea);
    scrollPane.setBorder(null);
    inputPanel.add(scrollPane, BorderLayout.CENTER);

    sendButton = new JButton("Envoyer");
    sendButton.setBackground(new Color(30, 144, 255));
    sendButton.setFocusPainted(false);
    sendButton.setOpaque(true);
    CompoundBorder buttonBorder = new CompoundBorder(new RoundedBorder(15), new EmptyBorder(10, 20, 10, 20));
    sendButton.setBorder(buttonBorder);

    bottomPanel.add(inputPanel, BorderLayout.CENTER);
    bottomPanel.add(sendButton, BorderLayout.EAST);
    add(bottomPanel, BorderLayout.SOUTH);

    if (sendAction != null) {
      sendButton.addActionListener(sendAction);
    }

    // Intégration de l'auto-complétion :
    // Vous devez fournir ici l'instance de votre base de données (IDatabase)
    // Exemple : DatabaseSingleton.getInstance()
    SuggestionProvider suggestionProvider = new UserSuggestionProviderFX(database);
    AutoCompletionModel autoCompletionModel = new AutoCompletionModel(suggestionProvider);
    AutoCompletionView autoCompletionView = new AutoCompletionView();
    autoCompletionController = new AutoCompletionController(messageInputArea, autoCompletionModel, autoCompletionView);
  }

  public JButton getSendButton() {
    return sendButton;
  }

  public SearchBar getSearchBar() {
    return searchBar;
  }

  @Override
  public String getMessageText() {
    return messageInputArea.getText();
  }

  @Override
  public void clearMessageInput() {
    messageInputArea.setText("");
  }

  @Override
  public String getSearchQuery() {
    return searchBar.getSearchQuery();
  }

  @Override
  public void addMessageToFeed(Message message) {
    User connectedUser = session.getConnectedUser();
    boolean isOutgoing = (connectedUser != null && connectedUser.equals(message.getSender()));
    String senderDisplayName = message.getSender().getName() + " (" + message.getSender().getUserTag() + ")";
    String timeString = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(message.getEmissionDate()));
    MessageBubble bubble = new MessageBubble(senderDisplayName, message.getText(), isOutgoing, timeString);
    chatPanel.addMessageBubble(bubble, isOutgoing);
  }

  @Override
  public void updateSearchResults(List<Message> messages) {
    chatPanel.clearMessages();
    for (Message m : messages) {
      addMessageToFeed(m);
    }
  }

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

  @Override
  public void setModel(MessageModel model) {
    this.model = model;
    model.addObserver(this);
  }

  @Override
  public void clearMessages() {
    chatPanel.clearMessages();
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    searchBar.setEnabled(enabled);
    messageInputArea.setEnabled(enabled);
    chatPanel.setEnabled(enabled);
  }

  @Override
  public Component getComponent() {
    return this;
  }
}
