/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminclient;

import ejb.LibraryServerBeanRemote;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Vector;
import javax.ejb.EJB;

/**
 *
 * @author Peiyi
 */
public class Main {

    @EJB
    private static LibraryServerBeanRemote libraryServerBean;

    private static void populate() {
        libraryServerBean.addLibMember("test", "123456", "00000000", "113@142.com", "gotham");
        libraryServerBean.addLibMember("haha", "123458", "00000001", "133@142.com", "cupertino");
        libraryServerBean.addLibMember("hehe", "123452", "00000002", "153@142.com", "newyork");
        libraryServerBean.addLibMember("dede", "123452", "00000003", "173@142.com", "beverlyhills");
        libraryServerBean.addRequest("test", "When will the website be fixed?");
        libraryServerBean.addRequest("haha", "Cannot update my particulars.");

        Long book1 = libraryServerBean.addBooks("T123456", 1, "Lady Windermere", "NUS", "2016");
        Long book2 = libraryServerBean.addBooks("T654321", 1, "English", "NUS", "2014");
        Long book3 = libraryServerBean.addBooks("T654321", 2, "English", "NUS", "2014");
        Long book4 = libraryServerBean.addBooks("9999999", 1, "Who's There", "Guardian", "2010");
        Long book5 = libraryServerBean.addBooks("8888888", 1, "Knock Knock", "Guardian", "2011");
        Long book6 = libraryServerBean.addBooks("7777777", 1, "Who am I", "Guardian", "2012");
        Long book7 = libraryServerBean.addBooks("6666666", 1, "Why Me", "Guardian", "2013");
        Long book8 = libraryServerBean.addBooks("5555555", 1, "Romance of Three Kingdoms", "Watson", "1987");
        Long book9 = libraryServerBean.addBooks("5555555", 2, "Romance of Three Kingdoms", "Watson", "1987");
        Long book10 = libraryServerBean.addBooks("5555555", 3, "Romance of Three Kingdoms", "Watson", "1988");
        Long book11 = libraryServerBean.addBooks("4444444", 1, "In the Mood for Love", "The King", "1987");

        Long author1 = libraryServerBean.addAuthor("Jack London", "Socialist");
        Long author2 = libraryServerBean.addAuthor("Bernard Shaw", "Playwright");
        Long author3 = libraryServerBean.addAuthor("Xie Peiyi", "Student Writer");
        Long author4 = libraryServerBean.addAuthor("Wong Kar-Wai", "Filmmaker");
        Long author5 = libraryServerBean.addAuthor("Stanley Kubrick", "Genius");
        Long author6 = libraryServerBean.addAuthor("Tsai Ming-liang", "Master");

        if (author1 != null) {
            libraryServerBean.linkAuthorsToBook(author1, book1);
            libraryServerBean.linkAuthorsToBook(author2, book1);
            libraryServerBean.linkAuthorsToBook(author1, book2);
            libraryServerBean.linkAuthorsToBook(author2, book3);
            libraryServerBean.linkAuthorsToBook(author3, book4);
            libraryServerBean.linkAuthorsToBook(author4, book5);
            libraryServerBean.linkAuthorsToBook(author5, book6);
            libraryServerBean.linkAuthorsToBook(author6, book7);
            libraryServerBean.linkAuthorsToBook(author2, book8);
            libraryServerBean.linkAuthorsToBook(author2, book9);
            libraryServerBean.linkAuthorsToBook(author2, book10);
            libraryServerBean.linkAuthorsToBook(author5, book11);
        }

        libraryServerBean.checkoutBook("test", "T654321", 1);
        libraryServerBean.checkoutBook("test", "T654321", 2);
        libraryServerBean.checkoutBook("haha", "8888888", 1);
        libraryServerBean.checkoutBook("dede", "9999999", 1);
        libraryServerBean.checkoutBook("dede", "4444444", 1);

        libraryServerBean.makeReserve("T654321", 1, "dede", "testkekeke");
        libraryServerBean.makeReserve("T654321", 1, "haha", "hahakekeke");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Main client = new Main();
        //populate();
        
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("\nPlease enter your choice:\n1. Add user\n"
                    + "2. Delete user\n3. Add author\n4. Add book\n"
                    + "5. Update book\n6. Delete book\n7. Checkout book\n"
                    + "8. Return book\n9. View reservations\n10. View checkouts\n"
                    + "11. View fines\n12. Process Request");
            System.out.print("Enter your choice: ");                        
            String commandS = sc.next();
            
            while(!commandS.matches("\\d+")){
                System.out.print("Enter your choice again, invalid input: ");                                        
                commandS = sc.next();            
            }            
            int command = Integer.parseInt(commandS);                        
            sc.nextLine();
            switch (command) {
                case 1:
                    client.addUser(sc);
                    break;
                case 2:
                    client.delUser(sc);
                    break;

                case 3:
                    client.addAuthor(sc);
                    break;
                case 4:
                    client.addBook(sc);
                    break;

                case 5:
                    client.updateBook(sc);
                    break;

                case 6:
                    client.delBook(sc);
                    break;

                case 7:
                    client.checkoutBook(sc);
                    break;

                case 8:
                    client.returnBook(sc);
                    break;

                case 9:
                    client.viewReserves(sc);
                    break;

                case 10:
                    client.viewCheckouts();
                    break;

                case 11:
                    client.viewFines();
                    break;

                case 12:
                    client.processRequest(sc);
                    break;
                default:
                    break;
            }
        }

    }

    private void processRequest(Scanner sc) {
        for (Object o : libraryServerBean.viewRequests()) {
            Vector item = (Vector) o;
            System.out.println(item.get(0) + ": " + item.get(1) + ", " + item.get(2) + ", "
                    + item.get(3) + ", \"" + item.get(4) + "\"");

        }
        System.out.print("\nProcess Requests \n");        
        
        System.out.print("\nEnter the request ID: ");
        int id = sc.nextInt();
        System.out.print("Change the status to: ");
        String status = sc.next().toUpperCase();
        sc.nextLine();
        System.out.print("Comments: ");
        String comment = sc.nextLine();
        if (libraryServerBean.processRequest(id, status, comment)) {
            System.out.println("Request has been updated.");
        } else {
            System.out.println("ID cannot be found.");
        }
    }

    private void addUser(Scanner sc) {
        System.out.print("\nAdd User \n");  
        System.out.print("Enter user name: ");

        String userName = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();

        System.out.print("Enter contact number: ");
        String contact = sc.nextLine().trim();
        while (!contact.matches("\\d+")) {
            System.out.print("Invalid contact number, enter again: ");
            contact = sc.nextLine().trim();
        }

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        while (!email.matches("[\\w\\.]+@[\\w\\.]+")) {
            System.out.print("Invalid email format, enter again: ");
            email = sc.nextLine().trim();
        }

        System.out.print("Enter address: ");
        String address = sc.nextLine().trim();

        try {
            password = md5Hash(password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e);
        }
        if (libraryServerBean.addLibMember(userName, password, contact, email, address)) {
            System.out.println("Account has been successfully created!");
        } else {
            System.out.println("Error! Account already exists!");
        }
    }

    private String md5Hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private void delUser(Scanner sc) {
        System.out.print("\nDelete User \n");  
        
        
        System.out.print("Enter username to delete: ");
        String userName = sc.next();
        int result = libraryServerBean.delLibMember(userName);
        if (result == 1) {
            System.out.println("Account successfully deleted!");
        } else if (result == 0) {
            System.out.println("Error! Account does not exist!");
        } else if (result == -1) {
            System.out.println("Error! Member has requests!");
        } else if (result == -2) {
            System.out.println("Error! Member has checkouts!");
        } else {
            System.out.println("Error! Member has reservations!");
        }

    }

    private void addBook(Scanner sc) {
        System.out.print("\nAdd Book \n");  
        
        System.out.print("Enter Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Enter Publisher: ");
        String publisher = sc.nextLine().trim();
        System.out.print("Enter Publication Year: ");
        String year = sc.nextLine().trim();
        while (!year.matches("\\d+")) {
            System.out.print("Invalid year format, enter again: ");
            year = sc.nextLine().trim();
        }
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Enter Copy Number: ");
        String copyNoString = sc.nextLine().trim();
        while (!copyNoString.matches("\\d+")) {
            System.out.print("Invalid copy number format, enter again: ");
            copyNoString = sc.nextLine().trim();
        }
        int copyNo = Integer.parseInt(copyNoString);

        Long newBookId = libraryServerBean.addBooks(isbn, copyNo, title, publisher, year);

        System.out.print("Enter Number of Authors: ");
        String authorNoString = sc.nextLine().trim();
        while (!authorNoString.matches("\\d+")) {
            System.out.print("Invalid author number, enter again: ");
            authorNoString = sc.nextLine().trim();
        }
        int authorNo = Integer.parseInt(authorNoString);
        for (int i = 0; i < authorNo; i++) {
            System.out.print("Enter Id of Author " + (i + 1) + ": ");
            String authorID = sc.nextLine().trim();
            while (!authorID.matches("\\d+")) {
                System.out.print("Invalid author ID format, enter again: ");
                authorID = sc.nextLine().trim();
            }
            long authorIDLong = Long.parseLong(authorID);
            while (!libraryServerBean.checkAuthor(authorIDLong)) {
                System.out.println("Author does not exist!");
                System.out.print("Enter Id of Author " + (i + 1) + ": ");
                authorID = sc.nextLine().trim();
                while (!authorID.matches("\\d+")) {
                    System.out.print("Invalid author ID format, enter again: ");
                    authorID = sc.nextLine().trim();
                }
                authorIDLong = Long.parseLong(authorID);
            }
            libraryServerBean.linkAuthorsToBook(authorIDLong, newBookId);
        }
        if (newBookId != null) {
            System.out.println("Book successfully added!");
            System.out.println("Book ID: " + newBookId);
        } else {
            System.out.println("Duplicate book! Not added to the system.");
        }
    }

    private void addAuthor(Scanner sc) {
        System.out.print("\nAdd Author \n");  
        
        System.out.print("Enter Name: ");
        String authorName = sc.nextLine().trim();
        System.out.print("Enter description: ");
        String desc = sc.nextLine().trim();
        Long temp = libraryServerBean.addAuthor(authorName, desc);
        if (temp != null) {
            System.out.println("Author successfully added!");
            System.out.println("Author ID: " + temp);
        } else {
            System.out.println("Error!");
        }

    }

    private void updateBook(Scanner sc) {
        System.out.print("\nUpdate Book \n");  
                
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Eter Copy Number: ");
        String copyNoString = sc.nextLine().trim();
        while (!copyNoString.matches("\\d+")) {
            System.out.print("Invalid copy number format, enter again: ");
            copyNoString = sc.nextLine().trim();
        }
        int copyNo = Integer.parseInt(copyNoString);
        int result = libraryServerBean.locateBookISBNCopyNo(isbn, copyNo);
        switch (result) {
            case -2:
                System.out.println("Book associated with reservation. Cannot update!");
                break;
            case -1:
                System.out.println("Book associated with checkout. Cannot update!");
                break;
            case 0:
                System.out.println("Book not found!");
                break;
            default:
                long bookID = libraryServerBean.getBookID(isbn, copyNo);
                System.out.println("Enter new details of the book");
                System.out.print("Enter New Title: ");
                String titleNew = sc.nextLine().trim();
                System.out.print("Enter New Publisher: ");
                String publisherNew = sc.nextLine().trim();
                System.out.print("Enter New Publication year: ");
                String yearNew = sc.nextLine().trim();
                while (!yearNew.matches("\\d+")) {
                    System.out.print("Invalid year format, enter again: ");
                    yearNew = sc.nextLine().trim();
                }
                System.out.print("Enter New ISBN: ");
                String isbnNew = sc.nextLine().trim();
                System.out.print("Enter New Copy Number: ");
                String copyNoNewString = sc.nextLine().trim();
                while (!copyNoNewString.matches("\\d+")) {
                    System.out.print("Invalid copy number format, enter again: ");
                    copyNoNewString = sc.nextLine().trim();
                }
                int copyNoNew = Integer.parseInt(copyNoNewString);
                libraryServerBean.updateBook(bookID, isbnNew, copyNoNew, titleNew,
                        publisherNew, yearNew);
                System.out.println("Enter New Number of Authors: ");
                String authorNoString = sc.nextLine().trim();
                while (!authorNoString.matches("\\d+")) {
                    System.out.print("Invalid author number, enter again: ");
                    authorNoString = sc.nextLine().trim();
                }
                int authorNo = Integer.parseInt(authorNoString);
                for (int i = 0; i < authorNo; i++) {
                    System.out.print("Enter Id of Author " + (i + 1) + ": ");
                    String authorID = sc.nextLine().trim();
                    while (!authorID.matches("\\d+")) {
                        System.out.print("Invalid author ID format, enter again: ");
                        authorID = sc.nextLine().trim();
                    }
                    long authorIDLong = Long.parseLong(authorID);
                    while (!libraryServerBean.checkAuthor(authorIDLong)) {
                        System.out.println("Author does not exist!");
                        System.out.print("Enter Id of Author " + (i + 1) + ": ");
                        authorID = sc.nextLine().trim();
                        while (!authorID.matches("\\d+")) {
                            System.out.print("Invalid author ID format, enter again: ");
                            authorID = sc.nextLine().trim();
                        }
                        authorIDLong = Long.parseLong(authorID);
                    }
                    libraryServerBean.linkAuthorsToBook(authorIDLong, bookID);
                }
                System.out.println("Book successfully updated!");
                break;
        }

    }

    private void delBook(Scanner sc) {
        System.out.print("\nDelete Book \n");                  
        
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine();
        System.out.print("Eter Copy Number: ");
        String copyNoString = sc.nextLine().trim();
        while (!copyNoString.matches("\\d+")) {
            System.out.print("Invalid copy number format, enter again: ");
            copyNoString = sc.nextLine().trim();
        }
        int copyNo = Integer.parseInt(copyNoString);
        int result = libraryServerBean.locateBookISBNCopyNo(isbn, copyNo);
        switch (result) {
            case -2:
                System.out.println("Book cannot be deleted! Associated with reservation.");
                break;
            case -1:
                System.out.println("Book cannot be deleted! Associated with checkouts.");
                break;
            case 0:
                System.out.println("Book not found!");
                break;
            default:
                libraryServerBean.removeBookISBNCopyNo(isbn, copyNo);
                System.out.println("Book successfully deleted!");
                break;
        }
    }

    private void checkoutBook(Scanner sc) {
        System.out.println("\nCheckout Book");
        
        System.out.print("Enter Username: ");
        String userName = sc.nextLine().trim();
        while(!libraryServerBean.checkLibMember(userName)){
            System.out.println("Library member doesn't exist!");
            System.out.print("Enter Username: ");
            userName = sc.nextLine().trim();
        }            
        //need to put validation for user
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Enter Copy Number: ");
        String copyNoString = sc.nextLine().trim();
        while (!copyNoString.matches("\\d+")) {
            System.out.print("Invalid copy number format, enter again: ");
            copyNoString = sc.nextLine().trim();
        }
        int copyNo = Integer.parseInt(copyNoString);
        //need to put validation for book

        while (libraryServerBean.locateBookISBNCopyNo(isbn, copyNo) == 0) {
            System.out.println("Book does not exist, enter again.");
            System.out.print("Enter ISBN: ");
            isbn = sc.nextLine().trim();
            System.out.print("Enter Copy Number: ");
            copyNoString = sc.nextLine().trim();
            while (!copyNoString.matches("\\d+")) {
                System.out.print("Invalid copy number format, enter again: ");
                copyNoString = sc.nextLine().trim();
            }
            copyNo = Integer.parseInt(copyNoString);
        }

        int result = libraryServerBean.checkoutBook(userName, isbn, copyNo);

        switch (result) {
            case 1:
                System.out.println("Book successfully checked out!");
                break;
            case 2:
                System.out.println("Book cannot be checked out! User already checked out 2 books.");
                break;
            case 3:
                System.out.println("Book cannot be checked out! Unpaid fines.");
                break;
            case 4:
                System.out.println("Book already checked out!");
                break;
            case 5:
                System.out.println("Book cannot be checked out! Reserved by another user.");
                break;
            default:
                break;
        }

    }

    private void returnBook(Scanner sc) {
        System.out.println("\nReturn Book");
        
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Enter Copy No: ");
        
        String copyNoString = sc.nextLine().trim();
        while (!copyNoString.matches("\\d+")) {
            System.out.print("Invalid copy number format, enter again: ");
            copyNoString = sc.nextLine().trim();
        }
        int copyNo = Integer.parseInt(copyNoString);
        
        while (libraryServerBean.locateBookISBNCopyNo(isbn, copyNo) == 0) {
            System.out.println("Book does not exist, enter again.");
            System.out.print("Enter ISBN: ");
            isbn = sc.nextLine().trim();
            System.out.print("Enter Copy Number: ");
            copyNoString = sc.nextLine().trim();
            while (!copyNoString.matches("\\d+")) {
                System.out.print("Invalid copy number format, enter again: ");
                copyNoString = sc.nextLine().trim();
            }
            copyNo = Integer.parseInt(copyNoString);
        }
        
        int result = libraryServerBean.returnBook(isbn, copyNo);
        switch (result) {
            case 1:
                System.out.println("Book returned successfully!");
                break;
            case 2:
                System.out.println("Book overdue. Fine record created. \nBook returned successfully!");
                break;
            case 0:
                System.out.println("Error. No checkout for book.");
                break;
            default:
                break;
        }
    }

    private void viewReserves(Scanner sc) {

        System.out.println("\nView Reservations");
        
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Enter Copy No: ");
        String copyNoString = sc.nextLine().trim();
        while (!copyNoString.matches("\\d+")) {
            System.out.print("Invalid copy number format, enter again: ");
            copyNoString = sc.nextLine().trim();
        }
        int copyNo = Integer.parseInt(copyNoString);
        
        while (libraryServerBean.locateBookISBNCopyNo(isbn, copyNo) == 0) {
            System.out.println("Book does not exist, enter again.");
            System.out.print("Enter ISBN: ");
            isbn = sc.nextLine().trim();
            System.out.print("Enter Copy Number: ");
            copyNoString = sc.nextLine().trim();
            while (!copyNoString.matches("\\d+")) {
                System.out.print("Invalid copy number format, enter again: ");
                copyNoString = sc.nextLine().trim();
            }
            copyNo = Integer.parseInt(copyNoString);
        }
        
        System.out.print(libraryServerBean.viewReservesISBNCopyNo(isbn, copyNo));

    }

    private void viewCheckouts() {

        System.out.println("\nView Current Checkouts");
        System.out.print(libraryServerBean.viewCheckouts());

    }

    private void viewFines() {

        System.out.println("\nView Unpaid Fines");
        System.out.print(libraryServerBean.viewFines());

    }

}
