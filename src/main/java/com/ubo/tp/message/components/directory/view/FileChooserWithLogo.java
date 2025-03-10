package com.ubo.tp.message.components.directory.view;

import com.ubo.tp.message.common.utils.ImageUtils;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Image;
import java.io.File;

public class FileChooserWithLogo {
  private FileChooserWithLogo() {
  }

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

  public static class MyFileChooser extends JFileChooser {

    private final Image iconImage;

    public MyFileChooser(Image iconImage) {
      super();
      this.iconImage = iconImage;
    }

    @Override
    protected JDialog createDialog(Component parent) throws HeadlessException {
      // On laisse JFileChooser créer le JDialog
      JDialog dialog = super.createDialog(parent);
      // Puis on applique l’icône souhaitée
      dialog.setIconImage(iconImage);
      return dialog;
    }
  }
}


