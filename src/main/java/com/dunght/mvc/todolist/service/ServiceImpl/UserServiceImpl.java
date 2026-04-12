package com.dunght.mvc.todolist.service.ServiceImpl;

import com.dunght.mvc.todolist.dto.UserDto;
import com.dunght.mvc.todolist.entity.User;
import com.dunght.mvc.todolist.repository.UserRepository;
import com.dunght.mvc.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public void createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        } else if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFullname(userDto.getFullname());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
    }
}
