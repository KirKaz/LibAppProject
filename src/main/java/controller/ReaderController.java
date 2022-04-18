package controller;

import entities.*;
import model.ReaderFacade;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "readerController")
@SessionScoped
public class ReaderController implements Serializable {

    @EJB
    private ReaderFacade readerFacade;
    private Reader r;
    private String condition;
    private String findValue;
    private List<String> conditions;
    private List<Reader> readers;
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
        r = new Reader();
        condition = ReaderFacade.READER_FIND_FULLNAME;
        findValue = "";
        initConditions();
        readers = findAll();
        disabled = true;
    }
    
    private void initConditions(){
        conditions = new ArrayList<>();
        conditions.add(ReaderFacade.READER_FIND_FULLNAME);
        conditions.add(ReaderFacade.READER_FIND_PASSPORT);
    }
    
    public String getFindValue() {
        return findValue;
    }

    public void setFindValue(String findValue) {
        this.findValue = findValue;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Reader getR() {
        return r;
    }

    public void setR(Reader r) {
        this.r = r;
    }
  
    public List<String> getConditions() {
        return conditions;
    }
    
    public List<Reader> getReaders() {
        return readers; 
    }
    
    public void setReaders(List<Reader> readers){
        this.readers = readers;
    }
    
    public void find(){
        if(findValue.isEmpty()) readers = findAll();
        else if(ReaderFacade.READER_FIND_FULLNAME.equals(condition)) readers = findByName(findValue);
        else readers = findByPassport(findValue);
    }
    
    public ReaderController() {
    }
    
    public List<Reader> findAll(){
        return readerFacade.findAll();
    }
    
    public List<Reader> findByName(String name){
        return readerFacade.findByName(name);
    }
    
    public List<Reader> findByPassport(String passport){
        return readerFacade.findByPassport(passport);
    }
    
    public void clearFilters(){
        findValue = "";
        readers = findAll();
    }
    
    private void validate(Reader r) throws Exception {
        if(r.getName().trim().isEmpty())
            throw new Exception("Не указано ФИО читателя!");
        else if(r.getPassport().trim().isEmpty())
            throw new Exception("Не указан номер паспорта!");
        else if(r.getPassport().length() != 10)
            throw new Exception("Номер паспорта должен иметь длину 10 символов!");
        else if(!r.getPassport().matches("^[0-9]+$"))
            throw new Exception("Номер паспорта должен содержать только цифры!");
    }
    
    public void add() throws Exception {
        validate(r);
        r.setOnDelete(Boolean.FALSE);
        readerFacade.create(r);
        r = new Reader();
        refreshTable();
    }

    public void delete(){
        r.setOnDelete(Boolean.TRUE);
        readerFacade.edit(r);
        r = new Reader();
        refreshTable();
    }
    
    public void edit() throws Exception {
        validate(r);
        readerFacade.edit(r);
        r = new Reader();
        refreshTable();
    }
    
    public void refreshTable(){
        if(findValue.isEmpty()) readers = findAll();
        else if(ReaderFacade.READER_FIND_FULLNAME.equals(condition)) readers = findByName(findValue);
        else readers = findByPassport(findValue);
    }
}
