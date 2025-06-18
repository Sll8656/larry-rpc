package com.larry.config;

import lombok.Data;

/**
 * RPC 框架注册中⼼配置
 */
@Data
public class RegistryConfig {
    /**
     * 注册中⼼类别
     */
    private String registry = "etcd";
    /**
     * 注册中⼼地址
     */
    private String address = "http://localhost:2380";
    /**
     * ⽤户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;
}