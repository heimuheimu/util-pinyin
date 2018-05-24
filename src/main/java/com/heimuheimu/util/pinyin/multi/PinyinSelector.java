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

import com.heimuheimu.util.pinyin.dictionary.PinyinDictionaryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多音字拼音选择器，每个选择器实例对应一个多音字符。
 *
 * <p><strong>说明：</strong>{@code PinyinSelector} 类是线程安全的，可在多个线程中使用同一个实例。</p>
 *
 * @author heimuheimu
 */
public class PinyinSelector {

    /**
     * 多音字对应的 UNICODE 编码值
     */
    private final int codePoint;

    /**
     * 多音字默认带有数字声调的拼音
     */
    private final String defaultPinyin;

    /**
     * 多音字中文词组匹配器 Map，Key 为多音字对应的带有数字声调的拼音，Value 为该拼音对应的中文词组匹配器
     */
    private final Map<String, List<ChineseWordMatcher>>  chineseWordMatcherMap;

    /**
     * 构造一个多音字拼音选择器，每个选择器实例对应一个多音字符。
     *
     * @param codePoint 多音字对应的 UNICODE 编码值，必须为中文字符
     * @param defaultPinyin 多音字默认带有数字声调的拼音
     * @param chineseWordMatcherMap 多音字中文词组匹配器 Map，Key 为多音字对应的带有数字声调的拼音，Value 为该拼音对应的中文词组匹配器，允许为 {@code null}
     * @throws IllegalArgumentException 如果 {@code codePoint} 不是中文字符，将抛出此异常
     * @throws IllegalArgumentException 如果 {@code defaultPinyin} 或 {@code chineseWordMatcherMap} 中的拼音不是带有数字声调的拼音，将抛出此异常
     */
    public PinyinSelector(int codePoint, String defaultPinyin, Map<String, List<ChineseWordMatcher>> chineseWordMatcherMap)
        throws IllegalArgumentException {
        if (!PinyinDictionaryHelper.isChineseCharacter(codePoint)) {
            throw new IllegalArgumentException("`" + Integer.toString(codePoint, 16) + "` is not a valid chinese character.");
        }
        this.codePoint = codePoint;
        if (!PinyinDictionaryHelper.isPinyinWithToneNumber(defaultPinyin)) {
            throw new IllegalArgumentException("`" + defaultPinyin + "` is not a valid pinyin with tone number.");
        }
        this.defaultPinyin = defaultPinyin;
        if (chineseWordMatcherMap != null) {
            for (String pinyin : chineseWordMatcherMap.keySet()) {
                if (!PinyinDictionaryHelper.isPinyinWithToneNumber(pinyin)) {
                    throw new IllegalArgumentException("`" + pinyin + "` is not a valid pinyin with tone number.");
                }
            }
            this.chineseWordMatcherMap = chineseWordMatcherMap;
        } else {
            this.chineseWordMatcherMap = new HashMap<>();
        }
    }

    /**
     * 获得指定索引位置的多音字符对应的带有数字声调的拼音。
     *
     * @param targetCharacters 目标字符数组
     * @param targetIndex 目标字符索引位置
     * @return 多音字符对应的带有数字声调的拼音
     * @throws IllegalArgumentException 如果指定索引位置的多音字符与当前选择器对应的多音字符不一致，则抛出此异常
     */
    public String getPinyin(char[] targetCharacters, int targetIndex) throws IllegalArgumentException {
        if (targetCharacters[targetIndex] != codePoint) {
            throw new IllegalArgumentException("Invalid target character: `" + Integer.toString(targetCharacters[targetIndex], 16)
                + "`. Expected character: `" + Integer.toString(codePoint, 16) + "`.");
        }
        for (String pinyin : chineseWordMatcherMap.keySet()) {
            for (ChineseWordMatcher matcher : chineseWordMatcherMap.get(pinyin)) {
                if (matcher.match(targetCharacters, targetIndex)) {
                    return pinyin;
                }
            }
        }
        return defaultPinyin;
    }
}
