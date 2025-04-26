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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

public class AlphabetConverterPartitionTest {
    // 1. Valid input: Basic encoding/decoding
    @Test
    void testSimpleEncodeDecode() throws UnsupportedEncodingException {
        Character[] original = {'a', 'b', 'c'};
        Character[] encoding = {'0', '1', '2'};
        Character[] doNotEncode = {};
        AlphabetConverter converter = AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);

        assertEquals("012", converter.encode("abc"));
        assertEquals("abc", converter.decode("012"));
    }

    // 2. Valid input: Encoding with doNotEncode characters
    @Test
    void testDoNotEncodeCharacters() throws UnsupportedEncodingException {
        Character[] original = {'a', 'b', 'c'};
        Character[] encoding = {'0', '1', 'c'};
        Character[] doNotEncode = {'c'};
        AlphabetConverter converter = AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);

        assertEquals("00c", converter.encode("aac"));
        assertEquals("aac", converter.decode("00c"));
    }

    // 3. Invalid input: doNotEncode character not in original alphabet
    @Test
    void testDoNotEncodeMissingFromOriginal() {
        Character[] original = {'a', 'b'};
        Character[] encoding = {'0', '1', 'c'};
        Character[] doNotEncode = {'c'}; // c not in original

        assertThrows(IllegalArgumentException.class, () ->
                AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode)
        );
    }

    // 4. Invalid input: decode contains unknown encoded group
    @Test
    void testDecodeWithInvalidGroup() {
        Character[] original = {'a', 'b', 'c'};
        Character[] encoding = {'0', '1', '2'};
        Character[] doNotEncode = {};
        AlphabetConverter converter = AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);

        assertThrows(UnsupportedEncodingException.class, () ->
                converter.decode("xyz")
        );
    }

    // 5. Valid input: Empty string encoding/decoding
    @Test
    void testEmptyString() throws UnsupportedEncodingException {
        Character[] original = {'a', 'b', 'c'};
        Character[] encoding = {'0', '1', '2'};
        Character[] doNotEncode = {};
        AlphabetConverter converter = AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);

        assertEquals("", converter.encode(""));
        assertEquals("", converter.decode(""));
    }

    // 6. Valid input: null passed to encode/decode should return null
    @Test
    void testNullInputEncodeDecode() throws UnsupportedEncodingException {
        Character[] original = {'a', 'b', 'c'};
        Character[] encoding = {'0', '1', '2'};
        Character[] doNotEncode = {};
        AlphabetConverter converter = AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);

        assertNull(converter.encode(null));
        assertNull(converter.decode(null));
    }

    // 7. Invalid input: encoding alphabet too small
    @Test
    void testEncodingAlphabetTooSmallThrows() {
        Character[] original = {'a', 'b', 'c'};
        Character[] encoding = {'x'}; // too small
        Character[] doNotEncode = {};

        assertThrows(IllegalArgumentException.class, () ->
                AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode)
        );
    }

    // 8. Valid input: repeated characters in original or encoding should be ignored
    @Test
    void testRepeatedCharactersAreIgnored() throws UnsupportedEncodingException {
        Character[] original = {'a', 'b', 'a', 'c'};
        Character[] encoding = {'0', '1', '0', '2'};
        Character[] doNotEncode = {};
        AlphabetConverter converter = AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);

        String encoded = converter.encode("abc");
        String decoded = converter.decode(encoded);
        assertEquals("abc", decoded);
    }
}
