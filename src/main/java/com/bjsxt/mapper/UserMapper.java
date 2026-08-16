package com.bjsxt.mapper;

import com.bjsxt.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT id, name FROM `user`")
    List<UserDO> selectAll();
}
