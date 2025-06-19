package com.larry;

import com.larry.config.RegistryConfig;
import com.larry.config.RpcConfig;
import com.larry.model.ServiceMetaInfo;
import com.larry.registry.LocalRegistry;
import com.larry.registry.Registry;
import com.larry.registry.RegistryFactory;
import com.larry.server.VertHttpServer;
import com.larry.service.UserService;

public class ProviderExample {
    public static void main(String[] args) {
        // 框架初始化
        RpcApplication.init();
        // 注册中心
//        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);
        // 注册服务到注册中⼼
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            System.out.println("注册中心实例类型: " + registry.getClass().getName());
            registry.register(serviceMetaInfo);
            System.out.println("注册成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //启动web
        VertHttpServer vertHttpServer = new VertHttpServer();
        vertHttpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
//        vertHttpServer.doStart(8082);
    }
}
