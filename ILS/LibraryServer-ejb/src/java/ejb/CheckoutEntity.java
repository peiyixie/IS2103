/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Peiyi
 */
@Entity(name = "Checkout")
public class CheckoutEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String loanDate;
    private String dueDate;
    private String returnDate;
    @ManyToOne
    private BookEntity loanBook = new BookEntity();
    @ManyToOne
    private LibMemberEntity loaner = new LibMemberEntity();
    
    public LibMemberEntity getLoaner(){
        return loaner;
    }
    
    public void setLoaner(LibMemberEntity loaner){
        this.loaner = loaner;
    }
    
    public BookEntity getLoanBook(){
        return loanBook;
    }
    
    public void setLoanBook(BookEntity loanBook){
        this.loanBook = loanBook;
    }
    
    public CheckoutEntity() {
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void create(String loanDate, String dueDate, String returnDate) {
        this.setLoanDate(loanDate);
        this.setDueDate(dueDate);
        this.setReturnDate(returnDate);
    }
    
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof CheckoutEntity)) {
            return false;
        }
        CheckoutEntity other = (CheckoutEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.CheckoutEntity[ id=" + id + " ]";
    }

}
