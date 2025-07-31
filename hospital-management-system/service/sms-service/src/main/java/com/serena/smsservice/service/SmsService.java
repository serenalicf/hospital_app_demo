package com.serena.smsservice.service;

public interface SmsService {
    boolean send(String phone, String code);
}
