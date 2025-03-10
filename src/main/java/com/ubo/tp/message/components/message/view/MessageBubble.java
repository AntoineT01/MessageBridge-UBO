package com.ubo.tp.message.components.message.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

/**
 * Bulle de message affichant en haut le nom (en petite police) et,
 * en dessous, le message dont le texte est automatiquement wrapé si
 * la largeur dépasse une valeur maximale.
 */
public class MessageBubble extends JPanel {
  private final String senderName;    // ex: "Alice (@Alice)"
  private final String messageText;
  private final boolean isOutgoing;   // true si c’est l’utilisateur connecté
  private static final int MAX_WIDTH = 250; // largeur maximale pour le texte

  public MessageBubble(String senderName, String messageText, boolean isOutgoing) {
    this.senderName = senderName;
    this.messageText = messageText;
    this.isOutgoing = isOutgoing;
    setOpaque(false);
    // Marges internes réduites
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }

  /**
   * Découpe le texte en lignes dont la largeur ne dépasse pas maxWidth.
   * Gère également le cas d’un mot unique très long en le découpant.
   */
  private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
    List<String> lines = new ArrayList<>();
    String[] words = text.split(" ");
    StringBuilder currentLine = new StringBuilder();
    for (String word : words) {
      // Si le mot lui-même dépasse maxWidth, le découper
      if (fm.stringWidth(word) > maxWidth) {
        // Si currentLine contient déjà du texte, l'ajouter et réinitialiser
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
        // Ajoute la dernière partie du mot
        if (!chunk.isEmpty()) {
          lines.add(chunk.toString());
        }
      } else {
        // Pour un mot normal, essayer de l'ajouter à la ligne courante
        if (currentLine.length() == 0) {
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
    return lines;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // Préparer Graphics2D
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();
    int arc = 15;

    // Choisir la couleur de fond selon l’expéditeur
    Color bubbleColor = isOutgoing ? new Color(220, 248, 198) : Color.WHITE;
    g2.setColor(bubbleColor);
    g2.fillRoundRect(0, 0, width, height, arc, arc);
    g2.setColor(Color.LIGHT_GRAY);
    g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

    int x = 8;
    int y = 5;

    // Dessiner le nom de l’expéditeur (petite police)
    Font originalFont = g2.getFont();
    Font nameFont = originalFont.deriveFont(Font.PLAIN, 11f);
    g2.setFont(nameFont);
    FontMetrics fmName = g2.getFontMetrics();
    g2.setColor(Color.DARK_GRAY);
    g2.drawString(senderName, x, y + fmName.getAscent());
    y += fmName.getHeight() + 2; // Petit espace après le nom

    // Dessiner le texte du message avec wrapping
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
    List<String> lines = wrapText(messageText, fmMessage, MAX_WIDTH);
    int textWidth = 0;
    for (String line : lines) {
      textWidth = Math.max(textWidth, fmMessage.stringWidth(line));
    }
    int contentWidth = Math.max(nameWidth, textWidth);
    int totalHeight = fmName.getHeight() + 2 + (lines.size() * fmMessage.getHeight()) + 10;
    return new Dimension(contentWidth + 20, totalHeight);
  }

  @Override
  public Dimension getMaximumSize() {
    // Pour éviter que la bulle ne s'étire au-delà de sa taille calculée
    return getPreferredSize();
  }
}
