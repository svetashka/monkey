package com.github.svetashka.monkey;

import java.util.*;

public class ParseBencode {

    int currentIndex;
    private final byte[] buffer;

    private static final char DICTIONARY_PREFIX = 'd';
    private static final char LIST_PREFIX = 'l';
    private static final char INT_PREFIX = 'i';
    private static final char END_POSTFIX = 'e';
    private static final char SEPARATOR = ':';

    public ParseBencode(byte[] buffer) {
        this.buffer = buffer;
        currentIndex = 0;
    }

    public Bencode parse() {

        switch (buffer[currentIndex]) {
            case DICTIONARY_PREFIX:
                return bencodeDictionary();
            case LIST_PREFIX:
                return bencodeList();
            case INT_PREFIX:
                return bencodeInt();
            default:
                return bencodeString();
        }
    }

    Bencode bencodeString() {
        Bencode out = new Bencode();
        out.startPosition = currentIndex;
        int num;
        byte[] result;
        while (buffer[currentIndex] != SEPARATOR) {
            currentIndex++;
        }
        num = Integer.parseInt(new String(Arrays.copyOfRange(buffer, out.startPosition, currentIndex)));
        currentIndex++;
        result = Arrays.copyOfRange(buffer, currentIndex, num + currentIndex);
        currentIndex = num + currentIndex;
        out.type = BencodeType.STRING;
        out.string = result;
        return out;
    }

    Bencode bencodeInt() {
        int num;
        Bencode out = new Bencode();
        out.startPosition = currentIndex;
        while (buffer[currentIndex] != END_POSTFIX) {
            currentIndex++;
        }
        num = Integer.parseInt(new String(Arrays.copyOfRange(buffer, out.startPosition + 1, currentIndex)));
        currentIndex++;
        out.type = BencodeType.INTEGER;
        out.integer = num;
        return out;
    }

    Bencode bencodeList() {
        List<Bencode> result = new ArrayList<>();
        Bencode out = new Bencode();
        currentIndex++;
        while (buffer[currentIndex] != END_POSTFIX) {
            Bencode item = parse();
            result.add(item);
        }
        currentIndex++;
        out.type = BencodeType.LIST;
        out.list = result;
        return out;
    }

    Bencode bencodeDictionary() {
        Map<String, Bencode> result = new HashMap<>();
        Bencode out = new Bencode();
        currentIndex++;
        while (buffer[currentIndex] != END_POSTFIX) {
            Bencode key = bencodeString();
            Bencode value = parse();
            result.put(new String(key.string), value);
        }
        currentIndex++;
        out.type = BencodeType.DICTIONARY;
        out.dictionary = result;
        return out;
    }
}