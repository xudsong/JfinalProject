package hisense.code.interceptor;

import hisense.code.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

/**
 * 业务层级的异常
 * <p>这类异常在message中不能包含任何调试信息，必须是用户友好的描述信息，
 * 能够显示在界面给用户看代码在抛出这类型异常之前，
 * 必须先将详细描述信息、内部异常堆栈等记录到日志中</p>
 * @author BBF
 */
public final class BusinessException extends RuntimeException {
  private static final long serialVersionUID = -4205085434296416455L;

  /**
   * 异常发生时间
   */
  private final long date;

  /**
   * 错误消息
   */
  private String message;

  /**
   * 错误代码
   */
  private String code = StringUtils.EMPTY;

  /**
   * 构造函数
   * @param message 消息
   */
  public BusinessException(final String message) {
    super(message);
    this.message = message;
    this.date = System.currentTimeMillis();
  }

  /**
   * 构造函数
   * @param pattern MessageFormat模型
   * @param msgs 多参数
   */
  public BusinessException(final String pattern, final Object... msgs) {
    super(MessageFormat.format(pattern, msgs));
    this.message = MessageFormat.format(pattern, msgs);
    this.date = System.currentTimeMillis();
  }

  /**
   * 构造函数
   * @param e 异常
   */
  public BusinessException(final Throwable e) {
    super(e);
    this.message = e.getMessage();
    this.date = System.currentTimeMillis();
  }

  /**
   * 构造函数
   * @param e 异常
   */
  public BusinessException(final Exception e) {
    super(e);
    this.message = e.getMessage();
    this.date = System.currentTimeMillis();
  }

  /**
   * 获取异常发生时间
   * @return 异常发生时间
   */
  public long getDate() {
    return date;
  }

  /**
   * 获取错误代码
   * @return 错误代码
   */
  public String getCode() {
    return code;
  }

  /**
   * 设置错误代码
   * @param code 错误代码
   */
  public void setCode(final String code) {
    this.code = code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return this.message;
  }

  /**
   * 将异常转换为自定义的BusinessException
   * <p>Java的异常被分为两大类：Checked异常和Runtime异常（运行时异常）</p>
   * @param e 异常
   * @return BusinessException
   */
  public static BusinessException convertException(final Exception e) {
    return new BusinessException(e);
  }

  /**
   * 将ErrorStack转化为String
   * @param e Throwable
   * @return stackTrace字符串
   */
  public static String getStackTraceAsString(final Throwable e) {
    if (null == e) {
      return StringUtils.EMPTY;
    }
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }

  /**
   * 在request中获取异常类
   * @param request HttpServletRequest
   * @return Throwable
   */
  public static Throwable getThrowable(final HttpServletRequest request) {
    Throwable ex = null;
    if (null != request.getAttribute("exception")) {
      ex = (Throwable) request.getAttribute("exception");
    } else if (null != request.getAttribute("javax.servlet.error.exception")) {
      ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
    return ex;
  }
}
