package com.github.svetashka.monkey;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

@SpringBootApplication
public class MonkeyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String fileName = args[0];
        System.out.println("File name: " + fileName);

        FileInputStream fin = new FileInputStream(fileName);
        byte[] buffer = fin.readAllBytes();

        ParseBencode parser = new ParseBencode(buffer);
        Bencode result = parser.parse();

        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] announce = mDigest.digest(result.dictionary.get("info").toString().getBytes());
        StringBuilder announceHash = new UrlHashCode().getHashCode(announce);

        byte[] peerId = "12345678901234567890".getBytes();
        StringBuilder peerIdHash = new UrlHashCode().getHashCode(peerId);

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:5555/announce?info_hash=" + announceHash + "&peer_id=" + peerIdHash;

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate.setUriTemplateHandler(factory);
        String getResult = restTemplate.getForObject(url, String.class);
    }

}
