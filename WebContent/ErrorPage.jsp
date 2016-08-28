<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
    </head>
    <body>
        <h1>500 Server Error</h1>
        <p>
        <%
        	StringWriter stackTrace = (StringWriter)request.getAttribute("stackTrace");
        	if (stackTrace != null) out.println(stackTrace.toString());
        %>
        </p>
    </body>
    
</html>