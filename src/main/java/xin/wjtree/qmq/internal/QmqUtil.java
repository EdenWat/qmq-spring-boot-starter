package xin.wjtree.qmq.internal;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * QMQ 内部工具类
 * @author kye
 */
public class QmqUtil {
    private static final Logger log = LoggerFactory.getLogger(QmqUtil.class);

    /**
     * 将 Object 转换为 Map
     * @param object 消息对象
     * @return Map
     */
    public static Map<String, Object> objToMap(Object object) {
        // 参数校验，Object 不能是基础数据类型或 Map
        if (ObjectUtils.isEmpty(object)) {
            throw new QmqException("QMQ 消息对象 Object 不能为空");
        }
        if (object.getClass().isPrimitive() || object instanceof Map) {
            throw new QmqException("QMQ 消息对象 Object 不能是基本数据类型或 Map 类型");
        }

        // TODO 增加注解，设置注解属性为别名，标记注解别名的，发送消息时 key 会使用别名而不是字段名
        Map<String, Object> describe = null;

        try {
            // 将 Object 转换为 Map
            describe = PropertyUtils.describe(object);
            if (CollectionUtils.isEmpty(describe)) {
                log.warn("QMQ 消息对象无法转换为 Map ，转换结果为空");
                return null;
            }

            // 移除 Object 的 class 名称
            describe.remove("class");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            log.error("QMQ 消息对象无法转换为 Map", ex);
        }

        return describe;
    }

    public static Map<String, Object> objToMap2(Object object) {
        Class<?> aClass = object.getClass();

        return null;
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
