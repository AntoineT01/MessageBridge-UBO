package com.ubo.tp.message.ui;

public interface IButton {
    // Définit le texte à afficher
    void setText(String text);

    // Définir l’action déclenchée au clic
    void setOnAction(Runnable action);

    // Rendez ce bouton désactivé/activé
    void setEnabled(boolean enabled);
}
