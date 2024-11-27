package com.larry.serializer;

import java.io.IOException;

/**
 * 序列化器接⼝
 */
public interface Serializer {
    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;
    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
    序列化器
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
