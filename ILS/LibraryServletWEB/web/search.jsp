<%-- 
    Document   : search
    Created on : Oct 26, 2016, 4:21:44 PM
    Author     : Peiyi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Integrated Library System</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    </head>
    <body>

        <%
            ArrayList userData = (ArrayList) request.getAttribute("userData");
            String currentUser = (String) userData.get(0);
            System.out.print(request.getContextPath());
            System.out.print("user is " + currentUser);
            boolean isLoggedIn = !currentUser.equals("");

        %>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a href="index" class="navbar-brand">Integrated Library System</a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav nav-pills navbar-nav">
                        <%                            if (isLoggedIn) {
                        %>
                        <li><a href="profile" title="">Profile</a></li>
                        <li><a href="search" title="">Search</a></li>
                        <li><a href="checkout" title="">Checkouts</a></li>
                        <li><a href="fine" title="">Fines</a></li>
                        <li><a href="request" title="">Requests</a></li>
                            <%
                                }
                            %>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <%
                            if (isLoggedIn) {
                        %>

                        <li class="dropdown">
                            <a data-target="#" id="user-menu" data-toggle="dropdown" role="button" class="dropdown-toggle"><%= currentUser%><span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu" aria-labelledby="user-menu">
                                <li role="presentation">
                                    <a href="update" role="menuitem" tabindex="-1">Update</a>
                                </li>

                                <li role="presentation">
                                    <a href="logout" role="menuitem" tabindex="-1">Sign out</a>
                                </li>
                            </ul>
                        </li>
                        <%
                        } else {
                        %>

                        <li>
                            <a href="login">Login</a>
                        </li>
                        <%
                            }
                        %>
                    </ul>                
                </div>

            </div>
        </nav>        
        <div class="container">
            <%
                ArrayList data = (ArrayList) request.getAttribute("userData");
                if (data != null) {
            %>

            <%
                    ArrayList bookList = (ArrayList) request.getAttribute("searchResult");                                        
            %>     
            
            <h1>Search</h1>
            <br>
            <div class="well">
            
                <form action="getSearch" style="width:100%" method="POST" accept-charset="UTF-8"/>
                    <h4>Title:</h4>                    
                    <input type="text" style="width:100%" required name="title" min="0" length="30"/>
                    
                    <h4>ISBN:</h4>                    
                    <input type="text" style="width:100%" required name="isbn" min="0" length="30"/>
                    
                    <h4>Author:</h4>                    
                    <input type="text" style="width:100%" required name="author" min="0" length="30"/>                        
                    <input class="btn btn-success" type="submit" >                    
                
                </form>                                
            </div>
            
            <%
                
                ArrayList<Long> bookID = new ArrayList<Long>();
                ArrayList<String> bookInfo = new ArrayList<String>();
                if(bookList != null){// open- if bookList is not null, have already searched
                    bookID = (ArrayList<Long>) bookList.get(0);
                    bookInfo = (ArrayList<String>) bookList.get(1);
                    int numResult = bookID.size();
                    if(bookID.isEmpty()){                                                        %>             
                        <p style="color:red"> No books found! </p>>                        
            <%      }else{                                                                                            
            %>
            
            <p> <%=numResult%> Result(s) </p>
            <table class="table">                
                    <%
                        for (int i = 0; i < numResult; i++) {
                    %>
                <tr>
                    <td> <%=bookInfo.get(i)%></td>
                    <% 
                        long thisBookID = 0L;
                    if(!bookID.isEmpty())
                        thisBookID = bookID.get(i);
                    %>
                    <form action="viewBook" method="POST" accept-charset="UTF-8"/>                     
                    <td> <a href="book?bookID=<%=thisBookID%>"> View </a> </td>                                                            
                </tr>               
                <%
                    }//table
                %>
            </table>
            
            
            <% } 
                    }    //close - if bookList is not null, have already searched        %>

            
            
            <%            } else {   
//            if user data null
            %>
            
            <p>You need to login first!</p>
            <% }%>
        </div>
    </body>
</html>