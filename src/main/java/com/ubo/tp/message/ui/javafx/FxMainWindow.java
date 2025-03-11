package com.ubo.tp.message.ui.javafx;

import com.ubo.tp.message.ui.IButton;
import com.ubo.tp.message.ui.IMainWindow;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FxMainWindow implements IMainWindow {

  private final Stage stage;
  private final FlowPane rootPane;

  public FxMainWindow(Stage stage) {
    this.stage = stage;
    this.rootPane = new FlowPane();

    // On paramètre le stage
    Scene scene = new Scene(rootPane, 800, 600);
    stage.setScene(scene);
  }

  @Override
  public void setTitle(String title) {
    stage.setTitle(title);
  }

  @Override
  public void showWindow() {
    // Attention : doit être appelé après avoir fait un launch JavaFX
    Platform.runLater(() -> stage.show());
  }

  @Override
  public void closeWindow() {
    Platform.runLater(() -> stage.close());
  }

  @Override
  public void addButton(IButton button) {
    if (button instanceof FxButton) {
      FxButton fb = (FxButton) button;
      Button realButton = fb.getFxButton();
      rootPane.getChildren().add(realButton);
    }
  }
}
