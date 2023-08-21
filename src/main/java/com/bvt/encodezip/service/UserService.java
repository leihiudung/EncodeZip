package com.bvt.encodezip.service;

import com.bvt.encodezip.entity.User;

public interface UserService {

    User findUserByUsernameAndPassword(String username, String password);

    User findUserById(Long id);

    boolean changeAccount(User user, String jwt);
}

