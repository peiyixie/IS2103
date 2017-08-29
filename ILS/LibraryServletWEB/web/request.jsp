<%-- 
    Document   : request
    Created on : Oct 26, 2016, 12:05:18 PM
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

            <h1>View Requests</h1>
            <br>

            <%
                ArrayList requestList = (ArrayList) request.getAttribute("requestList");
                if (requestList != null) {
                    ArrayList<String> listDate = (ArrayList<String>) requestList.get(0);
                    ArrayList<String> listState = (ArrayList<String>) requestList.get(1);
                    ArrayList<String> listMessage = (ArrayList<String>) requestList.get(2);
                    ArrayList<String> listComment = (ArrayList<String>) requestList.get(3);
                    int size = listDate.size();
            %>
            <table class="table">
                <tr>
                                    <th>Request No</th>                                   
                                    <th>Date</th>
                                    <th>State</th>
                                    <th>Message</th>
                                    <th>Comment</th>                    
                                    </tr>
                    <%
                        for (int i = 0; i < size; i++) {
                    %>

                <tr>
                    <th scope="row"><%=(i + 1)%></th>
                    <td> <%=listDate.get(i)%></td>
                    <td><%=listState.get(i)%> </td>
                    <td> <%=listMessage.get(i)%></td>
                    <td> <%=listComment.get(i)%></td>
                                        
                </tr>               

                <%
                    }
                %>
            </table>
            <%
            } else {
            %>
            <p style="color: red"> No request record found! </p>

            <% } %>


            <%            } else {
            %>
            <p>You need to login first!</p>
            <% }%>
        </div>
    </body>
</html>