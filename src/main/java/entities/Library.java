package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "library")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Library.findAll", query = "SELECT l FROM Library l WHERE l.onDelete = false")
    , @NamedQuery(name = "Library.getBadReaders", query = "SELECT l.readerID FROM Library l WHERE l.realReturnDate > l.returnDate OR (CURRENT_DATE > l.returnDate AND l.realReturnDate IS NULL) GROUP BY l.readerID")  
    , @NamedQuery(name = "Library.getDebtCountByReaderID", query = "SELECT COUNT(l) FROM Library l WHERE l.readerID.id = :readerID AND (l.realReturnDate > l.returnDate OR (CURRENT_DATE > l.returnDate AND l.realReturnDate IS NULL))")  
    , @NamedQuery(name = "Library.getNoReturn", query = "SELECT l FROM Library l WHERE l.realReturnDate IS NULL AND CURRENT_DATE > l.returnDate")
})
public class Library extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "IssueDate")
    @Temporal(TemporalType.DATE)
    private Date issueDate;
    @Column(name = "ReturnDate")
    @Temporal(TemporalType.DATE)
    private Date returnDate;
    @Column(name = "RealReturnDate")
    @Temporal(TemporalType.DATE)
    private Date realReturnDate;
    @Column(name = "OnDelete")
    private Boolean onDelete;
    @JoinColumn(name = "BookID", referencedColumnName = "ID")
    @ManyToOne
    private Book bookID;
    @JoinColumn(name = "ReaderID", referencedColumnName = "ID")
    @ManyToOne
    private Reader readerID;

    public Library() {
    }

    public Library(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getRealReturnDate() {
        return realReturnDate;
    }

    public void setRealReturnDate(Date realReturnDate) {
        this.realReturnDate = realReturnDate;
    }

    public Boolean getOnDelete() {
        return onDelete;
    }

    public void setOnDelete(Boolean onDelete) {
        this.onDelete = onDelete;
    }

    public Book getBookID() {
        return bookID;
    }

    public void setBookID(Book bookID) {
        this.bookID = bookID;
    }

    public Reader getReaderID() {
        return readerID;
    }

    public void setReaderID(Reader readerID) {
        this.readerID = readerID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Library)) {
            return false;
        }
        Library other = (Library) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Library[ id=" + id + " ]";
    }
    
}
