package com.bjsxt.service.impl;

import com.bjsxt.domain.UserDO;
import com.bjsxt.dto.UserDTO;
import com.bjsxt.mapper.UserMapper;
import com.bjsxt.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> listUsers() {
        return userMapper.selectAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private UserDTO toDTO(UserDO user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
