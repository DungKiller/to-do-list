package com.dunght.mvc.todolist.service;

import com.dunght.mvc.todolist.dto.UserDto;
import com.dunght.mvc.todolist.entity.User;

public interface UserService {
    User findByUsernameAndPassword(String username, String password);

    void createUser(UserDto userDto);
}
