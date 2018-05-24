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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 汉字拼音字典工厂类，字典内容与 "/com/heimuheimu/util/pinyin/dictionary/pinyin_mapping.txt" 文件内容一致。
 *
 * <p><strong>说明：</strong>{@code PinyinDictionaryFactory} 类是线程安全的，可在多个线程中使用同一个实例。</p>
 *
 * @author heimuheimu
 */
public class PinyinDictionaryFactory {

    private static final String PINYIN_MAPPING_FILE_PATH = "/com/heimuheimu/util/pinyin/dictionary/pinyin_mapping.txt";

    private static final PinyinDictionary DICTIONARY;

    static {
        try (InputStream in = PinyinDictionaryFactory.class.getResourceAsStream(PINYIN_MAPPING_FILE_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            Map<Integer, String[]> pinyinMap = new HashMap<>();
            int codePoint = 0x4e00;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] pinyinWithToneNumberArray = line.split(",");
                if (pinyinWithToneNumberArray.length == 0) {
                    throw new IllegalArgumentException("Empty pinyin with tone number array. Code point: `"
                            + Integer.toString(codePoint, 16) + ". Line number: `" + (codePoint - 0x4e00 + 1) + ".");
                }
                for (String pinyinWithToneNumber : pinyinWithToneNumberArray) {
                    if (!PinyinDictionaryHelper.isPinyinWithToneNumber(pinyinWithToneNumber)) {
                        throw new IllegalArgumentException("Invalid pinyin with tone number: `" + pinyinWithToneNumber
                                + "`. Code point: `" + Integer.toString(codePoint, 16) + "`. Line number: `"
                                + + (codePoint - 0x4e00 + 1) + ".");
                    }
                }
                pinyinMap.put(codePoint++, pinyinWithToneNumberArray);
            }
            DICTIONARY = new PinyinDictionary(pinyinMap);
        } catch (Exception e) {
            throw new IllegalArgumentException("Load pinyin mapping file failed: `" + PINYIN_MAPPING_FILE_PATH + "`.", e);
        }
    }

    /**
     * 获得汉字拼音字典，字典内容与 "/com/heimuheimu/util/pinyin/dictionary/pinyin_mapping.txt" 文件内容一致。
     *
     * @return 汉字拼音字典
     */
    public static PinyinDictionary getDictionary() {
        return DICTIONARY;
    }
}
