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
 * Bulle de message affichant en haut le nom (en petite police) et, à droite,
 * la date complète d'envoi, puis le texte du message avec wrapping automatique.
 */
public class MessageBubble extends JPanel {
  private final String senderName;    // ex: "Alice (@Alice)"
  private final String messageText;
  private final boolean isOutgoing;   // true si c’est l’utilisateur connecté
  private final String fullDateString;    // Date complète formatée (ex: "27/03/2025 14:35:12")
  private static final int MAX_WIDTH = 250; // largeur maximale pour le texte

  public MessageBubble(String senderName, String messageText, boolean isOutgoing, String fullDateString) {
    this.senderName = senderName;
    this.messageText = messageText;
    this.isOutgoing = isOutgoing;
    this.fullDateString = fullDateString;
    setOpaque(false);
    // Marges internes réduites
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }

  /**
   * Découpe le texte en lignes dont la largeur ne dépasse pas maxWidth,
   * en tenant compte des retours à la ligne (s'ils sont présents dans le texte).
   * Le dernier paragraphe vide est ignoré.
   */
  private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
    List<String> lines = new ArrayList<>();
    // Remplacer les séquences "\n" par de vrais sauts de ligne (si elles apparaissent)
    String normalizedText = text.replace("\\n", "\n");
    // Séparer par saut de ligne
    String[] paragraphs = normalizedText.split("\n");
    for (int p = 0; p < paragraphs.length; p++) {
      String paragraph = paragraphs[p];
      // Ignorer le dernier paragraphe s'il est vide
      if (paragraph.isEmpty() && p == paragraphs.length - 1) {
        continue;
      }
      if (paragraph.isEmpty()) {
        lines.add("");
        continue;
      }
      // Pour chaque paragraphe, découper en mots et effectuer le wrapping
      String[] words = paragraph.split(" ");
      StringBuilder currentLine = new StringBuilder();
      for (String word : words) {
        // Si le mot dépasse la largeur maximale, le découper
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
    // Préparer Graphics2D
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();
    int arc = 15;

    // Couleur de fond en fonction de l'expéditeur
    Color bubbleColor = isOutgoing ? new Color(220, 248, 198) : Color.WHITE;
    g2.setColor(bubbleColor);
    g2.fillRoundRect(0, 0, width, height, arc, arc);
    g2.setColor(Color.LIGHT_GRAY);
    g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

    int x = 8;
    int y = 5;

    // Dessiner le nom de l'expéditeur (petite police)
    Font originalFont = g2.getFont();
    Font nameFont = originalFont.deriveFont(Font.PLAIN, 11f);
    g2.setFont(nameFont);
    FontMetrics fmName = g2.getFontMetrics();
    g2.setColor(Color.DARK_GRAY);
    g2.drawString(senderName, x, y + fmName.getAscent());

    // Dessiner la date complète à droite sur la même ligne que le nom
    int dateWidth = fmName.stringWidth(fullDateString);
    g2.drawString(fullDateString, width - dateWidth - 8, y + fmName.getAscent());

    y += fmName.getHeight() + 2; // espace après le header

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
    int dateWidth = fmName.stringWidth(fullDateString);
    // Largeur suffisante pour le nom + la date complète
    int headerWidth = nameWidth + 10 + dateWidth;

    List<String> lines = wrapText(messageText, fmMessage, MAX_WIDTH);
    int textWidth = 0;
    for (String line : lines) {
      textWidth = Math.max(textWidth, fmMessage.stringWidth(line));
    }
    int contentWidth = Math.max(headerWidth, textWidth);
    int totalHeight = fmName.getHeight() + 2 + (lines.size() * fmMessage.getHeight()) + 10;
    return new Dimension(contentWidth + 20, totalHeight);
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }
}
