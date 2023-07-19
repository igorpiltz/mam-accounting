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
                <th>Number</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
        	<c:forEach items="${transactions}" var="trans">
                <tr>
                	<td><c:out value="${trans.id}" /></td>
                    <td><c:out value="${trans.number}" /></td>
                    <td><a href="TransactionController.do?action=view&transactionId=<c:out value="${trans.id}"/>">View</a></td>
                    <td><a href="TransactionController.do?action=correct&transactionId=<c:out value="${trans.id}"/>">Correct</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    
</body>
</html>
