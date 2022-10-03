package com.zerobase.wifiinfo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class SearchWifiInfoHistorySelectService {
    private static final String SELECT_SQL = "SELECT ID, LAT, LNT, SEARCH_DATE FROM SEARCHHISTORY ORDER BY ID DESC";

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Info {
        private int id;
        private float lat;
        private float lnt;
        private String searchData;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    public static class Response extends ResponseBase {
        private List<Info> infos;

        public Response(boolean isError, String errorMessage) {
            super(isError, errorMessage);
            this.infos = null;
        }

        public Response(boolean isError, String errorMessage, List<Info> infos) {
            super(isError, errorMessage);
            this.infos = infos;
        }
    }

    public Response execute() {
        // select
        QueryExecuter queryExecuter = new QueryExecuter();
        QueryExecuter.Response queryExecuterResponse = queryExecuter.prepare(SELECT_SQL);
        if(queryExecuterResponse.isError()) {
            queryExecuter.release();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        List<Info> infos = new ArrayList<>();
        queryExecuter.executeQuery((rs) -> {
            Info info = new Info(
                rs.getInt(1),
                rs.getFloat(2),
                rs.getFloat(3),
                rs.getString(4)
            );
            infos.add(info);
        });

        // release & return
        queryExecuter.release();
        return new Response(false, "SUCCESS", infos);
    }
}
