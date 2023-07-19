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

<title>Choose Account</title>
</head>
<body>
    <script>
        $(function() {
        });
    </script>
				    	
      
       	<form method="GET" action='UploadedFileController.do' name="frmChooseAccount">
        	<input type="hidden" name="action" value="chooseAccount" />
        	
        	<select name="account">
        		<c:forEach items="${accounts}" var="account">
					<option value="<c:out value="${account.id}" />"><c:out value="${account.name}" /></option>
  				</c:forEach>
  			</select>
  			
  			<input type="submit" value="Submit" />
        </form>
        
        <p><a href="AccountController.do?action=listAll&bookId=<c:out value="${book.id}" />">Back to Book</a></p>
        
        <c:forEach items="${sessionScope.transactions}" var="trans">
			${trans.toString()} <br/>
  		</c:forEach>
	
		
</body>
</html>
