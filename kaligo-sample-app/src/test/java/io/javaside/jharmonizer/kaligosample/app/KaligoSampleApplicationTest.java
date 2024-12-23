package io.javaside.jharmonizer.kaligosample.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KaligoSampleApplicationTest {

    private KaligoSampleApplication app;

    @BeforeEach
    void setUp() {
        var shouldPrintJson = false;
        app = KaligoSampleApplication.create(shouldPrintJson);
    }

    @Test
    void runSample1() {
        app.runWith("none", "none");
        assertEquals(3, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample2() {
        app.runWith("iJhz", "none");
        assertEquals(3, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample3() {
        app.runWith("none", "5432");
        assertEquals(3, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample4() {
        app.runWith("iJhz", "5432");
        assertEquals(1, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample5() {
        app.runWith("iJhz", "5432");
        assertEquals(1, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample6() {
        app.runWith("iJhz", "5431");
        assertEquals(0, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample7() {
        app.runWith("iJhz", "5431,5432");
        assertEquals(1, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample8() {
        app.runWith("unknown", "5432");
        assertEquals(0, app.getHarmonizedHotels().size());
    }

    @Test
    void runSample9() {
        app.runWith("iJhz,SjyX,f8c9", "1122");
        assertEquals(1, app.getHarmonizedHotels().size());
    }
}




















