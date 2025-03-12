package com.ubo.tp.message.components.message.chatbot.llm;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ubo.tp.message.common.utils.EnvLoader;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiClient implements LLMClient {
  private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
  private final String apiKey;
  private final HttpClient httpClient;

  public GeminiClient() {
    // Charger le .env pour récupérer la clé API
    Dotenv dotenv = EnvLoader.getDotenv();
    this.apiKey = dotenv.get("GEMINI_API_KEY");
    if (this.apiKey == null || this.apiKey.isEmpty()) {
      throw new RuntimeException("La clé GEMINI_API_KEY n'est pas définie dans le .env ou dans l'environnement.");
    }
    httpClient = HttpClient.newHttpClient();
  }

  @Override
  public String generateContent(String prompt, GenerationConfig config) throws Exception {
    String jsonPayload = buildJsonPayload(prompt, config);
    String url = BASE_URL + apiKey;
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
      .build();

    System.out.println("[GeminiClient] Envoi de la requête vers : " + url);
    System.out.println("[GeminiClient] Payload : " + jsonPayload);

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println("[GeminiClient] Réponse (" + response.statusCode() + ") : " + response.body());
    if (response.statusCode() != 200) {
      throw new RuntimeException("Erreur lors de l'appel à l'API Gemini, code HTTP : " + response.statusCode());
    }
    // Extraction du texte généré (à adapter en fonction du format de réponse réel)
    return extractGeneratedText(response.body());
  }

  private String buildJsonPayload(String prompt, GenerationConfig config) {
    StringBuilder sb = new StringBuilder();
    sb.append("{")
      .append("\"contents\": [{")
      .append("\"parts\": [{")
      .append("\"text\": \"").append(escapeJson(prompt)).append("\"")
      .append("}]")
      .append("}],");
    sb.append("\"generationConfig\": {")
      .append("\"temperature\": ").append(config.getTemperature()).append(",")
      .append("\"maxOutputTokens\": ").append(config.getMaxOutputTokens()).append(",")
      .append("\"topP\": ").append(config.getTopP()).append(",")
      .append("\"topK\": ").append(config.getTopK());
    if (config.getStopSequences() != null && !config.getStopSequences().isEmpty()) {
      sb.append(",\"stopSequences\": [");
      for (int i = 0; i < config.getStopSequences().size(); i++) {
        sb.append("\"").append(escapeJson(config.getStopSequences().get(i))).append("\"");
        if (i < config.getStopSequences().size() - 1) {
          sb.append(",");
        }
      }
      sb.append("]");
    }
    sb.append("}")
      .append("}");
    return sb.toString();
  }

  private String escapeJson(String text) {
    return text == null ? "" : text.replace("\"", "\\\"");
  }

  private String extractGeneratedText(String responseBody) {
    try {
      // Utiliser Gson pour parser la réponse JSON
      com.google.gson.JsonElement root = JsonParser.parseString(responseBody);
      if (root.isJsonObject()) {
        JsonObject rootObj = root.getAsJsonObject();
        if (rootObj.has("candidates") && rootObj.get("candidates").isJsonArray()) {
          JsonObject firstCandidate = rootObj.getAsJsonArray("candidates")
            .get(0).getAsJsonObject();
          JsonObject content = firstCandidate.getAsJsonObject("content");
          if (content.has("parts") && content.get("parts").isJsonArray()) {
            JsonObject firstPart = content.getAsJsonArray("parts")
              .get(0).getAsJsonObject();
            if (firstPart.has("text")) {
              String text = firstPart.get("text").getAsString();
              // Supprime le dernier \n s'il existe, puis effectue un trim
              return text.replaceAll("\\n$", "").trim();
            }
          }
        }
      }
    } catch (Exception e) {
      System.err.println("[GeminiClient] Erreur d'extraction via Gson : " + e.getMessage());
    }
    return "";
  }
}
