package com.larry.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * 配置⼯具类，用于从外部配置文件中加载配置，支持按照指定前缀和环境加载。
 */
public class ConfigUtils {
    /**
     * 加载配置对象
     *
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }
    /**
     * 加载配置对象，⽀持区分环境
     *
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }

        // 支持 YAML 文件
        String propertiesFile = configFileBuilder + ".properties";
        String yamlFile = configFileBuilder + ".yml";
        String yamlFileAlt = configFileBuilder + ".yaml";

        // 尝试加载 YAML 配置文件
        if (fileExists(yamlFile)) {
            return loadFromYaml(tClass, yamlFile, prefix);
        } else if (fileExists(yamlFileAlt)) {
            return loadFromYaml(tClass, yamlFileAlt, prefix);
        }

        // 尝试加载 properties 文件
        Props props = new Props(propertiesFile);
        return props.toBean(tClass, prefix);
    }

    /**
     * 加载 YAML 配置文件
     *
     * @param tClass 配置类
     * @param filePath 文件路径
     * @param prefix 配置前缀
     * @param <T> 泛型
     * @return 配置对象
     */
    private static <T> T loadFromYaml(Class<T> tClass, String filePath, String prefix) {
        try (InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("YAML file not found: " + filePath);
            }
            Yaml yaml = new Yaml();
            Map<String, Object> yamlMap = yaml.load(inputStream);

            // 按前缀过滤配置
            Map<String, Object> filteredMap = filterByPrefix(yamlMap, prefix);

            // 使用 Hutool 转换为对象
            return cn.hutool.core.bean.BeanUtil.toBean(filteredMap, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML configuration: " + filePath, e);
        }
    }

    /**
     * 根据前缀过滤配置
     *
     * @param yamlMap YAML 文件内容
     * @param prefix 配置前缀
     * @return 过滤后的配置
     */
    private static Map<String, Object> filterByPrefix(Map<String, Object> yamlMap, String prefix) {
        if (StrUtil.isBlank(prefix)) {
            return yamlMap;
        }

        Object subMap = yamlMap.get(prefix);
        if (subMap instanceof Map) {
            return (Map<String, Object>) subMap;
        }

        throw new IllegalArgumentException("Invalid prefix or YAML structure for prefix: " + prefix);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    private static boolean fileExists(String filePath) {
        return ConfigUtils.class.getClassLoader().getResource(filePath) != null;
    }
}
