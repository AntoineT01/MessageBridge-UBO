package com.ubo.tp.message.components.message.autocompletion;

import java.util.List;

public interface SuggestionProvider {
  List<String> getSuggestions(String prefix);
}

