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
@Entity(name="Request")
public class RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long timeCreated;
    private String message;
    private String status;
    private String comment;
    @ManyToOne
    private LibMemberEntity libMember = new LibMemberEntity();

    /**
     * Get the value of comment
     *
     * @param time
     * @param message
     * @param status
     * @param comment
     */
    
    public void create(long time, String message, String status, String comment) {
        this.setTimeCreated(time);
        this.setMessage(message);
        this.setStatus(status);
        this.setComment(comment);
    }
    
    public LibMemberEntity getLibMember() {
        return libMember;
    }

    public void setLibMember(LibMemberEntity libMember) {
        this.libMember = libMember;
    }
    
    
    public String getComment() {
        return comment;
    }

    /**
     * Set the value of comment
     *
     * @param comment new value of comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the value of timeCreated
     *
     * @return the value of timeCreated
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * Set the value of timeCreated
     *
     * @param timeCreated new value of timeCreated
     */
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
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
        if (!(object instanceof RequestEntity)) {
            return false;
        }
        RequestEntity other = (RequestEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.RequestEntity[ id=" + id + " ]";
    }
    
}
