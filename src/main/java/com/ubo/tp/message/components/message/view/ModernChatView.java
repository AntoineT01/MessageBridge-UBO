package com.ubo.tp.message.components.message.view;

import com.ubo.tp.message.common.ui.RoundedBorder;
import com.ubo.tp.message.common.ui.RoundedTextArea;
import com.ubo.tp.message.common.ui.SearchBar;
import com.ubo.tp.message.common.utils.AttachmentUtils;
import com.ubo.tp.message.components.message.autocompletion.*;
import com.ubo.tp.message.components.message.model.MessageModel;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.Message;
import com.ubo.tp.message.core.datamodel.User;
import com.ubo.tp.message.core.session.ISession;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModernChatView extends JPanel implements IChatView {

  private final ChatPanel chatPanel;
  private final RoundedTextArea messageInputArea;
  private final SearchBar searchBar;
  private final ISession session;
  private final JButton sendButton;
  private final JButton attachButton;  // Nouveau bouton pour joindre des images

  private MessageModel model;
  private List<String> currentAttachments = new ArrayList<>();  // Liste des pièces jointes pour le message courant

  // Contrôleur de l'autocomplétion
  private AutoCompletionController autoCompletionController;

  public ModernChatView(ISession session, ActionListener sendAction, ActionListener searchAction, IDatabase database) {
    this.session = session;
    setLayout(new BorderLayout());

    searchBar = new SearchBar("Recherche: ", 18, "Rechercher");
    if (searchAction != null) {
      searchBar.addSearchAction(searchAction);
    }
    add(searchBar, BorderLayout.NORTH);

    chatPanel = new ChatPanel();
    add(chatPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
    bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel inputPanel = new JPanel(new BorderLayout());
    messageInputArea = new RoundedTextArea(2, 30);
    messageInputArea.setLineWrap(true);
    messageInputArea.setWrapStyleWord(true);

    CompoundBorder textAreaBorder = new CompoundBorder(new RoundedBorder(15), new EmptyBorder(10, 10, 10, 10));
    messageInputArea.setBorder(textAreaBorder);

    JScrollPane scrollPane = new JScrollPane(messageInputArea);
    scrollPane.setBorder(null);
    inputPanel.add(scrollPane, BorderLayout.CENTER);

    // Création du panel pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

    // Bouton pour joindre des images
    attachButton = new JButton("Joindre");
    attachButton.setBackground(new Color(240, 240, 240));
    attachButton.setForeground(Color.BLACK);
    attachButton.setFocusPainted(false);
    attachButton.setOpaque(true);
    CompoundBorder attachButtonBorder = new CompoundBorder(new RoundedBorder(15), new EmptyBorder(10, 15, 10, 15));
    attachButton.setBorder(attachButtonBorder);
    attachButton.addActionListener(e -> selectAttachments());
    buttonPanel.add(attachButton);

    // Bouton d'envoi
    sendButton = new JButton("Envoyer");
    sendButton.setBackground(new Color(30, 144, 255));
    sendButton.setForeground(Color.WHITE);
    sendButton.setFocusPainted(false);
    sendButton.setOpaque(true);
    CompoundBorder buttonBorder = new CompoundBorder(new RoundedBorder(15), new EmptyBorder(10, 20, 10, 20));
    sendButton.setBorder(buttonBorder);
    buttonPanel.add(sendButton);

    bottomPanel.add(inputPanel, BorderLayout.CENTER);
    bottomPanel.add(buttonPanel, BorderLayout.EAST);
    add(bottomPanel, BorderLayout.SOUTH);

    if (sendAction != null) {
      sendButton.addActionListener(sendAction);
    }

    // Configuration de l'autocomplétion
    SuggestionProvider suggestionProvider = new UserSuggestionProviderFX(database);
    AutoCompletionModel autoCompletionModel = new AutoCompletionModel(suggestionProvider);
    AutoCompletionView autoCompletionView = new AutoCompletionView();
    autoCompletionController = new AutoCompletionController(messageInputArea, autoCompletionModel, autoCompletionView);

    // Si nous avons des pièces jointes, afficher un label informatif
    updateAttachmentStatus();
  }

  /**
   * Méthode pour sélectionner des pièces jointes
   */
  private void selectAttachments() {
    List<String> selectedAttachments = AttachmentUtils.selectImageAttachments(this);
    if (selectedAttachments != null && !selectedAttachments.isEmpty()) {
      currentAttachments.addAll(selectedAttachments);
      updateAttachmentStatus();
    }
  }

  /**
   * Met à jour l'affichage du statut des pièces jointes
   */
  private void updateAttachmentStatus() {
    attachButton.setText(currentAttachments.isEmpty() ? "Joindre" : "Joindre (" + currentAttachments.size() + ")");

    // On pourrait ajouter un label ou un indicateur visuel supplémentaire
    if (!currentAttachments.isEmpty()) {
      attachButton.setBackground(new Color(255, 215, 0)); // Jaune doré pour indiquer qu'il y a des pièces jointes
    } else {
      attachButton.setBackground(new Color(240, 240, 240)); // Gris par défaut
    }
  }

  public JButton getSendButton() {
    return sendButton;
  }

  public SearchBar getSearchBar() {
    return searchBar;
  }

  @Override
  public String getMessageText() {
    return messageInputArea.getText();
  }

  @Override
  public void clearMessageInput() {
    messageInputArea.setText("");
    currentAttachments.clear();
    updateAttachmentStatus();
  }

  @Override
  public String getSearchQuery() {
    return searchBar.getSearchQuery();
  }

  @Override
  public void addMessageToFeed(Message message) {
    User connectedUser = session.getConnectedUser();
    boolean isOutgoing = (connectedUser != null && connectedUser.equals(message.getSender()));
    String senderDisplayName = message.getSender().getName() + " (" + message.getSender().getUserTag() + ")";
    String timeString = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(message.getEmissionDate()));

    // Log pour déboguer
    System.out.println("Ajout d'un message au fil: " + message.getText());
    System.out.println("Message a des pièces jointes: " + message.hasAttachments());
    if (message.hasAttachments()) {
      System.out.println("Nombre de pièces jointes: " + message.getAttachments().size());
      for (String attachment : message.getAttachments()) {
        System.out.println("  - Pièce jointe: " + attachment);
        // Vérifier si le fichier existe
        File file = new File(attachment);
        System.out.println("  - Le fichier existe: " + file.exists());
      }
    }

    // Création de la bulle avec les éventuelles pièces jointes
    MessageBubble bubble = new MessageBubble(senderDisplayName, message.getText(), isOutgoing, timeString, message.getAttachments());
    chatPanel.addMessageBubble(bubble, isOutgoing);
  }

  @Override
  public void updateSearchResults(List<Message> messages) {
    chatPanel.clearMessages();
    for (Message m : messages) {
      addMessageToFeed(m);
    }
  }

  @Override
  public void notifyMessageAdded() {
    SwingUtilities.invokeLater(() -> {
      chatPanel.clearMessages();
      for (Message m : model.getMessages()) {
        addMessageToFeed(m);
      }
    });
  }

  @Override
  public void notifyMessageRemoved() {
    SwingUtilities.invokeLater(() -> {
      chatPanel.clearMessages();
      for (Message m : model.getMessages()) {
        addMessageToFeed(m);
      }
    });
  }

  @Override
  public void setModel(MessageModel model) {
    this.model = model;
    model.addObserver(this);
  }

  @Override
  public void clearMessages() {
    chatPanel.clearMessages();
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    searchBar.setEnabled(enabled);
    messageInputArea.setEnabled(enabled);
    chatPanel.setEnabled(enabled);
    sendButton.setEnabled(enabled);
    attachButton.setEnabled(enabled);
  }

  @Override
  public Component getComponent() {
    return this;
  }

  /**
   * Récupère la liste des pièces jointes du message en cours de composition
   * @return Liste des chemins des pièces jointes
   */
  public List<String> getCurrentAttachments() {
    return new ArrayList<>(currentAttachments);
  }
}