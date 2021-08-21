package com.lonicera.exception;

import com.lonicera.token.Token;

public class UnExpectTokenException extends RuntimeException {
    private String reportPattern;
    private Token present;
    private String expect;
    public UnExpectTokenException(String pattern, Token present, String expect){
        int offset = present.offset() > 0 ? present.offset() : pattern.length();
        this.reportPattern = new StringBuilder(pattern).insert(offset + 1, '←').toString();
        this.present = present;
        this.expect = expect;
    }


    public UnExpectTokenException(String pattern, Token present, Class<? extends Token> clazz){
        this(pattern, present, clazz.getSimpleName());
    }

    @Override
    public String toString(){
        String prefix = "Error Pattern : " + reportPattern + "  @Offset = " + present.offset() + ". ";
        if(expect == null){
            return prefix + "Try Delete :" + present.text();
        }else {
            return prefix + "Expect : " + expect;
        }
    }
}
