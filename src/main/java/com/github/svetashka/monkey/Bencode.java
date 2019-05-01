package com.github.svetashka.monkey;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Bencode {
    BencodeType type;
    byte[] string;
    int integer;
    List<Bencode> list;
    Map<String, Bencode> dictionary;
    int startPosition;
    int endPosition;

    @Override
    public String toString() {
        switch (type) {
            case STRING:
                return "\"" + new String(string) + "\"";
            case INTEGER:
                return String.valueOf(integer);
            case LIST:
                return Arrays.toString(list.toArray());
            case DICTIONARY:
                return dictionary.toString();
        }
        return super.toString();
    }
}