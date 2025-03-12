package com.ubo.tp.message.components.message.chatbot.llm;

public interface LLMClient {
  /**
   * Génère du contenu textuel à partir d'un prompt et d'une configuration de génération.
   * @param prompt Le prompt/invite textuel.
   * @param config La configuration de génération.
   * @return Le texte généré.
   * @throws Exception en cas d'erreur lors de la requête.
   */
  String generateContent(String prompt, GenerationConfig config) throws Exception;
}

