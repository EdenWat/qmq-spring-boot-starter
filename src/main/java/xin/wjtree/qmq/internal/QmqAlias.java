package xin.wjtree.qmq.internal;

import java.lang.annotation.*;

/**
 * 重命名实体类属性名称
 * @author Wang
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QmqAlias {
    /**
     * 属性别名的名称
     * @return 属性别名
     */
    String value();
}
