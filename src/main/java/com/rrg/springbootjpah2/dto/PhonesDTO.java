package com.rrg.springbootjpah2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PhonesDTO {

    private String number;
    private String cityCode;
    private String countryCode;
}
