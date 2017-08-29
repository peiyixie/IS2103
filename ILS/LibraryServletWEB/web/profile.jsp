<%-- 
    Document   : profile
    Created on : Oct 25, 2016, 9:26:57 AM
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

            <h1>Update Profile</h1>
            <div id="password-form">
                <form class="form-horizontal" action="changePass" method="POST">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Username</label>
                        <div class="col-sm-10">
                            <p class="form-control-static"><%= data.get(0)%></p>
                        </div>
                    </div>
                    <%
                        Integer resultPass = (Integer) request.getAttribute("resultPass");
                        if (resultPass != null && resultPass == 1) {

                    %>
                    <div class="form-group has-error has-feedback">
                        <label for="oldPass" class="col-sm-2 control-label">Old Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="oldPass" id="oldPass" placeholder="Enter old password" aria-describedby="inputError" required>
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span id="inputError" class="sr-only">(error)</span>
                            <p class="form-control-static"> Wrong password!  </p>

                        </div>
                    </div>
                    <%                    } else {
                    %>
                    <div class="form-group">
                        <label for="oldPass" class="col-sm-2 control-label">Old Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="oldPass" id="oldPass" placeholder="Enter old password" required>
                        </div>
                    </div>
                    <%
                        }
                        if (resultPass != null && resultPass == 2) {
                    %>

                    <div class="form-group has-error has-feedback">
                        <label for="newPass" class="col-sm-2 control-label">New Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass" id="newPass" placeholder="Enter new password" aria-describedby="inputError" required>
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span id="inputError" class="sr-only">(error)</span>
                            <p class="form-control-static"> Please enter a new password!  </p>

                        </div>
                    </div>
                    <%
                    } else {
                    %>

                    <div class="form-group">
                        <label for="newPass" class="col-sm-2 control-label">New Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass" id="newPass" placeholder="Enter new password" required>
                        </div>
                    </div>
                    <% }
                        if (resultPass != null && resultPass == 3) {
                    %>
                    <div class="form-group has-error has-feedback">
                        <label for="newPass2" class="col-sm-2 control-label">New Password Again</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass2" id="newPass2" placeholder="Re-enter new password" aria-describedby="inputError" required>
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span id="inputError" class="sr-only">(error)</span>
                            <p class="form-control-static"> New passwords must be the same!  </p>


                        </div>
                    </div>
                    <% }
                        if (resultPass != null && resultPass == 0) {
                    %>
                    <div class="form-group">
                        <label for="newPass2" class="col-sm-2 control-label">New Password Again</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass2" id="newPass2" placeholder="Re-enter new password" required>
                            <p class="form-control-static"> Update successful!  </p>
                        </div>
                    </div>
                    <%
                    } else {
                    %>
                    <div class="form-group">
                        <label for="newPass2" class="col-sm-2 control-label">New Password Again</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass2" id="newPass2" placeholder="Re-enter new password" required>
                        </div>
                    </div>
                    <% }%>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
            <div id="details-form">
                <form class="form-horizontal" action="changeDetails" method="POST">   
                    <%
                        Integer resultDetails = (Integer) request.getAttribute("resultDetails");
                        if (resultDetails != null && resultDetails == 1) {

                    %>
                    <div class="form-group has-error has-feedback">
                        <label for="contactNo" class="col-sm-2 control-label">Contact Number</label>
                        <div class="col-sm-10">
                            <input type="number" class="form-control" name="contactNo" id="contactNo" placeholder="Enter contact number" value="<%= Integer.parseInt((String) data.get(3))%>" required>
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span id="inputError" class="sr-only">(error)</span>      
                            <p class="form-control-static"> Invalid contact number format!  </p>
                        </div>
                    </div>


                    <%                    } else {
                    %>

                    <div class="form-group">
                        <label for="contactNo" class="col-sm-2 control-label">Contact Number</label>
                        <div class="col-sm-10">
                            <input type="number" class="form-control" name="contactNo" id="contactNo" placeholder="Enter contact number" value="<%= Integer.parseInt((String) data.get(3))%>" required>
                        </div>
                    </div>

                    <%
                        }
                        if (resultDetails != null && resultDetails == 3) {
                    %>
                    <div class="form-group has-error has-feedback">
                        <label for="email" class="col-sm-2 control-label">Email Address</label>
                        <div class="col-sm-10">
                            <input type="email" class="form-control" name="email" id="email" placeholder="Enter email address" value="<%= (String) data.get(4)%>" required>
                            <span id="inputError" class="sr-only">(error)</span>      
                            <p class="form-control-static"> Invalid email format!  </p>

                        </div>
                    </div>

                    <%                    } else {
                    %>

                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">Email Address</label>
                        <div class="col-sm-10">
                            <input type="email" class="form-control" name="email" id="email" placeholder="Enter email address" value="<%= (String) data.get(4)%>" required>
                        </div>
                    </div>
                    <%
                        }
                        if (resultDetails != null && resultDetails == 2) {
                    %>    
                    <div class="form-group has-error has-feedback">
                        <label for="address" class="col-sm-2 control-label">Address</label>
                        <div class="col-sm-10">
                            <input type="address" class="form-control" name="address" id="email" placeholder="Enter address" value="<%= (String) data.get(2)%>" required>
                            <span id="inputError" class="sr-only">(error)</span>      
                            <p class="form-control-static"> Invalid address format!  </p>


                        </div>
                    </div>    
                    <%                    } else {
                    %>
                    <div class="form-group">
                        <label for="address" class="col-sm-2 control-label">Address</label>
                        <div class="col-sm-10">
                            <input type="address" class="form-control" name="address" id="email" placeholder="Enter address" value="<%= (String) data.get(2)%>" required>
                            <% if (resultDetails != null && resultDetails == 0) {
                            %>
                            <p class="form-control-static"> Update successful!  </p>
                            <%
    }%>

                        </div>
                    </div>    
                    <% } %>



                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
            <%
            } else {
            %>
            <h1>You need to login first!</h1>
            <% }%>
        </div>
    </body>
</html>

