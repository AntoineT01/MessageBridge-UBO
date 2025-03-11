package com.ubo.tp.message.ui.javafx;

import com.ubo.tp.message.ui.IButton;
import javafx.scene.control.Button;

public class FxButton implements IButton {

  private final Button fxButton;

  public FxButton() {
    this.fxButton = new Button();
  }

  @Override
  public void setText(String text) {
    fxButton.setText(text);
  }

  @Override
  public void setEnabled(boolean enabled) {
    fxButton.setDisable(!enabled);
  }

  @Override
  public void setOnAction(Runnable action) {
    // On retire l'éventuel ancien EventHandler
    fxButton.setOnAction(null);
    // On rattache l’action
    fxButton.setOnAction(evt -> action.run());
  }

  // Méthode d’accès au Button JavaFX réel
  public Button getFxButton() {
    return fxButton;
  }
}
