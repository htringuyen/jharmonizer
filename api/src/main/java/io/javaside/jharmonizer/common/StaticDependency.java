package io.javaside.jharmonizer.common;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StaticDependency {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

}
