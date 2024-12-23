package io.javaside.jharmonizer;

import io.javaside.jharmonizer.common.Configurable;

import java.util.List;

public interface Fetcher extends Configurable {

    List<FetcherRecord> fetch();

}
