package com.ubo.tp.message.components.message.chatbot.llm;

import java.util.List;

public class GenerationConfig {
  private List<String> stopSequences;
  private double temperature;
  private int maxOutputTokens;
  private double topP;
  private int topK;

  public GenerationConfig(List<String> stopSequences, double temperature, int maxOutputTokens, double topP, int topK) {
    this.stopSequences = stopSequences;
    this.temperature = temperature;
    this.maxOutputTokens = maxOutputTokens;
    this.topP = topP;
    this.topK = topK;
  }

  public List<String> getStopSequences() {
    return stopSequences;
  }

  public double getTemperature() {
    return temperature;
  }

  public int getMaxOutputTokens() {
    return maxOutputTokens;
  }

  public double getTopP() {
    return topP;
  }

  public int getTopK() {
    return topK;
  }
}
