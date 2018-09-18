package com.example.test.mapper;

import com.example.test.pojo.Appuser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
//@Mapper
public interface AppUserMapper {

    //@Select("select * from appuser limit 1")
    Appuser getAppuser();

    int getAppuserCount();
}
