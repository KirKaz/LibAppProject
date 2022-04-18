package controller;

import entities.*;
import model.BookFacade;
import model.LibraryFacade;
import model.ReaderFacade;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named(value = "libraryController")
@SessionScoped
public class LibraryController implements Serializable {

    @EJB
    private BookFacade bookFacade;

    @EJB
    private ReaderFacade readerFacade;

    @EJB
    private LibraryFacade libraryFacade;
    
    private Library l = new Library();
    private Reader r = new Reader();
    private String findReaderName;
    private String findBookName;
    private String regime;
    private Date startDate;
    private Date endDate;
    private List<String> regimes;
    private List<Library> libraries;
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
        l = new Library();
        r = new Reader();
        findReaderName = "";
        findBookName = "";
        initRegimes();
        libraries = findAll();
        disabled = true;
    }
    
    private void initRegimes(){
        regimes = new ArrayList<>();
        regimes.add(LibraryFacade.JOURNAL_FIND_ISSUE_DATE);
        regimes.add(LibraryFacade.JOURNAL_FIND_RETURN_DATE);
        regimes.add(LibraryFacade.JOURNAL_FIND_REAL_RETURN_DATE);
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getFindReaderName() {
        return findReaderName;
    }

    public void setFindReaderName(String findReaderName) {
        this.findReaderName = findReaderName;
    }

    public String getFindBookName() {
        return findBookName;
    }

    public void setFindBookName(String findBookName) {
        this.findBookName = findBookName;
    }
    
    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }
    
    public List<String> getRegimes(){
        return regimes;
    }
    
    public List<Library> getLibraries() {
        return libraries;
    }
    
    public void setLibraries(List<Library> libraries){
        this.libraries = libraries;
    }
    
    public Library getL() {
        return l;
    }

    public void setL(Library l) {
        this.l = l;
    }

    public Reader getR() {
        return r;
    }

    public void setR(Reader r) {
        this.r = r;
    }
    
    public LibraryController() {
    }
    
    public void find(){
        libraries = findWithFilters();
    }
    
    public List<Library> findAll(){
        return libraryFacade.findAll();
    }
    
    public List<Library> findWithFilters(){
        return libraryFacade.findWithFilters(findReaderName, findBookName, regime, startDate, endDate);
    }
    
    public void clearFilters(){
        findReaderName = "";
        findBookName = "";
        startDate = null;
        endDate = null;
        libraries = findAll();
    }
    
    public List<Reader> getReadersForAddJournalEntry(){
        return readerFacade.findAll();
    }
    
    public List<Reader> getReadersForEditJournalEntry(){
        Set<Reader> readerSet = new LinkedHashSet();
        readerSet.addAll(getReadersForAddJournalEntry());
        readerSet.add(l.getReaderID());
        return new LinkedList<>(readerSet);
    }
    
    public List<Book> getBooksForAddJournalEntry(){
        return bookFacade.findAllInLibrary();
    }
    
    public List<Book> getBooksForEditJournalEntry(){
        Set<Book> bookSet = new LinkedHashSet();
        bookSet.addAll(bookFacade.findAllInLibrary());
        bookSet.add(l.getBookID());
        return new LinkedList<>(bookSet);
    }
    
    public List<Reader> getBadReaders(){
        return libraryFacade.getBadReaders();
    }
    
    public Object getDebtCountByReaderID(Integer readerID){
        return libraryFacade.getDebtCountByReaderID(readerID);
    }
    
    public List<Library> getNoReturn(){
        return libraryFacade.getNoReturn();
    }
    
    public int getExpiredDays(Date returnDate){
        return (int)((new Date().getTime() - returnDate.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    private void validate(Library l) throws Exception {
        if(l.getIssueDate() == null)
            throw new Exception("Не указана дата выдачи!");
        else if(l.getReturnDate() == null)
            throw new Exception("Не указана дата возврата!");
        else if(l.getIssueDate().after(new Date()))
            throw new Exception("Дата выдачи не может быть позже текущей даты!");
        else if(l.getReturnDate().after(new Date()))
            throw new Exception("Дата возврата не может быть позже текущей даты!");
        else if(l.getRealReturnDate() != null && l.getRealReturnDate().after(new Date()))
            throw new Exception("Дата реального возврата не может быть позже текущей даты!");
        else if(l.getIssueDate().after(l.getReturnDate()))
            throw new Exception("Дата возврата не может быть раньше даты выдачи!");
        else if(l.getRealReturnDate() != null && l.getIssueDate().after(l.getRealReturnDate()))
            throw new Exception("Дата реального возврата не может быть раньше даты выдачи!");
    }
    
    public void add() throws Exception {
        validate(l);
        l.setOnDelete(Boolean.FALSE);
        l.getBookID().setInLibrary(l.getRealReturnDate() != null);
        libraryFacade.create(l);
        bookFacade.edit(l.getBookID());
        l = new Library();
        refreshTable();
    }
    
    public void delete(){
        l.setOnDelete(Boolean.TRUE);
        libraryFacade.edit(l);
        l = new Library();
        refreshTable();
    }
    
    public void edit() throws Exception {
        validate(l);
        l.getBookID().setInLibrary(l.getRealReturnDate() != null);
        libraryFacade.edit(l);
        bookFacade.edit(l.getBookID());
        l = new Library();
        refreshTable();
    }
    
    public void refreshTable(){
        if(!findReaderName.trim().isEmpty() || !findBookName.trim().isEmpty() || startDate != null || endDate != null)
            libraries = findWithFilters();
        else libraries = findAll();
    }

}
