package com.lonicera.token;

public class SkipToken extends AbstractToken {
    public SkipToken(char c, int offset) {
        super(String.valueOf(c), offset);
    }

    public String toString(){
        return getClass().getSimpleName() + " : " + text();
    }
}
