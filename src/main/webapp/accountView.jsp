<%@ page language="java" contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css"
    href="css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
<title>Add new account for <c:out value="${company.name}" /></title>
</head>
<body>
    <script>
        $(function() {
          
        });
    </script>

    
    	<input type="hidden" name="bookId" value="<c:out value="${book.id}" />">
        Account ID : <input type="text" readonly="readonly" name="id"
            value="<c:out value="${account.id}" />" /> <br /> 
        Name : <input
            type="text" name="name"
            value="<c:out value="${account.name}" />" /> <br /> 
        Code : <input
            type="text" name="accountCode"
            value="<c:out value="${account.accountCode}" />" /> <br /> 
        Account Class : 
        	<c:out value="${account.accountClass}" /> <br />  
        Placeholder (readOnly) :
        	<c:out value="${account.readOnly}" /> <br />
        
        Subaccounts <br />	
        <table border=1>
        <thead>
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Account Code</th>
                <th>Class</th>
                <th colspan=4>Action</th>
            </tr>
        </thead>
        
        <form method="GET" action='AccountController.do' name="frmAddAccount">
        	<input type="hidden" name="action" value="addChild" />
        	<input type="hidden" name="parent" value="<c:out value="${account.id}" />" />
        	
        	<select name="newChild">
        		<c:forEach items="${children}" var="child">
        			<c:if test="${child.id != account.id}">  
  						<option value="<c:out value="${child.id}" />"><c:out value="${child.name}" /></option>
  					</c:if>	
  				</c:forEach>
  			</select>
  			
  			<input type="submit" value="Submit" />
        </form>
        
        <tbody>
        	<c:forEach items="${account.children}" var="child">
                <tr>
                	<td><c:out value="${child.id}" /></td>
                    <td><c:out value="${child.name}" /></td>
                    <td><c:out value="${child.accountCode}" /></td>
                    <td><c:out value="${child.accountClass}" /></td>
                    <td><a href="AccountController.do?action=edit&accountId=<c:out value="${child.id}"/>">Update</a></td>
                    <td><a href="AccountController.do?action=orphan&accountId=<c:out value="${child.id}"/>">Orphan</a></td>
                    <td><a href="AccountController.do?action=delete&accountId=<c:out value="${child.id}"/>">Delete</a></td>
                    <td><a href="AccountController.do?action=view&accountId=<c:out value="${child.id}"/>">View</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    Transactions <br />
    <table border=1>
        <thead>
            <tr>
                <th>Id</th>
                <th>Date Noticed</th>
                <th>Number</th>
                <th>Description</th>
                <th>Amount</th>
                <th>Balance</th>
                <th>View</td>
            </tr>
        </thead>
        <tbody>
        	<c:forEach items="${accountView.lineItemsReverse}" var="item">
                <tr>
                	<td><c:out value="${item.part.transaction.id}" /></td>
                	<td><c:out value="${item.part.transaction.dateNoticed}" /></td>
                	<td><c:out value="${item.part.transaction.number}" /></td>
                	<td><c:out value="${item.part.transaction.description}" /></td>
                    <td><c:out value="${item.part.amount}" /></td>
                    <td><c:out value="${item.balance}" /></td>
                    <td><a href="TransactionController.do?action=view&transactionId=<c:out value="${item.part.transaction.id}"/>">View</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
        
</body>
</html>