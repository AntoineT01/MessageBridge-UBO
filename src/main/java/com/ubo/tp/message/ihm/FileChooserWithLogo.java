package com.ubo.tp.message.ihm;

import com.ubo.tp.message.common.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileChooserWithLogo {

  public static File showDirectoryChooser(Component parent) {

    // Créer un panneau accessoire affichant le logo
    JPanel accessoryPanel = new JPanel();
    accessoryPanel.setLayout(new BorderLayout());
    // Charger l'icône à une taille adaptée (ex : 100x100)
    ImageIcon logoIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 100, 100);
    MyFileChooser chooser;
    if (logoIcon == null) {
      throw new IllegalStateException("Impossible de charger l'icône du JFileChooser");
    }
    chooser = new MyFileChooser(logoIcon.getImage());
    chooser.setDialogTitle("Choisir le répertoire d'échange");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    chooser.setAccessory(accessoryPanel);

    int result = chooser.showOpenDialog(parent);
    if (result == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    }
    return null;
  }
}
