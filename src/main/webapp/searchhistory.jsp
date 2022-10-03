<%@ page import="com.zerobase.wifiinfo.UpdateWifiInfoService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zerobase.wifiinfo.SearchWifiInfoService" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.zerobase.wifiinfo.SearchWifiInfoHistorySelectService" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<style>
    table {
        width: 100%;
        border-collapse: collapse;
        border: 1px solid;
    }

    th {
        background-color: cadetblue;
        text-align: center;
        height: 35px;
        border-collapse: collapse;
        border: 1px solid;
    }

    td {
        text-align: center;
        height: 35px;
        border-collapse: collapse;
        border: 1px solid;
    }
</style>

<script>
    function removeHistory(id) {
        console.log(id);
        location.href = "searchhistorydelete.jsp?id=" + id;
    }
</script>
<head>
    <title>와이파이 정보 구하기</title>
</head>
<body>
<%
    List<SearchWifiInfoHistorySelectService.Info> infos = null;

    SearchWifiInfoHistorySelectService service = new SearchWifiInfoHistorySelectService();
    SearchWifiInfoHistorySelectService.Response serviceResponse = service.execute();
    if(!serviceResponse.isError()) {
        infos = serviceResponse.getInfos();
    }
%>

<h1>와이파이 정보 구하기</h1>
<br>

<div class=".menu">
    <a href="index.jsp">홈</a> |
    <a href="searchhistory.jsp">히스토리 목록</a> |
    <a href="updatewifiinfo.jsp">Open API 와이파이 정보 가져오기</a>
</div>
<br>

<div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>X 좌표</th>
            <th>Y 좌표</th>
            <th>조회일자</th>
            <th>비고</th>
        </tr>
        </thead>
        <tbody>
        <%
            if(infos == null || infos.size() == 0) {
                out.print("<tr>");
                out.print("<td colspan='5'>");
                out.print("검색 기록이 없습니다.");
                out.print("</td>");
                out.print("</tr>");
            } else {
                for (int ii = 0; ii < infos.size(); ++ii) {
                    SearchWifiInfoHistorySelectService.Info info = infos.get(ii);
                    out.print("<tr>");
                    out.print("<td>" + info.getId() + "</td>");
                    out.print("<td>" + info.getLat() + "</td>");
                    out.print("<td>" + info.getLnt() + "</td>");
                    out.print("<td>" + info.getSearchData() + "</td>");
                    out.print("<td><button onclick=\"removeHistory(" + info.getId() + ");\">삭제</button></td>");
                    out.print("</tr>");
                }
            }
        %>
        </tbody>
    </table>
</div>

</body>
</html>