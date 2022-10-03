package com.zerobase.wifiinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

public class SearchWifiInfoHistoryDeleteService {
    private static final String DELETE_SQL = "DELETE FROM SEARCHHISTORY WHERE ID = ?";

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    public static class Response extends ResponseBase {
        private int deleteCount;

        public Response(boolean isError, String errorMessage) {
            super(isError, errorMessage);
            this.deleteCount = 0;
        }

        public Response(boolean isError, String errorMessage, int deleteCount) {
            super(isError, errorMessage);
            this.deleteCount = deleteCount;
        }
    }

    public Response execute(int id) {
        // delete
        QueryExecuter queryExecuter = new QueryExecuter();
        QueryExecuter.Response queryExecuterResponse = queryExecuter.prepare(DELETE_SQL);
        if(queryExecuterResponse.isError()) {
            queryExecuter.release();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        queryExecuter.setInt(1, id);

        QueryExecuter.Response queryExcuterResponse = queryExecuter.executeUpdate();
        if(queryExecuterResponse.isError()) {
            queryExecuter.release();
            return new Response(true, queryExecuterResponse.getErrorMessage());
        }

        // release & return
        queryExecuter.release();
        return new Response(false, "SUCCESS", queryExcuterResponse.getExecuteCount());
    }
}
