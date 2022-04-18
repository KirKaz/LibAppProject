package model;

import entities.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BookFacade extends AbstractFacade<Book> {

    @PersistenceContext(unitName = "LibraryApp-ejbPU")
    private EntityManager em;
    
    public static final String BOOK_FIND_AUTHOR = "Автор";
    public static final String BOOK_FIND_NAME = "Название";
    public static final String BOOK_FILTER_ALL = "Все";
    public static final String BOOK_FILTER_IN_LIBRARY = "Да";
    public static final String BOOK_FILTER_NOT_IN_LIBRARY = "Нет";

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookFacade() {
        super(Book.class);
    }
    
    public List<Book> findAllInLibrary(){
        return getEntityManager().createNamedQuery("Book.findAllInLibrary")
                .getResultList();
    }
    
    public List<Book> findWithFilters(String findVal, String condition, String inLibraryRegime, int startYear, int endYear){
        StringBuilder query = new StringBuilder("SELECT b FROM Book b WHERE b.onDelete = false");
        
        if(!findVal.trim().isEmpty()){
            query.append(" AND ");
            query.append((BOOK_FIND_AUTHOR.equals(condition)) ? "b.author" : "b.name");
            query.append(" LIKE '%");
            query.append(findVal);
            query.append("%'");
        }
        
        if(!BOOK_FILTER_ALL.equals(inLibraryRegime)){
            query.append(" AND b.inLibrary = ");
            query.append(BOOK_FILTER_IN_LIBRARY.equals(inLibraryRegime));
        }
        
        if(startYear > 0 || endYear > 0){
            query.append(" AND b.year ");
            if(startYear == 0){
                query.append("<= ");
                query.append(endYear);
            } else if(endYear == 0){
                query.append(">= ");
                query.append(startYear);
            } else {
                query.append("BETWEEN ");
                query.append(startYear);
                query.append(" AND ");
                query.append(endYear);
            } 
            
        }
        return getEntityManager().createQuery(query.toString()).getResultList();
    }
    
}
