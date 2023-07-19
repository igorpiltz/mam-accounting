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

<title>Add new verification for <c:out value="${book.company.name}" /></title>
</head>
<body>
    <script>
        $(function() {
            $('input[name=dateOccurred]').datepicker({ dateFormat: "yy-mm-dd" });
            $('input[name=dateNoticed]').datepicker({ dateFormat: "yy-mm-dd" });
        });
    </script>

    <form method="POST" action='TransactionController.do' name="frmAddTransaction">
    	<input type="hidden" name="bookId" value="<c:out value="${book.id}" />">
    	<c:if test="${not empty transaction}">
    		<input type="hidden" name="importTransaction" value="importTransaction">
    	</c:if>
    	
        Transaction ID : <input type="text" readonly="readonly" name="id"
            value="<c:out value="${transaction.id}" />" /> <br/>
            
        Number : <input type="text" readonly="readonly" name="number"
            value="<c:out value="${number+1}" />" /> <br/>
            
        Currency : <input type="text" readonly="readonly" name="currency"
            value="<c:out value="${book.currency}" />" /> <br/>
             
        Date created : <input type="text" readonly="readonly" name="dateCreated"
            value="<c:out value="${dateCreated}" />" /> <br/>
        
        Date occurred : <input
            type="text" name="dateOccurred" value="<fmt:formatDate value="${transaction.dateOccurred}" pattern="yyyy-MM-dd" />"/> <br/>
                 
        Date noticed : <input
            type="text" name="dateNoticed" value="<fmt:formatDate value="${transaction.dateNoticed}" pattern="yyyy-MM-dd" />" /> <br/>    
            
        Description : <textarea name="description"><c:out value="${transaction.description}" /></textarea> <br/>
        
        Other Party : <input
            type="text" size="50" name="otherParty" value="<c:out value="${transaction.otherParty}" />" /> <br/>    
        
        Base document : <input
            type="text" size="50" name="baseDocument" value="<c:out value="${transaction.baseDocument}" />" /> <br/>
            
       
       <%
       	corporation.model.bookkeeping.Transaction transaction = (corporation.model.bookkeeping.Transaction)request.getAttribute("transaction");
       	if (transaction != null) {
       		java.util.List<corporation.model.bookkeeping.Part> parts = transaction.getParts();
       		for (int index = 1; index <= parts.size(); index++) {
				corporation.model.bookkeeping.Part part = parts.get(index-1);
				out.print("<input  type='text' size='50' list='accounts' name='account" + index + "' value='" + part.getAccount().getName() + "' />");      
   			
   				if (part.getAmount().compareTo(java.math.BigDecimal.ZERO) >= 0) {
  					out.print("<input  type='text' name='credit" + index + "' value='" + part.getAmount() + "' />");
					out.print("<input  type='text' name='debit" + index + "' />");
				} else {
					out.print("<input  type='text' name='credit" + index + "' />");
					out.print("<input  type='text' name='debit" + index + "' value='" + part.getAmount().negate() + "' />");
				}								
				out.print("<br/>");
       		}
       	} else {
       		for (int index = 1; index <= 15; index++) {
				out.print("<input  type='text' size='50' list='accounts' name='account" + index + "'/>");      
				out.print("<input  type='text' name='credit" + index + "' />");
				out.print("<input  type='text' name='debit" + index + "' />");
				out.print("<br/>");
       		}
       	}
       %>       
		 
		<datalist id="accounts">
			<c:forEach items="${accounts}" var="account">
       			<option value="<c:out value="${account.name}" />" />	
			</c:forEach>		
  
		</datalist>
 
        <input type="submit" value="Submit" />
    </form>
</body>
</html>


