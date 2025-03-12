package com.ubo.tp.message.common.utils;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe utilitaire pour jouer des sons de notification
 */
public class SoundPlayer {

  private static final String NOTIFICATION_SOUND = "/sounds/notification.wav";

  // Format audio standard largement supporté (PCM signé, 44100 Hz, 16 bits, stéréo)
  private static final AudioFormat TARGET_FORMAT = new AudioFormat(
    AudioFormat.Encoding.PCM_SIGNED,
    44100.0f,
    16,
    2,
    4,
    44100.0f,
    false
  );

  /**
   * Joue le son de notification si activé dans les préférences
   */
  public static void playNotificationSound() {
    // Vérifier les préférences
    com.ubo.tp.message.common.utils.NotificationConfig config = com.ubo.tp.message.common.utils.NotificationConfig.getInstance();
    if (config.isNotificationsEnabled() && config.isNotificationSoundEnabled()) {
      try {
        playSound(NOTIFICATION_SOUND);
      } catch (Exception e) {
        // En cas d'erreur, on affiche simplement un message mais on ne perturbe pas l'application
        System.err.println("Impossible de jouer le son de notification: " + e.getMessage());
      }
    }
  }

  /**
   * Joue un son depuis une ressource
   *
   * @param soundPath Chemin de la ressource sonore
   */
  private static void playSound(String soundPath) {
    new Thread(() -> {
      Clip clip = null;
      try {
        // Charger le son depuis les ressources
        InputStream soundStream = SoundPlayer.class.getResourceAsStream(soundPath);
        if (soundStream == null) {
          System.err.println("Son non trouvé: " + soundPath);

          // Utiliser un son de fallback simple généré
          AudioFormat format = new AudioFormat(8000, 8, 1, true, false);
          ByteArrayOutputStream out = new ByteArrayOutputStream();

          // Générer un simple "beep" (1 seconde, 500 Hz)
          for (int i = 0; i < 8000; i++) {
            out.write((byte) (Math.sin(i / (8000 / 500) * 2 * Math.PI) * 127));
          }

          soundStream = new ByteArrayInputStream(out.toByteArray());

          AudioInputStream audioStream = new AudioInputStream(
            soundStream,
            format,
            out.size()
          );

          // Configurer la ligne audio
          DataLine.Info info = new DataLine.Info(Clip.class, format);
          clip = (Clip) AudioSystem.getLine(info);
          clip.open(audioStream);
        } else {
          // Lire le flux audio
          AudioInputStream originalStream = AudioSystem.getAudioInputStream(soundStream);

          // Obtenir le format d'origine
          AudioFormat originalFormat = originalStream.getFormat();
          System.out.println("Format audio original: " + originalFormat);

          // Convertir vers un format plus largement supporté si nécessaire
          AudioInputStream targetStream;
          if (!originalFormat.matches(TARGET_FORMAT)) {
            targetStream = AudioSystem.getAudioInputStream(TARGET_FORMAT, originalStream);
            System.out.println("Conversion vers format cible: " + TARGET_FORMAT);
          } else {
            targetStream = originalStream;
          }

          // Configurer la ligne audio
          DataLine.Info info = new DataLine.Info(Clip.class, targetStream.getFormat());

          if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Format audio non supporté. Essai avec un format alternatif.");
            // Essayer avec un format plus simple
            AudioFormat simpleFormat = new AudioFormat(
              AudioFormat.Encoding.PCM_SIGNED,
              8000.0f,
              8,
              1,
              1,
              8000.0f,
              false
            );
            targetStream = AudioSystem.getAudioInputStream(simpleFormat, originalStream);
            info = new DataLine.Info(Clip.class, simpleFormat);
          }

          clip = (Clip) AudioSystem.getLine(info);
          clip.open(targetStream);
        }

        // Ajouter un écouteur pour fermer le clip après la lecture
        Clip finalClip = clip;
        clip.addLineListener(event -> {
          if (event.getType() == LineEvent.Type.STOP) {
            finalClip.close();
          }
        });

        // Jouer le son
        clip.start();

      } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        System.err.println("Erreur lors de la lecture du son: " + e.getMessage());
        e.printStackTrace();

        // Essayer de fermer le clip si disponible
        if (clip != null && clip.isOpen()) {
          clip.close();
        }
      }
    }).start();
  }
}