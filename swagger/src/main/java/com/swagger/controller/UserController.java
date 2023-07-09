package com.swagger.controller;

import com.swagger.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Api("用户接口")
public class UserController {
    @GetMapping("get")
    @ApiOperation("获取")
    public User getUser(User user) {
        user.setId(user.getId());
        user.setName(user.getName());
        user.setAge(user.getAge());
        user.setCreateTime(user.getCreateTime());
        user.setUpdateTime(user.getUpdateTime());
        return user;
    }

    @PostMapping(value = "/upload")
    @ApiOperation("上传")
    public String upload(@RequestPart("file") MultipartFile file) {
        return "上传成功";
    }
}
