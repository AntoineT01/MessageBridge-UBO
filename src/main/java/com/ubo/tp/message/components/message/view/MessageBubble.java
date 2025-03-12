package com.ubo.tp.message.components.message.view;

import com.ubo.tp.message.common.utils.AttachmentUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Composant de bulle de message amélioré avec support des pièces jointes
 */
public class MessageBubble extends JPanel {
  private final String senderName;
  private final String messageText;
  private final boolean isOutgoing;
  private final String fullDateString;
  private final List<String> attachments;
  private static final int MAX_WIDTH = 250;
  private static final int IMAGE_PREVIEW_HEIGHT = 150;
  private final List<JLabel> imageLabels = new ArrayList<>();

  public MessageBubble(String senderName, String messageText, boolean isOutgoing, String fullDateString) {
    this(senderName, messageText, isOutgoing, fullDateString, new ArrayList<>());
  }

  public MessageBubble(String senderName, String messageText, boolean isOutgoing, String fullDateString, List<String> attachments) {
    this.senderName = senderName;
    this.messageText = messageText;
    this.isOutgoing = isOutgoing;
    this.fullDateString = fullDateString;

    // Normaliser les chemins des pièces jointes
    this.attachments = new ArrayList<>();
    if (attachments != null) {
      for (String attachment : attachments) {
        if (attachment != null) {
          // Remplacer les backslashes par des forward slashes
          this.attachments.add(attachment.replace('\\', '/'));
        }
      }
    }

    setOpaque(false);
    setLayout(new BorderLayout(5, 5));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    // Si nous avons des pièces jointes, créez des vignettes
    if (!this.attachments.isEmpty()) {
      createAttachmentThumbnails();
    }
  }

  private void createAttachmentThumbnails() {
    System.out.println("Création des miniatures pour " + attachments.size() + " pièce(s) jointe(s)");

    JPanel imagesPanel = new JPanel();
    imagesPanel.setOpaque(false);
    imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));

    for (String attachment : attachments) {
      System.out.println("Traitement de la pièce jointe: " + attachment);

      // Assurez-vous que le chemin est correctement formaté
      String normalizedPath = attachment.replace('\\', '/');
      System.out.println("Chemin normalisé: " + normalizedPath);

      File file = new File(normalizedPath);
      System.out.println("Le fichier existe: " + file.exists());

      if (file.exists() && AttachmentUtils.isImageFile(file)) {
        System.out.println("Tentative de création de la miniature...");
        ImageIcon thumbnail = AttachmentUtils.createThumbnail(normalizedPath);

        if (thumbnail != null) {
          System.out.println("Miniature créée avec succès, dimensions: "
                               + thumbnail.getIconWidth() + "x" + thumbnail.getIconHeight());

          JLabel imageLabel = new JLabel(thumbnail);
          imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
          imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

          // Ajouter un écouteur de clic pour ouvrir l'image complète
          final String imagePath = normalizedPath;
          imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              openImage(imagePath);
            }
          });

          imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

          imagesPanel.add(imageLabel);
          imageLabels.add(imageLabel);
          System.out.println("Miniature ajoutée au panel");
        } else {
          System.err.println("Impossible de créer la miniature pour: " + normalizedPath);
        }
      } else {
        System.err.println("Fichier non trouvé ou non reconnu comme image: " + normalizedPath);
      }
    }

    if (!imageLabels.isEmpty()) {
      System.out.println("Ajout du panel d'images avec " + imageLabels.size() + " miniature(s)");
      add(imagesPanel, BorderLayout.SOUTH);
      revalidate();
      repaint();
    } else {
      System.out.println("Aucune miniature à afficher");
    }
  }

  private void openImage(String imagePath) {
    try {
      // Normaliser le chemin
      String normalizedPath = imagePath.replace('\\', '/');

      File imageFile = new File(normalizedPath);
      if (imageFile.exists()) {
        Desktop.getDesktop().open(imageFile);
      }
    } catch (Exception e) {
      System.err.println("Erreur lors de l'ouverture de l'image: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Décompose le texte en lignes pour s'adapter à la largeur maximale
   */
  private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
    List<String> lines = new ArrayList<>();

    // Normaliser les retours à la ligne
    String normalizedText = text.replace("\\n", "\n");

    // Séparer par paragraphes
    String[] paragraphs = normalizedText.split("\n");
    for (int p = 0; p < paragraphs.length; p++) {
      String paragraph = paragraphs[p];

      if (paragraph.isEmpty() && p == paragraphs.length - 1) {
        continue;
      }
      if (paragraph.isEmpty()) {
        lines.add("");
        continue;
      }

      // Découper le paragraphe en mots
      String[] words = paragraph.split(" ");
      StringBuilder currentLine = new StringBuilder();
      for (String word : words) {

        // Si le mot est trop long, le couper
        if (fm.stringWidth(word) > maxWidth) {
          if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
            currentLine = new StringBuilder();
          }
          StringBuilder chunk = new StringBuilder();
          for (char c : word.toCharArray()) {
            if (fm.stringWidth(chunk.toString() + c) <= maxWidth) {
              chunk.append(c);
            } else {
              lines.add(chunk.toString());
              chunk = new StringBuilder();
              chunk.append(c);
            }
          }
          if (!chunk.isEmpty()) {
            lines.add(chunk.toString());
          }
        } else {
          if (currentLine.isEmpty()) {
            currentLine.append(word);
          } else {
            String testLine = currentLine + " " + word;
            if (fm.stringWidth(testLine) <= maxWidth) {
              currentLine.append(" ").append(word);
            } else {
              lines.add(currentLine.toString());
              currentLine = new StringBuilder(word);
            }
          }
        }
      }
      if (!currentLine.isEmpty()) {
        lines.add(currentLine.toString());
      }
    }
    return lines;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();
    int arc = 15;

    // Dessin de la bulle
    Color bubbleColor = isOutgoing ? new Color(220, 248, 198) : Color.WHITE;
    g2.setColor(bubbleColor);
    g2.fillRoundRect(0, 0, width, height, arc, arc);
    g2.setColor(Color.LIGHT_GRAY);
    g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

    int x = 8;
    int y = 5;

    // Affichage du nom de l'expéditeur
    Font originalFont = g2.getFont();
    Font nameFont = originalFont.deriveFont(Font.PLAIN, 11f);
    g2.setFont(nameFont);
    FontMetrics fmName = g2.getFontMetrics();
    g2.setColor(Color.DARK_GRAY);
    g2.drawString(senderName, x, y + fmName.getAscent());

    // Affichage de la date
    int dateWidth = fmName.stringWidth(fullDateString);
    g2.drawString(fullDateString, width - dateWidth - 8, y + fmName.getAscent());

    y += fmName.getHeight() + 2;

    // Affichage du texte du message
    Font messageFont = originalFont.deriveFont(Font.PLAIN, 13f);
    g2.setFont(messageFont);
    FontMetrics fmMessage = g2.getFontMetrics();
    List<String> lines = wrapText(messageText, fmMessage, MAX_WIDTH);
    g2.setColor(Color.BLACK);
    for (String line : lines) {
      g2.drawString(line, x, y + fmMessage.getAscent());
      y += fmMessage.getHeight();
    }

    g2.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    Font baseFont = getFont();
    Font nameFont = baseFont.deriveFont(Font.PLAIN, 11f);
    Font messageFont = baseFont.deriveFont(Font.PLAIN, 13f);
    FontMetrics fmName = getFontMetrics(nameFont);
    FontMetrics fmMessage = getFontMetrics(messageFont);

    int nameWidth = fmName.stringWidth(senderName);
    int dateWidth = fmName.stringWidth(fullDateString);

    int headerWidth = nameWidth + 10 + dateWidth;

    List<String> lines = wrapText(messageText, fmMessage, MAX_WIDTH);
    int textWidth = 0;
    for (String line : lines) {
      textWidth = Math.max(textWidth, fmMessage.stringWidth(line));
    }
    int contentWidth = Math.max(headerWidth, textWidth);

    // Calcul de la hauteur totale
    int totalHeight = fmName.getHeight() + 2 + (lines.size() * fmMessage.getHeight()) + 10;

    // Ajout de l'espace pour les images
    if (!imageLabels.isEmpty()) {
      for (JLabel imageLabel : imageLabels) {
        if (imageLabel.getIcon() != null) {
          contentWidth = Math.max(contentWidth, imageLabel.getIcon().getIconWidth());
          totalHeight += imageLabel.getIcon().getIconHeight() + 10;
        }
      }
    }

    return new Dimension(contentWidth + 20, totalHeight);
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }
}