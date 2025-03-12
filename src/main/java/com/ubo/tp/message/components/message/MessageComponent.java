package com.ubo.tp.message.components.message;

import com.ubo.tp.message.components.IComponent;
import com.ubo.tp.message.components.message.chatbot.ChatbotController;
import com.ubo.tp.message.components.message.chatbot.GeminiResponseGenerator;
import com.ubo.tp.message.components.message.chatbot.IResponseGenerator;
import com.ubo.tp.message.components.message.chatbot.llm.GeminiClient;
import com.ubo.tp.message.components.message.chatbot.llm.GenerationConfig;
import com.ubo.tp.message.components.message.chatbot.llm.LLMClient;
import com.ubo.tp.message.components.message.controller.MessageController;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.components.message.view.IChatView;
import com.ubo.tp.message.components.message.view.ModernChatView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
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
    // Créer le modèle
    this.messageModel = new MessageModel(session);

    // Créer la vue
    ModernChatView concreteChatView = new ModernChatView(session, null, null);
    this.chatView = concreteChatView;
    this.messagePanel = concreteChatView;
    this.chatView.setModel(messageModel);

    // Créer le contrôleur de messages
    this.messageController = new com.ubo.tp.message.components.message.controller.MessageController(session, chatView, database, messageModel, entityManager);

    // Actions pour l'envoi et la recherche
    ActionListener sendAction = _ -> {
      String text = chatView.getMessageText();
      messageController.sendMessage(text);
    };

    ActionListener searchAction = _ -> {
      String query = chatView.getSearchQuery();
      chatView.updateSearchResults(searchMessages(query, messageModel.getMessages()));
    };

    concreteChatView.getSendButton().removeActionListener(null);
    concreteChatView.getSendButton().addActionListener(sendAction);
    concreteChatView.getSearchBar().addSearchAction(searchAction);

    // --- Intégration du Chatbot ---
    // Récupérer l'utilisateur chatbot via l'EntityManager (celui-ci doit être instancié dans l'EntityManager)
    User geminiFlash = entityManager.getChatbotUser();
    // Instancier le client Gemini
    LLMClient llmClient = new GeminiClient();
    // Configuration de génération (exemple)
    GenerationConfig generationConfig = new GenerationConfig(
      java.util.List.of("Title"),
      1.0,
      800,
      0.8,
      10
    );
    // Créer le générateur de réponse Gemini
    IResponseGenerator responseGenerator = new GeminiResponseGenerator(llmClient, generationConfig, "chatbotConfig.yml");    // Créer le contrôleur chatbot et l'enregistrer comme observateur

    ChatbotController chatbotController = new ChatbotController(database, entityManager, geminiFlash, responseGenerator,
      messageModel);
    database.addObserver(chatbotController);
  }

  @Override
  public JPanel getComponent() {
    return messagePanel;
  }

  @Override
  public void initialize() { }

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

  public void refreshMessages() {
    chatView.clearMessages();
    for (Message m : messageModel.getMessages()) {
      chatView.addMessageToFeed(m);
    }
  }
}
