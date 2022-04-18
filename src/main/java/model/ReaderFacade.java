package model;

import entities.Reader;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ReaderFacade extends AbstractFacade<Reader> {

    @PersistenceContext(unitName = "LibraryApp-ejbPU")
    private EntityManager em;
    
    public static final String READER_FIND_FULLNAME = "ФИО";
    public static final String READER_FIND_PASSPORT = "№ паспорта";
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReaderFacade() {
        super(Reader.class);
    }

    public List<Reader> findByPassport(String passport){
        return getEntityManager().createNamedQuery("Reader.findByPassport").setParameter("passport", "%" + passport + "%").getResultList();
    }
    
    public List<Reader> findByName(String name){
        return getEntityManager().createNamedQuery("Reader.findByName").setParameter("name", "%" + name + "%").getResultList();
    }
    
}
