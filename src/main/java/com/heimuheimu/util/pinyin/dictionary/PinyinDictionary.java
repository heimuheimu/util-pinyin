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

import java.util.HashMap;
import java.util.Map;

/**
 * 汉字拼音字典，拼音带有数字声调，数字声调位于拼音最后，使用数字 1 - 5 表示，5 为轻声，例如：lv3, bai4, de5。
 *
 * <p><strong>说明：</strong>{@code PinyinDictionary} 类是线程安全的，可在多个线程中使用同一个实例。</p>
 *
 * @author heimuheimu
 */
public class PinyinDictionary {

    /**
     * 元音 'a'
     */
    private static final char VOWEL_A = 'a';

    /**
     * 元音 'e'
     */
    private static final char VOWEL_E = 'e';

    /**
     * 元音 'i'
     */
    private static final char VOWEL_I = 'i';

    /**
     * 元音 'o'
     */
    private static final char VOWEL_O = 'o';

    /**
     * 元音 'u'
     */
    private static final char VOWEL_U = 'u';

    /**
     * 元音 'ü'
     */
    private static final char VOWEL_V = 'v';

    /**
     * 非元音 'n'，但可标注声调
     */
    private static final char VOWEL_N = 'n';

    /**
     * 元音四种声调字符 Map，Key 为元音字母，Value 为该元音对应的四种声调字符
     */
    private static final Map<Character, Character[]> MARKED_VOWEL_MAP;

    static {
        MARKED_VOWEL_MAP = new HashMap<>();
        MARKED_VOWEL_MAP.put(VOWEL_A, new Character[]{'ā', 'á', 'ǎ', 'à'});
        MARKED_VOWEL_MAP.put(VOWEL_E, new Character[]{'ē', 'é', 'ě', 'è'});
        MARKED_VOWEL_MAP.put(VOWEL_I, new Character[]{'ī', 'í', 'ǐ', 'ì'});
        MARKED_VOWEL_MAP.put(VOWEL_O, new Character[]{'ō', 'ó', 'ǒ', 'ò'});
        MARKED_VOWEL_MAP.put(VOWEL_U, new Character[]{'ū', 'ú', 'ǔ', 'ù'});
        MARKED_VOWEL_MAP.put(VOWEL_V, new Character[]{'ǖ', 'ǘ', 'ǚ', 'ǜ'});
        MARKED_VOWEL_MAP.put(VOWEL_N, new Character[]{'n', 'ń', 'ň', 'ǹ'});
    }

    /**
     * 拼音 Map，Key 为汉字对应的 UNICODE 编码值，Value 为汉字对应的带有数字声调的拼音数组
     */
    private final Map<Integer, String[]> pinyinMap;

    /**
     * 构造一个汉字拼音字典。
     *
     * @param pinyinMap 词典使用的拼音 Map，Key 为汉字对应的 UNICODE 编码值，Value 为汉字对应的带有数字声调的拼音数组
     */
    public PinyinDictionary(Map<Integer, String[]> pinyinMap) {
        this.pinyinMap = pinyinMap;
    }

    /**
     * 根据中文字符 UNICODE 编码值获得对应的带有数字声调的拼音数组，数字声调位于拼音最后，使用数字 1 - 5 表示，5 为轻声，例如：lv3, bai4, de5。
     *
     * <p>如果查找的 UNICODE 编码值在字典中不存在，将会返回 {@code null}。</p>
     *
     * @param codePoint 中文字符 UNICODE 编码值
     * @return 带有数字声调的拼音数组，可能返回 {@code null}
     */
    public String[] getPinyinWithToneNumber(int codePoint) {
        return pinyinMap.get(codePoint);
    }

    /**
     * 移除拼音中最后一位的数字声调并返回，例如 "lv3" 移除数字声调后的拼音为 "lv"。
     *
     * <p>如果传入的不是带数字声调的拼音，将原内容返回。</p>
     *
     * @param pinyinWithToneNumber 带有数字声调的拼音
     * @return 不含声调的拼音
     */
    public String removeToneNumber(String pinyinWithToneNumber) {
        if (PinyinDictionaryHelper.isPinyinWithToneNumber(pinyinWithToneNumber)) {
            return pinyinWithToneNumber.substring(0, pinyinWithToneNumber.length() - 1);
        } else {
            return pinyinWithToneNumber;
        }
    }

    /**
     * 将带有数字声调的拼音转换为带有符号声调的拼音，例如 "lv3" 转换后为 "lǚ"。
     * <p>
     *     声调标注规则：
     *     <ul>
     *         <li>有 a 不放过。</li>
     *         <li>没 a 找 o、e。</li>
     *         <li>i、u 并列标在后。</li>
     *         <li>单个韵母不必说。</li>
     *     </ul>
     * </p>
     *
     * <p>如果传入的不是带数字声调的拼音，将原内容返回。</p>
     *
     * @param pinyinWithToneNumber 带有数字声调的拼音
     * @return 带有符号声调的拼音
     */
    public String toPinyinWithToneMark(String pinyinWithToneNumber) {
        if (PinyinDictionaryHelper.isPinyinWithToneNumber(pinyinWithToneNumber)) {
            int toneNumber = Integer.parseInt(String.valueOf(pinyinWithToneNumber.charAt(pinyinWithToneNumber.length() - 1)));
            String pinyinWithoutTone = removeToneNumber(pinyinWithToneNumber);
            if (toneNumber >= 1 && toneNumber <= 4) {
                int indexOfA = pinyinWithoutTone.indexOf(VOWEL_A);
                if (indexOfA >= 0) {
                    return replaceVowelWithToneMark(pinyinWithoutTone, toneNumber, indexOfA);
                }

                int indexOfO = pinyinWithoutTone.indexOf(VOWEL_O);
                if (indexOfO >= 0) {
                    return replaceVowelWithToneMark(pinyinWithoutTone, toneNumber, indexOfO);
                }

                int indexOfE = pinyinWithoutTone.indexOf(VOWEL_E);
                if (indexOfE >= 0) {
                    return replaceVowelWithToneMark(pinyinWithoutTone, toneNumber, indexOfE);
                }

                int indexOfI = pinyinWithoutTone.indexOf(VOWEL_I);
                int indexOfU = pinyinWithoutTone.indexOf(VOWEL_U);
                int indexOfIOrU = Math.max(indexOfI, indexOfU);
                if (indexOfIOrU >= 0) {
                    return replaceVowelWithToneMark(pinyinWithoutTone, toneNumber, indexOfIOrU);
                }

                int indexOfV = pinyinWithoutTone.indexOf(VOWEL_V);
                if (indexOfV >= 0) {
                    return replaceVowelWithToneMark(pinyinWithoutTone, toneNumber, indexOfV);
                }

                int indexOfN = pinyinWithoutTone.indexOf(VOWEL_N);
                if (indexOfN >= 0) {
                    return replaceVowelWithToneMark(pinyinWithoutTone, toneNumber, indexOfN);
                }
            }
            return pinyinWithoutTone.replace(VOWEL_V, 'ü');
        } else {
            return pinyinWithToneNumber;
        }
    }

    /**
     * 将指定位置的无声调元音字符替换为带有声调符号的元音字符。
     *
     * @param pinyinWithoutTone 不含声调的拼音
     * @param toneNumber 声调
     * @param vowelIndex 需要替换的元音位置
     * @return 替换后带有声调符号的拼音
     */
    private String replaceVowelWithToneMark(String pinyinWithoutTone, int toneNumber, int vowelIndex) {
        char[] chars = pinyinWithoutTone.toCharArray();
        char vowelWithoutTone = chars[vowelIndex];
        char vowelWithToneMark = MARKED_VOWEL_MAP.get(vowelWithoutTone)[toneNumber - 1];
        chars[vowelIndex] = vowelWithToneMark;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == VOWEL_V) {
                chars[i] = 'ü';
            }
        }
        return new String(chars);
    }
}
