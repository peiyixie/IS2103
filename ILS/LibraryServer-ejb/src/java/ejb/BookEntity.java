/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author Peiyi
 */
@Entity(name="Book")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String publisher;
    private String yearPub;
    private String isbn;
    private int copyNo;
    @ManyToMany(cascade={CascadeType.PERSIST}, mappedBy="books")
    private Set<AuthorEntity> authors = new HashSet<AuthorEntity>();
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="loanBook")
    private Collection<CheckoutEntity> checkouts = new ArrayList<CheckoutEntity>();
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="reserveBook")
    private Collection<ReservationEntity> reserves = new ArrayList<ReservationEntity>();

    
    
    public BookEntity() {
    }
    
    public Set<AuthorEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorEntity> authors) {
        this.authors = authors;
    }
    
    
    public void create(String isbn, int copyNo, String title, String publisher, String year){
        this.setIsbn(isbn);
        this.setCopyNo(copyNo);
        this.setTitle(title);
        this.setPublisher(publisher);
        this.setYearPub(year);
    }

    public int getCopyNo() {
        return copyNo;
    }

    public void setCopyNo(int copyNo) {
        this.copyNo = copyNo;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getYearPub() {
        return yearPub;
    }

    
    public void setYearPub(String yearPub) {
        this.yearPub = yearPub;
    }


    
    public String getPublisher() {
        return publisher;
    }

    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<CheckoutEntity> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(Collection<CheckoutEntity> checkouts) {
        this.checkouts = checkouts;
    }
    
    public Collection<ReservationEntity> getReserves(){
        return reserves;
    }
    
    public void setReserves(Collection<ReservationEntity> reserves){
        this.reserves = reserves;
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
        if (!(object instanceof BookEntity)) {
            return false;
        }
        BookEntity other = (BookEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.BookEntity[ id=" + id + " ]";
    }
    
}
