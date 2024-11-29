package com.sonnguyen.iamservice2.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Otp {
    private String otp;
    private String email;
    private long timeToLiveSeconds;
}
