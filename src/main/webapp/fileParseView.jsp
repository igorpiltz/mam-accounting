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
			
	Encoding: <c:out value="${file.encoding}" /> <br/>
	<form method="GET" action='UploadedFileController.do' name="frmSetCharset">
		<input type="hidden" name="action" value="setCharset">
		<input type="hidden" name="fileId" value="<c:out value="${file.id}" />">

		<select name="charset">
        	<c:forEach items="${charsets}" var="charset">
				<option 
						<c:if test="${file.encoding.equals(charset.key)}">
							selected="selected"
  						</c:if>
						value="<c:out value="${charset.key}" />"
					>
					<c:out value="${charset.key}" />
				</option>
					
  			</c:forEach>
  		</select> 
		<input type="submit" value="Set Charset" name="setCharset" id="setCharset" /> 
	</form>

    Script : <br/> 
    <textarea name="script" form="frmScript" rows="20" style="width: 80%;">${file.script}</textarea> <br/>
    
    <form method="POST" action='UploadedFileController.do' id="frmScript">
		<input type="hidden" name="action" value="script">
		<input type="hidden" name="fileId" value="<c:out value="${file.id}" />">
		
		<input type="submit" value="Test Script" name="testScript" id="testScript" />
		<input type="submit" value="Run Script" name="runScript" id="runScript" /> 
	</form>
    
    Result :	<br/>
    <textarea name="result" rows="20" readonly="readonly" style="width: 80%;">
    	<c:out value="${result}" />
    </textarea> <br/>    
       
    	
		
		<p><a href="AccountController.do?action=listAll&bookId=<c:out value="${book.id}" />">Back to Book</a></p>
</body>
</html>
