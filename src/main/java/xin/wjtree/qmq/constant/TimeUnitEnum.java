package xin.wjtree.qmq.constant;

import java.util.concurrent.TimeUnit;

/**
 * 单位时间枚举
 * @author Wang
 */
public enum TimeUnitEnum {
	/**
	 * 1 秒
	 */
	ONE_SECOND(1, TimeUnit.SECONDS),
	/**
	 * 10 秒
	 */
	TEN_SECONDS(10, TimeUnit.SECONDS),
	/**
	 * 30 秒
	 */
	THIRTY_SECONDS(30, TimeUnit.SECONDS),
	/**
	 * 1 分钟
	 */
	ONE_MINUTE(1, TimeUnit.MINUTES),
	/**
	 * 3 分钟
	 */
	THREE_MINUTES(3, TimeUnit.MINUTES),
	/**
	 * 5 分钟
	 */
	FIVE_MINUTES(5, TimeUnit.MINUTES),
	/**
	 * 10 分钟
	 */
	TEN_MINUTES(10, TimeUnit.MINUTES),
	/**
	 * 30 分钟
	 */
	THIRTY_MINUTES(30, TimeUnit.MINUTES),
	/**
	 * 1 小时
	 */
	ONE_HOUR(1, TimeUnit.HOURS),
	/**
	 * 12 小时
	 */
	TWELVE_HOURS(12, TimeUnit.HOURS),
	/**
	 * 1 天
	 */
	ONE_DAY(1, TimeUnit.DAYS),
	/**
	 * 2 天
	 */
	TWO_DAYS(2, TimeUnit.DAYS);

	private final long duration;
	private final TimeUnit timeUnit;

	TimeUnitEnum(long duration, TimeUnit timeUnit) {
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	public long getDuration() {
		return duration;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
