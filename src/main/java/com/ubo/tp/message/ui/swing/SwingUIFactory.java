package com.ubo.tp.message.ui.swing;

import com.ubo.tp.message.ui.IButton;
import com.ubo.tp.message.ui.IMainWindow;
import com.ubo.tp.message.ui.IUIFactory;

public class SwingUIFactory implements IUIFactory {

  @Override
  public IMainWindow createMainWindow() {
    return new SwingMainWindow();
  }

  @Override
  public IButton createButton() {
    return new SwingButton();
  }
}
