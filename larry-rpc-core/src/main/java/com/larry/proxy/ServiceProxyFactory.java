package com.larry.proxy;

import com.larry.RpcApplication;
import com.larry.config.RpcConfig;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ServiceProxyFactory {
    /**
     * 根据服务类获取代理对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        System.out.println("Creating proxy for: " + serviceClass.getName());
        RpcConfig config = RpcApplication.getRpcConfig();
        if (config == null) {
            throw new IllegalStateException("RpcConfig is not initialized");
        }

        if (config.isMock()) {
            T mockProxy = getMockProxy(serviceClass);
            System.out.println("Mock proxy created: " + mockProxy);
            return mockProxy;
        }

        try {
            T proxy = (T) Proxy.newProxyInstance(
                    serviceClass.getClassLoader(),
                    new Class[]{serviceClass},
                    new ServiceProxy()
            );
//            System.out.println("Proxy created: " + proxy);
//            System.out.println("Proxy created: " + proxy);
            return proxy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据服务类获取 Mock 代理对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }

}