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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 中文词组匹配器。
 *
 * <p><strong>说明：</strong>{@code ChineseWordMatcher} 类是线程安全的，可在多个线程中使用同一个实例。</p>
 *
 * @author heimuheimu
 */
public class ChineseWordMatcher {

    /**
     * 中文词组对应的字符数组
     */
    private final char[] wordCharacters;

    /**
     * 关键字符在词组字符数组中的索引位置数组
     */
    private final int[] pivotalCharacterIndexes;

    /**
     * 构造一个中文词组匹配器。
     *
     * @param chineseWord 中文词组，全部由中文字符组成
     * @param targetCodePoint 关键字符 UNICODE 编码值
     * @throws IllegalArgumentException 如果 {@code chineseWord} 为空或者包含非中文字符，将抛出此异常
     * @throws IllegalArgumentException 如果 {@code pivotalCharacterIndexes} 为空，将抛出此异常
     */
    public ChineseWordMatcher(String chineseWord, int targetCodePoint) throws IllegalArgumentException  {
        this(chineseWord, getPivotalIndexes(chineseWord, targetCodePoint));
    }

    /**
     * 构造一个中文词组匹配器。
     *
     * @param chineseWord 中文词组，全部由中文字符组成
     * @param pivotalCharacterIndexes 关键字符索引位置数组
     * @throws IllegalArgumentException 如果 {@code chineseWord} 为空或者包含非中文字符，将抛出此异常
     * @throws IllegalArgumentException 如果 {@code pivotalCharacterIndexes} 为空，将抛出此异常
     */
    public ChineseWordMatcher(String chineseWord, int[] pivotalCharacterIndexes) throws IllegalArgumentException {
        if (chineseWord == null || chineseWord.isEmpty()) {
            throw new IllegalArgumentException("chineseWord could not be empty.");
        }
        this.wordCharacters = chineseWord.toCharArray();
        for (char c : wordCharacters) {
            if (!PinyinDictionaryHelper.isChineseCharacter(c)) {
                throw new IllegalArgumentException("Invalid chinese character: `" + c + "`. chineseWord: `" + chineseWord + "`.");
            }
        }
        this.pivotalCharacterIndexes = pivotalCharacterIndexes;
        if (pivotalCharacterIndexes == null || pivotalCharacterIndexes.length == 0) {
            throw new IllegalArgumentException("pivotalCharacterIndexes could not be empty. chineseWord: `" + chineseWord + "`.");
        }
    }

    /**
     * 判断目标字符数组中指定位置的字符，是否作为关键字符出现在当前中文词组中，如果出现，则返回 {@code true}，否则返回 {@code false}。
     *
     * @param targetCharacters 目标字符数组
     * @param targetIndex 目标字符索引位置
     * @return 是否作为关键字符出现在当前中文词组中
     */
    public boolean match(char[] targetCharacters, int targetIndex) {
        outLoop: for (int pivotalCharacterIndex : pivotalCharacterIndexes) {
            int targetStartIndex = targetIndex - pivotalCharacterIndex;
            int targetEndIndex = targetIndex + (wordCharacters.length - pivotalCharacterIndex - 1);
            if (targetStartIndex >= 0 && targetEndIndex < targetCharacters.length) {
                for (int i = 0; i < wordCharacters.length; i++) {
                    if (wordCharacters[i] != targetCharacters[targetStartIndex + i]) {
                        continue outLoop;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "ChineseWordMatcher{" +
                "word=" + new String(wordCharacters) +
                ", pivotalCharacterIndexes=" + Arrays.toString(pivotalCharacterIndexes) +
                '}';
    }

    private static int[] getPivotalIndexes(String chineseWord, int targetCodePoint) {
        List<Integer> pivotalIndexList = new ArrayList<>();
        for (int i = 0; i < chineseWord.length(); i++) {
            char a = chineseWord.charAt(i);
            if (a == targetCodePoint) {
                pivotalIndexList.add(i);
            }
        }
        int[] pivotalIndexes = new int[pivotalIndexList.size()];
        for (int i = 0; i < pivotalIndexList.size(); i++) {
            pivotalIndexes[i] = pivotalIndexList.get(i);
        }
        return pivotalIndexes;
    }
}
