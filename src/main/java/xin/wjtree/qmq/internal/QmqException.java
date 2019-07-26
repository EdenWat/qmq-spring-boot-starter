package xin.wjtree.qmq.internal;

/**
 * Qmq 异常类
 * @author Wang
 */
public class QmqException extends RuntimeException {
    private static final long serialVersionUID = 7377392559851081601L;

    public QmqException() {
        super();
    }

    public QmqException(String message) {
        super(message);
    }

    public QmqException(String message, Throwable cause) {
        super(message, cause);
    }

    public QmqException(Throwable cause) {
        super(cause);
    }

    public QmqException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}