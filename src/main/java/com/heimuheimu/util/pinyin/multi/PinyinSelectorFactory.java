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
import com.heimuheimu.util.pinyin.dictionary.PinyinDictionaryHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * 多音字拼音选择器 Map，Key 为多音字 UNICODE 编码值，Value 为对应的多音字拼音选择器
     */
    private static final Map<Integer, PinyinSelector> PINYIN_SELECTOR_MAP;

    static {
        PINYIN_SELECTOR_MAP = new HashMap<>();
        int lineNumber = 1;
        try (InputStream in = PinyinDictionaryFactory.class.getResourceAsStream(MULTI_PINYIN_MAPPING_FILE_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            int codePoint = -1;
            String defaultPinyin = "";
            Map<String, List<ChineseWordMatcher>>  chineseWordMatcherMap = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    char firstChar = line.charAt(0);
                    if (PinyinDictionaryHelper.isChineseCharacter(firstChar)) {
                        if (codePoint != -1) {
                            throw new IllegalArgumentException("Previous PinyinSelector is not saved.");
                        }
                        codePoint = firstChar;
                        defaultPinyin = line.split(",")[1];
                        chineseWordMatcherMap = new HashMap<>();
                    } else if (firstChar >= 'a' && firstChar <= 'z') {
                        if (codePoint == -1) {
                            throw new IllegalArgumentException("There is no chinese character unicode code point.");
                        }
                        String[] parts = line.split(",");
                        if (chineseWordMatcherMap.containsKey(parts[0])) {
                            throw new IllegalArgumentException("Duplicate pinyin: `" + parts[0] + "`. Invalid char: `"
                                    + (char) codePoint + "`.");
                        }
                        List<ChineseWordMatcher> wordMatcherList = new ArrayList<>();
                        for (int i = 1; i < parts.length; i++) {
                            String chineseWord = parts[i].trim();
                            if (chineseWord.indexOf("_") < 0) {
                                wordMatcherList.add(new ChineseWordMatcher(chineseWord, codePoint));
                            } else {
                                String[] wordParts = chineseWord.split("_");
                                int[] pivotalIndexes = new int[wordParts.length - 1];
                                for (int j = 0; j < pivotalIndexes.length; j++) {
                                    pivotalIndexes[j] = Integer.parseInt(wordParts[j + 1]);
                                }
                                wordMatcherList.add(new ChineseWordMatcher(wordParts[0], pivotalIndexes));
                            }
                        }
                        chineseWordMatcherMap.put(parts[0], wordMatcherList);
                    } else {
                        throw new IllegalArgumentException("Unknown first char: `" + firstChar + "`.");
                    }
                } else {
                    if (codePoint != -1) {
                        PinyinSelector selector = new PinyinSelector(codePoint, defaultPinyin, chineseWordMatcherMap);
                        PINYIN_SELECTOR_MAP.put(codePoint, selector);
                        codePoint = -1;
                        defaultPinyin = "";
                        chineseWordMatcherMap = null;
                    }
                }
                lineNumber++;
            }
            if (codePoint != -1) {
                PINYIN_SELECTOR_MAP.put(codePoint, new PinyinSelector(codePoint, defaultPinyin, chineseWordMatcherMap));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Load multi pinyin mapping file failed: `" + MULTI_PINYIN_MAPPING_FILE_PATH
                    + "`. Error line number: `" + lineNumber + "`.", e);
        }
    }

    /**
     * 根据多音字 UNICODE 编码值获得对应的多音字拼音选择器，如果不存在，则返回 {@code null}。
     *
     * @param codePoint 多音字 UNICODE 编码值
     * @return 多音字拼音选择器，可能为 {@code null}
     */
    public static PinyinSelector getSelector(int codePoint) {
        return PINYIN_SELECTOR_MAP.get(codePoint);
    }
}
