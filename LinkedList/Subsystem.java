package com.intellekta.linkedmassive.etalon;

public interface Subsystem {
    String getName();

    int getVersion();

    void install();

    Subsystem[] getPrerequisites();
}
