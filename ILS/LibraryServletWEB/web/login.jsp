<%-- 
    Document   : login
    Created on : Oct 24, 2016, 9:03:46 PM
    Author     : Peiyi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Integrated Library System</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">       
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <title>Integrated Library System</title>
    </head>
    <body>
        <div style="margin-top: 100px;">
            <div class="jumbotron" style="width: 60%; margin-left: auto; margin-right: auto; padding: 50px;">
                <header class="page-header">
                    <h3>Login to Integrated Library System</h3>             
                </header>
                <%
                    String message = (String) request.getAttribute("message");
                    if (message != null) {
                %>
                <h4 style="color: red"><%= message%></h4>
                <% }%>
                <div class="well">

                    <form action="loginUser" method="POST" accept-charset="UTF-8"/>
                    <h4>User Name:</h4>
                    <input type="text" required name="userName" min="0" length="30"/>
                    <h4>Password:</h4>
                    <input type="password" required name="password" length="30"/>
                    <br>
                    <br>
                    <input class="btn btn-success" type="submit">
                    </form>
                </div>
                <center>
                <a href="signup"> Sign Up </a>
                </center>
            </div>      
        </div>

    </body>
</html>