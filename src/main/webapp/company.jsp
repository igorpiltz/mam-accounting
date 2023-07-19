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
<title>Add new user</title>
</head>
<body>
    <script>
        $(function() {
            $('input[name=dob]').datepicker();
        });
    </script>

    <form method="POST" action='CompanyController.do' name="frmAddUser">
        User ID : <input type="text" readonly="readonly" name="id"
            value="<c:out value="${comp.id}" />" /> <br /> 
        Name : <input
            type="text" name="name"
            value="<c:out value="${comp.name}" />" /> <br /> 
        Org. number : <input
            type="text" name="stateIdNumber"
            value="<c:out value="${comp.stateIdNumber}" />" /> <br /> 
        Company Type : 
        
        	<% if (request.getAttribute("comp") == null) { %> 
            <select name="companyType">
  				<option value="ENSKILD_FIRMA">Enskild Firma</option>
  				<option value="AKTIEBOLAG">Aktiebolag</option>
  				<option value="PERSON">Person</option>
  			</select>
  			<% } else out.print(((corporation.model.Company)request.getAttribute("comp")).getCompanyType()); %>
  			<br />  
        <input type="submit" value="Submit" />
    </form>
</body>
</html>