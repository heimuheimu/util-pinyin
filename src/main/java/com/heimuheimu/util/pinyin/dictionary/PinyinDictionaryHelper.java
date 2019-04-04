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

/**
 * 提供单个汉字或拼音的工具方法。
 *
 * <p><strong>说明：</strong>{@code PinyinDictionaryHelper} 类是线程安全的，可在多个线程中使用同一个实例。</p>
 *
 * @author heimuheimu
 */
public class PinyinDictionaryHelper {

    /**
     * 中文字符最小的 UNICODE 编码值
     */
    public static final int CHINESE_CHAR_MIN_CODE_POINT = 0x4e00;

    /**
     * 中文字符最大的 UNICODE 编码值
     */
    public static final int CHINESE_CHAR_MAX_CODE_POINT = 0x9fa5;

    /**
     * 根据 UNICODE 编码值判断是否为中文字符。
     *
     * @param codePoint UNICODE 编码值
     * @return 是否为中文字符
     */
    public static boolean isChineseCharacter(int codePoint) {
        return codePoint >= CHINESE_CHAR_MIN_CODE_POINT && codePoint <= CHINESE_CHAR_MAX_CODE_POINT;
    }

    /**
     * 判断传入的拼音是否为带有数字声调的拼音，数字声调位于拼音最后，使用数字 1 - 5 表示，5 为轻声，例如：lv3, bai4, de5。
     *
     * @param pinyin 需要检查的拼音
     * @return 是否为带有数字声调的拼音
     */
    public static boolean isPinyinWithToneNumber(String pinyin) {
        if (pinyin != null && pinyin.length() > 1) {
            int length = pinyin.length();
            for (int i = 0; i < length; i++) {
                char c = pinyin.charAt(i);
                if (i < (length - 1)) {
                    if (c < 'a' || c > 'z') {
                        return false;
                    }
                } else {
                    if (c < '1' || c > '5') {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
