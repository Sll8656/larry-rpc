package com.larry.spi;


import cn.hutool.core.io.resource.ResourceUtil;
import com.larry.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器（支持键值对映射）
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类：接口名 =>（key => 实现类）
     */
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存（避免重复 new），类路径 => 对象实例，单例模式
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型
     */
    public static void loadAll() {
        log.info("加载所有 SPI");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /**
     * 获取某个接口的实例
     *
     * @param tClass
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        // 获取接口类的完整类名
        String tClassName = tClass.getName();

        // 从 loaderMap 中获取与该接口类相关的键值对映射表（key -> 实现类）
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);

        // 如果该接口类的映射表不存在，抛出异常
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }

        // 如果映射表中不存在指定的 key，抛出异常
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }

        // 获取到 key 对应的实现类
        Class<?> implClass = keyClassMap.get(key);

        // 获取实现类的完整类名，用于实例缓存的标识
        String implClassName = implClass.getName();

        // 如果实例缓存中没有该实现类的实例，创建并缓存它
        if (!instanceCache.containsKey(implClassName)) {
            try {
                // 使用反射创建实现类的实例，并放入缓存
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                // 如果实例化失败，抛出异常并附加错误信息
                String errorMsg = String.format("%s 类实例化失败", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }

        // 从实例缓存中获取实现类的实例并返回
        return (T) instanceCache.get(implClassName);
    }

    /**
     * 加载某个类型
     *
     * @param loadClass
     * @throws IOException
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        // 记录日志，表示开始加载指定类型的 SPI
        log.info("加载类型为 {} 的 SPI", loadClass.getName());

        // 创建一个 Map，用于存储 SPI 的 key 和实现类之间的映射关系
        Map<String, Class<?>> keyClassMap = new HashMap<>();

        // 遍历 SCAN_DIRS 中的每个扫描路径（SCAN_DIRS 是预定义的扫描目录列表）
        for (String scanDir : SCAN_DIRS) {
            // 获取当前扫描路径中与指定类型相关的资源文件 URL 列表
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());

            // 遍历每个资源文件的 URL
            for (URL resource : resources) {
                try {
                    // 打开资源文件的输入流
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    // 按行读取资源文件内容
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        // 将每行内容按 "=" 分割，获取 key 和类名
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];  // SPI 实现的 key
                            String className = strArray[1];  // 实现类的完整类名

                            // 加载实现类的 Class 对象，并存入映射表
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    // 如果资源文件加载出错，记录错误日志
                    log.error("spi resource load error", e);
                }
            }
        }

        // 将加载的 key 和实现类映射关系存入全局 loaderMap
        loaderMap.put(loadClass.getName(), keyClassMap);

        // 返回当前加载的 key 和实现类映射
        return keyClassMap;
    }
}

