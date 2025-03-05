package com.ubo.tp.message.common.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

public class SearchBar extends JPanel {
  private final RoundedTextArea searchArea;
  private final JButton searchButton;
  private Timer dynamicSearchTimer; // Timer pour le debounce

  /**
   * Constructeur de la SearchBar.
   * @param labelText Texte du label placé devant le champ (peut être null ou vide)
   * @param columns Nombre de colonnes pour le RoundedTextArea
   * @param buttonText Texte affiché sur le bouton
   */
  public SearchBar(String labelText, int columns, String buttonText) {
    setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

    if (labelText != null && !labelText.isEmpty()) {
      JLabel label = new JLabel(labelText);
      add(label);
    }

    // Utilisation d'un RoundedTextArea sur une seule ligne
    searchArea = new RoundedTextArea(1, columns);
    add(searchArea);

    searchButton = new JButton(buttonText);
    searchButton.setBorder(new RoundedBorder(15));
    searchButton.setFocusPainted(false);
    add(searchButton);
  }

  /**
   * Permet d’injecter une ActionListener pour le bouton de recherche.
   * @param actionListener l'action à déclencher lors du clic sur le bouton
   */
  public void addSearchAction(ActionListener actionListener) {
    searchButton.addActionListener(actionListener);
  }

  /**
   * Renvoie le texte saisi dans le champ de recherche.
   * @return la chaîne de caractères entrée par l'utilisateur
   */
  public String getSearchQuery() {
    return searchArea.getText();
  }

  /**
   * Vide le champ de recherche.
   */
  public void clearSearch() {
    searchArea.setText("");
  }
}
