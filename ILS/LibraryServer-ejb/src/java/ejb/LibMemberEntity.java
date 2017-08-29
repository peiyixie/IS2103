/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;


/**
 *
 * @author Peiyi
 */
@Entity(name="LibMember")
public class LibMemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String userName; 
    private String password;
    private String contactNumber;    
    private String email;    
    private String address;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "libMember")
    private Collection<RequestEntity> requests = new ArrayList<RequestEntity>();
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "libMember")
    private Collection<FineEntity> fines = new ArrayList<FineEntity>();
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="loaner")
    private Collection<CheckoutEntity> checkouts = new ArrayList<CheckoutEntity>();
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="reserver")
    private Collection<ReservationEntity> reserves = new ArrayList<ReservationEntity>();


    public LibMemberEntity() {
    }

    public void create(String userName, String password, String contactNumber, 
            String email, String address){
        this.setUserName(userName);
        this.setAddress(address);
        this.setContactNumber(contactNumber);
        this.setEmail(email);
        this.setPassword(password);
    }
    
    public Collection<ReservationEntity> getReserves(){
        return reserves;
    }
    
    public void setReserves(Collection<ReservationEntity> reserves){
        this.reserves = reserves;
    }
    
    public Collection<FineEntity> getFines() {
        return fines;
    }

    public void setFines(Collection<FineEntity> fines) {
        this.fines = fines;
    }
    
    /**
     * Get the value of address
     *
     * @return the value of address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the value of address
     *
     * @param address new value of address
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of email
     *
     * @param email new value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the value of contactNumber
     *
     * @return the value of contactNumber
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Set the value of contactNumber
     *
     * @param contactNumber new value of contactNumber
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    public Collection<RequestEntity> getRequests() {
        return requests;
    }

    public void setReqeusts(Collection<RequestEntity> requests) {
        this.requests = requests;
    }
    
    public Collection<CheckoutEntity> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(Collection<CheckoutEntity> checkouts) {
        this.checkouts = checkouts;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userName fields are not set
        if (!(object instanceof LibMemberEntity)) {
            return false;
        }
        LibMemberEntity other = (LibMemberEntity) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.LibMemberEntity[ id=" + userName + " ]";
    }
    
}
