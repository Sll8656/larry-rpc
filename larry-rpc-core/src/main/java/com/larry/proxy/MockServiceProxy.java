package com.larry.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock 服务代理（JDK 动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    /**
     * 调⽤代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据⽅法的返回值类型，⽣成特定的默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }
    /**
     * ⽣成指定类型的默认值对象（可⾃⾏完善默认值逻辑）
     *
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type) {
        // 基本类型
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            } else if (type == float.class) {
                return 0.0f;
            } else if (type == double.class) {
                return 0.0d;
            } else if (type == char.class) {
                return '\u0000'; // char 的默认值是 '\u0000' (空字符)
            } else if (type == byte.class) {
                return (byte) 0;
            }
        }
        // 对象类型
        return null;
    }
}
