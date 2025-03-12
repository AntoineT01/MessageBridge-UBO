package com.ubo.tp.message.components.message.autocompletion;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.datamodel.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserSuggestionProviderFX implements SuggestionProvider {
  private final IDatabase database;
  public UserSuggestionProviderFX(IDatabase database) {
    this.database = database;
  }
  @Override
  public List<String> getSuggestions(String prefix) {
    final String lowerPrefix = prefix == null ? "" : prefix.toLowerCase();
    return database.getUsers().stream()
      .map(User::getUserTag)
      .filter(tag -> tag.toLowerCase().startsWith("@" + lowerPrefix))
      .collect(Collectors.toList());
  }
}
