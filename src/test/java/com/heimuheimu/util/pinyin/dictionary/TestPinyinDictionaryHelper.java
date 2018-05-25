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

package com.heimuheimu.util.pinyin.dictionary;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link PinyinDictionaryHelper} 单元测试类。
 *
 * @author heimuheimu
 */
public class TestPinyinDictionaryHelper {

    /**
     * 非中文字符 UNICODE 编码值数组
     */
    private static int[] INVALID_CHINESE_CHARACTER_ARRAY = new int[] {
            0x4e00 - 1, 0x9fa5 + 1, 0, Integer.MAX_VALUE, Integer.MIN_VALUE, '.', '，'
    };

    /**
     * 中文字符 UNICODE 编码值数组
     */
    private static int[] VALID_CHINESE_CHARACTER_ARRAY = new int[] {
            0x4e00, 0x4e00 + 1, 0x9fa5, 0x9fa5 - 1, '嗯', '得', '隆'
    };

    /**
     * 非带数字音标的拼音数组
     */
    private static String[] INVALID_PINYIN_WITH_TONE_NUMBER_ARRAY = new String[] {
            null, "", "3", "33", "a", "a0", "a6", "bai", "bai33", "b3i3", " a1", "a1 "
    };

    /**
     * 带数字音标的拼音数组
     */
    private static String[] VALID_PINYIN_WITH_TONE_NUMBER_ARRAY = new String[] {
            "a1", "a2", "a3", "a4", "a5", "bai1", "bai2", "bai3", "bai4", "bai5"
    };

    /**
     * 对 {@link PinyinDictionaryHelper#isChineseCharacter(int)} 方法进行测试
     */
    @Test
    public void testIsChineseCharacter() {
        // 非法中文字符测试
        for (int invalidChineseCharacter : INVALID_CHINESE_CHARACTER_ARRAY) {
            Assert.assertFalse("`" + (char) invalidChineseCharacter + "` is a valid chinese character.",
                    PinyinDictionaryHelper.isChineseCharacter(invalidChineseCharacter));
        }

        // 合法中文字符测试
        for (int validChineseCharacter : VALID_CHINESE_CHARACTER_ARRAY) {
            Assert.assertTrue("`" + Integer.toString(validChineseCharacter, 16) + "` is not a valid chinese character.",
                    PinyinDictionaryHelper.isChineseCharacter(validChineseCharacter));
        }
    }

    /**
     * 对 {@link PinyinDictionaryHelper#isPinyinWithToneNumber(String)} 方法进行测试
     */
    @Test
    public void testIsPinyinWithToneNumber() {
        // 非法带数字拼音测试
        for (String invalidPinyinWithToneNumber : INVALID_PINYIN_WITH_TONE_NUMBER_ARRAY) {
            Assert.assertFalse("`" + invalidPinyinWithToneNumber + "` is a valid pinyin with tone number.",
                    PinyinDictionaryHelper.isPinyinWithToneNumber(invalidPinyinWithToneNumber));
        }

        // 合法带数字拼音测试
        for (String validPinyinWithToneNumber : VALID_PINYIN_WITH_TONE_NUMBER_ARRAY) {
            Assert.assertTrue("`" + validPinyinWithToneNumber + "` is not a valid pinyin with tone number.",
                    PinyinDictionaryHelper.isPinyinWithToneNumber(validPinyinWithToneNumber));
        }
    }
}
