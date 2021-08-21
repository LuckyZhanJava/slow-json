package com.lonicera.exception;

public class UnRecognizeTokenException extends RuntimeException {

    private String token;

    public UnRecognizeTokenException(String token){
        this.token = token;
    }

    @Override
    public String toString() {
        return "UnRecognize Token : " + token +
                "←";
    }

    public static void main(String[] args) {
        throw new UnRecognizeTokenException("a");
    }
}
