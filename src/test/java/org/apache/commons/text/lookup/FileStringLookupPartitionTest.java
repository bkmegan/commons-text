/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class FileStringLookupPartitionTest {
    private final FileStringLookup lookup = new FileStringLookup();

    @Test
    void testNullKeyReturnsNull() {
        assertNull(lookup.lookup(null));
    }

    @Test
    void testKeyMissingColonThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                lookup.lookup("UTF-8")
        );
        assertTrue(ex.getMessage().contains("Bad file key format"));
    }

    @Test
    void testInvalidCharsetThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                lookup.lookup("FAKECHARSET:/some/fake/file.txt")
        );
        assertTrue(ex.getMessage().contains("Error looking up file"));
    }

    @Test
    void testNonexistentFileThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                lookup.lookup("UTF-8:/definitely/does/not/exist.txt")
        );
        assertTrue(ex.getMessage().contains("Error looking up file"));
    }

    @Test
    void testValidKeyReturnsFileContents() throws Exception {
        Path tempFile = Files.createTempFile("test-file", ".txt");
        Files.write(tempFile, "Hello, world!".getBytes(StandardCharsets.UTF_8));

        String key = "UTF-8:" + tempFile.toString();
        String result = lookup.lookup(key);
        assertEquals("Hello, world!", result);
    }

    @Test
    void testMultipleColonsStillWorksOrThrowsMeaningfully() {
        // File doesn't exist, but structure is valid
        String key = "UTF-8:/fake/path:ignored";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                lookup.lookup(key)
        );
        assertTrue(ex.getMessage().contains("Error looking up file"));
    }
}
