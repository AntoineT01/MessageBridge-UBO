package com.ubo.tp.message.components.message.autocompletion;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AutoCompletionView extends JPopupMenu {
  private final JList<String> suggestionList;
  private final DefaultListModel<String> listModel = new DefaultListModel<>();
  private SuggestionSelectedListener suggestionSelectedListener;

  public AutoCompletionView() {
    suggestionList = new JList<>(listModel);
    suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(suggestionList);
    scrollPane.setBorder(null);
    add(scrollPane);
    setFocusable(false);

    // Sélection par clic
    suggestionList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int index = suggestionList.locationToIndex(e.getPoint());
        if (index != -1) {
          selectSuggestion(index);
        }
      }
    });
  }

  public void updateSuggestions(List<String> suggestions) {
    listModel.clear();
    for (String suggestion : suggestions) {
      listModel.addElement(suggestion);
    }
    if (!suggestions.isEmpty()) {
      suggestionList.setSelectedIndex(0);
    }
  }

  public void moveSelectionUp() {
    int index = suggestionList.getSelectedIndex();
    if (index > 0) {
      suggestionList.setSelectedIndex(index - 1);
      suggestionList.ensureIndexIsVisible(index - 1);
    }
  }

  public void moveSelectionDown() {
    int index = suggestionList.getSelectedIndex();
    if (index < listModel.size() - 1) {
      suggestionList.setSelectedIndex(index + 1);
      suggestionList.ensureIndexIsVisible(index + 1);
    }
  }

  public String getSelectedSuggestion() {
    return suggestionList.getSelectedValue();
  }

  public void setSuggestionSelectedListener(SuggestionSelectedListener listener) {
    this.suggestionSelectedListener = listener;
  }

  public void selectSuggestion(int index) {
    suggestionList.setSelectedIndex(index);
    if (suggestionSelectedListener != null) {
      suggestionSelectedListener.onSuggestionSelected(getSelectedSuggestion());
    }
  }

  public interface SuggestionSelectedListener {
    void onSuggestionSelected(String suggestion);
  }
}
