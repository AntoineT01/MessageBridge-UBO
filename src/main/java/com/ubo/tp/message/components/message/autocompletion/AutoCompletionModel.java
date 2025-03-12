package com.ubo.tp.message.components.message.autocompletion;

import java.util.ArrayList;
import java.util.List;

public class AutoCompletionModel {
  private final SuggestionProvider suggestionProvider;
  private List<String> suggestions = new ArrayList<>();
  private final List<AutoCompletionListener> listeners = new ArrayList<>();

  public AutoCompletionModel(SuggestionProvider suggestionProvider) {
    this.suggestionProvider = suggestionProvider;
  }

  public void updateSuggestions(String prefix) {
    suggestions = suggestionProvider.getSuggestions(prefix);
    notifySuggestionsChanged();
  }

  public List<String> getSuggestions() {
    return suggestions;
  }

  public void addListener(AutoCompletionListener listener) {
    listeners.add(listener);
  }

  public void removeListener(AutoCompletionListener listener) {
    listeners.remove(listener);
  }

  private void notifySuggestionsChanged() {
    for (AutoCompletionListener listener : listeners) {
      listener.onSuggestionsChanged(suggestions);
    }
  }

  public interface AutoCompletionListener {
    void onSuggestionsChanged(List<String> suggestions);
  }
}
