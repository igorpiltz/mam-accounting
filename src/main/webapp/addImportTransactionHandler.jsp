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
				    	
      
       	<form method="POST" action='UploadedFileController.do' name="frmAddHandler">
        	<input type="hidden" name="action" value="importTransactionHandler" />
        	
        	Title: 
        		<input type="text" name="title" value="${handler.title}"><br>
        	Description: <br/> 
    			<textarea name="description" style="width: 80%;">${handler.description}</textarea> <br/>
    		Script: <br/> 
    			<textarea name="script" style="width: 80%;" rows="10">${handler.script}</textarea> <br/>
    		Result: <br/> 
    			<textarea name="result" style="width: 80%;" rows="10">${result}</textarea> <br/>
    
    		
  			<input type="submit" value="Test Script" name="testScript"/>
  			<input type="submit" value="Add Handler" name="addHandler"/>
        </form>
        
        <p><a href="AccountController.do?action=listAll&bookId=<c:out value="${book.id}" />">Back to Book</a></p>
        
</body>
</html>
