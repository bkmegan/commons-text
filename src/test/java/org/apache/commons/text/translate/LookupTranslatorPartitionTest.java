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

package org.apache.commons.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class LookupTranslatorPartitionTest {
    private final LookupTranslator translator;

    {
        Map<CharSequence, CharSequence> map = new HashMap<>();
        map.put("cat", "feline");
        map.put("dog", "canine");
        map.put("do", "perform");
        translator = new LookupTranslator(map);
    }

    // --------------------------
    // EQUIVALENCE PARTITION TESTS
    // --------------------------

    @Test
    void testExactMatchFullKey() throws Exception {
        // Input matches full key "cat"
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("cat", 0, writer);
        assertEquals("feline", writer.toString());
        assertEquals(3, consumed); // three codepoints
    }

    @Test
    void testNoMatchWrongPrefix() throws Exception {
        // "x" is not a prefix of any key
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("xylophone", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    @Test
    void testPrefixMatchButNotFullKey() throws Exception {
        // "d" is a prefix of "dog" and "do", but "d" alone isn't a key
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("d", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    @Test
    void testGreedyMatchPrefersLongestKey() throws Exception {
        // "dog" and "do" both match ? should pick "dog" due to greedy strategy
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("dogma", 0, writer);
        assertEquals("canine", writer.toString());
        assertEquals(3, consumed); // "dog" is three Unicode codepoint sequence
    }

    // --------------------------
    // BOUNDARY VALUE TESTS
    // --------------------------

    @Test
    void testInputShorterThanShortestKey() throws Exception {
        // "c" is shorter than the shortest key "do" (length 2)
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("c", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    @Test
    void testMatchAtEndOfString() throws Exception {
        // "dog" is at the very end of the input
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("gooddog", 4, writer);
        assertEquals("canine", writer.toString());
        assertEquals(3, consumed);
    }

    @Test
    void testInputEqualToLongestKey() throws Exception {
        // "cat" is one of the keys, matches exactly
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("cat", 0, writer);
        assertEquals("feline", writer.toString());
        assertEquals(3, consumed);
    }

    @Test
    void testIndexPlusLongestEqualsInputLength() throws Exception {
        // longest = 3, input length = 7, index = 4
        // substring from 4 to 7 should match "dog"
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("bigdogdog", 6, writer);
        assertEquals("canine", writer.toString());
        assertEquals(3, consumed);
    }
}
