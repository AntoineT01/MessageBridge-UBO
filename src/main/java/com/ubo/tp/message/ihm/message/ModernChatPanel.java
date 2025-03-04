package com.ubo.tp.message.ihm.message;

import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Set;

public class ModernChatPanel extends JPanel {
  private final ChatPanel chatPanel;
  private final RoundedTextArea messageInputArea;
  private final JButton sendButton;
  private final com.ubo.tp.message.ihm.component.SearchBar searchBar;
  private final ISession session;

  public ModernChatPanel(ISession session,
                         ActionListener sendAction,
                         ActionListener searchAction) {
    this.session = session;
    setLayout(new BorderLayout());

    // --- Barre de recherche en haut avec le composant SearchBar ---
    searchBar = new com.ubo.tp.message.ihm.component.SearchBar("Recherche: ", 18, "Rechercher");
    searchBar.addSearchAction(searchAction);
    add(searchBar, BorderLayout.NORTH);

    // --- Zone centrale : ChatPanel ---
    chatPanel = new ChatPanel();
    add(chatPanel, BorderLayout.CENTER);

    // --- Panneau d’envoi en bas ---
    // On applique des marges externes autour de ce panneau
    JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
    bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Panneau intermédiaire pour la zone de texte (pour mieux contrôler l'espace)
    JPanel inputPanel = new JPanel(new BorderLayout());

    // Création de la zone de saisie de message
    messageInputArea = new RoundedTextArea(2, 30);
    messageInputArea.setLineWrap(true);
    messageInputArea.setWrapStyleWord(true);

    // On combine la bordure arrondie + une EmptyBorder pour des marges internes
    Border textAreaRounded = new RoundedBorder(15);
    Border textAreaEmpty   = new EmptyBorder(10, 10, 10, 10);
    messageInputArea.setBorder(new CompoundBorder(textAreaRounded, textAreaEmpty));

    // Encapsulation du RoundedTextArea dans un JScrollPane
    JScrollPane scrollPane = new JScrollPane(messageInputArea);
    // On peut retirer la bordure du scrollPane si souhaité
    scrollPane.setBorder(null);

    // On ajoute le scrollPane au panneau intermédiaire
    inputPanel.add(scrollPane, BorderLayout.CENTER);

    // Création du bouton "Envoyer"
    sendButton = new JButton("Envoyer");
    sendButton.setBackground(new Color(30, 144, 255));
    sendButton.setForeground(Color.WHITE);
    sendButton.setFocusPainted(false);
    sendButton.setOpaque(true);

    // Pour le bouton, on applique aussi une CompoundBorder pour l'arrondi + les marges internes
    Border buttonRounded = new RoundedBorder(15);
    Border buttonEmpty   = new EmptyBorder(10, 20, 10, 20);
    sendButton.setBorder(new CompoundBorder(buttonRounded, buttonEmpty));

    // On place inputPanel à gauche (CENTER) et le bouton à droite (EAST)
    bottomPanel.add(inputPanel, BorderLayout.CENTER);
    bottomPanel.add(sendButton, BorderLayout.EAST);

    add(bottomPanel, BorderLayout.SOUTH);

    // Brancher l'action d'envoi
    sendButton.addActionListener(sendAction);
  }

  // ------------------------------
  // Méthodes pour le contrôleur
  // ------------------------------

  /**
   * Renvoie le texte saisi dans la zone de message.
   */
  public String getMessageText() {
    return messageInputArea.getText();
  }

  /**
   * Vide la zone de saisie du message.
   */
  public void clearMessageInput() {
    messageInputArea.setText("");
  }

  public JButton getSendButton() {
    return sendButton;
  }

  /**
   * Renvoie la requête de recherche saisie par l'utilisateur.
   */
  public String getSearchQuery() {
    return searchBar.getSearchQuery();
  }

  /**
   * Vide le champ de recherche.
   */
  public void clearSearchQuery() {
    searchBar.clearSearch();
  }

  /**
   * Ajoute un message au fil de discussion.
   */
  public void addMessageToFeed(Message message) {
    User connectedUser = session.getConnectedUser();
    boolean isOutgoing = (connectedUser != null && connectedUser.equals(message.getSender()));

    String senderDisplayName = message.getSender().getName()
                               + " (@" + message.getSender().getUserTag() + ")";
    MessageBubble bubble = new MessageBubble(senderDisplayName, message.getText(), isOutgoing);
    chatPanel.addMessageBubble(bubble, isOutgoing);
  }

  /**
   * Met à jour l'affichage en ne montrant que les messages recherchés.
   */
  public void updateSearchResults(Set<Message> messages) {
    chatPanel.clearMessages();
    for (Message m : messages) {
      addMessageToFeed(m);
    }
  }
}
