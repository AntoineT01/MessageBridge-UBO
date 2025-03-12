package com.ubo.tp.message.common.utils;

import com.ubo.tp.message.common.constants.Constants;
import com.ubo.tp.message.core.datamodel.Message;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Classe utilitaire pour gérer les pièces jointes des messages
 */
public class AttachmentUtils {

  // Taille maximale des vignettes (en pixels)
  private static final int THUMBNAIL_MAX_WIDTH = 300;
  private static final int THUMBNAIL_MAX_HEIGHT = 200;

  // Extensions supportées pour les images
  private static final String[] SUPPORTED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp"};

  /**
   * Ouvre un sélecteur de fichier pour choisir une image
   * @param parent Composant parent
   * @return Chemin du fichier sélectionné ou null si annulé
   */
  public static List<String> selectImageAttachments(Component parent) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Sélectionner des images à joindre");
    fileChooser.setMultiSelectionEnabled(true);

    // Filtre pour n'afficher que les images
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
      "Images", SUPPORTED_IMAGE_EXTENSIONS);
    fileChooser.setFileFilter(filter);

    List<String> selectedFiles = new ArrayList<>();

    if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File[] files = fileChooser.getSelectedFiles();
      System.out.println("Fichiers sélectionnés: " + files.length);

      for (File file : files) {
        System.out.println("Traitement du fichier: " + file.getAbsolutePath());

        if (isImageFile(file)) {
          System.out.println("Fichier reconnu comme image: " + file.getName());
          try {
            // Copie le fichier vers le répertoire des pièces jointes
            String savedPath = saveAttachment(file);
            System.out.println("Fichier sauvegardé: " + savedPath);

            if (savedPath != null) {
              // Utilisation de séparateurs universels pour éviter les problèmes avec les backslashes
              String normalizedPath = savedPath.replace('\\', '/');
              selectedFiles.add(normalizedPath);
            }
          } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la copie: " + e.getMessage());
            JOptionPane.showMessageDialog(
              parent,
              "Erreur lors de la copie du fichier: " + file.getName(),
              "Erreur",
              JOptionPane.ERROR_MESSAGE
            );
          }
        } else {
          System.out.println("Fichier non reconnu comme image: " + file.getName());
        }
      }

      System.out.println("Pièces jointes sélectionnées: " + selectedFiles.size());
    } else {
      System.out.println("Sélection de fichier annulée par l'utilisateur");
    }

    return selectedFiles;
  }

  /**
   * Vérifie si le fichier est une image
   */
  public static boolean isImageFile(File file) {
    if (file == null) {
      System.err.println("isImageFile: fichier null");
      return false;
    }

    if (!file.exists()) {
      System.err.println("isImageFile: fichier inexistant - " + file.getAbsolutePath());
      return false;
    }

    if (!file.isFile()) {
      System.err.println("isImageFile: n'est pas un fichier - " + file.getAbsolutePath());
      return false;
    }

    String fileName = file.getName().toLowerCase();
    for (String ext : SUPPORTED_IMAGE_EXTENSIONS) {
      if (fileName.endsWith("." + ext)) {
        System.out.println("isImageFile: fichier reconnu comme image - " + file.getAbsolutePath());
        return true;
      }
    }

    System.err.println("isImageFile: extension non reconnue - " + file.getAbsolutePath());
    return false;
  }

  /**
   * Sauvegarde une pièce jointe dans le répertoire des attachments
   * @param sourceFile Fichier source
   * @return Chemin du fichier sauvegardé ou null en cas d'erreur
   */
  public static String saveAttachment(File sourceFile) throws IOException {
    // Création du répertoire des pièces jointes s'il n'existe pas
    String attachmentDir = getAttachmentDirectory();
    System.out.println("Répertoire des pièces jointes: " + attachmentDir);

    // Normalisation du chemin - remplacer les antislashes par des slashes
    attachmentDir = attachmentDir.replace('\\', '/');

    Path attachmentPath = Paths.get(attachmentDir);
    if (!Files.exists(attachmentPath)) {
      System.out.println("Création du répertoire des pièces jointes");
      Files.createDirectories(attachmentPath);
    }

    // Génère un nom unique pour le fichier
    String extension = getFileExtension(sourceFile);
    String fileName = UUID.randomUUID().toString() + "." + extension;

    Path destPath = Paths.get(attachmentDir, fileName);
    System.out.println("Destination de la pièce jointe: " + destPath);

    // Copie le fichier
    Files.copy(sourceFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
    System.out.println("Fichier copié avec succès");

    // Retourner le chemin normalisé
    return destPath.toString().replace('\\', '/');
  }

  /**
   * Récupère l'extension d'un fichier
   */
  private static String getFileExtension(File file) {
    String name = file.getName();
    int lastIndexOf = name.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return ""; // Pas d'extension
    }
    return name.substring(lastIndexOf + 1).toLowerCase();
  }

  /**
   * Récupère le répertoire des pièces jointes
   */
  public static String getAttachmentDirectory() {
    String exchangeDir = getExchangeDirectory();
    String attachmentDir = exchangeDir + Constants.SYSTEM_FILE_SEPARATOR + "attachments";

    // S'assurer que le répertoire existe
    File dir = new File(attachmentDir);
    if (!dir.exists()) {
      boolean created = dir.mkdirs();
      if (!created) {
        System.err.println("Impossible de créer le répertoire des pièces jointes: " + attachmentDir);
        // Fallback sur le répertoire d'échange
        return exchangeDir;
      }
    }

    return attachmentDir;
  }

  /**
   * Récupère le répertoire d'échange
   */
  private static String getExchangeDirectory() {
    Properties props = PropertiesManager.loadProperties(Constants.CONFIGURATION_FILE);
    String savedPath = props.getProperty(Constants.CONFIGURATION_KEY_EXCHANGE_DIRECTORY);

    if (savedPath != null && !savedPath.isEmpty()) {
      return savedPath;
    }

    // Fallback sur le répertoire temporaire du système
    return Constants.SYSTEM_TMP_DIR;
  }

  /**
   * Crée une vignette pour une image
   * @param imagePath Chemin de l'image
   * @return ImageIcon redimensionnée ou null en cas d'erreur
   */
  public static ImageIcon createThumbnail(String imagePath) {
    try {
      System.out.println("Création de miniature pour: " + imagePath);

      // Normaliser le chemin d'accès
      imagePath = imagePath.replace('\\', '/');

      File imageFile = new File(imagePath);
      if (!imageFile.exists()) {
        System.err.println("Le fichier n'existe pas: " + imagePath);
        return null;
      }

      if (!isImageFile(imageFile)) {
        System.err.println("Le fichier n'est pas une image: " + imagePath);
        return null;
      }

      System.out.println("Lecture de l'image...");
      BufferedImage originalImage = ImageIO.read(imageFile);
      if (originalImage == null) {
        System.err.println("Impossible de lire l'image: " + imagePath);

        // Essayons une approche alternative
        System.out.println("Tentative avec Toolkit...");
        try {
          Image image = Toolkit.getDefaultToolkit().getImage(imagePath);
          ImageIcon icon = new ImageIcon(image);
          // Attendre que l'image soit chargée
          if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            System.out.println("Image chargée avec Toolkit");

            // Redimensionner l'image
            int originalWidth = icon.getIconWidth();
            int originalHeight = icon.getIconHeight();

            // Si l'image est déjà plus petite que les dimensions max, la retourner directement
            if (originalWidth <= THUMBNAIL_MAX_WIDTH && originalHeight <= THUMBNAIL_MAX_HEIGHT) {
              return icon;
            }

            // Calcul des nouvelles dimensions en gardant le ratio d'aspect
            double widthRatio = (double) THUMBNAIL_MAX_WIDTH / originalWidth;
            double heightRatio = (double) THUMBNAIL_MAX_HEIGHT / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (originalWidth * ratio);
            int newHeight = (int) (originalHeight * ratio);

            // Création de la vignette
            Image thumbnailImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(thumbnailImage);
          } else {
            System.err.println("Chargement de l'image avec Toolkit échoué");
            return null;
          }
        } catch (Exception e) {
          System.err.println("Exception avec Toolkit: " + e.getMessage());
          e.printStackTrace();
          return null;
        }
      }

      // Calcul du ratio d'aspect
      int originalWidth = originalImage.getWidth();
      int originalHeight = originalImage.getHeight();

      System.out.println("Dimensions originales: " + originalWidth + "x" + originalHeight);

      // Si l'image est déjà plus petite que les dimensions max, la retourner directement
      if (originalWidth <= THUMBNAIL_MAX_WIDTH && originalHeight <= THUMBNAIL_MAX_HEIGHT) {
        System.out.println("L'image est déjà petite, pas besoin de redimensionner");
        return new ImageIcon(originalImage);
      }

      // Calcul des nouvelles dimensions en gardant le ratio d'aspect
      double widthRatio = (double) THUMBNAIL_MAX_WIDTH / originalWidth;
      double heightRatio = (double) THUMBNAIL_MAX_HEIGHT / originalHeight;
      double ratio = Math.min(widthRatio, heightRatio);

      int newWidth = (int) (originalWidth * ratio);
      int newHeight = (int) (originalHeight * ratio);

      System.out.println("Dimensions redimensionnées: " + newWidth + "x" + newHeight);

      // Création de la vignette
      Image thumbnailImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
      ImageIcon icon = new ImageIcon(thumbnailImage);
      System.out.println("Miniature créée avec succès");
      return icon;

    } catch (Exception e) {
      System.err.println("Exception dans createThumbnail: " + e.getMessage());
      e.printStackTrace();

      // En cas d'échec, essayer de fournir une icône par défaut
      try {
        BufferedImage defaultImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics g = defaultImg.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 100, 100);
        g.setColor(Color.BLACK);
        g.drawString("Image non disponible", 10, 50);
        g.dispose();
        System.out.println("Icône par défaut créée");
        return new ImageIcon(defaultImg);
      } catch (Exception ex) {
        System.err.println("Même pas possible de créer une icône par défaut");
        return null;
      }
    }
  }

  /**
   * Méthode de diagnostic pour vérifier les problèmes avec les pièces jointes
   */
  public static void diagnoseAttachment(String attachmentPath) {
    System.out.println("========== DIAGNOSTIC DE PIÈCE JOINTE ==========");
    System.out.println("Chemin: " + attachmentPath);

    // Normaliser le chemin
    String normalizedPath = attachmentPath.replace('\\', '/');
    System.out.println("Chemin normalisé: " + normalizedPath);

    File file = new File(normalizedPath);
    System.out.println("Existe: " + file.exists());
    if (file.exists()) {
      System.out.println("Est un fichier: " + file.isFile());
      System.out.println("Peut lire: " + file.canRead());
      System.out.println("Taille: " + file.length() + " octets");
      System.out.println("Extension: " + getFileExtension(file));
      System.out.println("Est une image reconnue: " + isImageFile(file));

      try {
        BufferedImage image = ImageIO.read(file);
        if (image != null) {
          System.out.println("Image lue avec succès: " + image.getWidth() + "x" + image.getHeight());
        } else {
          System.out.println("ImageIO.read a retourné null");
        }
      } catch (Exception e) {
        System.out.println("Exception lors de la lecture: " + e.getMessage());
      }
    }
    System.out.println("================================================");
  }

  /**
   * Supprime les pièces jointes orphelines d'un message
   * @param message Message contenant les pièces jointes
   */
  public static void deleteAttachments(Message message) {
    if (message != null && message.hasAttachments()) {
      for (String attachmentPath : message.getAttachments()) {
        // Normaliser le chemin
        String normalizedPath = attachmentPath.replace('\\', '/');

        File attachmentFile = new File(normalizedPath);
        if (attachmentFile.exists() && attachmentFile.isFile()) {
          boolean deleted = attachmentFile.delete();
          if (!deleted) {
            System.err.println("Impossible de supprimer la pièce jointe: " + normalizedPath);
          }
        }
      }
    }
  }
}