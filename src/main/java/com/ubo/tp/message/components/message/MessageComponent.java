package com.ubo.tp.message.components.message;

import com.ubo.tp.message.components.message.controller.MessageController;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.components.message.view.ModernChatView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISession;

import javax.swing.JPanel;
import java.awt.event.ActionListener;

import static com.ubo.tp.message.components.message.controller.MessageController.searchMessages;

public class MessageComponent {
  private ModernChatView messagePanel;
  private MessageController messageController;
  private MessageModel messageModel; // stocker le modèle

  public MessageComponent(IDatabase database, ISession session, EntityManager entityManager) {
    ActionListener sendAction = _ -> {
      String text = messagePanel.getMessageText();
      messageController.sendMessage(text);
    };

    ActionListener searchAction = _ -> {
      String query = messagePanel.getSearchQuery();
      messagePanel.updateSearchResults(searchMessages(query, messageModel.getMessages()));
    };

    messagePanel = new ModernChatView(session, sendAction, searchAction);
    messageModel = new MessageModel(session);
    messagePanel.setModel(messageModel);
    messageController = new MessageController(session, messagePanel, database, messageModel, entityManager);
  }

  public JPanel getMessagePanel() {
    return messagePanel;
  }

  public MessageModel getMessageModel() {
    return messageModel;
  }

  /**
   * Rafraîchit l'affichage en réaffichant tous les messages filtrés.
   */
  public void refreshMessages() {
    messagePanel.clearMessages();
    for (Message m : messageModel.getMessages()) {
      messagePanel.addMessageToFeed(m);
    }
  }
}
