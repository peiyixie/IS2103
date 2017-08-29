<%-- 
    Document   : author
    Created on : Oct 27, 2016, 9:59:56 AM
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

            <h1>View Author</h1>
            <br>

            <%
                System.out.println("reached here");
                String[] authorInfo = (String[]) request.getAttribute("authorInfo");
                
                System.out.println("reached here 1");
                
                int bookNum = (authorInfo.length - 1)/2;
                
                System.out.println("reached here 2");

                %>
            
            <p> <%=authorInfo[0] %>  </p>            
            <table class="table">
                               
                
            <%                              
                for(int i = 1; i < authorInfo.length; i++){//start for loop for books
                    long bookID = Long.parseLong(authorInfo[i]);
                    i++;
                    String bookInfo = authorInfo[i];                
                %>
            <tr>
                <td>      <%= bookInfo %>          </td>
                
                    <form action="viewBook" method="POST" accept-charset="UTF-8"/>                     
                    <td> <a href="book?bookID=<%=bookID%>"> View </a> </td>           
           </tr>
            <%                                
                }//end for loop for books
            %>
                
            </table>
            


            <%            } else {
            %>
            <h1>You need to login first!</h1>
            <% }%>
        </div>
    </body>
</html>

