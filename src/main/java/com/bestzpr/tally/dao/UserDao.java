package com.bestzpr.tally.dao;

import com.bestzpr.tally.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @className UserDao
 * @Desc 用户
 * @Author 张埔枘
 * @Date 2023/12/8 00:14
 * @Version 1.0
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByOpenId(String openId);
}