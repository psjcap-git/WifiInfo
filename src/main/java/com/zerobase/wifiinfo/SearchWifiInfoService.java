package com.zerobase.wifiinfo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SearchWifiInfoService {
    private static final String SELECT_SQL =
            "SELECT " +
                    "ID, " +
                    "(6371 * ACOS(COS(RADIANS( ? )) * COS(RADIANS(LAT)) * COS(RADIANS(LNT) - RADIANS( ? )) + SIN(RADIANS( ? )) * SIN(RADIANS(LAT)))) AS DISTANCE, " +
                    "MGR_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, INSTL_FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, " +
                    "CNSTC_YEAR, INOUT_DOOR, REMARS3, LAT, LNT, WORK_DTTM " +
                    "FROM WIFIINFO " +
                    "ORDER BY DISTANCE " +
                    "LIMIT ?";

    private static final String INSERTHISTORY_SQL =
            "INSERT INTO SEARCHHISTORY (LAT, LNT, SEARCH_DATE) VALUES (?, ?, ?)";

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class WifiInfo {
        private int id;
        private float distance;
        private String mgrNo;
        private String wrddfc;
        private String mainNm;
        private String address1;
        private String address2;
        private String instlFloor;
        private String instlTy;
        private String instlMby;
        private String svcSe;
        private String cmCwr;
        private String cnstcYear;
        private String inoutDoor;
        private String remars3;
        private float lat;
        private float lnt;
        private String workDttm;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Response extends ResponseBase {
        private List<WifiInfo> wifiInfos;

        public Response(boolean isError, String errorMessage) {
            super(isError, errorMessage);
            wifiInfos = null;
        }

        public Response(boolean isError, String errorMessage, List<WifiInfo> wifiInfos) {
            super(isError, errorMessage);
            this.wifiInfos = wifiInfos;
        }
    }

    public Response execute(float lat, float lnt, int maxCount) {
        // select
        QueryExecuter queryExecuter = new QueryExecuter();
        QueryExecuter.Response queryExecuterResponse = queryExecuter.prepare(SELECT_SQL);
        if(queryExecuterResponse.isError()) {
            queryExecuter.release();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        queryExecuter.setFloat(1, lat);
        queryExecuter.setFloat(2, lnt);
        queryExecuter.setFloat(3, lat);
        queryExecuter.setInt(4, maxCount);

        // get WifiIofo List
        List<WifiInfo> wifiInfos = new ArrayList<>();
        queryExecuterResponse = queryExecuter.executeQuery((rs) -> {
            WifiInfo wifiInfo = new WifiInfo(
                rs.getInt(1), rs.getFloat(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
                rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
                rs.getString(13), rs.getString(14), rs.getString(15), rs.getFloat(16), rs.getFloat(17), rs.getString(18)
            );
            wifiInfos.add(wifiInfo);
        });

        // insert searchhistory
        queryExecuterResponse = queryExecuter.prepare(INSERTHISTORY_SQL);
        if(queryExecuterResponse.isError()) {
            queryExecuter.release();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        queryExecuter.setFloat(1, lat);
        queryExecuter.setFloat(2, lnt);
        queryExecuter.setObject(3, LocalDateTime.now());

        queryExecuterResponse = queryExecuter.executeUpdate();
        if(queryExecuterResponse.isError()) {
            queryExecuter.release();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        // release & return
        queryExecuter.release();
        return new Response(false, "SUCCESS", wifiInfos);
    }
}