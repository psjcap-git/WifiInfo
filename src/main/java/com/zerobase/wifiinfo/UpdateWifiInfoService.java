package com.zerobase.wifiinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class UpdateWifiInfoService {
    private static final String DELETE_SQL = "DELETE FROM WIFIINFO";
    private static final String INSERT_SQL =
            "INSERT INTO WIFIINFO " +
                    "(MGR_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, INSTL_FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, CNSTC_YEAR, INOUT_DOOR, REMARS3, LAT, LNT, WORK_DTTM) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Response extends ResponseBase {
        private int dataCount;

        public Response(boolean isError, String errorMessage) {
            super(isError, errorMessage);
            this.dataCount = 0;
        }

        public Response(boolean isError, String errorMessage, int dataCount) {
            super(isError, errorMessage);
            this.dataCount = dataCount;
        }
    }

    public Response execute() {
        // open api
        OpenAPIService openAPIService = new OpenAPIService();
        OpenAPIService.Response openAPIServiceResponse = openAPIService.getPublicWifiDataAll();
        if (openAPIServiceResponse.isError()) {
            return new Response(true, openAPIServiceResponse.getErrorMessage());
        }

        // transaction start
        QueryExecuter queryExecuter = new QueryExecuter();
        QueryExecuter.Response queryExecuterResponse = queryExecuter.startTransaction();
        if(queryExecuterResponse.isError()) {
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        // delete
        queryExecuterResponse = queryExecuter.prepare(DELETE_SQL);
        if(queryExecuterResponse.isError()) {
            queryExecuter.rollback();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        queryExecuterResponse = queryExecuter.executeUpdate();
        if(queryExecuterResponse.isError()) {
            queryExecuter.rollback();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        // insert
        queryExecuterResponse = queryExecuter.prepare(INSERT_SQL);
        if(queryExecuterResponse.isError()) {
            queryExecuter.rollback();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        List<OpenAPIWifiInfo.WifiInfo> openAPIWifiInfos = openAPIServiceResponse.getOpenAPIWifiInfo().getTbPublicWifiInfo().getRow();
        for(int ii = 0; ii < openAPIWifiInfos.size(); ++ii) {
            OpenAPIWifiInfo.WifiInfo info = openAPIWifiInfos.get(ii);
            queryExecuter.setString(1, info.getX_SWIFI_MGR_NO());
            queryExecuter.setString(2, info.getX_SWIFI_WRDOFC());
            queryExecuter.setString(3, info.getX_SWIFI_MAIN_NM());
            queryExecuter.setString(4, info.getX_SWIFI_ADRES1());
            queryExecuter.setString(5, info.getX_SWIFI_ADRES2());
            queryExecuter.setString(6, info.getX_SWIFI_INSTL_FLOOR());
            queryExecuter.setString(7, info.getX_SWIFI_INSTL_TY());
            queryExecuter.setString(8, info.getX_SWIFI_INSTL_MBY());
            queryExecuter.setString(9, info.getX_SWIFI_SVC_SE());
            queryExecuter.setString(10, info.getX_SWIFI_CMCWR());
            queryExecuter.setString(11, info.getX_SWIFI_CNSTC_YEAR());
            queryExecuter.setString(12, info.getX_SWIFI_INOUT_DOOR());
            queryExecuter.setString(13, info.getX_SWIFI_REMARS3());
            // OpenAPI에서 LAT와 LNT가 반대로 나옴.
            // DB저장시 바꿔서 저장.
            queryExecuter.setFloat(14, info.getLNT());
            queryExecuter.setFloat(15, info.getLAT());
            queryExecuter.setString(16, info.getWORK_DTTM());

            queryExecuterResponse = queryExecuter.executeUpdate();
            if(queryExecuterResponse.isError()) {
                queryExecuter.rollback();
                return new Response(true, queryExecuterResponse.getErrorMessage());
            }
        }

        queryExecuterResponse = queryExecuter.executeUpdate();
        if(queryExecuterResponse.isError()) {
            queryExecuter.rollback();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        // commit & release
        queryExecuterResponse = queryExecuter.commit();
        if(queryExecuterResponse.isError()) {
            queryExecuter.rollback();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }
        queryExecuter.release();

        // success
        return new Response(false, "SUCCESS", openAPIWifiInfos.size());
    }
}
