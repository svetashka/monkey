package com.github.svetashka.monkey;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ParseBencodeTest {

    @Test
    public void parse() {
    }

    @Test
    public void bencodeString() {
        // Given
        byte[] str = "11:Hello world".getBytes();
        //assertThat(str.length, is(7));

        // When
        ParseBencode parser = new ParseBencode(str);
        Bencode result = parser.bencodeString();

        // Then
        assertThat(result.string, is("Hello world".getBytes()));
        assertThat(parser.currentIndex, is(str.length));
    }

    @Test
    public void bencodeIntNegative() {
        // Given
        byte[] str = "i-3e".getBytes();

        // When
        ParseBencode parser = new ParseBencode(str);
        Bencode result = parser.bencodeInt();

        // Then
        assertThat(result.integer, is(-3));
        assertThat(parser.currentIndex, is(str.length));
    }

    @Test
    public void bencodeIntPositive() {
        // Given
        byte[] str = "i10e".getBytes();

        // When
        ParseBencode parser = new ParseBencode(str);
        Bencode result = parser.bencodeInt();

        // Then
        assertThat(result.integer, is(10));
        assertThat(parser.currentIndex, is(str.length));
    }

    @Test
    public void bencodeListOneItem() {
        // Given
        byte[] str = "l4:spame".getBytes();

        // When
        ParseBencode parser = new ParseBencode(str);
        Bencode result = parser.bencodeList();

        // Then
        assertThat(result.list.get(0).string, is("spam".getBytes()));
        assertThat(parser.currentIndex, is(str.length));
    }

    @Test
    public void bencodeListItems() {
        // Given
        byte[] str = "l4:spam4:eggse".getBytes();
        List<String> resultList = new ArrayList<>();
        resultList.add("spam");
        resultList.add("eggs");

        // When
        ParseBencode parser = new ParseBencode(str);
        Bencode result = parser.bencodeList();


        // Then
        for (int i = 0; i < result.list.size(); i++) {
            assertThat(result.list.get(i).string, is(resultList.get(i).getBytes()));
        }
        assertThat(parser.currentIndex, is(str.length));
    }

    @Test
    public void bencodeListNoItem() {
        // Given
        byte[] str = "l4:spame".getBytes();

        // When
        ParseBencode parser = new ParseBencode(str);
        Bencode result = parser.bencodeList();

        // Then
        assertThat(result.list.get(0).string, is("spam".getBytes()));
        assertThat(parser.currentIndex, is(str.length));
    }

    @Test
    public void bencodeDictionary() {
        // Given
        byte[] str = "d3:cow3:moo4:spam4:eggse".getBytes();

        Map<String, String> resultMap = new HashMap<>();
        Bencode value = new Bencode();

        resultMap.put("cow", "moo");
        resultMap.put("spam", "eggs");

        // When
        ParseBencode parser = new ParseBencode(str);
        Bencode result = parser.bencodeDictionary();

        // Then
        for (HashMap.Entry<String, Bencode> entry : result.dictionary.entrySet()) {
            assertThat(result.dictionary.get(entry.getKey()).string, is(resultMap.get(entry.getKey()).getBytes()));
        }
        assertThat(parser.currentIndex, is(str.length));
    }
}