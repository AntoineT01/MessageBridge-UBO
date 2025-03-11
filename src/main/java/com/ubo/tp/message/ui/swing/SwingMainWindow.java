package com.ubo.tp.message.ui.swing;

import com.ubo.tp.message.ui.IButton;
import com.ubo.tp.message.ui.IMainWindow;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;

public class SwingMainWindow implements IMainWindow {

  private final JFrame frame;
  private final JPanel contentPanel;

  public SwingMainWindow() {
    this.frame = new JFrame();
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.setSize(800, 600);

    // Panneau principal
    contentPanel = new JPanel(new FlowLayout());
    frame.setContentPane(contentPanel);
  }

  @Override
  public void setTitle(String title) {
    frame.setTitle(title);
  }

  @Override
  public void showWindow() {
    SwingUtilities.invokeLater(() -> frame.setVisible(true));
  }

  @Override
  public void closeWindow() {
    frame.dispose();
  }

  @Override
  public void addButton(IButton button) {
    // On caste en SwingButton pour récupérer le composant réel
    if (button instanceof SwingButton sb) {
      contentPanel.add(sb.getJButton());
    }
  }
}
