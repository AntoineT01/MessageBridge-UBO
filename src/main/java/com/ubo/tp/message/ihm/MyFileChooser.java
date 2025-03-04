package com.ubo.tp.message.ihm;

import javax.swing.*;
import java.awt.*;

public class MyFileChooser extends JFileChooser {

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
