package org.pizzaia.todo.util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class SecretReader {

    private SecretReader() {}
    public static Map<String, String> read() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("production-env.yaml");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        Yaml yaml = new Yaml();
        return yaml.load(inputStream);
    }
}
