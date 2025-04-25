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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.Test;

public class CodePointTranslatorPartitionTest {

    // A simple implementation of CodePointTranslator:
    // Translates lowercase ASCII letters to uppercase, ignores everything else
    static class UppercaseOnlyTranslator extends CodePointTranslator {
        @Override
        public boolean translate(int codePoint, Writer writer) throws IOException {
            if (Character.isLowerCase(codePoint)) {
                writer.write(Character.toUpperCase(codePoint));
                return true;
            }
            return false;
        }
    }

    @Test
    void testLowercaseAsciiTranslates() throws IOException {
        CodePointTranslator translator = new UppercaseOnlyTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("a", 0, writer);

        assertEquals("A", writer.toString());
        assertEquals(1, consumed);
    }

    @Test
    void testUppercaseAsciiDoesNotTranslate() throws IOException {
        CodePointTranslator translator = new UppercaseOnlyTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("A", 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    @Test
    void testDigitDoesNotTranslate() throws IOException {
        CodePointTranslator translator = new UppercaseOnlyTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("5", 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    @Test
    void testUnicodeEmojiDoesNotTranslate() throws IOException {
        CodePointTranslator translator = new UppercaseOnlyTranslator();
        String input = "\uD83D\uDE00"; // ? emoji
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    // Edge case: empty input
    // Should throw IndexOutOfBoundsException due to no character at index 0
    @Test
    void testEmptyInputThrowsException() {
        CodePointTranslator translator = new UppercaseOnlyTranslator();
        String input = "";
        StringWriter writer = new StringWriter();

        assertThrows(IndexOutOfBoundsException.class, () -> translator.translate(input, 0, writer));
    }
}
