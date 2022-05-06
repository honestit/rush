package com.github.rush.shopping;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ShoppingServerApplication implements CommandLineRunner {

  public static void main(String[] args) {
      SpringApplication.run(ShoppingServerApplication.class, args);
  }

    @Override
    public void run(String... args) throws Exception {

    }
}
