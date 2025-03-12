package com.ubo.tp.message.components.message.chatbot;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class YamlResponseGenerator implements IResponseGenerator {
  private final String backstory;
  private final String goal;

  /**
   * Charge la configuration du chatbot depuis un fichier YAML.
   *
   * @param yamlFilePath Chemin du fichier YAML (par exemple "/chatbotConfig.yml" pour le classpath,
   *                     ou "chatbotConfig.yml" dans le répertoire courant).
   */
  public YamlResponseGenerator(String yamlFilePath) {
    Yaml yaml = new Yaml();
    InputStream in = getClass().getResourceAsStream(yamlFilePath);
    if (in == null) {
      System.err.println("Fichier YAML non trouvé dans le classpath avec le chemin : " + yamlFilePath);
      // Tente de charger le fichier depuis le répertoire courant
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
        if (in != null) {
          in.close();
        }
      } catch (Exception ignore) {
      }
    }
  }

  @Override
  public String generateResponse(String input, ConversationContext context) {
    // Ajoute le message entrant à l'historique
    context.addHistory("User: " + input);
    // Exemple simple de réponse intégrant la backstory, le but et le message reçu.
    String response = "Bonjour, je suis Gemini Flash 2.0. " +
                      "Mon histoire: " + backstory + ". " +
                      "Mon objectif: " + goal + ". " +
                      "Vous avez dit: " + input;
    context.addHistory("Gemini Flash 2.0: " + response);
    return response;
  }

  public String getBackstory() {
    return backstory;
  }

  public String getGoal() {
    return goal;
  }
}
