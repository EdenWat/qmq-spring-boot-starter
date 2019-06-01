package com.kyexpress.qmq.constant;

/**
 * 正则表达式-匹配格式
 * @author Wang
 */
public final class RexConstant {
	/**
	 * IPv4 地址正则表达式，示例：127.0.0.1
	 */
	public static final String IP4_REX = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

	/**
	 * IPv6 地址正则表达式，示例：240e:fe:2c2c:5b00:c513:fb36:5e47:980
	 */
	public static final String IP6_REX = "^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$";

	/**
	 * 端口号正则表达式，示例：8080
	 */
	public static final String PORT_REX = "^(1(02[4-9]|0[3-9][0-9]|[1-9][0-9]{2})|[2-9][0-9]{3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
}
