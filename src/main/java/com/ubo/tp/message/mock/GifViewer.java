package com.ubo.tp.message.mock;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class GifViewer extends JFrame {
  public GifViewer() {
    setTitle("Affichage d'un GIF en Swing");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Chargement du GIF (vérifiez que le chemin est correct)
    ImageIcon gifIcon = new ImageIcon("giphy.gif");

    // Création d'un JLabel pour afficher le GIF
    JLabel label = new JLabel(gifIcon);

    // Ajout du JLabel à la fenêtre
    add(label);

    setVisible(true);
  }

  public static void main(String[] args) {
    // Lancement de l'interface graphique dans le thread d'événements
    SwingUtilities.invokeLater(GifViewer::new);
  }
}
