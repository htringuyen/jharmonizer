package io.javaside.jharmonizer.kaligosample;

import io.javaside.jharmonizer.kaligosample.app.KaligoSampleApplication;

public class Launcher {

    private Launcher() {
        throw new IllegalStateException("Un-instantiable class");
    }

    public static void main(String... args) {
        var shouldPrintJson = true;
        var app = KaligoSampleApplication.create(shouldPrintJson);
        app.runWith(args);
    }
}
