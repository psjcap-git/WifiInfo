package com.zerobase.wifiinfo;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import okhttp3.*;

import java.util.ArrayList;
import java.util.List;

public class OpenAPIService {
    private static final String AUTHKEY = "444548455870736a32376e72734c76";
    private static final String OPEN_API_URL_FORMAT = "http://openapi.seoul.go.kr:8088/%s/json/TbPublicWifiInfo/%d/%d/";
    private static final int MAX_DOWNLOAD_COUNT = 1000;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Response extends ResponseBase {
        private OpenAPIWifiInfo openAPIWifiInfo;

        public Response(boolean isSuccess, String errorMessage) {
            super(isSuccess, errorMessage);
            this.openAPIWifiInfo = null;
        }

        public Response(boolean isSuccess, String errorMessage, OpenAPIWifiInfo openAPIWifiInfo) {
            super(isSuccess, errorMessage);
            this.openAPIWifiInfo = openAPIWifiInfo;
        }
    }

    public Response getPublicWifiDataAll() {
        OpenAPIWifiInfo openAPIWifiInfo = this.getWifiInfo(1, 1);
        if(openAPIWifiInfo == null) {
            return new Response(true, "Cannot load Data from OpenAPI", null);
        }

        int maxCount = openAPIWifiInfo.getTbPublicWifiInfo().getList_total_count();
        int startIndex = 2;
        int endIndex = Math.min(MAX_DOWNLOAD_COUNT, maxCount);
        while(true) {
            OpenAPIWifiInfo info = this.getWifiInfo(startIndex, endIndex);
            if(info == null) {
                return new Response(true, "Cannot load Data from OpenAPI", null);
            }

            openAPIWifiInfo.getTbPublicWifiInfo().getRow().addAll(info.getTbPublicWifiInfo().getRow());

            if(endIndex != maxCount) {
                startIndex = endIndex + 1;
                endIndex = Math.min(endIndex + MAX_DOWNLOAD_COUNT, maxCount);
            } else {
                break;
            }
        }

        return new Response(false, "", openAPIWifiInfo);
    }

    public OpenAPIWifiInfo getWifiInfo(int start, int end) {
        try {
            String url = String.format(OPEN_API_URL_FORMAT, AUTHKEY, start, end);
            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            okhttp3.Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                ResponseBody body = response.body();
                if(body != null) {
                    Gson gson = new Gson();
                    OpenAPIWifiInfo openAPIWifiInfo = gson.fromJson(body.string(), OpenAPIWifiInfo.class);
                    if(openAPIWifiInfo.getTbPublicWifiInfo().getRESULT().getCODE().equals("INFO-000")) {
                        return openAPIWifiInfo;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
    }
}