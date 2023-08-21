package com.bvt.encodezip.service.impl;

import com.bvt.encodezip.entity.LoginLog;
import com.bvt.encodezip.service.LoginLogService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Override
    public List<LoginLog> getLoginLogListByDate(String startDate, String endDate) {
        return null;
    }

    @Override
    public void saveLoginLog(LoginLog log) {

    }

    @Override
    public void deleteLoginLogById(Long id) {

    }
}
