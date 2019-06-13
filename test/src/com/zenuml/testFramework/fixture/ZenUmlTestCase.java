package com.zenuml.testFramework.fixture;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.nio.file.Paths;

public class ZenUmlTestCase extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return Paths.get("test/data/").toAbsolutePath().toString();
    }

    public void testEmptyTest() {

    }
}
