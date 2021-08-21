package com.lonicera.exception;

/**
 * @author LiBowei
 * @date 2021年-07月-05日
 **/
public class ExprEvalException extends RuntimeException {
  public ExprEvalException(Throwable t){
    super(t);
  }

  public ExprEvalException(String message){
    super(message);
  }
}
