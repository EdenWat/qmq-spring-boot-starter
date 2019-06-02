package com.kyexpress.qmq.util;

import com.kyexpress.qmq.autoconfigure.QmqProperties;
import com.kyexpress.qmq.constant.QmqConstant;
import com.kyexpress.qmq.constant.RexConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * QMQ 内部工具类
 * @author kye
 */
@Slf4j
public class QmqUtil {
	/**
	 * 组装 QMQ MetaServer，如果属性为空，则返回默认值
	 * @param properties QMQ 配置文件
	 * @return metaServer
	 * @see QmqConstant#DEFAULT_META_SERVER
	 */
	public static String defaultMetaServer(QmqProperties properties) {
		Assert.notNull(properties, "QMQ 属性配置文件 QmqProperties 不能为空");

		// Host:Port or MetaServer
		String host = properties.getHost();
		Integer port = properties.getPort();
		String metaServer = properties.getMetaServer();

		// 优先使用 Host:Port
		if (StringUtils.hasText(host) && host.matches(RexConstant.IP4_REX) && port != null && port.toString()
				.matches(RexConstant.PORT_REX)) {
			// 拼接 MetaServer
			return String.format(QmqConstant.META_SERVER_TEMP, host, port);
		}

		// Host:Port 为空时，使用 MetaServer；如果 MetaServer 也为空，则返回默认的 MetaServer
		return StringUtils.hasText(metaServer) ? metaServer : QmqConstant.DEFAULT_META_SERVER;
	}

	/**
	 * 将 Object 转换为 Map
	 * @param object 消息对象
	 * @return Map
	 */
	public static Map<String, Object> objToMap(Object object) {
		// 参数校验，Object 不能为 Map
		if (object == null || object instanceof Map) {
			return null;
		}

		// TODO 需要支持嵌套 Object 转换到同一个 Map
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
	public static boolean greaterThan32K(String str, Charset charset) {
		// 字符串为空，直接返回 false，即使用 Message.setProperty(key, value)
		if (StringUtils.isEmpty(str)) {
			return false;
		}

		// 使用指定编码，计算出字节数
		// 将 32k 转换为字节，1 KB = 1024 bytes
		return str.getBytes(charset).length >= 32 * 1024;
	}
}
