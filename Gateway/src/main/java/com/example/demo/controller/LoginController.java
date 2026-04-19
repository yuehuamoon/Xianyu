package com.example.demo.controller;

import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    // 模拟登录接口，生成Token
    @PostMapping("/login")
    public String login(@RequestParam String phone, @RequestParam String password) {
        // 1. 校验用户名密码（省略真实逻辑）
        if ("admin".equals(phone) && "123456".equals(password)) {
            // 2. 构建JWT载荷
            Map claims = new HashMap<>();
            claims.put("userId", "1001");
            claims.put("phone", phone);
            claims.put("role", "admin");

            // 3. 生成Token
            return jwtUtil.genJwt(claims);
        }
        return "用户名或密码错误";
    }
}