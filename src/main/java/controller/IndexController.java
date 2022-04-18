package controller;

import entities.*;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "indexController")
@SessionScoped
public class IndexController implements Serializable {
    
    private static final String BOOK_CATALOG = "view/book/bookCatalog.xhtml";
    private static final String READER_CATALOG = "view/reader/readerCatalog.xhtml";
    private static final String JOURNAL = "view/library/journal.xhtml";
    private static final String ADD_EDIT_BOOK = "view/book/addEditBook.xhtml";
    private static final String ADD_EDIT_READER = "view/reader/addEditReader.xhtml";
    private static final String ADD_EDIT_JOURNAL_ENTRY = "view/library/addEditJournalEntry.xhtml";
    private static final String BAD_READERS_REPORT = "view/report/badReadersReport.xhtml";
    private static final String NO_RETURN_BOOKS_REPORT = "view/report/noReturnBooksReport.xhtml";

    public void showBookCatalog() {
        setCurrentForm(BOOK_CATALOG);
    }

    public void showReaderCatalog() {
        setCurrentForm(READER_CATALOG);
    }

    public void showJournal() {
        setCurrentForm(JOURNAL);
    }

    public void showBadReadersReport() {
        setCurrentForm(BAD_READERS_REPORT);
    }

    public void showNoReturnBooksReport() {
        setCurrentForm(NO_RETURN_BOOKS_REPORT);
    }
    
    private String exceptionMessage;
    private boolean addRegime;
    private String currentForm;
    private boolean disabled;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @PostConstruct
    public void init() {
        exceptionMessage = "";
        currentForm = JOURNAL;
        disabled = true;
    }
    
    public void onRowSelect(SelectEvent event) { disabled = false; }
    
    public void onRowUnselect(UnselectEvent event) { disabled = true; }
    
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
    
    public boolean isAddRegime() {
        return addRegime;
    }

    public void setAddRegime(boolean addRegime) {
        this.addRegime = addRegime;
    }
    
    public String getCurrentForm() {
        return currentForm;
    }

    public void setCurrentForm(String currentForm) {
        this.currentForm = currentForm;
    }
    
    public IndexController() {
    }

    public List<Reader> getReadersList(LibraryController lc){
        if(addRegime) return lc.getReadersForAddJournalEntry();
        else return lc.getReadersForEditJournalEntry();
    }
    
    public List<Book> getBooksList(LibraryController lc){
        if(addRegime) return lc.getBooksForAddJournalEntry();
        else return lc.getBooksForEditJournalEntry();
    }
    
    private void clearExceptionMessage(){
        exceptionMessage = "";
    }
    
    public void openAddBookForm(BookController bc){
        clearExceptionMessage();
        addRegime = true;
        bc.setB(new Book());
        setCurrentForm(ADD_EDIT_BOOK);
    }
    
    public void addBook(BookController bc){
        try {
            System.out.println("ADD BOOK");
            bc.add();
            setCurrentForm(BOOK_CATALOG);
        } catch(Exception e){
            exceptionMessage = e.getMessage();
        }
    }
    
    public void openEditBookForm(){
        clearExceptionMessage();
        addRegime = false;
        setCurrentForm(ADD_EDIT_BOOK);
    }
    
    public void editBook(BookController bc){
        try {
            bc.edit();
            setCurrentForm(BOOK_CATALOG);
        } catch(Exception e){
            exceptionMessage = e.getMessage();
        }
    }
    
    public void openAddReaderForm(ReaderController rc){
        clearExceptionMessage();
        addRegime = true;
        rc.setR(new Reader());
        setCurrentForm(ADD_EDIT_READER);
    }
    
    public void addReader(ReaderController rc){
        try {
            rc.add();
            setCurrentForm(READER_CATALOG);
        } catch(Exception e){
            exceptionMessage = e.getMessage();
        }   
    }
    
    public void openEditReaderForm(){
        clearExceptionMessage();
        addRegime = false;
        setCurrentForm(ADD_EDIT_READER);
    }
    
    public void editReader(ReaderController rc){
        try {
            rc.edit();
            setCurrentForm(READER_CATALOG);
        } catch(Exception e){
            exceptionMessage = e.getMessage();
        }
    }
    
    public void openAddJournalEntryForm(LibraryController lc){
        clearExceptionMessage();
        addRegime = true;
        lc.setL(new Library());
        setCurrentForm(ADD_EDIT_JOURNAL_ENTRY);
    }
    
    public void addLibrary(LibraryController lc){
        try {
            lc.add();
            setCurrentForm(JOURNAL);
        } catch(Exception e){
            exceptionMessage = e.getMessage();
        }
    }
    
    public void openEditJournalEntryForm(){
        clearExceptionMessage();
        addRegime = false;
        setCurrentForm(ADD_EDIT_JOURNAL_ENTRY);
    }
    
    public void editLibrary(LibraryController lc){
        try {
            lc.edit();
            setCurrentForm(JOURNAL);
        } catch(Exception e){
            exceptionMessage = e.getMessage();
        } 
    }
    
}
