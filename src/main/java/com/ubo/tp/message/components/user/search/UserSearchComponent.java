package com.ubo.tp.message.components.user.search;

import com.ubo.tp.message.components.IComponent;
import com.ubo.tp.message.components.user.search.controller.SearchController;
import com.ubo.tp.message.components.user.search.model.SearchModel;
import com.ubo.tp.message.components.user.search.view.ISearchView;
import com.ubo.tp.message.components.user.search.view.SearchView;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.entity.EntityManager;
import com.ubo.tp.message.core.session.SessionManager;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

/**
 * Composant de recherche d'utilisateurs.
 * Implémente l'interface IComponent pour s'intégrer dans l'application.
 */
public class UserSearchComponent implements IComponent<JPanel> {

  /**
   * Panneau principal du composant.
   */
  private final JPanel mainPanel;

  /**
   * Vue de recherche d'utilisateurs.
   */
  private final ISearchView searchView;

  /**
   * Contrôleur de recherche.
   */
  private final SearchController searchController;

  /**
   * Modèle de recherche.
   */
  private final SearchModel searchModel;

  /**
   * Écouteur pour l'action de visualisation du profil.
   */
  private ActionListener viewProfileListener;

  /**
   * Constructeur.
   * @param database La base de données.
   * @param sessionManager Le gestionnaire de session.
   * @param entityManager Le gestionnaire d'entités.
   */
  public UserSearchComponent(IDatabase database, SessionManager sessionManager, EntityManager entityManager) {
    // Initialisation du panneau principal
    this.mainPanel = new JPanel(new BorderLayout());

    // Création des composants MVC pour la recherche
    this.searchModel = new SearchModel(database, sessionManager);

    // Création de la vue concrète
    SearchView concreteView = new SearchView();
    this.searchView = concreteView;

    // Création du contrôleur avec l'interface de vue
    this.searchController = new SearchController(searchView, searchModel, entityManager);

    // Ajout de la vue au panneau principal
    this.mainPanel.add(concreteView, BorderLayout.CENTER);

    // Initialisation
    initialize();
  }

  @Override
  public JPanel getComponent() {
    return mainPanel;
  }

  @Override
  public void initialize() {
    // Charger tous les utilisateurs au démarrage
    searchController.searchUsers("");
  }

  @Override
  public void reset() {
    searchController.reset();
  }

  @Override
  public void setEnabled(boolean enabled) {
    searchController.setEnabled(enabled);
    mainPanel.setEnabled(enabled);
  }

  /**
   * Définit l'écouteur pour l'action de visualisation du profil.
   * @param listener L'écouteur à définir.
   */
  public void setViewProfileListener(ActionListener listener) {
    this.viewProfileListener = listener;
    searchController.setViewProfileListener(listener);
  }

  /**
   * Affiche un message d'erreur.
   * @param message Le message d'erreur à afficher.
   */
  public void showError(String message) {
    searchController.showError(message);
  }
}