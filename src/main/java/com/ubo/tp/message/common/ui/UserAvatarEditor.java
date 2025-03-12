package com.ubo.tp.message.common.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class UserAvatarEditor extends JPanel {
  private final JLabel avatarLabel;
  private final JButton changeButton;
  private final int size;
  private String avatarPath;

  public UserAvatarEditor(int size) {
    this.size = size;
    setLayout(new BorderLayout());
    avatarLabel = new JLabel();
    avatarLabel.setPreferredSize(new Dimension(size, size));
    changeButton = new JButton("Choisir avatar");
    SwingTheme.styleButton(changeButton, true);
    add(avatarLabel, BorderLayout.CENTER);
    add(changeButton, BorderLayout.SOUTH);
    setAvatarPath(null);
    changeButton.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "jpeg"));
      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();
        avatarPath = file.getAbsolutePath();
        updateAvatar();
        if (avatarChangeListener != null) {
          avatarChangeListener.actionPerformed(null);
        }
      }
    });
  }

  public void setAvatarPath(String path) {
    this.avatarPath = path;
    updateAvatar();
  }

  public String getAvatarPath() {
    return avatarPath;
  }

  private void updateAvatar() {
    Icon icon = null;
    if (avatarPath != null && !avatarPath.isEmpty()) {
      File file = new File(avatarPath);
      if (file.exists()) {
        icon = new ImageIcon(new ImageIcon(avatarPath).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
      }
    }
    if (icon == null) {
      icon = IconFactory.createUserIcon(size);
    }
    avatarLabel.setIcon(icon);
  }

  private ActionListener avatarChangeListener;

  public void setAvatarChangeListener(ActionListener listener) {
    this.avatarChangeListener = listener;
  }
}
