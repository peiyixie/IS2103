<%-- 
    Document   : payment
    Created on : Oct 25, 2016, 8:39:33 PM
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
        <div class="container" >
            <%
                ArrayList data = (ArrayList) request.getAttribute("userData");
                if (data != null) {
                    long fineID = Long.parseLong(request.getParameter("fineID"));
                    Integer resultPay = (Integer) request.getAttribute("payResult");
                    
                    if(resultPay== null || (resultPay != null && resultPay != 0)){
            %>

            <h1>Card Information</h1>
            <br>

            <div class="well">

                <form action="makePay" method="POST" accept-charset="UTF-8"/>

                <%
                    if (resultPay != null && resultPay == 1) {

                %>

                <h4>Card Type:</h4>
                <input type="text" required name="cardType" min="0" length="30"/>
                <p class="form-control-static" style="color: red"> Invalid card type! </p>

                <% } else { %>
                <h4>Card Type:</h4>
                <input type="text" required name="cardType" min="0" length="30"/>
                <% } %>
                
                <%
                    if (resultPay != null && resultPay == 2) {
                %>
                <h4>Card Number:</h4>
                <input type="text" pattern="[0-9]{13,16}" required name="cardNo" min="0" length="30"/>
                <p class="form-control-static" style="color: red"> Invalid card number! </p>
                
                
                <% } else { %>
                <h4>Card Number:</h4>
                <input type="text" pattern="[0-9]{13,16}" required name="cardNo" min="0" length="30"/>
                <% }%>

                
                                <%
                    if (resultPay != null && resultPay == 3) {                        
                %>
                
                <h4>Holder Name:</h4>
                <input type="text" required name="holder" length="30"/>
                <p class="form-control-static" style="color: red"> Invalid holder name! </p>

                <% } else { %>
                <h4>Holder Name:</h4>
                <input type="text" required name="holder" length="30"/>
                <% }%>

                <%
                    if (resultPay != null && resultPay == 4) {                        
                %>                
                
                <h4>Value Paid ($):</h4>
                <input type="number" required name="amount" length="30"/>
                <p class="form-control-static" style="color: red"> Payment rejected! Pay exact fine value. </p>
                
                
                <% } else { %>
                
                <h4>Value Paid ($):</h4>
                <input type="number" required name="amount" length="30"/>
                
                <% }%>                                                                              
                
                <input type="hidden" required name="fineID" value=<%=fineID%> >


                <br>
                <br>
                <input class="btn btn-success" type="submit">
                </form>
            </div>


            <%  } else{
%>

<p style="color: green" >Your payment is successful!</p>

<%
}
} else {
            %>
            <h1>You need to login first!</h1>
            <% }%>
        </div>
    </body>
</html>
