package org.pizzaia.todo.util;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class SecretReader {

    public static Map<String, String> read() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("production-env.yaml");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Yaml yaml = new Yaml();
        return yaml.load(inputStream);
    }
}
