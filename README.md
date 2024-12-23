# jharmonizer
A simple framework for data harmonization.

Currently, the data pipeline includes the following steps:
1. **Sourcing**: Fetching or subscribing to data from multiple sources.
2. **Converting (SerDes)**: Converting JSON, Avro, etc., to Java POJOs.
3. **Pre-filtering**: Applying a filter specific to each source.
4. **Normalizing**: Normalizing the data to a common format.
5. **Merging**: Merging normalized records based on defined strategies.
6. **Consuming**: Passing the harmonized data to one or more listeners.

Every component in the pipeline is pluggable and supported with a Configuration API.
The client of the framework only needs to implement domain-specific tasks, as almost all technical details are abstracted away.

### Typical tasks include:
1. Completing the configuration for sourcing details, merging strategies, etc.
2. Defining domain entities.
3. Implementing domain-specific interfaces, e.g., `Filter`, `Mapper`, `Merge`.

## Example usage
The example usage can be found in the `kaligo-sample-app` module.
The executable file for this app is located at `./runner.sh`. The configuration file can be found at
[kaligo-sample-app.properties](/home/nhtri/javaside/jharmonizer/kaligo-sample-app/src/main/resources/io/javaside/jharmonizer/kaligosample/app/kaligo-sample-app.properties).

### Sample code of using `Harmonizer` API:
```java
try (var harmonizer = Harmonizer.create()
                .using(properties)
                .notifying(listener)
                .build()) {
        harmonizer.run();
}
```

## Build and run
The project has been tested with Java 21, but it should work with Java 17 and above. 

