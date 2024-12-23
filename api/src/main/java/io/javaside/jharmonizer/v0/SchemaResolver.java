package io.javaside.jharmonizer.v0;

import io.javaside.jharmonizer.common.Configurable;
import io.javaside.jharmonizer.FetcherRecord;

public interface SchemaResolver extends Configurable {

    SchemaRecord resolve(FetcherRecord record);
}
