/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.ejb.Remote;

/**
 *
 * @author Peiyi
 */
@Remote
public interface LibraryServerBeanRemote {

    boolean addLibMember(String name, String password, String contactNumber, String email, String address);

    int delLibMember(String userName);

    List<Vector> viewRequests();

    boolean processRequest(long id, String status, String comment);

    public boolean addRequest(String userName, String content);

    Long addBooks(String isbn, int copyNo, String title, String publisher, String year);

    Long addAuthor(String authorName, String desc);

    boolean linkAuthorsToBook(long authorID, long bookID);

    int locateBookISBNCopyNo(String isbn, int copyNo);

    long removeBookISBNCopyNo(String isbn, int copyNo);

    boolean updateBook(long bookID, String isbn, int copyNo, String title, String publisher, String yearPub);

    long getBookID(String isbn, int copyNo);

    int checkoutBook(String userName, String isbn, int copyNo);

    int returnBook(String isbn, int copyNo);

    int makeReserve(String isbn, int copyNo, String userName, String notes);

    String viewReservesISBNCopyNo(String isbn, int copyNo);

    String viewCheckouts();

    String viewFines();

    boolean checkAuthor(long authorID);

    boolean checkLibMember(String userName);

    Vector getLibMember(String userName);

    boolean checkPassword(String toCheck, String password);

    boolean changeUserPass(String userName, String newPass);

    boolean changeUserDetails(String userName, String contactNo, String email, String address);

    String getCheckoutsWeb(String userName);

    ArrayList getFinesWeb(String userName);

    boolean makePayment(long fineID, String cardType, String cardNo, String holder);

    long getFineAmount(long fineID);

    ArrayList getRequestsWeb(String userName);

    ArrayList getSearchWeb(String title, String isbn, String author);

    String[] getBook(long bookID, String userName);

    String[] getAuthor(long authorID);
    
    
    
}
