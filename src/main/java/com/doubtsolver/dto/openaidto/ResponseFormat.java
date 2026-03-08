package com.doubtsolver.dto.openaidto;

import lombok.Data;

@Data
public class ResponseFormat {

    private String type;

    public ResponseFormat(String type) {
        super();
        this.type = type;
    }


}
