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

import java.util.Arrays;

/**
 * {@link PinyinDictionary} 单元测试类。
 *
 * @author heimuheimu
 */
public class TestPinyinDictionary {

    private static final PinyinDictionary DICTIONARY = PinyinDictionaryFactory.getDictionary();

    /**
     * 中文字符 UNICODE 编码值数组
     */
    private static final int[] VALID_CHINESE_CHARACTER_ARRAY = new int[] {
            '一',
            '龥',
            '嗯',
            '呣',
            '查',
            '单',
            '元',
            '测',
            '试',
            '吕',
            '刘',
            '楼',
            '略',
            '的'
    };

    /**
     * 和 {@code VALID_CHINESE_CHARACTER_ARRAY} 对应的带有数字声调的拼音二维数组
     */
    private static final String[][] EXPECTED_PINYIN_WITH_TONE_NUMBER_TOW_DIMENSIONAL_ARRAY = new String[][] {
            new String[]{"yi1"},
            new String[]{"yu4"},
            new String[]{"ng4"},
            new String[]{"m2"},
            new String[]{"cha2", "zha1"},
            new String[]{"dan1", "chan2", "shan4"},
            new String[]{"yuan2"},
            new String[]{"ce4"},
            new String[]{"shi4"},
            new String[]{"lv3"},
            new String[]{"liu2"},
            new String[]{"lou2"},
            new String[]{"lve4"},
            new String[]{"de5","di4", "di2", "di1"}
    };

    /**
     * 和 {@code VALID_CHINESE_CHARACTER_ARRAY} 对应的不带声调的拼音二维数组
     */
    private static final String[][] EXPECTED_PINYIN_WITHOUT_TONE_TOW_DIMENSIONAL_ARRAY = new String[][] {
            new String[]{"yi"},
            new String[]{"yu"},
            new String[]{"ng"},
            new String[]{"m"},
            new String[]{"cha", "zha"},
            new String[]{"dan", "chan", "shan"},
            new String[]{"yuan"},
            new String[]{"ce"},
            new String[]{"shi"},
            new String[]{"lv"},
            new String[]{"liu"},
            new String[]{"lou"},
            new String[]{"lve"},
            new String[]{"de", "di", "di", "di"}
    };

    /**
     * 和 {@code VALID_CHINESE_CHARACTER_ARRAY} 对应的带有符号声调的拼音二维数组
     */
    private static final String[][] EXPECTED_PINYIN_WITH_TONE_MARK_TOW_DIMENSIONAL_ARRAY = new String[][] {
            new String[]{"yī"},
            new String[]{"yù"},
            new String[]{"ǹg"},
            new String[]{"m"},
            new String[]{"chá", "zhā"},
            new String[]{"dān", "chán", "shàn"},
            new String[]{"yuán"},
            new String[]{"cè"},
            new String[]{"shì"},
            new String[]{"lǚ"},
            new String[]{"liú"},
            new String[]{"lóu"},
            new String[]{"lüè"},
            new String[]{"de", "dì", "dí", "dī"}
    };

    /**
     * 非中文字符 UNICODE 编码值数组
     */
    private static final int[] INVALID_CHINESE_CHARACTER_ARRAY = new int[] {
            0x4e00 - 1, 0x9fa5 + 1, 0, Integer.MAX_VALUE, Integer.MIN_VALUE, '.', '，'
    };

    /**
     * 对 {@link PinyinDictionary#getPinyinWithToneNumber(int)} 方法进行测试
     */
    @Test
    public void testGetPinyinWithToneNumber() {
        // 中文字符获取拼音数组方法测试
        for (int i = 0; i < VALID_CHINESE_CHARACTER_ARRAY.length; i++) {
            int codePoint = VALID_CHINESE_CHARACTER_ARRAY[i];
            String[] expectedPinyinArray =  EXPECTED_PINYIN_WITH_TONE_NUMBER_TOW_DIMENSIONAL_ARRAY[i];
            String[] actualPinyinArray = DICTIONARY.getPinyinWithToneNumber(codePoint);
            Assert.assertArrayEquals("Invalid pinyin array for chinese character: `" + (char)codePoint,
                    expectedPinyinArray, actualPinyinArray);
        }

        // 非中文字符获取拼音数组方法测试
        for (int invalidChineseCharacter : INVALID_CHINESE_CHARACTER_ARRAY) {
            String[] actualPinyinArray = DICTIONARY.getPinyinWithToneNumber(invalidChineseCharacter);
            Assert.assertNull("`" + Integer.toString(invalidChineseCharacter, 16) + "` should not have pinyin array. Invalid pinyin array: `"
                    + Arrays.toString(actualPinyinArray) + "`." , actualPinyinArray);
        }
    }

    /**
     * 对 {@link PinyinDictionary#removeToneNumber(String)} 方法进行测试
     */
    @Test
    public void testRemoveToneNumber() {
        // 非带数字音标拼音测试
        Assert.assertEquals("`null` is not pinyin with tone number.", null, DICTIONARY.removeToneNumber(null));
        Assert.assertEquals("`` is not pinyin with tone number.", "", DICTIONARY.removeToneNumber(""));
        Assert.assertEquals("`，` is not pinyin with tone number.", "，", DICTIONARY.removeToneNumber("，"));
        Assert.assertEquals("`bai` is not pinyin with tone number.", "bai", DICTIONARY.removeToneNumber("bai"));

        // 带数字音标拼音测试
        for (int i = 0; i < VALID_CHINESE_CHARACTER_ARRAY.length; i++) {
            String[] pinyinWithToneNumberArray = EXPECTED_PINYIN_WITH_TONE_NUMBER_TOW_DIMENSIONAL_ARRAY[i];
            String[] pinyinWithoutToneArray = EXPECTED_PINYIN_WITHOUT_TONE_TOW_DIMENSIONAL_ARRAY[i];
            for (int j = 0; j < pinyinWithToneNumberArray.length; j++) {
                Assert.assertEquals("Remove tone number failed.", pinyinWithoutToneArray[j], DICTIONARY.removeToneNumber(pinyinWithToneNumberArray[j]));
            }
        }
    }

    /**
     * 对 {@link PinyinDictionary#toPinyinWithToneMark(String)} 方法进行测试
     */
    @Test
    public void testToPinyinWithToneMark() {
        // 非带数字音标拼音测试
        Assert.assertEquals("`null` is not pinyin with tone number.", null, DICTIONARY.toPinyinWithToneMark(null));
        Assert.assertEquals("`` is not pinyin with tone number.", "", DICTIONARY.toPinyinWithToneMark(""));
        Assert.assertEquals("`，` is not pinyin with tone number.", "，", DICTIONARY.toPinyinWithToneMark("，"));
        Assert.assertEquals("`bai` is not pinyin with tone number.", "bai", DICTIONARY.toPinyinWithToneMark("bai"));

        // 带数字音标拼音测试
        for (int i = 0; i < VALID_CHINESE_CHARACTER_ARRAY.length; i++) {
            String[] pinyinWithToneNumberArray = EXPECTED_PINYIN_WITH_TONE_NUMBER_TOW_DIMENSIONAL_ARRAY[i];
            String[] pinyinWithToneMarkArray = EXPECTED_PINYIN_WITH_TONE_MARK_TOW_DIMENSIONAL_ARRAY[i];
            for (int j = 0; j < pinyinWithToneNumberArray.length; j++) {
                Assert.assertEquals("To pinyin with tone mark failed.", pinyinWithToneMarkArray[j], DICTIONARY.toPinyinWithToneMark(pinyinWithToneNumberArray[j]));
            }
        }
    }
}
