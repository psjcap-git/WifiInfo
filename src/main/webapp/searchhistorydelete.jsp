<%@ page import="com.zerobase.wifiinfo.SearchWifiInfoHistoryDeleteService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    String idParam = request.getParameter("id");
    if(idParam == null || idParam.equals("")) {
        response.sendRedirect("searchhistory.jsp");
    } else {
        int id = Integer.parseInt(idParam);
        SearchWifiInfoHistoryDeleteService service = new SearchWifiInfoHistoryDeleteService();
        SearchWifiInfoHistoryDeleteService.Response serviceResponse = service.execute(id);
        response.sendRedirect("searchhistory.jsp");
    }
%>
</body>
</html>
