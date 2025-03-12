package com.ubo.tp.message.components.message.autocompletion;

import javax.swing.text.JTextComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AutoCompletionController {
  private final JTextComponent textComponent;
  private final AutoCompletionModel model;
  private final AutoCompletionView view;
  private boolean isPopupVisible = false;
  private int triggerPosition = -1; // Position du '@'

  public AutoCompletionController(JTextComponent textComponent, AutoCompletionModel model, AutoCompletionView view) {
    this.textComponent = textComponent;
    this.model = model;
    this.view = view;
    init();
  }

  private void init() {
    // Écoute du document
    textComponent.getDocument().addDocumentListener(new DocumentListener() {
      @Override public void insertUpdate(DocumentEvent e) { handleDocumentChange(); }
      @Override public void removeUpdate(DocumentEvent e) { handleDocumentChange(); }
      @Override public void changedUpdate(DocumentEvent e) { handleDocumentChange(); }
    });

    // Gestion des touches
    textComponent.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (isPopupVisible) {
          if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            view.moveSelectionDown();
            e.consume();
          } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            view.moveSelectionUp();
            e.consume();
          } else if (e.getKeyCode() == KeyEvent.VK_TAB ||
                     (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown())) {
            acceptSuggestion();
            e.consume();
          } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            hideSuggestions();
            e.consume();
          }
        }
      }
    });

    model.addListener(suggestions -> {
      if (suggestions.isEmpty()) {
        hideSuggestions();
      } else {
        view.updateSuggestions(suggestions);
        showSuggestions();
      }
    });

    view.setSuggestionSelectedListener(suggestion -> acceptSuggestion());
  }

  private void handleDocumentChange() {
    String text = textComponent.getText();
    int caretPos = textComponent.getCaretPosition();
    // S'assurer que caretPos ne dépasse pas la longueur du texte
    caretPos = Math.min(caretPos, text.length());

    int atPos = text.lastIndexOf("@", caretPos - 1);
    if (atPos != -1 && (atPos == 0 || Character.isWhitespace(text.charAt(atPos - 1)))) {
      triggerPosition = atPos;
      // Si le caret est bien positionné après le '@'
      if (caretPos > atPos + 1 && (atPos + 1) <= text.length()) {
        String prefix = text.substring(atPos + 1, caretPos);
        model.updateSuggestions(prefix);
      } else {
        model.updateSuggestions("");
      }
      return;
    }
    hideSuggestions();
  }
  private void acceptSuggestion() {
    String suggestion = view.getSelectedSuggestion();
    if (suggestion != null && triggerPosition != -1) {
      try {
        String text = textComponent.getText();
        int caretPos = textComponent.getCaretPosition();
        String newText = text.substring(0, triggerPosition) + suggestion + " " + text.substring(caretPos);
        textComponent.setText(newText);
        textComponent.setCaretPosition(triggerPosition + 1 + suggestion.length() + 1);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    hideSuggestions();
  }

  private void showSuggestions() {
    if (!isPopupVisible) {
      try {
        int pos = textComponent.getCaretPosition();
        Rectangle caretCoords = textComponent.modelToView(pos);
        if (caretCoords != null) {
          view.show(textComponent, caretCoords.x, caretCoords.y + caretCoords.height);
          isPopupVisible = true;
        }
      } catch (javax.swing.text.BadLocationException ex) {
        hideSuggestions();
      }catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void hideSuggestions() {
    view.setVisible(false);
    isPopupVisible = false;
    triggerPosition = -1;
  }
}
