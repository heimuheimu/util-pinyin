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

public class PinyinUtil {

    private static PinyinDictionary PINYIN_DICTIONARY = PinyinDictionaryFactory.getDictionary();

    public static String toPinyinWithToneNumber(String text) {
        if (text != null && !text.isEmpty()) {
            StringBuilder buffer = new StringBuilder();
            char[] targetCharacters = text.toCharArray();
            for (int i = 0; i < targetCharacters.length; i++) {
                char targetCharacter = targetCharacters[i];
                PinyinSelector selector = PinyinSelectorFactory.getSelector(targetCharacter);
                if (selector == null) {
                    String[] pinyinArray = PINYIN_DICTIONARY.getPinyinWithToneNumber(targetCharacter);
                    if (pinyinArray == null) {
                        buffer.append(targetCharacter);
                    } else {
                        String pinyin = pinyinArray[0];
                        if (i < (targetCharacters.length - 1)) {
                            pinyin += " ";
                        }
                        buffer.append(pinyin);
                    }
                } else {
                    String pinyin = selector.getPinyin(targetCharacters, i);
                    if (i < (targetCharacters.length - 1)) {
                        pinyin += " ";
                    }
                    buffer.append(pinyin);
                }
            }
            return buffer.toString();
        } else {
            return text;
        }
    }
}