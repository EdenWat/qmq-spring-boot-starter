package xin.wjtree.qmq.internal;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * QMQ 内部工具类
 * @author Wang
 */
public class QmqUtil {
    /**
     * 将 Object 转换为 Map
     * @param bean 消息对象
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object bean) {
        // 参数校验，Object 不能是基础数据类型或 Map
        if (ObjectUtils.isEmpty(bean)) {
            throw new QmqException("QMQ 消息对象 bean 不能为空");
        }
        if (bean.getClass().isPrimitive() || bean instanceof Map) {
            throw new QmqException("QMQ 消息对象 bean 不能是基本数据类型或 Map 类型");
        }

        // 获取指定实体类的所有属性
        List<Field> fields = getFieldsExcludeIgnore(bean.getClass());
        if (CollectionUtils.isEmpty(fields)) {
            throw new QmqException("QMQ 消息对象 Object 不是标准的 JavaBean 或者属性为空");
        }
        Map<String, Object> map = new HashMap<>(fields.size());
        // 遍历装载每个属性的名称和值，返回 Map
        fields.forEach(f -> map.put(getName(f), getValue(f, bean)));

        // 如果 Map 为空，则返回空
        return CollectionUtils.isEmpty(map) ? null : map;
    }

    /**
     * 获取属性名称
     * @param field 实体类属性
     * @return String
     */
    private static String getName(Field field) {
        // 如果属性有别名修饰，则使用设置的属性别名
        if (field.isAnnotationPresent(QmqAlias.class)) {
            String value = field.getDeclaredAnnotation(QmqAlias.class).value();
            return StringUtils.hasText(value) ? value : field.getName();
        }

        return field.getName();
    }

    /**
     * 获取属性值
     * @param field 实体类属性
     * @param bean 实体类对象实例
     * @return Object
     */
    private static Object getValue(Field field, Object bean) {
        try {
            // 读取私有属性必须设置为 true
            field.setAccessible(true);
            return field.get(bean);
        } catch (IllegalAccessException ex) {
            throw new QmqException("QMQ 实体类转换 Map 出错", ex);
        }
    }

    /**
     * 获取指定类及其父类的所有属性，包括私有属性，并过滤 QmqIgnore 注解修饰的属性
     * @param type 类型
     * @return 类的所有属性
     */
    private static List<Field> getFieldsExcludeIgnore(Class type) {
        // 获取指定类所有属性
        List<Field> fields = getDeclaredFields(type);
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }
        // 过滤掉 QmqIgnore 注解修饰的属性
        return fields.stream().filter(field -> !field.isAnnotationPresent(QmqIgnore.class))
                .collect(Collectors.toList());
    }

    /**
     * 获取指定类及其父类的所有属性，包括私有属性
     * @param type 类型
     * @return 类的所有属性
     */
    private static List<Field> getDeclaredFields(Class type) {
        if (type == null) {
            return null;
        }

        Class tempClass = type;
        List<Field> fields = new ArrayList<>();
        // 遍历父类
        while (tempClass != null) {
            // 获取当前类的所有属性，包括私有属性
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            // 重置为父类的类型
            tempClass = tempClass.getSuperclass();
        }
        return fields;
    }

    /**
     * 比较字符串大小是否超过32K
     * <ul>
     *     <li>QMQ的Message.setProperty(key, value)如果value是字符串，则value的大小默认不能超过32K</li>
     *     <li>如果你需要传输超大的字符串，请务必使用message.setLargeString(key, value)，这样你甚至可以传输十几兆的内容了</li>
     *     <li>但是消费消息的时候也需要使用message.getLargeString(key)</li>
     * </ul>
     * @param str 字符串
     * @param charset 字符编码
     * @return true or false
     */
    public static boolean isLargeString(String str, Charset charset) {
        // 字符串为空，直接返回 false，即使用 Message.setProperty(key, value)
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        // 使用指定编码，计算出字节数
        // 将 32k 转换为字节，1 KB = 1024 bytes
        return str.getBytes(charset).length >= 32 * 1024;
    }
}
