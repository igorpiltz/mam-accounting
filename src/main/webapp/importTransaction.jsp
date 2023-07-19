<%@ page language="java" contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link type="text/css" href="css/ui-lightness/jquery-ui-1.10.4.custom.css" rel="stylesheet" />

<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.4.custom.min.js"></script>

<title>Import Transaction for <c:out value="${sessionScope.account.book.company.name}" />)</title>
</head>
<body>
    <script>
        $(function() {
        });
    </script>
	
	
	Transaction: <br/>
		
	<table border=1>
        <tbody>
			<tr>
			   	<td>Noticed</td>
                <td><fmt:formatDate value="${transaction.dateNoticed}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
			   	<td>Occurred</td>
                <td><fmt:formatDate value="${transaction.dateOccurred}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
			<tr>
			   	<td>Description</td>
                <td><c:out value="${transaction.description}" /></td>
            </tr>
            <tr>
			   	<td>Other Party</td>
                <td><c:out value="${transaction.otherParty}" /></td>
            </tr>
			<tr>
			   	<td>Amount</td>
                <td><c:out value="${transaction.parts.get(0).amount}" /></td>
            </tr>
        </tbody>
    </table>
	
	<form method="GET" action='UploadedFileController.do' name="frmImportTransactionHandlers">
		<input type="hidden" name="action" value="runImportHandler">
		
       	<c:forEach items="${importTransactionHandlers}" var="handler">
       		<input type="submit" value="<c:out value="${handler.title}" />" name="handler_${handler.id}" id="handlers" />	
		</c:forEach>
	</form>

	<form method="GET" action='UploadedFileController.do' name="frmAddHandler">
		<input type="hidden" name="action" value="newHandler">
		<input type="hidden" name="bookId" value="<c:out value="${sessionScope.account.book.id}" />">
		<input type="hidden" name="transactionId" value="<c:out value="${transaction.id}" />">
		
		<input type="submit" value="New Handler" name="newHandler" id="newHandler" /> 
	</form>
	
	<p><a href="AccountController.do?action=listAll&bookId=<c:out value="${book.id}" />">Back to Book</a></p>
</body>
</html>
