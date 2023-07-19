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

    <form method="POST" action='AccountController.do' name="frmAddAccount">
    	<input type="hidden" name="bookId" value="<c:out value="${book.id}" />">
        Account ID : <input type="text" readonly="readonly" name="id"
            value="<c:out value="${accountEdit.id}" />" /> <br /> 
        Name : <input
            type="text" name="name"
            value="<c:out value="${accountEdit.name}" />" /> <br /> 
        Code : <input
            type="text" name="accountCode"
            value="<c:out value="${accountEdit.accountCode}" />" /> <br /> 
        Account Class : 
        
        	<% if (request.getAttribute("accountEdit") == null) { %> 
            <select name="accountClass">
  				<option value="ASSET">Tillgång</option>
  				<option value="LIABILITIES">Skuld</option>
  				<option value="REVENUE">Intäkter</option>
  				<option value="COST">Utgift</option>
  				<option value="NET">Kapital</option>
  			</select>
  			<% } else out.print(((corporation.model.bookkeeping.Account)request.getAttribute("accountEdit")).getAccountClass()); %>
  			<br />
  			
  		Placeholder (readOnly) : 
  			<input type="checkbox" name="readOnly" value="readOnly" 
  				<c:if test="${accountEdit.readOnly}"> checked="checked" </c:if> />
  			<br />
  				  
        <input type="submit" value="Submit" />
    </form>
</body>
</html>