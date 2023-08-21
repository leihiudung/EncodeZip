package com.bvt.encodezip.service;

import com.bvt.encodezip.entity.LoginLog;
import org.springframework.scheduling.annotation.Async;

import java.util.List;


public interface LoginLogService {

    List<LoginLog> getLoginLogListByDate(String startDate, String endDate);

    @Async
    void saveLoginLog(LoginLog log);


    void deleteLoginLogById(Long id);
}
