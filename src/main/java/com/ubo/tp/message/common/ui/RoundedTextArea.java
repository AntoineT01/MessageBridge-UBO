package com.ubo.tp.message.common.ui;

import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
  /**
   * Constructeur pour créer un RoundedTextArea avec un nombre de lignes et de colonnes spécifié.
   * @param rows nombre de lignes
   * @param columns nombre de colonnes
   */
  public RoundedTextArea(int rows, int columns) {
    super(rows, columns);
    setBorder(new RoundedBorder(15));  // On suppose l'existence de RoundedBorder

    setLineWrap(true);
    setWrapStyleWord(true);
  }
}
