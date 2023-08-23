package com.bvt.encodezip.mapper;

import com.bvt.encodezip.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User findByUsername(String username);

    User findById(Long id);

    int updateUserByUsername(String username, User user);
}
