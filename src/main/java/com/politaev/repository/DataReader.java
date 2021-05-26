package com.politaev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class DataReader {
    private final ObjectMapper mapper;

    public DataReader() {
        this.mapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(new JavaTimeModule())
                .build();
    }

    public List<StoredData> readStoredData(String path) {
        URL resourceURL = getResourceURL(path);
        return readStoredData(resourceURL);
    }

    private URL getResourceURL(String resourceName) {
        var classLoader = getClass().getClassLoader();
        var resourceURL = classLoader.getResource(resourceName);
        Objects.requireNonNull(resourceURL, "Resource not found");
        return resourceURL;
    }

    private List<StoredData> readStoredData(URL resourceURL) {
        var storedDataType = new TypeReference<List<StoredData>>() {
        };
        try (var parser = mapper.reader().createParser(resourceURL)) {
            return parser.readValueAs(storedDataType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
