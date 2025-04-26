/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CaseUtilsPartitionTest {
    // Null input string
    @Test
    void testNullInputReturnsNull() {
        assertNull(CaseUtils.toCamelCase(null, true, null));
    }

    // Empty input string
    @Test
    void testEmptyInputReturnsEmpty() {
        assertEquals("", CaseUtils.toCamelCase("", true, null));
    }

    // Only delimiters
    @Test
    void testOnlyDelimitersReturnsEmpty() {
        assertEquals("", CaseUtils.toCamelCase(" @ @", false, new char[]{'@'}));
    }

    // No delimiters (just case change)
    @Test
    void testNoDelimitersCapitalizationTrue() {
        assertEquals("Hello", CaseUtils.toCamelCase("hello", true, null));
    }

    @Test
    void testNoDelimitersCapitalizationFalse() {
        assertEquals("hello", CaseUtils.toCamelCase("HELLO", false, null));
    }

    // Capitalize first letter flag
    @Test
    void testCapitalizeFirstTrue() {
        assertEquals("FooBar", CaseUtils.toCamelCase("foo bar", true, null));
    }

    @Test
    void testCapitalizeFirstFalse() {
        assertEquals("fooBar", CaseUtils.toCamelCase("foo bar", false, null));
    }

    // Null delimiters (uses space as default)
    @Test
    void testNullDelimitersUsesSpace() {
        assertEquals("FooBar", CaseUtils.toCamelCase("foo bar", true, null));
    }

    // Empty delimiters (also uses space)
    @Test
    void testEmptyDelimitersUsesSpace() {
        assertEquals("FooBar", CaseUtils.toCamelCase("foo bar", true, new char[0]));
    }

    // Custom delimiter characters
    @Test
    void testCustomDelimiters() {
        assertEquals("FooBarBaz", CaseUtils.toCamelCase("foo-bar_baz", true, new char[]{'-', '_'}));
    }
}
