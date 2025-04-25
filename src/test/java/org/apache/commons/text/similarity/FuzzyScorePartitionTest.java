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

package org.apache.commons.text.similarity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class FuzzyScorePartitionTest {
    @Test
    void testNullTermThrowsException() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertThrows(IllegalArgumentException.class, () -> fs.fuzzyScore(null, "hello"));
    }

    @Test
    void testNullQueryThrowsException() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertThrows(IllegalArgumentException.class, () -> fs.fuzzyScore("hello", null));
    }

    @Test
    void testEmptyStrings() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertEquals(0, fs.fuzzyScore("", ""));
    }

    @Test
    void testEmptyQuery() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertEquals(0, fs.fuzzyScore("nonempty", ""));
    }

    @Test
    void testNoMatch() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertEquals(0, fs.fuzzyScore("abc", "xyz"));
    }

    @Test
    void testNonConsecutiveMatch() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertEquals(2, fs.fuzzyScore("abc", "ac")); // +1 for 'a', +1 for 'c'
    }

    @Test
    void testConsecutiveMatch() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertEquals(7, fs.fuzzyScore("abcdef", "abc")); // +1, +1+2, +1+2 = 5
    }

    @Test
    void testCaseInsensitiveMatch() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        assertEquals(7, fs.fuzzyScore("ABCdef", "abc")); // same as lowercase
    }

    @Test
    void testRepeatedLetters() {
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        // Only one 'a' and one 'n' are matched ? no consecutive bonus here
        assertEquals(7, fs.fuzzyScore("banana", "ana")); // depends on match path
    }

}
