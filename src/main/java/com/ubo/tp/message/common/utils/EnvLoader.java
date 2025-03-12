package com.ubo.tp.message.common.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
  private static Dotenv dotenv = null;

  /**
   * Charge le fichier .env s'il existe (dans le répertoire de travail) et renvoie l'instance Dotenv.
   */
  public static Dotenv getDotenv() {
    if (dotenv == null) {
      dotenv = Dotenv.configure()
        .ignoreIfMissing()  // Ne lève pas d'erreur si le fichier n'existe pas
        .load();
      System.out.println("[EnvLoader] Fichier .env chargé (ou non trouvé, mais ignoré).");
    }
    return dotenv;
  }
}
