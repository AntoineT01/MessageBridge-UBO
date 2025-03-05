package com.ubo.tp.message.components.message;

import com.ubo.tp.message.components.message.controller.MessageController;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.components.message.view.ModernChatView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import javax.swing.JPanel;
import java.awt.event.ActionListener;

public class MessageComponent {

  private ModernChatView messagePanel = null;
  private MessageController messageController = null;

  public MessageComponent(IDatabase database, ISession session) {
    ActionListener sendAction = _ -> {
      String text = messagePanel.getMessageText();
      messageController.sendMessage(text);
    };

    ActionListener searchAction = _ -> {
      String query = messagePanel.getSearchQuery();
      messageController.searchMessages(query);
    };

    messagePanel = new ModernChatView(session, sendAction, searchAction);
    MessageModel messageModel = new MessageModel();
    // La vue s'inscrit sur le modèle
    messagePanel.setModel(messageModel);
    // Le contrôleur travaille désormais via le modèle
    messageController = new MessageController(session, messagePanel, database, messageModel);
  }

  public JPanel getMessagePanel() {
    return messagePanel;
  }
}
