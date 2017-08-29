/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Peiyi
 */
@Stateless
public class LibraryServerBean implements LibraryServerBeanRemote {

    @PersistenceContext(unitName = "LibraryServer-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //return true if added, false if cannot be added
    @Override
    public boolean addRequest(String userName, String content) {
        LibMemberEntity temp = findLibMemberUserName(userName);

        //to prevent duplicate requests
        if (temp != null) {
            for (Object o : temp.getRequests()) {
                RequestEntity r = (RequestEntity) o;
                if (r.getMessage().equals(content)) {
                    return false;
                }
            }

            Calendar cal = Calendar.getInstance();
            long timeCreated = cal.getTimeInMillis();
            RequestEntity request = new RequestEntity();
            request.create(timeCreated, content, "UNREAD", "");
            request.setLibMember(temp);
            temp.getRequests().add(request);
            em.persist(request);
            return true;
        }
        return false;
    }

    @Override
    public boolean processRequest(long id, String status, String comment) {
        RequestEntity temp = em.find(RequestEntity.class, id);
        if (temp == null) {
            return false;
        }
        temp.setStatus(status);
        temp.setComment(comment);
        return true;
    }

    @Override
    public boolean addLibMember(String userName, String password, String contactNumber, String email, String address) {

        LibMemberEntity temp = findLibMemberUserName(userName);
        if (temp == null) {
            LibMemberEntity newLibMember = new LibMemberEntity();
            String hash = "";
            try {
                hash = encode(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            newLibMember.create(userName, hash, contactNumber, email, address);
            em.persist(newLibMember);//add in database
            return true;
        }
        return false;
    }

    private LibMemberEntity findLibMemberUserName(String userName) {
        LibMemberEntity temp = em.find(LibMemberEntity.class, userName);
        return temp;
    }

    private String encode(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    //return 1 if deleted, 0 if doesn't exist, -1 if cannot be deleted
    @Override
    public int delLibMember(String userName) {
        LibMemberEntity temp = findLibMemberUserName(userName);
        if (temp != null) {
            //have to add conditions
            //LibMember cannot have reservation: -3, checkout:-2, 
            //request: -1
            if (!temp.getCheckouts().isEmpty()) {
                return -2;
            }
            if (!temp.getReserves().isEmpty()) {
                return -3;
            }
            if (!temp.getRequests().isEmpty()) {
                return -1;
            }
            em.remove(temp);
            return 1;
        }
        return 0; // return 0 if LibMember doesn't exist
    }

    @Override
    public List<Vector> viewRequests() {
        Query q = em.createQuery("SELECT r FROM Request r WHERE r.status='UNREAD' OR r.status='PROCESSING'");
        List<Vector> requests = new ArrayList();
        for (Object o : q.getResultList()) {
            RequestEntity r = (RequestEntity) o;
            Vector req = new Vector();
            req.add(r.getId());
            req.add(r.getLibMember().getUserName());
            req.add(r.getMessage());
            req.add(r.getStatus());
            req.add(r.getComment());

            requests.add(req);
        }
        return requests;

    }

    //return true if can be added, return false if cannot be added
    @Override
    public Long addBooks(String isbn, int copyNo, String title, String publisher, String year) {
        List results = findBooksISBN(isbn);
        for (Object o : results) {
            BookEntity b = (BookEntity) o;
            if (b.getCopyNo() == copyNo) {
                return null;
            }
        }

        BookEntity newBook = new BookEntity();
        newBook.create(isbn, copyNo, title, publisher, year);
        em.persist(newBook);//add in database
        return newBook.getId();

    }

    private List findBooksISBN(String isbn) {
        List results = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbnNo")
                .setParameter("isbnNo", isbn).getResultList();
        return results;
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Long addAuthor(String authorName, String desc) {
        List results = em.createQuery("SELECT a FROM Author a WHERE a.authorName = :author")
                .setParameter("author", authorName).getResultList();
        for (Object o : results) {
            AuthorEntity a = (AuthorEntity) o;
            if (a.getDescription().equals(desc)) {
                return null;
            }
        }

        AuthorEntity newAuthor = new AuthorEntity();
        newAuthor.create(authorName, desc);
        em.persist(newAuthor);
        return newAuthor.getId();
    }

    @Override
    public boolean linkAuthorsToBook(long authorID, long bookID) {
        AuthorEntity authorTemp = findAuthorUsingID(authorID);
        if (authorTemp == null) {
            return false;
        }
        BookEntity bookTemp = findBookUsingID(bookID);
        if (!bookTemp.getAuthors().contains(authorTemp)) {
            bookTemp.getAuthors().add(authorTemp);
        }
        if (!authorTemp.getBooks().contains(bookTemp)) {
            authorTemp.getBooks().add(bookTemp);
        }
        em.persist(authorTemp);
        em.persist(bookTemp);
        em.flush();
        em.clear();
        return true;
    }

    private BookEntity findBookUsingID(long bookID) {
        return em.find(BookEntity.class, bookID);
    }

    private AuthorEntity findAuthorUsingID(long authorID) {
        return em.find(AuthorEntity.class, authorID);
    }

    /**
     *
     * @param isbn
     * @param copyNo
     * @return //return 1 if can be updated, 0 if doesn't exist, //return -1 if
     * associated with checkout, //return -2 if associated with reservation.
     */
    @Override
    public int locateBookISBNCopyNo(String isbn, int copyNo) {
        List temp = findBooksISBN(isbn);
        BookEntity thisBook = null;
        if (temp.isEmpty()) {
            return 0;
        }
        for (Object o : temp) {
            BookEntity b = (BookEntity) o;
            if (b.getCopyNo() == copyNo) {
                thisBook = b;
            }
        }
        if (thisBook != null) {
            if (!thisBook.getReserves().isEmpty()) {
                return -2;
            }
            if (!thisBook.getCheckouts().isEmpty()) {
                return -1;
            }
            return 1;
        }
        return 0;
    }

    //have to remove books from author also...
    @Override
    public long removeBookISBNCopyNo(String isbn, int copyNo) {
        List books = findBooksISBN(isbn);
        BookEntity thisBook = null;
        for (Object o : books) {
            BookEntity b = (BookEntity) o;
            if (b.getCopyNo() == copyNo) {
                thisBook = b;
            }
        }
        Set<AuthorEntity> listAuthors = thisBook.getAuthors();
        for (AuthorEntity author : listAuthors) {
            author.getBooks().remove(thisBook);
            em.merge(author);
        }

        if (thisBook == null) {
            return -1;
        }
        long temp = thisBook.getId();
        em.remove(thisBook);
        em.flush();
        em.clear();
        return temp;
    }

    @Override
    public boolean updateBook(long bookID, String isbn, int copyNo, String title, String publisher, String yearPub) {

        BookEntity thisBook = findBookUsingID(bookID);
        thisBook.setIsbn(isbn);
        thisBook.setCopyNo(copyNo);
        thisBook.setPublisher(publisher);
        thisBook.setTitle(title);
        thisBook.setYearPub(yearPub);

        Set<AuthorEntity> listAuthors = thisBook.getAuthors();
        for (AuthorEntity author : listAuthors) {
            author.getBooks().remove(thisBook);
        }

        thisBook.getAuthors().clear();

        return false;
    }

    @Override
    public long getBookID(String isbn, int copyNo) {
        List books = findBooksISBN(isbn);
        for (Object o : books) {
            BookEntity b = (BookEntity) o;
            if (b.getCopyNo() == copyNo) {
                return b.getId();
            }
        }
        return 0L;
    }

    @Override
    public int checkoutBook(String userName, String isbn, int copyNo) {
        BookEntity thisBook = findBookISBNCopyNo(isbn, copyNo);
        LibMemberEntity thisMember = findLibMemberUserName(userName);
        System.out.println("Book is: " + thisBook.getTitle());
        System.out.println("Member is: " + thisMember.getUserName());

        int numCheckouts = 0;
        for (Object o : thisMember.getCheckouts()) {
            CheckoutEntity c = (CheckoutEntity) o;
            if (c.getReturnDate().equals("Not returned yet")) {
                numCheckouts++;
            }
        }

        if (numCheckouts == 2) {
            return 2;//return 2 if member has 2 checkouts already
        }
        if (!thisMember.getFines().isEmpty()) {
            return 3;//return 3 if member has fines unpaid
        }
        for (Object o : thisBook.getCheckouts()) {
            CheckoutEntity c = (CheckoutEntity) o;
            if (c.getReturnDate().equals("Not returned yet")) {
                return 4;//return 4 if book has been checked out
            }
        }

        if (!thisBook.getReserves().isEmpty()) {
            Iterator<ReservationEntity> itr = thisBook.getReserves().iterator();
            ReservationEntity r1 = itr.next();
            if (!r1.getReserver().getUserName().equals(userName)) {
                return 5;
            } else {
                thisMember.getReserves().remove(r1);
                thisBook.getReserves().remove(r1);
                em.merge(thisMember);
                em.merge(thisBook);
                em.remove(r1);
                em.flush();
            }
        }

        CheckoutEntity thisCheckout = new CheckoutEntity();
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String loanDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 14);
        String dueDate = dateFormat.format(cal.getTime());
        thisCheckout.create(loanDate, dueDate, "Not returned yet");
        thisCheckout.setLoaner(thisMember);
        thisCheckout.setLoanBook(thisBook);
        thisMember.getCheckouts().add(thisCheckout);
        thisBook.getCheckouts().add(thisCheckout);
        em.persist(thisCheckout);
        em.merge(thisMember);
        em.merge(thisBook);
        em.flush();
        return 1;//return one if successfully updated
    }

    private BookEntity findBookISBNCopyNo(String isbn, int copyNo) {
        List books = findBooksISBN(isbn);
        for (Object o : books) {
            BookEntity b = (BookEntity) o;
            if (b.getCopyNo() == copyNo) {
                return b;
            }
        }
        return null;
    }

    //return 1 if successfully returned, 2 if fine created, 0 if no checkout
    @Override
    public int returnBook(String isbn, int copyNo) {
        BookEntity thisBook = findBookISBNCopyNo(isbn, copyNo);
        CheckoutEntity thisCheckout = null;
        LibMemberEntity thisLoaner = null;
        for (CheckoutEntity c : thisBook.getCheckouts()) {
            if (c.getReturnDate().equals("Not returned yet")) {
                thisCheckout = c;
                thisLoaner = c.getLoaner();
            }
        }
        if (thisCheckout == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String returnDateString = df.format(cal.getTime());
        thisCheckout.setReturnDate(returnDateString);
        Date dueDate = cal.getTime(), returnDate = cal.getTime();
        try {
            dueDate = df.parse(thisCheckout.getDueDate());
            returnDate = df.parse(returnDateString);
        } catch (ParseException ex) {
            Logger.getLogger(LibraryServerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (returnDate.after(dueDate)) {
            //create fine for user based on days passed
            long diff = returnDate.getTime() - dueDate.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            FineEntity fine = new FineEntity();
            fine.create(returnDateString, diff);
            fine.setLibMember(thisLoaner);
            thisLoaner.getFines().add(fine);
            em.persist(fine);
            return 2;
        }

        return 1;
    }

    /**
     *
     * @param isbn
     * @param copyNo
     * @param userName
     * @param notes
     * @return return 1 if book is not checked out return 2 if book has been
     * reserved by this user return 0 if book has been reserved successfully
     */
    @Override
    public int makeReserve(String isbn, int copyNo, String userName, String notes) {
        LibMemberEntity reserver = findLibMemberUserName(userName);
        BookEntity book = findBookISBNCopyNo(isbn, copyNo);
        boolean isCheckedOut = false;
        for (CheckoutEntity c : book.getCheckouts()) {
            if (c.getReturnDate().equals("Not returned yet")) {
                isCheckedOut = true;
            }
        }
        if (!isCheckedOut) {
            return 1;
        }

        for (ReservationEntity r : book.getReserves()) {
            if (r.getReserver().getUserName().equals(userName)) {
                return 2;
            }
        }

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String loanDateTime = dateFormat.format(cal.getTime());
        ReservationEntity reserve = new ReservationEntity();
        reserve.create(loanDateTime, notes);
        reserve.setReserver(reserver);
        reserve.setReserveBook(book);
        book.getReserves().add(reserve);
        reserver.getReserves().add(reserve);
        em.persist(reserve);
        em.merge(book);
        em.merge(reserver);
        em.flush();

        return 0;
    }

    @Override
    public String viewReservesISBNCopyNo(String isbn, int copyNo) {
        BookEntity thisBook = findBookISBNCopyNo(isbn, copyNo);
        if (thisBook == null) {
            return "Error! Incorrect book details.";
        }
        if (thisBook.getReserves().isEmpty()) {
            return "No reservations found!";
        }
        String result = "Title: " + thisBook.getTitle() + "\n";
        Iterator<ReservationEntity> itr = thisBook.getReserves().iterator();
        int count = 1;
        while (itr.hasNext()) {
            ReservationEntity r = itr.next();
            result = result + "Reservation " + count + "\n";
            result = result + "Username: " + r.getReserver().getUserName() + "\n";
            result = result + "Email: " + r.getReserver().getEmail() + "\n";
            result = result + "Reservation Time: " + r.getDateTime() + "\n";
            result = result + "Note: " + r.getNote() + "\n";
            count++;
        }
        return result;
    }

    @Override
    public String viewCheckouts() {
        List list = em.createQuery("SELECT c FROM Checkout c WHERE c.returnDate = 'Not returned yet'")
                .getResultList();

        String result = "";
        if (list.isEmpty()) {
            return "No current checkouts!";
        }

        int size = list.size();

        for (int i = 0; i < size; i++) {
            result = result + "Checkout " + (i + 1) + "\n";
            CheckoutEntity c = (CheckoutEntity) list.get(i);
            result = result + "ISBN: " + c.getLoanBook().getIsbn() + "\n";
            result = result + "Copy Number: " + c.getLoanBook().getCopyNo() + "\n";
            result = result + "Title:  " + c.getLoanBook().getTitle() + "\n";
            result = result + "Username: " + c.getLoaner().getUserName() + "\n";
            result = result + "Email: " + c.getLoaner().getEmail() + "\n";
            result = result + "Loan Date: " + c.getLoanDate() + "\n";
            result = result + "Due date: " + c.getDueDate() + "\n\n";
        }
        return result;
    }

    @Override
    public String viewFines() {
        List list = em.createQuery("SELECT f FROM Fine f WHERE f.amount != 0").getResultList();
        //need to check

        String result = "";

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<FineEntity> unpaidList = new ArrayList<FineEntity>();
        for (Object o : list) {
            FineEntity f = (FineEntity) o;
            if (f.getPayment() == null) {
                unpaidList.add(f);
            }
        }

        if (unpaidList.isEmpty()) {
            return "No unpaid fines!";
        }

        int size = unpaidList.size();
        for (int i = 0; i < size; i++) {
            result = result + "Fine " + (i + 1) + "\n";
            FineEntity f = (FineEntity) unpaidList.get(i);
            result = result + "Fine Date: " + f.getDate() + "\n";
            result = result + "Fine Amount: $" + f.getAmount() + "\n";
            LibMemberEntity m = f.getLibMember();
            //
            //
            List checkoutList = em.createQuery("SELECT c FROM Checkout c WHERE c.loaner.userName = :userName AND c.returnDate = :fineDate")
                    .setParameter("userName", m.getUserName())
                    .setParameter("fineDate", f.getDate())
                    .getResultList();

            CheckoutEntity checkout;

            if (!names.contains(m.getUserName() + f.getDate())) {
                checkout = (CheckoutEntity) checkoutList.get(0);
            } else {
                checkout = (CheckoutEntity) checkoutList.get(1);
            }
            names.add(m.getUserName() + f.getDate());
            //
            result = result + "Title: " + checkout.getLoanBook().getTitle() + "\n";
            result = result + "Username: " + m.getUserName() + "\n";
            result = result + "Email: " + m.getEmail() + "\n\n";

        }
        return result;

    }

    @Override
    public boolean checkAuthor(long authorID) {
        AuthorEntity author = findAuthorUsingID(authorID);
        if (author == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean checkLibMember(String userName) {

        LibMemberEntity user = findLibMemberUserName(userName);
        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public Vector getLibMember(String userName) {
        LibMemberEntity user = findLibMemberUserName(userName);
        Vector data = new Vector();
        if (user != null) {
            data.add(user.getUserName());
            data.add(user.getPassword());
            data.add(user.getAddress());
            data.add(user.getContactNumber());
            data.add(user.getEmail());

        }
        return data;
    }

    @Override
    public boolean checkPassword(String toCheck, String password) {
        String hashedToCheck = "";
        try {
            hashedToCheck = encode(toCheck);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LibraryServerBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (hashedToCheck.equals(password)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean changeUserPass(String userName, String newPass) {
        LibMemberEntity user = findLibMemberUserName(userName);
        try {
            newPass = encode(newPass);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LibraryServerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        user.setPassword(newPass);
        em.merge(user);
        em.flush();
        return true;
    }

    @Override
    public boolean changeUserDetails(String userName, String contactNo, String email, String address) {
        LibMemberEntity user = findLibMemberUserName(userName);
        user.setAddress(address);
        user.setEmail(email);
        user.setContactNumber(contactNo);
        em.merge(user);
        em.flush();
        return true;
    }

    @Override
    public String getCheckoutsWeb(String userName) {
        LibMemberEntity user = findLibMemberUserName(userName);
        List list = em.createQuery("SELECT c FROM Checkout c WHERE c.loaner.userName = :userName")
                .setParameter("userName", userName).getResultList();

        if (list.isEmpty()) {
            return "No checkout record!";
        }

        String result = "Current checkouts: <br>";
        for (Object o : list) {
            CheckoutEntity c = (CheckoutEntity) o;
            if (c.getReturnDate().equals("Not returned yet")) {
                result = result + "Title: ";
                result = result + c.getLoanBook().getTitle() + "&emsp;&emsp;&emsp;&emsp;";
                result = result + "ISBN: ";
                result = result + c.getLoanBook().getIsbn() + "<br>";
                result = result + "Checkout Date: ";
                result = result + c.getLoanDate() + "&emsp;&emsp;&emsp;&emsp;";
                result = result + "Due Date: ";
                result = result + c.getDueDate() + "<br>";
            }
        }
        result = result + "<br>Previous checkouts: <br>";
        for (Object o : list) {
            CheckoutEntity c = (CheckoutEntity) o;
            if (!c.getReturnDate().equals("Not returned yet")) {
                result = result + "Title: ";
                result = result + c.getLoanBook().getTitle() + "&emsp;&emsp;&emsp;&emsp;";
                result = result + "ISBN: ";
                result = result + c.getLoanBook().getIsbn() + "<br>";
                result = result + "Checkout Date: ";
                result = result + c.getLoanDate() + "&emsp;&emsp;&emsp;&emsp;";
                result = result + "Return Date: ";
                result = result + c.getReturnDate() + "<br>";
            }
        }

        return result;
    }

    @Override
    public ArrayList getFinesWeb(String userName) {
        LibMemberEntity user = findLibMemberUserName(userName);
        List list = em.createQuery("SELECT f FROM Fine f WHERE f.libMember = :userName")
                .setParameter("userName", user).getResultList();

        if (list.isEmpty()) {
            return null;
        }

        ArrayList<Long> listID = new ArrayList<Long>();
        ArrayList<Long> listAmount = new ArrayList<Long>();
        ArrayList<String> listFineDate = new ArrayList<String>();
        ArrayList<String> listPayDate = new ArrayList<String>();

        for (Object o : list) {
            FineEntity f = (FineEntity) o;
            listID.add(f.getId());
            listAmount.add(f.getAmount());
            listFineDate.add(f.getDate());
            if (f.getPayment() == null) {
                listPayDate.add("Payment due");
            } else {
                listPayDate.add(f.getPayment().getTimePayment());
            }
        }

        ArrayList result = new ArrayList();
        result.add(listID);
        result.add(listAmount);
        result.add(listFineDate);
        result.add(listPayDate);

        return result;
    }

    @Override
    public boolean makePayment(long fineID, String cardType, String cardNo, String holder) {
        FineEntity temp = em.find(FineEntity.class, fineID);
        PaymentEntity newPay = new PaymentEntity();
        newPay.setCardHolder(holder);
        newPay.setCardNo(cardNo);
        newPay.setCardType(cardType);
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String timePay = dateFormat.format(cal.getTime());
        newPay.setTimePayment(timePay);
        em.persist(newPay);
        temp.setPayment(newPay);
        em.merge(temp);
        em.flush();
        return true;
    }

    @Override
    public long getFineAmount(long fineID) {
        FineEntity temp = em.find(FineEntity.class, fineID);
        if (temp == null) {
            return 0L;
        }
        return temp.getAmount();
    }

    @Override
    public ArrayList getRequestsWeb(String userName) {
        LibMemberEntity user = findLibMemberUserName(userName);
        List list = em.createQuery("SELECT r FROM Request r WHERE r.libMember = :userName")
                .setParameter("userName", user).getResultList();

        if (list.isEmpty()) {
            return null;
        }

        ArrayList<String> reqDate = new ArrayList<String>();
        ArrayList<String> reqState = new ArrayList<String>();
        ArrayList<String> reqMessage = new ArrayList<String>();
        ArrayList<String> reqComment = new ArrayList<String>();

        for (Object o : list) {
            RequestEntity r = (RequestEntity) o;
            Date date = new Date(r.getTimeCreated());
            SimpleDateFormat df = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
            reqDate.add(df.format(date));
            reqState.add(r.getStatus());
            reqMessage.add(r.getMessage());
            reqComment.add(r.getComment());
        }

        ArrayList result = new ArrayList();
        result.add(reqDate);
        result.add(reqState);
        result.add(reqMessage);
        result.add(reqComment);

        return result;
    }

    @Override
    public ArrayList getSearchWeb(String title, String isbn, String author) {

        ArrayList result = new ArrayList();
        ArrayList<Long> bookID = new ArrayList<Long>();
        ArrayList<String> bookInfo = new ArrayList<String>();
        List list = new ArrayList();

        if (!title.equals("") && !isbn.equals("")) {
            list = em.createQuery("SELECT b FROM Book b WHERE  b.title LIKE :title "
                    + "AND  b.isbn LIKE :isbn")
                    .setParameter("title", ('%' + title + '%'))
                    .setParameter("isbn", ('%' + isbn + '%'))
                    .getResultList();
        }
        if (!title.equals("") && isbn.equals("")) {
            list = em.createQuery("SELECT b FROM Book b WHERE  b.title LIKE :title")
                    .setParameter("title", ('%' + title + '%'))
                    .getResultList();
        }

        if (title.equals("") && !isbn.equals("")) {
            list = em.createQuery("SELECT b FROM Book b WHERE  b.isbn LIKE :isbn")
                    .setParameter("isbn", ('%' + isbn + '%'))
                    .getResultList();
        }

        if (title.equals("") && isbn.equals("")) {
            list = em.createQuery("SELECT b FROM Book b")
                    .getResultList();
        }

        for (Object o : list) {
            BookEntity b = (BookEntity) o;
            Collection<AuthorEntity> authors = (Collection<AuthorEntity>) b.getAuthors();

            boolean hasAuthor = false;
            for (AuthorEntity a : authors) {
                if (a.getAuthorName().toUpperCase().contains(author.toUpperCase())) {
                    hasAuthor = true;
                }
            }

            if (hasAuthor) {
                bookID.add(b.getId());
                String info = "Title: ";
                info = info + b.getTitle() + "<br>" + "Author: ";
                int count = 0;
                for (AuthorEntity a : authors) {
                    info = info + a.getAuthorName();
                    if (count != authors.size() - 1) {
                        info = info + ", ";
                    } else {
                        info = info + "<br>";
                    }
                    count++;
                }
                List checkoutList = em.createQuery("SELECT c FROM Checkout c WHERE c.loanBook =:book AND c.returnDate = 'Not returned yet' ")
                        .setParameter("book", b)
                        .getResultList();
                if (checkoutList.isEmpty()) {
                    info = info + "Book Available";
                } else {
                    info = info + "Book Not Available <br>";
                    CheckoutEntity c = (CheckoutEntity) checkoutList.get(0);
                    info = info + "Due Date: " + c.getDueDate();
                }
                bookInfo.add(info);
            }
        }

        result.add(bookID);
        result.add(bookInfo);

        return result;

    }

    @Override
    public String[] getBook(long bookID, String userName) {
        LibMemberEntity libMember = findLibMemberUserName(userName);
        BookEntity thisBook = findBookUsingID(bookID);
        Integer authorNo = thisBook.getAuthors().size();
        System.out.println("authorNo is " + authorNo);

        Collection<AuthorEntity> authors = thisBook.getAuthors();
        String[] result = new String[authorNo * 2 + 6];
        result[0] = "Title: " + thisBook.getTitle() + "<br>Author: ";
        result[1] = authorNo.toString();
        int index = 2;
        for (Object o : authors) {
            AuthorEntity a = (AuthorEntity) o;
            result[index] = a.getId().toString();
            index++;
            result[index] = a.getAuthorName();
            index++;
        }
        String infoRest = "Publisher: ";
        infoRest = infoRest + thisBook.getPublisher() + "<br>Publication Year: ";
        infoRest = infoRest + thisBook.getYearPub() + "<br>ISBN: ";
        infoRest = infoRest + thisBook.getIsbn() + "<br>Copy Number: ";
        infoRest = infoRest + thisBook.getCopyNo() + "<br>";

        List checkoutList = em.createQuery("SELECT c FROM Checkout c WHERE c.loanBook =:book AND c.returnDate = 'Not returned yet' ")
                .setParameter("book", thisBook)
                .getResultList();
        if (checkoutList.isEmpty()) {
            infoRest = infoRest + "Book Available<br><br>";
            result[5 + authorNo * 2] = "cannot";

        } else {
            infoRest = infoRest + "Book Not Available <br>";
            CheckoutEntity c = (CheckoutEntity) checkoutList.get(0);
            infoRest = infoRest + "Due Date: " + c.getDueDate() + "<br>";
            result[5 + authorNo * 2] = "can";
        }

        //need to add reservation info if have any
        List resList = em.createQuery("SELECT r FROM Reservation r WHERE r.reserveBook =:book AND r.reserver = :user ")
                .setParameter("book", thisBook)
                .setParameter("user", libMember)
                .getResultList();
        if (!resList.isEmpty()) {//if have reservation
            ReservationEntity r = (ReservationEntity) resList.get(0);
            infoRest = infoRest + "Book reserved at " + r.getDateTime();
            result[5 + authorNo * 2] = "cannot";
        }

        result[2 + authorNo * 2] = infoRest;
        result[3 + authorNo * 2] = thisBook.getIsbn();
        result[4 + authorNo * 2] = "" + thisBook.getCopyNo();

//      String[]  
//       0. Title: title + <br>Author:
//       1. authorNo, need to parse
//        2. author1 ID in string, need to parse    
//        3. author1 name  
//        4. author2 ID in string, need to parse   
//        5. author2 name
//       6. rest of info    = 2 + authorNo*2
//       7. isbn            = 3 + authorNo*2
//       8. copyNo          = 4 + authorNo*2
//       9. canReserve      = 5 + authorNo*2
        return result;

    }

    @Override
    public String[] getAuthor(long authorID) {
        AuthorEntity thisAuthor = findAuthorUsingID(authorID);
        Integer bookNum = thisAuthor.getBooks().size();
        Collection<BookEntity> bookList = thisAuthor.getBooks();
//        List bookList = em.createQuery("SELECT b FROM Book b WHERE :author MEMBER OF b.authors")
//                .setParameter("author", thisAuthor)
//                .getResultList();

        String[] result = new String[bookNum * 2 + 1];
        String authorInfo = "Name: " + thisAuthor.getAuthorName() + "<br>"
                + "Arthor Description: " + thisAuthor.getDescription() + "<br> Books by Author <br>";
        result[0] = authorInfo;
        int index = 1;
        for (BookEntity b : bookList) {
            result[index] = b.getId().toString();
            index++;
            result[index] = "Title: " + b.getTitle() + "<br>Atuhors: ";
            Collection<AuthorEntity> authors = b.getAuthors();
            int count = 1;
            for (Object o : authors) {
                AuthorEntity a = (AuthorEntity) o;
                result[index] = result[index] + a.getAuthorName();
                if (count != b.getAuthors().size()) {
                    result[index] = result[index] + ", ";
                }
                count++;
            }
            result[index] = result[index] + "<br>ISBN: " + b.getIsbn() + "<br>";
            
            List checkoutList = em.createQuery("SELECT c FROM Checkout c WHERE c.loanBook =:book AND c.returnDate = 'Not returned yet' ")
                    .setParameter("book", b)
                    .getResultList();
            if (checkoutList.isEmpty()) {
                result[index] = result[index] + "Book Available<br><br>";
            } else {
                result[index] = result[index] + "Book Not Available <br>";
                CheckoutEntity c = (CheckoutEntity) checkoutList.get(0);
                result[index] = result[index] + "Due Date: " + c.getDueDate();
            }
            index++;
        }

//      0.         Name: Jane Doe
//                 Author Description: Popular writer on technology. Books by Author
//      1.   bookID 1 need to parse
//      2.   bookInfo 1
//      3.   bookID 2
//      4.   bookInfo 2
//      
//
//                
        return result;
    }

}
