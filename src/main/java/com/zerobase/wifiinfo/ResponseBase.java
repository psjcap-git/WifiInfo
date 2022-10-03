package com.zerobase.wifiinfo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseBase {
    private boolean isError;
    private String errorMessage;
}
