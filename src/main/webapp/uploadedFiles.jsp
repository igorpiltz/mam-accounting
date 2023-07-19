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

<title>Uploaded files for <c:out value="${book.company.name}" />)</title>
</head>
<body>
    <script>
        $(function() {
        });
    </script>

    	
        
       
    <table border=1>
        <thead>
            <tr>
                <th>Id</th>
                <th>Date</th>
                <th colspan=2>Action</th>
            </tr>
        </thead>
        <tbody>
        	<c:forEach items="${files}" var="file">
                <tr>
                	<td><c:out value="${file.id}" /></td>
                    <td><c:out value="${file.uploaded}" /></td>
                    <td><a href="UploadedFileController.do?action=view&fileId=<c:out value="${file.id}"/>&bookId=<c:out value="${book.id}"/>">View</a></td>
                    <td><a href="UploadedFileController.do?action=delete&fileId=<c:out value="${file.id}"/>&bookId=<c:out value="${book.id}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>   	        
	
	<form method="POST" action="upload" enctype="multipart/form-data" >
		<input type="hidden" name="bookId" value="<c:out value="${book.id}" />"> 
		File: <input type="file" name="file" id="file" /> <br/>
		 
		<input type="submit" value="Upload" name="upload" id="upload" /> 
	</form>	
		<p><a href="AccountController.do?action=listAll&bookId=<c:out value="${book.id}" />">Back to Book</a></p>
</body>
</html>
