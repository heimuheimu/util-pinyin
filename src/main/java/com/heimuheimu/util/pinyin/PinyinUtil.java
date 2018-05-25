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

package com.heimuheimu.util.pinyin;

import com.heimuheimu.util.pinyin.dictionary.PinyinDictionary;
import com.heimuheimu.util.pinyin.dictionary.PinyinDictionaryFactory;
import com.heimuheimu.util.pinyin.multi.PinyinSelector;
import com.heimuheimu.util.pinyin.multi.PinyinSelectorFactory;

/**
 * 提供将文本中的中文转换为拼音的工具方法，并支持常用多英字。
 *
 * @author heimuheimu
 */
public class PinyinUtil {

    private static final PinyinDictionary PINYIN_DICTIONARY = PinyinDictionaryFactory.getDictionary();

    /**
     * 拼音类型：带有数字声调
     */
    private static final int TYPE_WITH_TONE_NUMBER = 1;

    /**
     * 拼音类型：带有符号声调
     */
    private static final int TYPE_WITH_TONE_MARK = 2;

    /**
     * 拼音类型：不带声调
     */
    private static final int TYPE_WITHOUT_TONE = 3;

    /**
     * 将文本中的中文替换为带有数字声调的拼音后返回，例如 "他屏气凝神躲在屏风后面。" 替换后的内容为：
     * "ta1 bing3 qi4 ning2 shen2 duo3 zai4 ping2 feng1 hou4 mian4 。"
     *
     * @param text 需要进行拼音替换的文本
     * @return 替换后的文本
     */
    public static String toPinyinWithToneNumber(String text) {
        return toPinyin(text, TYPE_WITH_TONE_NUMBER);
    }

    /**
     * 将文本中的中文替换为带有符号声调的拼音后返回，例如 "他屏气凝神躲在屏风后面。" 替换后的内容为：
     * "tā bǐng qì níng shén duǒ zài píng fēng hòu miàn 。"
     *
     * @param text 需要进行拼音替换的文本
     * @return 替换后的文本
     */
    public static String toPinyinWithToneMark(String text) {
        return toPinyin(text, TYPE_WITH_TONE_MARK);
    }

    /**
     * 将文本中的中文替换为不带声调的拼音后返回，例如 "他屏气凝神躲在屏风后面。" 替换后的内容为：
     * "ta bing qi ning shen duo zai ping feng hou mian 。"
     *
     * @param text 需要进行拼音替换的文本
     * @return 替换后的文本
     */
    public static String toPinyinWithoutTone(String text) {
        return toPinyin(text, TYPE_WITHOUT_TONE);
    }

    private static String toPinyin(String text, int type) {
        if (text != null && !text.isEmpty()) {
            StringBuilder buffer = new StringBuilder();
            char[] targetCharacters = text.toCharArray();
            for (int i = 0; i < targetCharacters.length; i++) {
                String pinyin = "";
                char targetCharacter = targetCharacters[i];
                PinyinSelector selector = PinyinSelectorFactory.getSelector(targetCharacter);
                if (selector == null) {
                    String[] pinyinArray = PINYIN_DICTIONARY.getPinyinWithToneNumber(targetCharacter);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        pinyin = pinyinArray[0];
                    }
                } else {
                    pinyin = selector.getPinyin(targetCharacters, i);
                }
                if (!pinyin.isEmpty()) {
                    if (type == TYPE_WITH_TONE_MARK) {
                        pinyin = PINYIN_DICTIONARY.toPinyinWithToneMark(pinyin);
                    } else if (type == TYPE_WITHOUT_TONE) {
                        pinyin = PINYIN_DICTIONARY.removeToneNumber(pinyin);
                    }
                    if (i < (targetCharacters.length - 1)) {
                        pinyin += " ";
                    }
                    buffer.append(pinyin);
                } else {
                    buffer.append(targetCharacter);
                }
            }
            return buffer.toString();
        } else {
            return text;
        }
    }
}