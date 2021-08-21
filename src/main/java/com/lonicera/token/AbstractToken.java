package com.lonicera.token;

public abstract class AbstractToken implements Token {
    private String text;
    private int offset;

    public AbstractToken(String text, int offset){
        this.text = text;
        this.offset = offset;
    }

    @Override
    public String text(){
        return text;
    }

    @Override
    public int offset(){
        return offset;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + " : " + text();
    }
}
