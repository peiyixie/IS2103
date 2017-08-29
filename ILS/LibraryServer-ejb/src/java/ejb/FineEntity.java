/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Peiyi
 */
@Entity(name="Fine")
public class FineEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String date;
    private long amount;
    
    @OneToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "PAYMENT_ID")
    private PaymentEntity payment;    
    @ManyToOne
    private LibMemberEntity libMember = new LibMemberEntity();

    
    
    public FineEntity() {
    }      
    
    public void create(String date, long amount){
        this.setDate(date);
        this.setAmount(amount);
    }
    
    public PaymentEntity getPayment(){
        return payment;
    }
    
    public void setPayment(PaymentEntity payment){
       this.payment = payment;
    }
    
    public LibMemberEntity getLibMember() {
        return libMember;
    }

    public void setLibMember(LibMemberEntity libMember) {
        this.libMember = libMember;
    }
    
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        if (!(object instanceof FineEntity)) {
            return false;
        }
        FineEntity other = (FineEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.FineEntity[ id=" + id + " ]";
    }
    
}
