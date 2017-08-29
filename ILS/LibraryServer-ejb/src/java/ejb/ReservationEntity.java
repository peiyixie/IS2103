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
@Entity(name="Reservation")
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String dateTime;
    private String note;
    
    @ManyToOne
    private BookEntity reserveBook = new BookEntity();
    @ManyToOne
    private LibMemberEntity reserver = new LibMemberEntity();

    public ReservationEntity() {
    }

    public void create(String dateTime, String note){
        this.setDateTime(dateTime);
        this.setNote(note);
    }
    
    public LibMemberEntity getReserver(){
        return reserver;
    }
    
    public void setReserver(LibMemberEntity reserver){
        this.reserver = reserver;
    }
    
    public BookEntity getReserveBook(){
        return reserveBook;
    }
    
    public void setReserveBook(BookEntity reserveBook){
        this.reserveBook = reserveBook;
    }
    
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.ReservationEntity[ id=" + id + " ]";
    }
    
}
