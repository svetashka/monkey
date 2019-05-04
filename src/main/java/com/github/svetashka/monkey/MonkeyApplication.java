package com.github.svetashka.monkey;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.security.MessageDigest;

import java.io.FileInputStream;

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

        String url = result.dictionary.get("announce").toString();
        System.out.println(url);


        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] testHash = mDigest.digest(result.dictionary.get("info").toString().getBytes());
        StringBuilder resultHash = new StringBuilder();
        for (byte testHash1 : testHash) {
            resultHash.append(Integer.toString((testHash1 & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println(resultHash);
    }

}
