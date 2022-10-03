<%@ page import="com.zerobase.wifiinfo.UpdateWifiInfoService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 정보 구하기</title>
</head>
<body>
    <br>
    <div style="text-align:center; font-size:3em; font-weight:bold">
    <%
        UpdateWifiInfoService service = new UpdateWifiInfoService();
        UpdateWifiInfoService.Response serviceResponse = service.execute();
        if(serviceResponse.isError()) {
            out.print("WIFI정보를 저장하는데 실패하였습니다. - " + serviceResponse.getErrorMessage());
        } else {
            out.print(serviceResponse.getDataCount() + "개의 WIFI정보를 정상적으로 저장하였습니다.");
        }
    %>
    </div>

    <br>
    <div style="text-align:center">
        <a href="index.jsp">홈 으로 가기</a>
    </div>

</body>
</html>
