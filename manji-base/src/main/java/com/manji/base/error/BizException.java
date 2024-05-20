package com.manji.base.error;


import java.io.Serial;

public class BizException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;



    /**
     * 默认构造函数。
     */
    public BizException() {
        super();
    }

    /**
     * 使用指定的详细消息构造新的业务异常。
     *
     * @param message 详细消息
     */
    public BizException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细消息和原因构造新的业务异常。
     *
     * @param message 详细消息
     * @param cause   原因
     */
    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用指定的原因构造新的业务异常。
     *
     * @param cause 原因
     */
    public BizException(Throwable cause) {
        super(cause);
    }
}
