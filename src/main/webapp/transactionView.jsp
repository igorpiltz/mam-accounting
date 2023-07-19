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

<title>Transaction <c:out value="${transaction.number}" /> (<c:out value="${book.company.name}" />)</title>
</head>
<body>
    <script>
        $(function() {
        });
    </script>

    	
        Transaction ID : <input type="text" readonly="readonly" name="id"
            value="<c:out value="${transaction.id}" />" /> <br/>
            
        Transaction Number : <input type="text" readonly="readonly" name="id"
            value="<c:out value="${transaction.number}" />" /> <br/>    
            
        Currency : <input type="text" readonly="readonly" name="currency"
            value="<c:out value="${book.currency}" />" /> <br/>
             
        Date created : <input type="text" readonly="readonly" name="dateCreated"
            value="<c:out value="${transaction.dateCreated}" />" /> <br/>
        
        Date occurred : <input
            type="text" readonly="readonly" name="dateOccurred" 
            value="<c:out value="${transaction.dateOccurred}" />" /> <br/>
                 
        Date noticed : <input
            type="text" readonly="readonly" name="dateNoticed" 
            value="<c:out value="${transaction.dateNoticed}" />" /> <br/>    
            
        Description : <textarea name="description">
        				<c:out value="${transaction.description}" />
        			 </textarea> <br/>
        
        Other Party : <input
            type="text" name="otherParty" readonly="readonly"
            value="<c:out value="${transaction.otherParty}" />" /> <br/>    
        
        Base document : <input
            type="text" name="baseDocument" 
            value="<c:out value="${transaction.baseDocument}" />"
            /> <br/>
            
        Corrected : <input
            type="text" name="baseDocument" 
            value="<c:out value="${transaction.correctedTransaction.number}" />"
            /> <br/>    
       
       	<%
       	corporation.model.bookkeeping.Transaction transaction = (corporation.model.bookkeeping.Transaction)request.getAttribute("transaction");
       	java.util.List<corporation.model.bookkeeping.Part> parts = transaction.getParts();
       	for (int index = 0; index < parts.size(); index++) {
			corporation.model.bookkeeping.Part part = parts.get(index);
			out.print("<input  type='text' name='account' value='" + part.getAccount().getName() + "' />");      
   			
   			if (part.getAmount().compareTo(java.math.BigDecimal.ZERO) >= 0) {
  				out.print("<input  type='text' name='credit' value='" + part.getAmount() + "' />");
				out.print("<input  type='text' name='debit' />");
			} else {
				out.print("<input  type='text' name='credit' />");
				out.print("<input  type='text' name='debit' value='" + part.getAmount().negate() + "' />");
			}								
			out.print("<br/>");
       	}
       	%>        
	
		<p><a href="AccountController.do?action=listAll&bookId=<c:out value="${book.id}" />">Back to Book</a></p>
</body>
</html>
