package com.lonicera.token;

public class StringToken extends AbstractToken<StringToken> {

    public StringToken(){

    }

    public StringToken(char[] chars, int offset, int length) {
        super(chars, offset, length);
    }
}
