package com.ubo.tp.message.ui;

public interface IUIFactory {
  IMainWindow createMainWindow();
  IButton createButton();
  // createTextField(), etc., selon vos besoins
}
