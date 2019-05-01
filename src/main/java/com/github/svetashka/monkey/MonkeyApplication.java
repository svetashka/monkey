package com.github.svetashka.monkey;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        System.out.println(result);
    }
}
