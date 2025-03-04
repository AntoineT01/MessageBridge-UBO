package com.ubo.tp.message.observers;

import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

public class ConsoleDatabaseObserver implements IDatabaseObserver {

  @Override
  public void notifyMessageAdded(Message addedMessage) {
    System.out.println("[DB OBSERVER] Message ajouté : " + addedMessage);
  }

  @Override
  public void notifyMessageDeleted(Message deletedMessage) {
    System.out.println("[DB OBSERVER] Message supprimé : " + deletedMessage);
  }

  @Override
  public void notifyMessageModified(Message modifiedMessage) {
    System.out.println("[DB OBSERVER] Message modifié : " + modifiedMessage);
  }

  @Override
  public void notifyUserAdded(User addedUser) {
    System.out.println("[DB OBSERVER] Utilisateur ajouté : " + addedUser);
  }

  @Override
  public void notifyUserDeleted(User deletedUser) {
    System.out.println("[DB OBSERVER] Utilisateur supprimé : " + deletedUser);
  }

  @Override
  public void notifyUserModified(User modifiedUser) {
    System.out.println("[DB OBSERVER] Utilisateur modifié : " + modifiedUser);
  }
}
