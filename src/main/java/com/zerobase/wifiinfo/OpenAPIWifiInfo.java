package com.zerobase.wifiinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OpenAPIWifiInfo {
    @Getter
    public static class Result {
        private String CODE;
        private String MESSAGE;
    }

    @Getter
    public static class WifiInfo {
        private String X_SWIFI_MGR_NO;
        private String X_SWIFI_WRDOFC;
        private String X_SWIFI_MAIN_NM;
        private String X_SWIFI_ADRES1;
        private String X_SWIFI_ADRES2;
        private String X_SWIFI_INSTL_FLOOR;
        private String X_SWIFI_INSTL_TY;
        private String X_SWIFI_INSTL_MBY;
        private String X_SWIFI_SVC_SE;
        private String X_SWIFI_CMCWR;
        private String X_SWIFI_CNSTC_YEAR;
        private String X_SWIFI_INOUT_DOOR;
        private String X_SWIFI_REMARS3;
        private float LAT;
        private float LNT;
        private String WORK_DTTM;
    }

    @Getter
    public static class TbWifiInfo {
        private int list_total_count;
        private Result RESULT;
        private List<WifiInfo> row;
    }

    private TbWifiInfo TbPublicWifiInfo;
}