package io.javaside.jharmonizer.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javaside.jharmonizer.Fetcher;
import io.javaside.jharmonizer.FetcherRecord;
import io.javaside.jharmonizer.JHarmonizerException;
import io.javaside.jharmonizer.common.ConfigDef;
import io.javaside.jharmonizer.common.StaticDependency;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static io.javaside.jharmonizer.common.MiscUtils.stringValueFrom;

public class HttpFetcher implements Fetcher, AutoCloseable {

    public static final ConfigDef HTTP_URL_DEF = ConfigDef.of("http.url", true);

    public static final ConfigDef SOURCE_ID_DEF = ConfigDef.of("source.id", true);

    private String httpUrl;

    private String sourceId;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper jsonMapper = StaticDependency.OBJECT_MAPPER;

    @Override
    public void close() {
        httpClient.close();
    }

    @Override
    public void configure(Properties props) {
        this.httpUrl = stringValueFrom(props, HTTP_URL_DEF);
        this.sourceId = stringValueFrom(props, SOURCE_ID_DEF);
    }

    @Override
    public List<FetcherRecord> fetch() {
        try {
            var response = httpClient.send(createHttpRequest(), HttpResponse.BodyHandlers.ofString());
            return parseJsonArray(response.body())
                    .stream()
                    .map(json -> FetcherRecord.of(json, sourceId))
                    .toList();
        }
        catch (IOException | InterruptedException e) {
            throw new JHarmonizerException("Fetcher processing error", e);
        }
    }

    private HttpRequest createHttpRequest() {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(httpUrl))
                .build();
    }

    private List<String> parseJsonArray(String json) throws JsonProcessingException {

        var result = new LinkedList<String>();
        var orgNode = jsonMapper.readTree(json);

        if (orgNode.isArray()) {
            for (var n : orgNode) {
                result.add(jsonMapper.writeValueAsString(n));
            }
        }
        else {
            result.add(jsonMapper.writeValueAsString(orgNode));
        }

        return result;
    }
}











































