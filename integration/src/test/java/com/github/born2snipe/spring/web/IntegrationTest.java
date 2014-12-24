package com.github.born2snipe.spring.web;

import com.github.born2snipe.spring.web.controller.SimpleController;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    @Test
    public void shouldContainTheControllersInTheIndexFile() throws Exception {
        String indexFilename = SpringAnnotatedControllerIndexer.INDEX_FILENAME;
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(indexFilename).toURI());
        List<String> lines = Files.readLines(file, Charset.defaultCharset());

        assertEquals(Arrays.asList(SimpleController.class.getName()), lines);
    }
}
