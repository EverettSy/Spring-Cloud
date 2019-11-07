package com.syraven.cloud.service;

import com.syraven.cloud.domain.User;

import java.util.List;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/11/7 22:17
 */
public interface UserService {

    void create(User user);

    User getUser(Integer id);

    void update(User user);

    void delete(Integer id);

    User getByUsername(String username);

    List<User> getUserByIds(List<Integer> ids);
}
