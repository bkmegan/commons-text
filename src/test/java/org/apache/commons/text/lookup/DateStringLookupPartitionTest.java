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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;


public class DateStringLookupPartitionTest {

    private final DateStringLookup lookup = DateStringLookup.INSTANCE;

    // Partition 1: Valid format strings
    @Test
    public void testValidFormat_yyyyMMdd() {
        String result = lookup.lookup("yyyy-MM-dd");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testValidFormat_fullDateTime() {
        String result = lookup.lookup("yyyy-MM-dd HH:mm:ss");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testValidFormat_customText() {
        String result = lookup.lookup("'Today is' EEEE");
        assertNotNull(result);
        assertTrue(result.startsWith("Today is"));
    }

    // Partition 2: Null format (default format)
    @Test
    public void testNullFormat_usesDefault() {
        String result = lookup.lookup(null);
        assertNotNull(result);
        // Since default FastDateFormat output may vary, just check it's non-empty
        assertFalse(result.isEmpty());
    }

    // Partition 3: Invalid format strings
    @Test
    public void testInvalidFormat_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            lookup.lookup("invalid_format_%%");
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }
}
