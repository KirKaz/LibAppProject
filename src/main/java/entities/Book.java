package entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;

@Entity
@Table(name = "book")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b WHERE b.onDelete = false")
    , @NamedQuery(name = "Book.findAllInLibrary", query = "SELECT b FROM Book b WHERE b.inLibrary = true AND b.onDelete = false")
})
public class Book extends BaseEntity {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 50)
    @Column(name = "Author")
    private String author;
    @Size(max = 50)
    @Column(name = "Name")
    private String name;
    @Column(name = "Year")
    private Integer year;
    @Column(name = "InLibrary")
    private Boolean inLibrary;
    @Column(name = "OnDelete")
    private Boolean onDelete;
    @OneToMany(mappedBy = "bookID")
    private Collection<Library> libraryCollection;

    public Book() {
    }

    public Book(Integer id) {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getInLibrary() {
        return inLibrary;
    }

    public void setInLibrary(Boolean inLibrary) {
        this.inLibrary = inLibrary;
    }

    public Boolean getOnDelete() {
        return onDelete;
    }

    public void setOnDelete(Boolean onDelete) {
        this.onDelete = onDelete;
    }

    @XmlTransient
    public Collection<Library> getLibraryCollection() {
        return libraryCollection;
    }

    public void setLibraryCollection(Collection<Library> libraryCollection) {
        this.libraryCollection = libraryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return author + " \n\"" + name + "\"";
    }
    
}
