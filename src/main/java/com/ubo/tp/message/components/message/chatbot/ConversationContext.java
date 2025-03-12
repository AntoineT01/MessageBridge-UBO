package com.ubo.tp.message.components.message.chatbot;

import com.ubo.tp.message.components.message.model.IMessageObserver;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.core.datamodel.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationContext implements IMessageObserver {
  private final MessageModel model;
  private List<String> history = new ArrayList<>();
  private final String backstory;
  private final String goal;

  public ConversationContext(MessageModel model, String backstory, String goal) {
    this.model = model;
    this.model.addObserver(this);
    this.backstory = backstory;
    this.goal = goal;
  }

  public List<String> getHistory() {
    return history;
  }

  public void addHistory(String message) {
    history.add(message);
  }

  public String getBackstory() {
    return backstory;
  }

  public String getGoal() {
    return goal;
  }

  @Override
  public void notifyMessageAdded() {
    this.history = model.getMessages().stream().map(Message::getText).collect(Collectors.toList());
  }

  @Override
  public void notifyMessageRemoved() {
    this.history = model.getMessages().stream().map(Message::getText).collect(Collectors.toList());
  }
}
