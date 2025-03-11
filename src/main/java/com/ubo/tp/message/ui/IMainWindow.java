package com.ubo.tp.message.ui;

// Exemple d’interface pour une fenêtre principale
public interface IMainWindow {
  // Définit la méthode pour rendre visible la fenêtre
  void showWindow();

  // Ajoute un bouton (abstrait) à la fenêtre
  void addButton(IButton button);

  // Ferme la fenêtre
  void closeWindow();

  void setTitle(String title);
}
