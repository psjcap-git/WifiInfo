<%@ page import="com.zerobase.wifiinfo.UpdateWifiInfoService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.zerobase.wifiinfo.SearchWifiInfoService" %>
<%@ page import="java.util.ArrayList" %>
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
        function checkNumber(obj) {
            obj.value = obj.value.replace(/[^0-9.]/g, "");
        }

        function getCurrentPosition() {
            navigator.geolocation.getCurrentPosition(getCurrentPositionSuccess, getCurrentPositionFail);
        }

        function getCurrentPositionSuccess(position) {
            document.getElementById("LAT").value = position.coords.latitude;
            document.getElementById("LNT").value = position.coords.longitude;
        }

        function getCurrentPositionFail() {
            console.log("내 위치 정보를 가여졸 수 없습니다.");
        }

        function getWifiInfoAround() {
            let lat = document.getElementById("LAT").value.trim();
            let lnt = document.getElementById("LNT").value.trim();
            location.href="index.jsp?LAT=" + lat + "&LNT=" + lnt;
        }

        function removeHistory(id) {

        }
    </script>
<head>
    <title>와이파이 정보 구하기</title>
</head>
<body>
    <%
        String lat = request.getParameter("LAT");
        String lnt = request.getParameter("LNT");
        boolean isFirstPage = (lat == null || lat.equals("")) || (lnt == null || lnt.equals(""));

        if(isFirstPage) {
            lat = "0.0";
            lnt = "0.0";
        }

        List<SearchWifiInfoService.WifiInfo> wifiInfos = null;
        if(!isFirstPage) {
            float latValue = Float.parseFloat(lat);
            float lntValue = Float.parseFloat(lnt);
            float maxDistance = 0.5f;

            SearchWifiInfoService service = new SearchWifiInfoService();
            SearchWifiInfoService.Response serviceResponse = service.execute(latValue, lntValue, 30);
            if(!serviceResponse.isError()) {
                wifiInfos = serviceResponse.getWifiInfos();
            }
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

    <diiv>
        <label for="LAT">LAT :</label><input type="text" value="<%=lat%>" id="LAT" oninput='return checkNumber(this)'>,
        <label for="LNT">LNT :</label><input type="text" value="<%=lnt%>" id="LNT" oninput='return checkNumber(this)'>
        <button id="getMyPos" onclick="getCurrentPosition();">내 위치 가져오기</button>
        <button id="getWifiInfoAround" onclick="getWifiInfoAround();">근처 WIFI 정보 보기</button>
    </diiv>
    <br><br>

    <div>
        <table>
            <thead>
                <tr>
                    <th>거리(Km)</th>
                    <th>관리번호</th>
                    <th>자치구</th>
                    <th>와이파이명</th>
                    <th>도로명주소</th>
                    <th>상세주소</th>
                    <th>설치위치(층)</th>
                    <th>설치유형</th>
                    <th>설치기관</th>
                    <th>서비스구분</th>
                    <th>망종류</th>
                    <th>설치년도</th>
                    <th>실내외구분</th>
                    <th>WIFI접속환경</th>
                    <th>X좌표</th>
                    <th>Y좌표</th>
                    <th>작업일자</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if(isFirstPage) {
                        out.print("<tr>");
                        out.print("<td colspan='17'>");
                        out.print("위치 정보를 입력한 후에 조회해 주세요.");
                        out.print("</td>");
                        out.print("</tr>");
                    } else {
                        if(wifiInfos == null || wifiInfos.size() == 0) {
                            out.print("<tr>");
                            out.print("<td colspan='17'>");
                            out.print("WIFI 정보가 없습니다.");
                            out.print("</td>");
                            out.print("</tr>");
                        } else {
                            for(int ii = 0; ii < wifiInfos.size(); ++ii) {
                                SearchWifiInfoService.WifiInfo wifiInfo = wifiInfos.get(ii);
                                out.print("<tr>");
                                out.print("<td>" + wifiInfo.getDistance() + "</td>");
                                out.print("<td>" + wifiInfo.getMgrNo() + "</td>");
                                out.print("<td>" + wifiInfo.getWrddfc() + "</td>");
                                out.print("<td>" + wifiInfo.getMainNm() + "</td>");
                                out.print("<td>" + wifiInfo.getAddress1() + "</td>");
                                out.print("<td>" + wifiInfo.getAddress2() + "</td>");
                                out.print("<td>" + wifiInfo.getInstlFloor() + "</td>");
                                out.print("<td>" + wifiInfo.getInstlTy() + "</td>");
                                out.print("<td>" + wifiInfo.getInstlMby() + "</td>");
                                out.print("<td>" + wifiInfo.getSvcSe() + "</td>");
                                out.print("<td>" + wifiInfo.getCmCwr() + "</td>");
                                out.print("<td>" + wifiInfo.getCnstcYear() + "</td>");
                                out.print("<td>" + wifiInfo.getInoutDoor() + "</td>");
                                out.print("<td>" + wifiInfo.getRemars3() + "</td>");
                                out.print("<td>" + wifiInfo.getLat() + "</td>");
                                out.print("<td>" + wifiInfo.getLnt() + "</td>");
                                out.print("<td>" + wifiInfo.getWorkDttm() + "</td>");
                                out.print("</tr>");
                            }
                        }
                    }
                %>
            </tbody>
        </table>
    </div>

</body>
</html>