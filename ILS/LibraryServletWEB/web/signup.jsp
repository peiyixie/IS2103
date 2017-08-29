<%-- 
    Document   : signup
    Created on : Oct 25, 2016, 10:55:48 PM
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
                    <h3>Register Integrated Library System</h3>             
                </header>
            <%
                    Integer resultReg = (Integer) request.getAttribute("registerResult");                                        
            %>                
                
                
                
                <div class="well">

                    <form action="register" style="width:100%" method="POST" accept-charset="UTF-8"/>
                    <%                        
                        if (resultReg != null && resultReg == 1) {
                    %>
                    
                    
                    <h4>User Name:</h4>                    
                    <input type="text" style="width:100%" required name="newUserName" min="0" length="30"/>
                    <p style="color: red"> User name exists already!  </p>

                    
                    <%                    } else {
                    %>
                    
                    <h4>User Name:</h4>                    
                    <input type="text" style="width:100%" required name="newUserName" min="0" length="30"/>
                    
                    
                    <% } %>
                    
                    <%                        
                        if (resultReg != null && resultReg == 2) {
                    %>
                    
                    <h4>Enter password</h4>
                    <input type="password" style="width:100%" required name="pass1" length="30"/>
                    <p  style="color: red"> Must enter password!  </p>

                    <%                    } else {
                    %>
                    <h4>Enter password</h4>
                    <input type="password" style="width:100%" required name="pass1" length="30"/>                   

                    <% } %>

                    <%                        
                        if (resultReg != null && resultReg == 3) {
                    %>
                    
                    <h4>Enter password again</h4>
                    <input type="password" style="width:100%" required name="pass2" length="30"/>
                    <p  style="color: red"> Password does not match!  </p>
                    
                    
                    <%                    } else {
                    %>

                    <h4>Enter password again</h4>
                    <input type="password" style="width:100%" required name="pass2" length="30"/>                    
                    <% } %>

                    <%                        
                        if (resultReg != null && resultReg == 4) {
                    %>
                    
                    <h4>Contact Number:</h4>
                    <input type="text" style="width:100%"required name="contactNo" min="0" length="30"/>
                    <p  style="color: red"> Invalid contact number!  </p>
                    
                    <%                    } else {
                    %>

                    <h4>Contact Number:</h4>
                    <input type="text" style="width:100%"required name="contactNo" min="0" length="30"/>
                    
                    <% } %>

                    <%                        
                        if (resultReg != null && resultReg == 5) {
                    %>
                    <h4>Email:</h4>
                    <input type="email" style="width:100%" required name="email" min="0" length="30"/>
                    <p  style="color: red"> Invalid email!  </p>
                    <%                    } else {
                    %>                    
                    <h4>Email:</h4>
                    <input type="email" style="width:100%" required name="email" min="0" length="30"/>
                    <% } %>
                    
                    <%                        
                        if (resultReg != null && resultReg == 6) {
                    %>
                    <h4>Address</h4>                                         
                    <input type="text" style="width:100%; height:50px"  required name="address" min="0" length="30"/>                                        
                    <p  style="color: red"> Invalid address!  </p>
                    
                    <%                    } else {
                    %>                    
                    <h4>Address</h4>
                    <input type="text" style="width:100%; height:50px"  required name="address" min="0" length="30"/>                                        
                    <% } %>
                    
                    
                    <%                        
                        if (resultReg != null && resultReg == 0) {
                    %>
                    
                    <p  style="color: green"> Register successfully!  </p>
                    
                    
                    <% } %>
                    
                    <br>
                    <br>
                    <input class="btn btn-success" type="submit" >
                    </form>
                </div>
                    

                <center>
                <a href="login"> Login </a>
                </center>
            </div>      
        </div>

    </body>
</html>