/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import ejb.LibraryServerBeanRemote;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peiyi
 */
public class LibraryServlet extends HttpServlet {

    @EJB
    private LibraryServerBeanRemote libraryServerBean;
    private String userName = "";
    private ArrayList data;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            RequestDispatcher dispatcher;
            ServletContext servletContext = getServletContext();
            String page = request.getPathInfo();
            page = page.substring(1);
            System.out.println("[LibraryServlet] startPage: " + page + ", logged in as: " + userName);

            if (page.equals("login")) {
                if (isLoggedIn()) {
                    request.setAttribute("userData", getUserData(userName));
                    page = "index";
                }
            } else if (page.equals("loginUser")) {
                if (checkLogin(request)) {
                    request.setAttribute("userData", getUserData(userName));
                    page = "index";
                } else {
                    request.setAttribute("message", "Invalid user name or password!");
                    page = "login";
                }
            } else if(page.equals("index") || page.equals("")){                
                System.out.println("Open index page...");
                request.setAttribute("userData", getUserData(userName));
                page = "index";
                
            } else if(page.equals("logout")){
                userName = "";
                request.setAttribute("userData", null);
                page = "login";
            } else if (page.equals("profile")){
                data = getUserData(userName);
                request.setAttribute("userData", data);
                page = "profile";
            } else if (page.equals("changePass")){
                int result = changePass(request);
                data = getUserData(userName);
                request.setAttribute("userData", data);
                request.setAttribute("resultPass", result);
                page = "profile";            
            } else if (page.equals("changeDetails")){
                int result = changeDetails(request);
                data = getUserData(userName);
                request.setAttribute("userData", data);
                request.setAttribute("resultDetails", result);
                page = "profile";
            } else if (page.equals("checkout")){
                data = getUserData(userName);
                request.setAttribute("userData", data);
                String result = getCheckouts((String)data.get(0));
                request.setAttribute("checkoutList", result);
                page = "checkout";
            } else if(page.equals("fine")){
                data = getUserData(userName);
                request.setAttribute("userData", data);
                ArrayList result = getFines(userName);
                request.setAttribute("fineList", result);
                page = "fine";
            } else if(page.equals("payment")){
                data = getUserData(userName);
                request.setAttribute("userData", data);
                page = "payment";
            } else if(page.equals("makePay")){
                data = getUserData(userName);
                request.setAttribute("userData", data);
                int result = makePayment(request);
                request.setAttribute("payResult", result);
                page = "payment";
            } else if(page.equals("signup")){
                page = "signup";            
            } else if(page.equals("register")){
                int result = registerLibMember(request);
                request.setAttribute("registerResult", result);
                page = "signup";
            } else if (page.equals("request")){
                data = getUserData(userName);
                request.setAttribute("userData", data);
                ArrayList result = getRequests(userName);
                request.setAttribute("requestList", result);
                page = "request";            
            } else if(page.equals("search")){
                data = getUserData(userName);
                request.setAttribute("userData", data);                      
                page = "search";
            } else if(page.equals("getSearch")){
                data = getUserData(userName);
                request.setAttribute("userData", data);                                                                
                ArrayList result = getSearch(request);
                request.setAttribute("searchResult", result);
                page = "search";                       
            } else if(page.equals("book")){
                data = getUserData(userName);
                request.setAttribute("userData", data);                                                                
                String[] result = getBook(request);
                request.setAttribute("bookInfo", result);
                page = "book";                                                                  
            } else if(page.equals("reserve")){
                data = getUserData(userName);
                request.setAttribute("userData", data);                                                                
                int result = makeReserve(request);
                request.setAttribute("resultRev", result);
                if(result == 0)
                    page = "reservation";                                                                                  
                else
                    page = "error";
            } else if(page.equals("author")){
                data = getUserData(userName);
                request.setAttribute("userData", data);                                                                
                String[] result = getAuthor(request);
                request.setAttribute("authorInfo", result);                
                page = "author";
            }
            else {
                page = "error";
            }
           
            dispatcher = servletContext.getNamedDispatcher(page);
            System.out.println(dispatcher);
            
            System.out.println("[LibraryServlet] Dispatched: " + page + ", logged in as: " + userName);
            if (dispatcher == null) {
                dispatcher = servletContext.getNamedDispatcher("error");
            }
            dispatcher.forward(request, response);
        }catch (Exception ex) {
            log("Exception in LibraryServlet.processRequest()");
        }
    }

    private boolean isLoggedIn() {
        System.out.println("LibraryServlet: isLoggedIn():");
        if (userName.equals("")) {
            return false;
        }
        return true;
    }

    private ArrayList<String> getUserData(String userName) {
        System.out.println("LibraryServlet: getUserData():");
        ArrayList<String> list = new ArrayList();
        Vector libMember = libraryServerBean.getLibMember(userName);
        list.add((String) libMember.get(0)); //userName
        list.add((String) libMember.get(1)); //password
        list.add((String) libMember.get(2)); //address
        list.add((String) libMember.get(3)); //contact
        list.add((String) libMember.get(4)); //email

        return list;
    }

    private boolean checkLogin(HttpServletRequest request) {
        System.out.println("LibraryServlet: checkLogin():");
        String userEntered = request.getParameter("userName");
        String password = request.getParameter("password");
        Vector libMember = libraryServerBean.getLibMember(userEntered);
        if (!libMember.isEmpty()) {
            System.out.println("LibraryServlet: chekcPassword():");
            if (libraryServerBean.checkPassword(password, (String) libMember.get(1))) {
                userName = (String) libMember.get(0);
                System.out.println("user name is: " + userName);
                return true;
            }
        }
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private int changePass(HttpServletRequest request) {
        System.out.println("LibraryServlet: changePass():");
        Vector libMember = libraryServerBean.getLibMember(userName);
        String oldPass = request.getParameter("oldPass");
        String newPass = request.getParameter("newPass");
        String newPass2 = request.getParameter("newPass2");
        if(newPass.equals("")){
            return 2;
        }
        if(!newPass.equals(newPass2)){
            return 3;
        }
        if(!libraryServerBean.checkPassword(oldPass, (String) libMember.get(1))){
            return 1;
        }
        libraryServerBean.changeUserPass(userName, newPass);
        return 0; // if updated successfully
    }

    private int changeDetails(HttpServletRequest request) {
        System.out.println("LibraryServlet: changePass():");
        Vector libMember = libraryServerBean.getLibMember(userName);
        String newContact = request.getParameter("contactNo");
        String newAddress = request.getParameter("address");
        String newEmail = request.getParameter("email");
        
        if(!newContact.matches("\\d+"))
            return 1;
        if(newAddress.equals(""))
            return 2;
        if(!newEmail.matches("[\\w\\.]+@[\\w\\.]+"))
            return 3;            
        
        libraryServerBean.changeUserDetails(userName, newContact, newEmail, newAddress);
        return 0; // if updated successfully
    }

    private String getCheckouts(String userName) {

        String result = libraryServerBean.getCheckoutsWeb(userName);
        
        
        return result;
    }

    private ArrayList getFines(String userName) {
        ArrayList result = libraryServerBean.getFinesWeb(userName);
        
        return result;//return null if no record found

        
    }

    private int makePayment(HttpServletRequest request) {
        System.out.println("LibraryServlet: makePayment():");
        long fineID = Long.parseLong(request.getParameter("fineID"));
        String cardType = request.getParameter("cardType");
        String cardNo = request.getParameter("cardNo");
        String holder = request.getParameter("holder");
        String amountString = request.getParameter("amount");
        System.out.println("reached here");
        
        if(cardType.equals(""))
            return 1;
        if(cardNo.equals("")||(!cardNo.matches("\\d+")))
            return 2;
        if(holder.equals(""))
            return 3;            
        
        long amount = Long.parseLong(amountString);
        long correctAmt = libraryServerBean.getFineAmount(fineID);
        if(amountString.equals("")||amount != correctAmt)
            return 4;
                
        libraryServerBean.makePayment(fineID, cardType, cardNo, holder);
        return 0;
    }

    private int registerLibMember(HttpServletRequest request) {
        System.out.println("LibraryServlet: registerLibMember():");
        String newUser = (String) request.getParameter("newUserName");   
        System.out.println("user to add is " + newUser); 
        Vector libMember = libraryServerBean.getLibMember(newUser);
        if(newUser == null || !libMember.isEmpty())
            return 1;//user name exists already        
        String newContact = request.getParameter("contactNo");               
        String newAddress = request.getParameter("address");
        String newEmail = request.getParameter("email");
        String newPass = request.getParameter("pass1");
        String newPass2 = request.getParameter("pass2");
        if(newPass.equals(""))
            return 2;        //must enter 1st password
        if(!newPass.equals(newPass2))
            return 3;        //must match with 1st password
        if(!newContact.matches("\\d+"))
            return 4;   //invalid format
        if(!newEmail.matches("[\\w\\.]+@[\\w\\.]+"))
            return 5;   //invalid format
        if(newAddress.equals(""))
            return 6;   //invalid format
        
        

        libraryServerBean.addLibMember(newUser, newPass2, newContact, newEmail, newAddress);
        return 0;
    }

    private ArrayList getRequests(String userName) {
        ArrayList result = libraryServerBean.getRequestsWeb(userName);
        // tochange
        return result;//return null if no record found

    }

    private ArrayList getSearch(HttpServletRequest request) {
        System.out.println("LibraryServlet: getSearch():");
        
        String title = "";
        String isbn = "";
        String author = "";        
        System.out.println("this is " + request.getParameter("isbn"));
        title = request.getParameter("title");
        isbn = request.getParameter("isbn");
        author =  request.getParameter("author");
        ArrayList result = libraryServerBean.getSearchWeb(title, isbn, author);
        
        return result;
    }

    private String[] getBook(HttpServletRequest request) {
        System.out.println("LibraryServlet: getBook():");
        System.out.println(request.getParameter("bookID"));
        long bookID = Long.parseLong(request.getParameter("bookID"));

        String[] result = libraryServerBean.getBook(bookID, userName);

        return result;

    }

    private int makeReserve(HttpServletRequest request) {
        System.out.println("LibraryServlet: getBook():");
        String isbn = request.getParameter("isbn");
        
        System.out.println("got isbn is" + isbn);
        String note = request.getParameter("note");
        int copyNo = Integer.parseInt(request.getParameter("copyNo"));
        return libraryServerBean.makeReserve(isbn, copyNo, userName, note);
        
    }

    private String[] getAuthor(HttpServletRequest request) {
        System.out.println("LibraryServlet: getAuthor():");
        String authorIDString = request.getParameter("authorID");
        long authorID = Long.parseLong(authorIDString);
        String[] result = libraryServerBean.getAuthor(authorID);
        
        return result;
    }

}
