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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PropertiesStringLookupBoundaryTest {

    private final PropertiesStringLookup lookup = new PropertiesStringLookup();

    @Test
    void testNullKeyReturnsNull() {
        assertNull(lookup.lookup(null));
    }

    @Test
    void testEmptyKeyThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            lookup.lookup("");
        });
        assertTrue(ex.getMessage().contains("Bad properties key format"));
    }

    @Test
    void testOnlyFileNameNoSeparatorThrowsException() {
        String key = "document.properties"; // missing "::"
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            lookup.lookup(key);
        });
        assertTrue(ex.getMessage().contains("Bad properties key format"));
    }

    @Test
    void testOnlySeparatorThrowsException() {
        String key = "::"; // edge case: just the separator, no actual data
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            lookup.lookup(key);
        });
        assertTrue(ex.getMessage().contains("Bad properties key format"));
    }

    @Test
    void testBadFormatMissingSeparatorThrowsException() {
        String badKey = "justakey"; // No "::", so split will produce a length < 2

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PropertiesStringLookup.INSTANCE.lookup(badKey);
        });

        assertTrue(exception.getMessage().contains("Bad properties key format"));
    }

    @Test
    void testSeparatorAtStartThrowsException() {
        String key = "::someKey"; // edge case: no file path before separator
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            lookup.lookup(key);
        });
        assertTrue(ex.getMessage().contains("Error looking up properties"));
    }
}
