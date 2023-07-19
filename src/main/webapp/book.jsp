<%@ page language="java" contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="ISO-8859-1">
<title>Accounts - <c:out value="${book.company.name}" /></title>

<style>
.left
{
	float:left;
}
</style>

</head>
<body>

Bookid: <c:out value="${book.id}" /> Company: <c:out value="${book.company.name}" /> 
    <table border=1>
        <thead>
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Account Code</th>
                <th>Class</th>
                <th colspan=3>Action</th>
            </tr>
        </thead>
        <tbody>
        	<c:forEach items="${accounts}" var="account">
                <tr>
                	<td><c:out value="${account.id}" /></td>
                    <td><c:out value="${account.name}" /></td>
                    <td><c:out value="${account.accountCode}" /></td>
                    <td><c:out value="${account.accountClass}" /></td>
                    <td><a href="AccountController.do?action=edit&accountId=<c:out value="${account.id}"/>">Update</a></td>
                    <td><a href="javascript:askDeleteAccount(${account.id}, '${account.name}');">Delete</a></td>
                    <td><a href="AccountController.do?action=view&accountId=<c:out value="${account.id}"/>">View</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
        
    
    <p><a href="AccountController.do?action=insert&bookId=<c:out value="${book.id}" />">Add Account</a></p>
    <p><a href="TransactionController.do?action=insert&bookId=<c:out value="${book.id}" />">Add Verification</a></p>
    <p><a href="UploadedFileController.do?action=listAll&bookId=<c:out value="${book.id}" />">List Uploaded Files</a></p>
    <p><a href="ReportController.do?action=listAll&bookId=<c:out value="${book.id}" />">Reports</a></p>
    <p><a href="TransactionController.do?action=listAll&bookId=<c:out value="${book.id}" />">Transactions</a></p>
	
    <script>
      function askDeleteAccount(accountId, accountName) {
	  var response = window.prompt("If you wish to delete the account " + accountName + "(" + accountId + "), enter YES below", "");
	  if (response == "YES") {
	      window.location.assign("AccountController.do?action=delete&accountId=" + accountId);
	  }
      }
    </script>

    
</body>
</html>
