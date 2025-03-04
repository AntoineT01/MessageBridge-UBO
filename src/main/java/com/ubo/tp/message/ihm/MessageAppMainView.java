package com.ubo.tp.message.ihm;

import javax.swing.*;
import java.awt.*;

import com.ubo.tp.message.common.IconFactory;
import com.ubo.tp.message.common.ImageUtils;
import com.ubo.tp.message.core.database.IDatabase;

public class MessageAppMainView extends JFrame {

  private final IDatabase database;
  // on peut stocker d’autres choses si besoin, comme l’EntityManager, etc.

  public MessageAppMainView(IDatabase database) {
    this.database = database;
    initGUI();
  }

  private void initGUI() {
    // Définition du titre et de l’icône de la fenêtre
    this.setTitle("MessageApp");
    // Pour le logo : on peut charger une icône si on a un .png dans les ressources
    // setIconImage(Toolkit.getDefaultToolkit().getImage("resources/ubo.png"));

    // On fixe une taille ou on pack() plus tard
    this.setSize(600, 400);

    ImageIcon windowIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 32, 32);
    if(windowIcon != null){
      this.setIconImage(windowIcon.getImage());
    }

    // --- Barre de menu ---
    JMenuBar menuBar = new JMenuBar();

    // 1) Menu Fichier
    JMenu menuFichier = new JMenu("Fichier");

    // Ajout d'un item pour changer le répertoire d'échange
    JMenuItem itemChangeDir = new JMenuItem("Changer le répertoire d'échange");
    itemChangeDir.setToolTipText("Sélectionner un nouveau répertoire d'échange pour les messages");
    itemChangeDir.addActionListener(e -> {
      // L'action sera définie plus tard, car nous avons besoin d'une référence à MessageAppGUI
    });
    menuFichier.add(itemChangeDir);
    menuFichier.addSeparator(); // Ajouter un séparateur avant "Quitter"

    JMenuItem itemQuitter = new JMenuItem("Quitter");
    // Optionnel : associer une icône
    // itemQuitter.setIcon(new ImageIcon("resources/exit.png"));

    itemQuitter.setIcon(IconFactory.createCloseIcon(IconFactory.ICON_SMALL));

    itemQuitter.setToolTipText("Fermer l'application");


    itemQuitter.addActionListener(e -> {
      // Action de quitter
      System.exit(0);
    });
    menuFichier.add(itemQuitter);




    menuBar.add(menuFichier);

    // 2) Menu A propos
    JMenu menuAPropos = new JMenu("?");
    JMenuItem itemAbout = new JMenuItem("A propos");
    itemAbout.setIcon(IconFactory.createInfoIcon(IconFactory.ICON_SMALL));
    itemAbout.setToolTipText("Informations sur l'application");
    itemAbout.addActionListener(e -> {
      // Action : ouvrir une boite de dialogue
      showAboutDialog();
    });
    menuAPropos.add(itemAbout);
    menuBar.add(menuAPropos);


    menuFichier.setToolTipText("Opérations sur les fichiers");
    menuAPropos.setToolTipText("Aide et informations");
    // On ajoute la barre de menu à la fenêtre
    this.setJMenuBar(menuBar);

    // Par défaut, on ferme la fenêtre quand on clique sur la croix
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Layout par défaut
    this.setLayout(new BorderLayout());

    // On peut placer un bouton ou du contenu au centre
    JLabel label = new JLabel("Contenu principal de l'application");
    label.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(label, BorderLayout.CENTER);

    // On pack, ou on laisse le setSize(…)
    this.setLocationRelativeTo(null); // centre sur l’écran
  }

  private void showAboutDialog() {
    // Boîte de dialogue “A propos"
    // Par exemple un simple JOptionPane :
    ImageIcon logoIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 100, 100);
    JOptionPane.showMessageDialog(
    this,
      "UBO M2-TIIL\nDépartement Informatique",
      "A propos",
      JOptionPane.INFORMATION_MESSAGE,
      logoIcon
    );
  }
}
