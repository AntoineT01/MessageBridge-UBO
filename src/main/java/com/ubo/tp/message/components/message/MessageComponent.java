package com.ubo.tp.message.components.message;

import com.ubo.tp.message.components.IComponent;
import com.ubo.tp.message.components.message.controller.MessageController;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.components.message.view.IChatView;
import com.ubo.tp.message.components.message.view.ModernChatView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.ISession;

import javax.swing.JPanel;
import java.awt.event.ActionListener;

import static com.ubo.tp.message.components.message.controller.MessageController.searchMessages;

public class MessageComponent implements IComponent<JPanel> {

  private final JPanel messagePanel;
  private final MessageController messageController;
  private final MessageModel messageModel;
  private final IChatView chatView;

  public MessageComponent(IDatabase database, ISession session, EntityManager entityManager) {
    // CrÃ©er d'abord le modÃ¨le pour Ã©viter les dÃ©pendances circulaires
    this.messageModel = new MessageModel(session);

    // CrÃ©er la vue sans actions pour le moment
    ModernChatView concreteChatView = new ModernChatView(session, null, null);
    this.chatView = concreteChatView;
    this.messagePanel = concreteChatView;

    // Configurer le modÃ¨le
    this.chatView.setModel(messageModel);

    // CrÃ©er le contrÃ´leur
    this.messageController = new MessageController(session, chatView, database, messageModel, entityManager);

    // Maintenant que tout est initialisÃ©, crÃ©er et dÃ©finir les actions
    ActionListener sendAction = _ -> {
      String text = chatView.getMessageText();
      messageController.sendMessage(text);
    };

    ActionListener searchAction = _ -> {
      String query = chatView.getSearchQuery();
      chatView.updateSearchResults(searchMessages(query, messageModel.getMessages()));
    };

    for (ActionListener l : concreteChatView.getSendButton().getActionListeners()) {
      concreteChatView.getSendButton().removeActionListener(l);
    }
    concreteChatView.getSendButton().addActionListener(sendAction);
    concreteChatView.getSearchBar().addSearchAction(searchAction);
  }

  @Override
  public JPanel getComponent() {
    return messagePanel;
  }

  public JPanel getMessagePanel() {
    return getComponent();
  }

  @Override
  public void initialize() {
    // Rien Ã  faire ici, le modÃ¨le est dÃ©jÃ  configurÃ©
  }

  @Override
  public void reset() {
    chatView.clearMessages();
  }

  @Override
  public void setEnabled(boolean enabled) {
    messagePanel.setEnabled(enabled);
  }

  public MessageModel getMessageModel() {
    return messageModel;
  }

  /**
   * Rafraîchit l'affichage en réaffichant tous les messages filtrés.
   */
  public void refreshMessages() {
    chatView.clearMessages();
    for (Message m : messageModel.getMessages()) {
      chatView.addMessageToFeed(m);
    }
  }
}