package com.ubo.tp.message.components.message.chatbot;

import com.ubo.tp.message.common.utils.DataFilesManager;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.entity.EntityManager;

public class ChatbotController implements IDatabaseObserver {

  private final IDatabase database;
  private final EntityManager entityManager;
  private final User chatbotUser;
  private final IResponseGenerator responseGenerator;
  private final ConversationContext conversationContext;
  private final long startTimestamp;

  public ChatbotController(IDatabase database, EntityManager entityManager, User chatbotUser,
                           IResponseGenerator responseGenerator, MessageModel model) {
    this.database = database;
    this.entityManager = entityManager;
    this.chatbotUser = chatbotUser;
    this.responseGenerator = responseGenerator;
    this.startTimestamp = System.currentTimeMillis();

    // On récupère directement la backstory et le goal depuis le générateur
    this.conversationContext = new ConversationContext(model, responseGenerator.getBackstory(), responseGenerator.getGoal());

    // S'assurer que l'utilisateur chatbot est écrit dans le répertoire
    User geminiFlash = DataFilesManager.CHATBOT_USER;
    if (this.database.getUsers().stream().noneMatch(u -> u.equals(geminiFlash))) {
      entityManager.writeUserFile(geminiFlash);
    }
    entityManager.writeUserFile(geminiFlash);
  }

  @Override
  public void notifyMessageAdded(Message addedMessage) {
    if (addedMessage.getEmissionDate() < startTimestamp) {
      return;
    }
    if (addedMessage.getSender().equals(chatbotUser)) {
      return;
    }
    if (addedMessage.getText().contains(chatbotUser.getUserTag())) {
      String replyText = responseGenerator.generateResponse(addedMessage.getText(), conversationContext);
      Message reply = new Message(chatbotUser, replyText);
      entityManager.writeMessageFile(reply);
    }
  }

  @Override public void notifyMessageDeleted(Message deletedMessage) { }
  @Override public void notifyMessageModified(Message modifiedMessage) { }
  @Override public void notifyUserAdded(User addedUser) { }
  @Override public void notifyUserDeleted(User deletedUser) { }
  @Override public void notifyUserModified(User modifiedUser) { }
}
