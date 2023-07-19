<%@ page language="java" contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="ISO-8859-1">
<title>Import transactions for <c:out value="${account.name}" /></title>

<style>
#left 
{
	float:left;
}
</style>

</head>
<body>

Bookid: <c:out value="${account.book.id}" /> Company: <c:out value="${book.company.name}" /> 

<div id = "left">
    <table border=1>
        <thead>
            <tr>
                <th>Id</th>
                <th>Noticed</th>
                <th>Number</th>
                <th>Description</th>
                <th>Other Party</th>
                <th>Amount</th>
                <th>Balance</th>
            </tr>
        </thead>
        <tbody>
        	<c:forEach items="${accountView.getLineItemsLatest(10)}" var="item">
                <tr>
                	<td><c:out value="${item.part.transaction.id}" /></td>
                    <td><c:out value="${item.part.transaction.dateNoticed}" /></td>
                    <td><c:out value="${item.part.transaction.number}" /></td>
                    <td><c:out value="${item.part.transaction.description}" /></td>
                    <td><c:out value="${item.part.transaction.otherParty}" /></td>
                    <td><c:out value="${item.part.amount}" /></td>
                    <td><c:out value="${item.balance}" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

    <table border=1>
        <thead>
            <tr>
                <th>Date Noticed</th>
                <th>Other Party</th>
                <th>Amount</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
        	<c:forEach items="${transactions}" var="trans">
                <tr>
                	<td><c:out value="${trans.dateNoticed}" /></td>
                    <td><c:out value="${trans.otherParty}" /></td>
                    <td><c:out value="${trans.parts.get(0).amount}" /></td>
                    <td><a href="UploadedFileController.do?action=importTransaction&transactionId=<c:out value="${trans.id}"/>">Import</a></td>
                    <td><a href="UploadedFileController.do?action=deleteTransaction&transactionId=<c:out value="${trans.id}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <p><a href="AccountController.do?action=insert&bookId=<c:out value="${book.id}" />">Add Account</a></p>
    <p><a href="TransactionController.do?action=insert&bookId=<c:out value="${book.id}" />">Add Verification</a></p>
    
</body>
</html>
