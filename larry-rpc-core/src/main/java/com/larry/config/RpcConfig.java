package com.larry.config;

import com.larry.serializer.SerializerKeys;
import lombok.Data;
/**
 * RPC 框架配置
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "larry-rpc";
    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端⼝号
     */
    private Integer serverPort = 8081;
    /**
     * 模拟调⽤
     */
    private boolean mock = false;
    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中⼼配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
}