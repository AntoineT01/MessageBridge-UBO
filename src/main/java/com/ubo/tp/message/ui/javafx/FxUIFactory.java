package com.ubo.tp.message.ui.javafx;

import com.ubo.tp.message.ui.IButton;
import com.ubo.tp.message.ui.IMainWindow;
import com.ubo.tp.message.ui.IUIFactory;
import javafx.stage.Stage;

public class FxUIFactory implements IUIFactory {

  // Dans le monde JavaFX, il faut souvent qu’une Application soit lancée
  private Stage primaryStage;

  public FxUIFactory(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  @Override
  public IMainWindow createMainWindow() {
    return new FxMainWindow(primaryStage);
  }

  @Override
  public IButton createButton() {
    return new FxButton();
  }
}
