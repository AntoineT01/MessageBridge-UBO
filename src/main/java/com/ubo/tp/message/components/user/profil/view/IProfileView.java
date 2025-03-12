package com.ubo.tp.message.components.user.profil.view;

import com.ubo.tp.message.core.datamodel.Message;

import java.awt.event.ActionListener;
import java.util.Set;

public interface IProfileView {
  void updateUserInfo(String tag, String name, int followersCount, String avatarPath);
  void updateUserMessages(Set<Message> messages);
  void setUpdateButtonListener(ActionListener listener);
  void setErrorMessage(String message);
  void setSuccessMessage(String message);
  String getNewName();
  String getNewPassword();
  String getConfirmPassword();
  // Nouvel ajout pour récupérer le chemin de l'avatar choisi
  String getAvatarPath();
  void resetFields();
  void setEnabled(boolean enabled);
}
