<%@ page language="java" contentType="text/html;charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="ISO-8859-1">
<title>Companies</title>
</head>
<body>


    <table border=1>
        <thead>
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>State ID Nr</th>
                <th>Type</th>
                <th colspan=3>Action</th>
            </tr>
        </thead>
        <tbody>
        	<c:forEach items="${companies}" var="comp">
                <tr>
                    <td><c:out value="${comp.id}" /></td>
                    <td><c:out value="${comp.name}" /></td>
                    <td><c:out value="${comp.stateIdNumber}" /></td>
                    <td><c:out value="${comp.companyType}" /></td>
                    <td><a href="CompanyController.do?action=edit&companyId=<c:out value="${comp.id}"/>">Update</a></td>
                    <td><a href="javascript:askDeleteCompany(${comp.id}, '${comp.name}');">Delete</a></td>
                    <td><a href="CompanyController.do?action=bookkeeping&companyId=<c:out value="${comp.id}"/>">Bookkeeping</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p><a href="CompanyController.do?action=insert">Add Company</a></p>

    <script>
      function askDeleteCompany(compId, compName) {
	  var response = window.prompt("If you wish to delete the company " + compName + "(" + compId + "), enter YES below", "");
	  if (response == "YES") {
	      window.location.assign("CompanyController.do?action=delete&companyId=" + compId);
	  }
      }
    </script>

      
</body>
</html>
