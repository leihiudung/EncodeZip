package com.bvt.encodezip.service.impl;

import com.bvt.encodezip.entity.User;
import com.bvt.encodezip.mapper.UserMapper;
import com.bvt.encodezip.service.UserService;
import com.bvt.encodezip.util.HashUtils;
import com.bvt.encodezip.util.JwtUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.service.impl
 * @className: UserServiceImpl
 * @author: Tom
 * @description: TODO
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不能存在");
        }

        return user;
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (!HashUtils.matchBC(password, user.getPassword())) {
            throw new UsernameNotFoundException("密码错误");
        }
        return user;
    }

    @Override
    public User findUserById(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @Override
    public boolean changeAccount(User user, String jwt) {
        String username = JwtUtils.getTokenBody(jwt).getSubject();
        user.setPassword(HashUtils.getBC(user.getPassword()));
        if (userMapper.updateUserByUsername(username, user) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }
}
