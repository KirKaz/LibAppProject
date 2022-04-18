package model;

import entities.Library;
import entities.Reader;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class LibraryFacade extends AbstractFacade<Library> {

    @PersistenceContext(unitName = "LibraryApp-ejbPU")
    private EntityManager em;

    public static final String JOURNAL_FIND_ISSUE_DATE = "Выдана";
    public static final String JOURNAL_FIND_RETURN_DATE = "До";
    public static final String JOURNAL_FIND_REAL_RETURN_DATE = "Возвращена";
        
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LibraryFacade() {
        super(Library.class);
    }
       
    public Object getDebtCountByReaderID(Integer readerID){
        return getEntityManager().createNamedQuery("Library.getDebtCountByReaderID")
                .setParameter("readerID", readerID)
                .getSingleResult();
    }
    
    public List<Library> getNoReturn(){
        return getEntityManager().createNamedQuery("Library.getNoReturn")
                .getResultList();
    }
    
    public List<Reader> getBadReaders(){
        return getEntityManager().createNamedQuery("Library.getBadReaders")
                .getResultList();
    }
    
    public List<Library> findWithFilters(String readerName, String bookName, String dateRegime, Date startDate, Date endDate){
        StringBuilder query = new StringBuilder("SELECT l FROM Library l WHERE l.onDelete = false");
        
        if(!readerName.trim().isEmpty()){
            query.append(" AND l.readerID.name LIKE '%");
            query.append(readerName);
            query.append("%'");
        }
        
        if(!bookName.trim().isEmpty()){
            query.append(" AND l.bookID.name LIKE '%");
            query.append(bookName);
            query.append("%'");
        }

        if(startDate != null || endDate != null){
            query.append(" AND ");
            switch (dateRegime) {
                case JOURNAL_FIND_ISSUE_DATE:
                    query.append("l.issueDate");
                    break;
                case JOURNAL_FIND_RETURN_DATE:
                    query.append("l.returnDate");
                    break;
                case JOURNAL_FIND_REAL_RETURN_DATE:
                    query.append("l.realReturnDate");
                    break;
            }
            
            if(startDate == null){
                query.append(" < :endDate");
                return getEntityManager().createQuery(query.toString()).setParameter("endDate", endDate).getResultList();
            } else if(endDate == null){
                query.append(" >= :startDate");
                return getEntityManager().createQuery(query.toString()).setParameter("startDate", startDate).getResultList();
            } else {
                query.append(" BETWEEN :startDate AND :endDate");
                return getEntityManager().createQuery(query.toString()).setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
            }
        }
        return getEntityManager().createQuery(query.toString()).getResultList();
    }
}
