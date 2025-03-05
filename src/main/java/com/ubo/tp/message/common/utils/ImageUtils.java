package com.ubo.tp.message.common.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {

  // Cache pour stocker les icônes redimensionnées
  private static final Map<String, ImageIcon> iconCache = new HashMap<>();

  /**
   * Charge une image depuis le classpath, la redimensionne tout en gardant ses proportions
   * et met en cache le résultat.
   *
   * @param resourcePath Chemin de la ressource (ex: "/tux_logo.png")
   * @param maxWidth     Largeur maximale souhaitée
   * @param maxHeight    Hauteur maximale souhaitée
   * @return L'ImageIcon redimensionnée ou null en cas d'erreur
   */
  public static ImageIcon loadScaledIcon(String resourcePath, int maxWidth, int maxHeight) {
    // Clé du cache basée sur le chemin et les dimensions
    String cacheKey = resourcePath + "_" + maxWidth + "_" + maxHeight;

    if (iconCache.containsKey(cacheKey)) {
      return iconCache.get(cacheKey);
    }

    try {
      // Charge l'image originale en tant que BufferedImage
      BufferedImage originalImage = ImageIO.read(Objects.requireNonNull(ImageUtils.class.getResource(resourcePath)));
      if (originalImage == null) {
        System.err.println("Image non trouvée : " + resourcePath);
        return null;
      }

      int originalWidth = originalImage.getWidth();
      int originalHeight = originalImage.getHeight();

      // Calculer le facteur d'échelle pour garder les proportions
      double scaleFactor = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
      int newWidth = (int) (originalWidth * scaleFactor);
      int newHeight = (int) (originalHeight * scaleFactor);

      // Redimensionner l'image en utilisant SCALE_SMOOTH pour une meilleure qualité
      Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
      ImageIcon scaledIcon = new ImageIcon(scaledImage);

      // Stocker dans le cache
      iconCache.put(cacheKey, scaledIcon);

      return scaledIcon;
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
