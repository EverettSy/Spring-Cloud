package com.syraven.cloud.service.impl;

import com.syraven.cloud.domain.User;
import com.syraven.cloud.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/11/7 22:25
 */
@Service
public class UserServiceImpl implements UserService {

    private List<User> userList;

    @Override
    public void create(User user) {
        userList.add(user);
    }

    @Override
    public User getUser(Integer id) {
        List<User> findUserList = userList.stream().filter(userItem -> userItem.getId().equals(id)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(findUserList)) {
            return findUserList.get(0);
        }
        return null;
    }

    @Override
    public void update(User user) {
        userList.stream().filter(userItem -> userItem.getId().equals(user.getId())).forEach(userItem -> {
            userItem.setUsername(user.getUsername());
            userItem.setUsername(user.getPassword());
        });

    }

    @Override
    public void delete(Integer id) {
        User user = getUser(id);
        if (user != null) {
            userList.remove(user);
        }
    }

    @Override
    public User getByUsername(String username) {
        List<User> findUserList = userList.stream().filter(userItem -> userItem.getUsername().equals(username)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(findUserList)) {
            return findUserList.get(0);
        }
        return null;
    }

    @Override
    public List<User> getUserByIds(List<Integer> ids) {
        return userList.stream().filter(userItem -> ids.contains(userItem.getId())).collect(Collectors.toList());
    }

    @PostConstruct
    public void initData() {
        userList = new ArrayList<>();
        userList.add(new User(1,"syraven","123456"));
        userList.add(new User(2,"andy","123456"));
        userList.add(new User(3,"mark","123456"));
    }
}