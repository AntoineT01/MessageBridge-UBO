package com.ubo.tp.message.components.message.chatbot;

import com.ubo.tp.message.components.message.chatbot.llm.GenerationConfig;
import com.ubo.tp.message.components.message.chatbot.llm.LLMClient;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class GeminiResponseGenerator implements IResponseGenerator {

  private final LLMClient llmClient;
  private final GenerationConfig generationConfig;
  private final String backstory;
  private final String goal;

  /**
   * Charge la configuration depuis un fichier YAML donné et initialise le générateur.
   * @param llmClient Le client LLM (ici GeminiClient).
   * @param generationConfig La configuration de génération.
   * @param yamlFilePath Chemin du fichier YAML (par exemple "/chatbotConfig.yml").
   */
  public GeminiResponseGenerator(LLMClient llmClient, GenerationConfig generationConfig, String yamlFilePath) {
    this.llmClient = llmClient;
    this.generationConfig = generationConfig;

    Yaml yaml = new Yaml();
    InputStream in = getClass().getResourceAsStream(yamlFilePath);
    if (in == null) {
      System.err.println("Fichier YAML non trouvé dans le classpath avec le chemin : " + yamlFilePath);
      File file = new File(yamlFilePath);
      if (file.exists()) {
        try {
          in = new FileInputStream(file);
          System.out.println("Fichier YAML trouvé dans le répertoire courant : " + file.getAbsolutePath());
        } catch (Exception ex) {
          System.err.println("Erreur lors de l'ouverture du fichier YAML dans le répertoire courant : " + file.getAbsolutePath());
          throw new RuntimeException("Fichier YAML introuvable : " + yamlFilePath, ex);
        }
      } else {
        System.err.println("Le fichier YAML n'existe pas dans le répertoire courant. Chemin absolu recherché : " + file.getAbsolutePath());
        throw new RuntimeException("Fichier YAML introuvable : " + yamlFilePath);
      }
    } else {
      System.out.println("Fichier YAML chargé depuis le classpath : " + yamlFilePath);
    }
    try {
      Map<String, Object> data = yaml.load(in);
      this.backstory = (String) data.get("backstory");
      this.goal = (String) data.get("goal");
    } catch (Exception e) {
      throw new RuntimeException("Erreur de chargement du fichier YAML", e);
    } finally {
      try {
        if (in != null) { in.close(); }
      } catch (Exception ignore) {}
    }
  }

  @Override
  public String generateResponse(String input, ConversationContext context) {
    context.addHistory("User: " + input);
    StringBuilder promptBuilder = new StringBuilder();
    promptBuilder.append("Backstory: ").append(getBackstory()).append("\n");
    promptBuilder.append("Goal: ").append(getGoal()).append("\n");
    promptBuilder.append("Conversation History:\n");
    for (String historyLine : context.getHistory()) {
      promptBuilder.append(historyLine).append("\n");
    }
    promptBuilder.append("User: ").append(input);
    String prompt = promptBuilder.toString();

    try {
      String response = llmClient.generateContent(prompt, generationConfig);
      context.addHistory("Gemini Flash 2.0: " + response);
      return response;
    } catch (Exception e) {
      e.printStackTrace();
      return "Désolé, une erreur s'est produite lors de la génération de la réponse.";
    }
  }

  @Override
  public String getBackstory() {
    return backstory;
  }

  @Override
  public String getGoal() {
    return goal;
  }
}
