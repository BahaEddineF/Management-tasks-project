package com.alibou.security.emailSender;


import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String code;
}
