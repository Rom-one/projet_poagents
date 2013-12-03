/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import javax.persistence.Persistence;

/**
 *
 * @author Romain
 */
public class DAOFactory {
    
    private static final ObjetJpaController objetDAO = new ObjetJpaController(Persistence.createEntityManagerFactory("POAgentPU"));
    private static final VenteJpaController venteDAO = new VenteJpaController(Persistence.createEntityManagerFactory("POAgentPU"));
    private static final CategorieJpaController categorieDAO = new CategorieJpaController(Persistence.createEntityManagerFactory("POAgentPU"));
    private static final StockJpaController stockDAO = new StockJpaController(Persistence.createEntityManagerFactory("POAgentPU"));
    private static final VendeurJpaController vendeurDAO = new VendeurJpaController(Persistence.createEntityManagerFactory("POAgentPU"));
    
    public static ObjetJpaController getObjetDAO() {
        return objetDAO;
    }

    public static VenteJpaController getVenteDAO() {
        return venteDAO;
    }
    
    public static CategorieJpaController getCategorieDAO() {
        return categorieDAO;
    }
    
    public static StockJpaController getStockDAO() {
        return stockDAO;
    }
    
    public static VendeurJpaController getVendeurDAO() {
        return vendeurDAO;
    }
}
