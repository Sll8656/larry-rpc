package com.larry;

import com.larry.registry.LocalRegistry;
import com.larry.server.VertHttpServer;
import com.larry.service.UserService;

public class EasyProviderExample {
    public static void main(String[] args) {
        RpcApplication.init();
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        VertHttpServer vertHttpServer = new VertHttpServer();
        vertHttpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
