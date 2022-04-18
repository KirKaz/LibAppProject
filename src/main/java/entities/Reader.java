package entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;

@Entity
@Table(name = "reader")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reader.findAll", query = "SELECT r FROM Reader r WHERE r.onDelete = false")
    , @NamedQuery(name = "Reader.findByName", query = "SELECT r FROM Reader r WHERE r.name LIKE :name AND r.onDelete = false")
    , @NamedQuery(name = "Reader.findByPassport", query = "SELECT r FROM Reader r WHERE r.passport LIKE :passport AND r.onDelete = false")
})
public class Reader extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 50)
    @Column(name = "Name")
    private String name;
    @Size(max = 10)
    @Column(name = "Passport")
    private String passport;
    @Column(name = "OnDelete")
    private Boolean onDelete;
    @OneToMany(mappedBy = "readerID")
    private Collection<Library> libraryCollection;

    public Reader() {
    }

    public Reader(Integer id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reader)) {
            return false;
        }
        Reader other = (Reader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " \n[" + passport + "]";
    }
    
}
