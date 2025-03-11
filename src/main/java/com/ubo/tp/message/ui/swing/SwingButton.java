package com.ubo.tp.message.ui.swing;

import com.ubo.tp.message.ui.IButton;

import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingButton implements IButton {

  private final JButton swingButton;

  public SwingButton() {
    this.swingButton = new JButton();
  }

  @Override
  public void setText(String text) {
    swingButton.setText(text);
  }

  @Override
  public void setEnabled(boolean enabled) {
    swingButton.setEnabled(enabled);
  }

  @Override
  public void setOnAction(Runnable action) {
    // On retire tous les anciens ActionListeners si on veut un comportement exclusif
    for (ActionListener al : swingButton.getActionListeners()) {
      swingButton.removeActionListener(al);
    }
    // On rattache l’action en la convertissant en ActionListener
    swingButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        action.run();
      }
    });
  }


  public Component getJButton() {
    return this.swingButton;
  }
}
