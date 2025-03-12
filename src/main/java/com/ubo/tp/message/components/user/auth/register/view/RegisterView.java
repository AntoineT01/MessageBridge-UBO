package com.ubo.tp.message.components.user.auth.register.view;

import com.ubo.tp.message.common.ui.ShadowBorder;
import com.ubo.tp.message.common.ui.UserAvatarEditor;
import com.ubo.tp.message.common.utils.ImageUtils;
import com.ubo.tp.message.core.database.IDatabase;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RegisterView extends JPanel implements IRegisterView {
  private final IDatabase database;
  private JTextField tagField;
  private JTextField nameField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  private JButton registerButton;
  private JButton loginButton;
  private JLabel errorLabel;
  private UserAvatarEditor avatarEditor;

  public RegisterView(IDatabase database) {
    this.database = database;
    this.initGUI();
  }

  private void initGUI() {
    this.setBackground(new Color(245, 248, 250));
    this.setLayout(new GridBagLayout());
    this.setBorder(new EmptyBorder(40, 40, 40, 40));
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(10, 10, 10, 10);
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(Color.WHITE);
    centerPanel.setBorder(BorderFactory.createCompoundBorder(
      new ShadowBorder(5, Color.BLACK, 0.1f),
      BorderFactory.createEmptyBorder(25, 30, 25, 30)
    ));
    GridBagConstraints centerConstraints = new GridBagConstraints();
    centerConstraints.fill = GridBagConstraints.HORIZONTAL;
    centerConstraints.insets = new Insets(8, 8, 8, 8);
    JLabel logoLabel = new JLabel();
    ImageIcon logo = ImageUtils.loadScaledIcon("/tux_logo.png", 100, 100);
    logoLabel.setIcon(logo);
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 0;
    centerConstraints.gridwidth = 2;
    centerPanel.add(logoLabel, centerConstraints);
    JLabel titleLabel = new JLabel("Cr\u00E9er un compte", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(new Color(46, 204, 113));
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 1;
    centerConstraints.gridwidth = 2;
    centerPanel.add(titleLabel, centerConstraints);
    JLabel subtitleLabel = new JLabel("Rejoignez la communaut\u00E9 MessageApp", SwingConstants.CENTER);
    subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    subtitleLabel.setForeground(new Color(100, 100, 100));
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 2;
    centerConstraints.gridwidth = 2;
    centerPanel.add(subtitleLabel, centerConstraints);
    centerConstraints.gridy = 3;
    centerPanel.add(Box.createVerticalStrut(15), centerConstraints);
    JPanel tagPanel = new JPanel(new BorderLayout(5, 5));
    tagPanel.setOpaque(false);
    JLabel tagLabel = new JLabel("Tag utilisateur");
    tagLabel.setFont(new Font("Arial", Font.BOLD, 14));
    tagPanel.add(tagLabel, BorderLayout.NORTH);
    tagField = new JTextField(20);
    tagField.setFont(new Font("Arial", Font.PLAIN, 14));
    tagField.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    tagField.addFocusListener(createFocusListener(tagField));
    tagPanel.add(tagField, BorderLayout.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 4;
    centerConstraints.gridwidth = 2;
    centerPanel.add(tagPanel, centerConstraints);
    JPanel namePanel = new JPanel(new BorderLayout(5, 5));
    namePanel.setOpaque(false);
    JLabel nameLabel = new JLabel("Nom d'utilisateur");
    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    namePanel.add(nameLabel, BorderLayout.NORTH);
    nameField = new JTextField(20);
    nameField.setFont(new Font("Arial", Font.PLAIN, 14));
    nameField.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    nameField.addFocusListener(createFocusListener(nameField));
    namePanel.add(nameField, BorderLayout.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 5;
    centerConstraints.gridwidth = 2;
    centerPanel.add(namePanel, centerConstraints);
    JPanel avatarPanel = new JPanel(new BorderLayout(5, 5));
    avatarPanel.setOpaque(false);
    JLabel avatarLabel = new JLabel("Avatar");
    avatarLabel.setFont(new Font("Arial", Font.BOLD, 14));
    avatarPanel.add(avatarLabel, BorderLayout.NORTH);
    avatarEditor = new UserAvatarEditor(80);
    avatarPanel.add(avatarEditor, BorderLayout.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 6;
    centerConstraints.gridwidth = 2;
    centerPanel.add(avatarPanel, centerConstraints);
    JPanel passwordPanel = new JPanel(new BorderLayout(5, 5));
    passwordPanel.setOpaque(false);
    JLabel passwordLabel = new JLabel("Mot de passe");
    passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    passwordPanel.add(passwordLabel, BorderLayout.NORTH);
    passwordField = new JPasswordField(20);
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    passwordField.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    passwordField.addFocusListener(createFocusListener(passwordField));
    passwordPanel.add(passwordField, BorderLayout.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 7;
    centerConstraints.gridwidth = 2;
    centerPanel.add(passwordPanel, centerConstraints);
    JPanel confirmPasswordPanel = new JPanel(new BorderLayout(5, 5));
    confirmPasswordPanel.setOpaque(false);
    JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe");
    confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    confirmPasswordPanel.add(confirmPasswordLabel, BorderLayout.NORTH);
    confirmPasswordField = new JPasswordField(20);
    confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
    confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
      BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    confirmPasswordField.addFocusListener(createFocusListener(confirmPasswordField));
    confirmPasswordPanel.add(confirmPasswordField, BorderLayout.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 8;
    centerConstraints.gridwidth = 2;
    centerPanel.add(confirmPasswordPanel, centerConstraints);
    errorLabel = new JLabel(" ");
    errorLabel.setForeground(new Color(231, 76, 60));
    errorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 9;
    centerConstraints.gridwidth = 2;
    centerPanel.add(errorLabel, centerConstraints);
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
    buttonPanel.setOpaque(false);
    registerButton = new JButton("S'inscrire");
    registerButton.setFont(new Font("Arial", Font.BOLD, 14));
    registerButton.setBackground(new Color(46, 204, 113));
    registerButton.setForeground(Color.WHITE);
    registerButton.setFocusPainted(false);
    registerButton.setBorderPainted(false);
    registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    registerButton.setPreferredSize(new Dimension(150, 45));
    buttonPanel.add(registerButton);
    loginButton = new JButton("Se connecter");
    loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
    loginButton.setBackground(new Color(236, 240, 241));
    loginButton.setForeground(new Color(44, 62, 80));
    loginButton.setFocusPainted(false);
    loginButton.setBorderPainted(false);
    loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loginButton.setPreferredSize(new Dimension(150, 45));
    buttonPanel.add(loginButton);
    centerConstraints.gridx = 0;
    centerConstraints.gridy = 10;
    centerConstraints.gridwidth = 2;
    centerConstraints.insets = new Insets(20, 8, 8, 8);
    centerPanel.add(buttonPanel, centerConstraints);
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1.0;
    constraints.weighty = 1.0;
    this.add(centerPanel, constraints);
  }

  private FocusAdapter createFocusListener(JComponent component) {
    return new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        component.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(46, 204, 113), 2, true),
          BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
      }
      @Override
      public void focusLost(FocusEvent e) {
        component.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
          BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
      }
    };
  }

  @Override
  public void setRegisterButtonListener(ActionListener listener) {
    registerButton.addActionListener(listener);
  }

  @Override
  public void setLoginButtonListener(ActionListener listener) {
    loginButton.addActionListener(listener);
  }

  @Override
  public String getUserTag() {
    return tagField.getText().trim();
  }

  @Override
  public String getUserName() {
    return nameField.getText().trim();
  }

  @Override
  public String getUserPassword() {
    return new String(passwordField.getPassword());
  }

  @Override
  public String getConfirmPassword() {
    return new String(confirmPasswordField.getPassword());
  }

  @Override
  public void setErrorMessage(String message) {
    errorLabel.setText(message);
  }

  @Override
  public void resetFields() {
    tagField.setText("");
    nameField.setText("");
    passwordField.setText("");
    confirmPasswordField.setText("");
    errorLabel.setText("");
    avatarEditor.setAvatarPath("");
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.setEnabled(enabled);
    tagField.setEnabled(enabled);
    nameField.setEnabled(enabled);
    passwordField.setEnabled(enabled);
    confirmPasswordField.setEnabled(enabled);
    registerButton.setEnabled(enabled);
    loginButton.setEnabled(enabled);
  }

  @Override
  public IDatabase getDatabase() {
    return database;
  }

  public String getAvatarPath() {
    return avatarEditor.getAvatarPath();
  }
}
