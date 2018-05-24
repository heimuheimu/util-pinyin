/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 heimuheimu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.heimuheimu.util.pinyin.multi;

import com.heimuheimu.util.pinyin.dictionary.PinyinDictionaryFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 多音字拼音选择器工厂类。
 *
 * <p><strong>说明：</strong>{@code PinyinSelectorFactory} 类是线程安全的，可在多个线程中使用同一个实例。</p>
 *
 * @author heimuheimu
 */
public class PinyinSelectorFactory {

    private static final String MULTI_PINYIN_MAPPING_FILE_PATH = "/com/heimuheimu/util/pinyin/dictionary/multi_pinyin_mapping.txt";

    private static final Map<String, PinyinSelector> PINYIN_SELECTOR_MAP;

    static {
        try (InputStream in = PinyinDictionaryFactory.class.getResourceAsStream(MULTI_PINYIN_MAPPING_FILE_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            PINYIN_SELECTOR_MAP = new HashMap<>();
            String line;
            PinyinSelector selector = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Load multi pinyin mapping file failed: `" + MULTI_PINYIN_MAPPING_FILE_PATH + "`.", e);
        }
    }
}
