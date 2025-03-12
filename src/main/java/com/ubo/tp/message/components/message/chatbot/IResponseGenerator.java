package com.ubo.tp.message.components.message.chatbot;

public interface IResponseGenerator {
  String generateResponse(String input, ConversationContext context);
  String getBackstory();
  String getGoal();
}
