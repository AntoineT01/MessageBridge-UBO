package com.ubo.tp.message.ihm.message;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
  private final JPanel messagesContainer;
  private final JScrollPane scrollPane;

  public ChatPanel() {
    setLayout(new BorderLayout());

    messagesContainer = new JPanel();
    messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
    messagesContainer.setBackground(new Color(245, 245, 245));
    // Une petite marge autour
    messagesContainer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    scrollPane = new JScrollPane(messagesContainer);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    // Masquer la barre horizontale
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    add(scrollPane, BorderLayout.CENTER);
  }

  public void addMessageBubble(MessageBubble bubble, boolean isOutgoing) {
    // Aligner à gauche ou à droite
    FlowLayout flow = new FlowLayout(isOutgoing ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0);
    JPanel wrapper = new JPanel(flow);
    wrapper.setOpaque(false);
    wrapper.add(bubble);

    // Pour éviter que le wrapper ne s'étende sur toute la hauteur
    wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE,
      bubble.getPreferredSize().height + 10));

    // On ajoute le wrapper
    messagesContainer.add(wrapper);
    // Petit espace vertical après chaque message
    messagesContainer.add(Box.createVerticalStrut(5));

    messagesContainer.revalidate();
    messagesContainer.repaint();

    // Défilement auto vers le bas
    SwingUtilities.invokeLater(() -> {
      JScrollBar vertical = scrollPane.getVerticalScrollBar();
      vertical.setValue(vertical.getMaximum());
    });
  }

  public void clearMessages() {
    messagesContainer.removeAll();
    messagesContainer.revalidate();
    messagesContainer.repaint();
  }
}
