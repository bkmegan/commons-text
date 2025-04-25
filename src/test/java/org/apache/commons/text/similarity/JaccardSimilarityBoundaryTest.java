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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

public class JaccardSimilarityBoundaryTest {

    private final JaccardSimilarity jaccard = new JaccardSimilarity();

    // --- Null cases ---
    @Test
    public void testLeftNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> jaccard.apply(null, "abc"));
    }

    @Test
    public void testRightNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> jaccard.apply("abc", null));
    }

    @Test
    public void testBothNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> jaccard.apply((CharSequence) null, (CharSequence) null));
    }

    // --- Empty strings ---
    @Test
    public void testBothEmptyStringsReturnsOne() {
        assertEquals(1.0, jaccard.apply("", ""), 0.00001);
    }

    @Test
    public void testOneEmptyOneNonEmptyReturnsZero() {
        assertEquals(0.0, jaccard.apply("", "abc"), 0.00001);
        assertEquals(0.0, jaccard.apply("abc", ""), 0.00001);
    }

    // --- Single character ---
    @Test
    public void testSingleCharacterMatchReturnsOne() {
        assertEquals(1.0, jaccard.apply("a", "a"), 0.00001);
    }

    @Test
    public void testSingleCharacterMismatchReturnsZero() {
        assertEquals(0.0, jaccard.apply("a", "b"), 0.00001);
    }

    // --- Identical multi-char strings ---
    @Test
    public void testIdenticalStringsReturnsOne() {
        assertEquals(1.0, jaccard.apply("hello", "hello"), 0.00001);
    }

    // --- No shared characters ---
    @Test
    public void testNoOverlapReturnsZero() {
        assertEquals(0.0, jaccard.apply("abc", "xyz"), 0.00001);
    }

    // --- Partial overlap ---
    @Test
    public void testPartialOverlap() {
        // "abc" vs "bcd" -> intersection = {b, c}, union = {a, b, c, d} -> 2/4 = 0.5
        assertEquals(0.5, jaccard.apply("abc", "bcd"), 0.00001);
    }
}

