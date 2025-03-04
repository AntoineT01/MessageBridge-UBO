package com.ubo.tp.message.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.ubo.tp.message.common.IconFactory;
import com.ubo.tp.message.common.ImageUtils;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.datamodel.User;

public class MessageAppMainView extends JFrame implements ISessionObserver {

  private final IDatabase database;
  private final ISession session;

  // Éléments de menu pour la gestion de la session
  private JMenuItem loginMenuItem;
  private JMenuItem logoutMenuItem;
  private JMenuItem profileMenuItem;

  // Panneau principal qui change selon qu'un utilisateur est connecté ou non
  private JPanel mainContentPanel;
  private JPanel welcomePanel;
  private JPanel connectedPanel;
  private CardLayout cardLayout;

  // Label pour afficher l'utilisateur connecté
  private JLabel userStatusLabel;

  public MessageAppMainView(IDatabase database, ISession session) {
    this.database = database;
    this.session = session;

    // S'abonner aux événements de session
    session.addObserver(this);

    initGUI();
    updateSessionUI();
  }

  private void initGUI() {
    // Définition du titre et de l'icône de la fenêtre
    this.setTitle("MessageApp");
    this.setSize(800, 600);

    ImageIcon windowIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 32, 32);
    if(windowIcon != null){
      this.setIconImage(windowIcon.getImage());
    }

    // --- Barre de menu ---
    JMenuBar menuBar = new JMenuBar();

    // 1) Menu Fichier
    JMenu menuFichier = new JMenu("Fichier");
    menuFichier.setToolTipText("Opérations sur les fichiers");

    // Ajout d'un item pour changer le répertoire d'échange
    JMenuItem itemChangeDir = new JMenuItem("Changer le répertoire d'échange");
    itemChangeDir.setToolTipText("Sélectionner un nouveau répertoire d'échange pour les messages");
    menuFichier.add(itemChangeDir);
    menuFichier.addSeparator();

    JMenuItem itemQuitter = new JMenuItem("Quitter");
    itemQuitter.setIcon(IconFactory.createCloseIcon(IconFactory.ICON_SMALL));
    itemQuitter.setToolTipText("Fermer l'application");
    itemQuitter.addActionListener(e -> {
      firePropertyChange("ACTION_EXIT", false, true);
    });
    menuFichier.add(itemQuitter);
    menuBar.add(menuFichier);

    // 2) Menu Utilisateur
    JMenu menuUser = new JMenu("Utilisateur");
    menuUser.setToolTipText("Gestion des utilisateurs");

    loginMenuItem = new JMenuItem("Se connecter");
    loginMenuItem.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    loginMenuItem.setToolTipText("Se connecter à un compte existant");
    loginMenuItem.addActionListener(e -> {
      firePropertyChange("ACTION_LOGIN", false, true);
    });
    menuUser.add(loginMenuItem);

    logoutMenuItem = new JMenuItem("Se déconnecter");
    logoutMenuItem.setToolTipText("Se déconnecter du compte actuel");
    logoutMenuItem.addActionListener(e -> {
      session.disconnect();
    });
    menuUser.add(logoutMenuItem);

    menuUser.addSeparator();

    profileMenuItem = new JMenuItem("Mon profil");
    profileMenuItem.setToolTipText("Modifier mon profil");
    profileMenuItem.addActionListener(e -> {
      // Action à implémenter plus tard
      JOptionPane.showMessageDialog(this,
                                    "Fonctionnalité à implémenter dans une prochaine séance",
                                    "En construction",
                                    JOptionPane.INFORMATION_MESSAGE);
    });
    menuUser.add(profileMenuItem);

    menuBar.add(menuUser);

    // 3) Menu A propos
    JMenu menuAPropos = new JMenu("?");
    menuAPropos.setToolTipText("Aide et informations");

    JMenuItem itemAbout = new JMenuItem("A propos");
    itemAbout.setIcon(IconFactory.createInfoIcon(IconFactory.ICON_SMALL));
    itemAbout.setToolTipText("Informations sur l'application");
    itemAbout.addActionListener(e -> {
      showAboutDialog();
    });
    menuAPropos.add(itemAbout);
    menuBar.add(menuAPropos);

    // On ajoute la barre de menu à la fenêtre
    this.setJMenuBar(menuBar);

    // Gestion de fermeture
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        firePropertyChange("ACTION_EXIT", false, true);
      }
    });

    // Création du panneau de contenu principal avec CardLayout
    cardLayout = new CardLayout();
    mainContentPanel = new JPanel(cardLayout);

    // Panneau de bienvenue (quand non connecté)
    welcomePanel = createWelcomePanel();
    mainContentPanel.add(welcomePanel, "welcome");

    // Panneau principal (quand connecté)
    connectedPanel = createConnectedPanel();
    mainContentPanel.add(connectedPanel, "connected");

    // Affichage du panneau de bienvenue par défaut
    cardLayout.show(mainContentPanel, "welcome");

    // Ajout d'une barre d'état en bas
    JPanel statusPanel = new JPanel(new BorderLayout());
    statusPanel.setBorder(BorderFactory.createEtchedBorder());

    userStatusLabel = new JLabel("Non connecté");
    statusPanel.add(userStatusLabel, BorderLayout.WEST);

    // Layout principal
    this.setLayout(new BorderLayout());
    this.add(mainContentPanel, BorderLayout.CENTER);
    this.add(statusPanel, BorderLayout.SOUTH);

    this.setLocationRelativeTo(null);
  }

  private JPanel createWelcomePanel() {
    JPanel panel = new JPanel(new BorderLayout());

    // Image au centre
    ImageIcon welcomeIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 200, 200);
    JLabel iconLabel = new JLabel(welcomeIcon);
    iconLabel.setHorizontalAlignment(JLabel.CENTER);

    // Texte de bienvenue
    JLabel welcomeLabel = new JLabel("Bienvenue dans MessageApp");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
    welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

    JLabel infoLabel = new JLabel("Veuillez vous connecter pour accéder à l'application");
    infoLabel.setHorizontalAlignment(JLabel.CENTER);

    // Bouton de connexion
    JButton loginButton = new JButton("Se connecter");
    loginButton.setIcon(IconFactory.createUserIcon(IconFactory.ICON_SMALL));
    loginButton.addActionListener(e -> {
      firePropertyChange("ACTION_LOGIN", false, true);
    });

    // Organisation des composants
    JPanel textPanel = new JPanel(new GridLayout(2, 1));
    textPanel.add(welcomeLabel);
    textPanel.add(infoLabel);

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(iconLabel, BorderLayout.CENTER);
    centerPanel.add(textPanel, BorderLayout.SOUTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(loginButton);

    panel.add(centerPanel, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    return panel;
  }

  private JPanel createConnectedPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    // Pour l'instant, juste un message indiquant que l'utilisateur est connecté
    JLabel connectedLabel = new JLabel("Vous êtes connecté !");
    connectedLabel.setFont(new Font("Arial", Font.BOLD, 18));
    connectedLabel.setHorizontalAlignment(JLabel.CENTER);

    JLabel infoLabel = new JLabel("Cette interface sera enrichie dans les prochaines séances");
    infoLabel.setHorizontalAlignment(JLabel.CENTER);

    JPanel labelPanel = new JPanel(new GridLayout(2, 1));
    labelPanel.add(connectedLabel);
    labelPanel.add(infoLabel);

    panel.add(labelPanel, BorderLayout.CENTER);

    return panel;
  }

  private void showAboutDialog() {
    ImageIcon logoIcon = ImageUtils.loadScaledIcon("/tux_logo.png", 100, 100);
    JOptionPane.showMessageDialog(
      this,
      "UBO M2-TIIL\nDépartement Informatique",
      "A propos",
      JOptionPane.INFORMATION_MESSAGE,
      logoIcon
    );
  }

  private void updateSessionUI() {
    User connectedUser = session.getConnectedUser();

    if (connectedUser != null) {
      // Interface pour utilisateur connecté
      userStatusLabel.setText("Connecté en tant que : " + connectedUser.getName() + " (@" + connectedUser.getUserTag() + ")");
      cardLayout.show(mainContentPanel, "connected");
      loginMenuItem.setEnabled(false);
      logoutMenuItem.setEnabled(true);
      profileMenuItem.setEnabled(true);
    } else {
      // Interface pour utilisateur non connecté
      userStatusLabel.setText("Non connecté");
      cardLayout.show(mainContentPanel, "welcome");
      loginMenuItem.setEnabled(true);
      logoutMenuItem.setEnabled(false);
      profileMenuItem.setEnabled(false);
    }
  }

  @Override
  public void notifyLogin(User connectedUser) {
    updateSessionUI();
  }

  @Override
  public void notifyLogout() {
    updateSessionUI();
  }
}