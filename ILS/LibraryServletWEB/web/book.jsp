<%-- 
    Document   : book
    Created on : Oct 26, 2016, 9:35:10 PM
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

            <h1>View Book</h1>
            <br>

            <%
                System.out.println("reached here");
                String[] bookInfo = (String[]) request.getAttribute("bookInfo");
                System.out.println("reached here 1");
                int authorNo = Integer.parseInt(bookInfo[1]);

                System.out.println("reached here 2");

            %>

            <p>  <%= bookInfo[0] //title                    
                    %> 
                <%  for (int i = 2; i < 2 + authorNo * 2; i++) {//start for loop print author                  
%>
                <a href="author?authorID=<%=bookInfo[i]%>"> 
                    <% i++;%>                    
                    <%= bookInfo[i]%> </a>
                    <% if (i != 2 + authorNo * 2 - 1) { %>            
                ,
                <%    }//end for inner loop to print comma
                %>

                <% } //end for loop print author
%>
            </p>     
                <p>  <%= bookInfo[2 + authorNo * 2] //rest of info                    
                    %>   </p>

            <br>
            <br>

            <%  if (bookInfo[5 + authorNo * 2].equals("can")) { //start if statement for make reserve
%>

            <p> Make a reservation: </p>            
            <p> Note: </p>
            <form action="reserve" method="POST" accept-charset="UTF-8"/>            
            <input type="text" required name="note" length="30"/>
            <input type="hidden" required name="isbn" value=<%=bookInfo[3 + authorNo * 2]%> >
            <input type="hidden" required name="copyNo" value=<%= bookInfo[4 + authorNo * 2]%> >
            <input class="btn btn-success" type="submit">

            </form>

            <% }// end if statement for make reserve
            %>
            


            <%            } else {
            %>
            <h1>You need to login first!</h1>
            <% }%>
        </div>
    </body>
</html>
