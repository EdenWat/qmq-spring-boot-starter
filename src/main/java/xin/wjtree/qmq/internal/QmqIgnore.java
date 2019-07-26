package xin.wjtree.qmq.internal;

import java.lang.annotation.*;

/**
 * 忽略实体类字段
 * @author Wang
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QmqIgnore {
}
