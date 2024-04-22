package de.muenchen.rbs.kitafindereai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KitaFinderEaiApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplication(KitaFinderEaiApplication.class).run(args);
    }

}
