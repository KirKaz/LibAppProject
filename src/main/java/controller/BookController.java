package controller;

import entities.*;
import model.BookFacade;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.*;

@Named(value = "bookController")
@SessionScoped
public class BookController implements Serializable {

    @EJB
    private BookFacade bookFacade;
    private Book b;
    private String findVal;
    private String condition;
    private String inLibraryRegime;
    private int startYear;
    private int endYear;
    private List<String> inLibraryRegimes;
    private List<String> conditions;
    private List<Book> books;
    private boolean disabled;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    public void onRowSelect(SelectEvent event) { disabled = false; }
    
    public void onRowUnselect(UnselectEvent event) { disabled = true; }
    
    @PostConstruct
    public void init() {
        b = new Book();
        findVal = "";
        condition = BookFacade.BOOK_FIND_AUTHOR;
        inLibraryRegime = BookFacade.BOOK_FILTER_ALL;
        initInLibraryRegimes();
        initConditions();
        books = findAll();
        disabled = true;
    }
    
    private void initInLibraryRegimes(){
        inLibraryRegimes = new ArrayList<>();
        inLibraryRegimes.add(BookFacade.BOOK_FILTER_ALL);
        inLibraryRegimes.add(BookFacade.BOOK_FILTER_IN_LIBRARY);
        inLibraryRegimes.add(BookFacade.BOOK_FILTER_NOT_IN_LIBRARY);
    }
    
    private void initConditions(){
        conditions = new ArrayList<>();
        conditions.add(BookFacade.BOOK_FIND_AUTHOR);
        conditions.add(BookFacade.BOOK_FIND_NAME);
    }
    
    public Book getB() {
        return b;
    }

    public void setB(Book b) {
        this.b = b;
    }

    public String getFindVal() {
        return findVal;
    }

    public void setFindVal(String findVal) {
        this.findVal = findVal;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getInLibraryRegime() {
        return inLibraryRegime;
    }

    public void setInLibraryRegime(String inLibraryRegime) {
        this.inLibraryRegime = inLibraryRegime;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
    
    public List<String> getInLibraryRegimes(){
        return inLibraryRegimes;
    }
    
    public List<String> getConditions() {
        return conditions;
    }
    
    public void find(){
        books = findWithFilters();
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    public void setBooks(List<Book> books){
        this.books = books;
    }
    
    public BookController() {
    }
    
    public List<Book> findAll(){
        return bookFacade.findAll();
    }
    
    public List<Book> findWithFilters(){
        return bookFacade.findWithFilters(findVal, condition, inLibraryRegime, startYear, endYear);
    }
    
    public void clearFilters(){
        findVal = "";
        inLibraryRegime = BookFacade.BOOK_FILTER_ALL;
        startYear = 0;
        endYear = 0;
        books = findAll();
    }
    
    private void validate(Book b) throws Exception {
        if(b.getAuthor().trim().isEmpty())
            throw new Exception("Не указано ФИО автора!");
        else if(b.getName().trim().isEmpty())
            throw new Exception("Не указано название произведения!");
        else if(b.getYear() < 0)
            throw new Exception("Год должен быть больше 0!");
        else if(!b.getYear().toString().matches("^[0-9]+$"))
            throw new Exception("Год должен содержать только цифры!");
    }
    
    public void add() throws Exception {
        validate(b);
        b.setOnDelete(Boolean.FALSE);
        b.setInLibrary(Boolean.TRUE);
        bookFacade.create(b);
        b = new Book();
        System.out.println("ПЕРЕД");
        refreshTable();
        System.out.println("ПОСЛЕ");
    }
    
    public void delete(){
        b.setOnDelete(Boolean.TRUE);
        bookFacade.edit(b);
        b = new Book();
        refreshTable();
    }

    public void edit() throws Exception {
        validate(b);
        bookFacade.edit(b);
        b = new Book();
        refreshTable();
    }
    
    public void refreshTable(){
        if(!findVal.isEmpty() || !inLibraryRegime.equals(BookFacade.BOOK_FILTER_ALL) || startYear != 0 || endYear != 0)
            books = findWithFilters();
        else books = findAll();
    }
}
