package com.github.svetashka.monkey;

public class UrlHashCode {
    public StringBuilder getHashCode(byte[] urlString) {
        StringBuilder hashCode = new StringBuilder();
        for (byte urlStringElement : urlString) {
            hashCode.append("%").append(Integer.toString((urlStringElement & 0xff) + 0x100, 16).substring(1).toUpperCase());
        }
        return hashCode;
    }
}