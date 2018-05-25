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

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * {@link PinyinSelector} 单元测试类。
 *
 * @author heimuheimu
 */
public class TestPinyinSelector {

    /**
     * 多音字中文字符 UNICODE 编码值数组
     */
    private static int[] MULTI_PINYIN_CHINESE_CHARACTER_ARRAY = new int[] {
            '万'
    };

    /**
     * {@link PinyinSelector#PinyinSelector(int, String, Map)} 构造函数测试
     */
    @Test
    public void testConstructor() {
        // 构造函数测试：非中文字符 codePoint
        try {
            new PinyinSelector(0x4e00 -1, "bai3", null);
            Assert.fail("PinyinSelector#PinyinSelector#PinyinSelector(int, String, Map) didn't throw IllegalArgumentException when i used invalid codePoint.");
        } catch (IllegalArgumentException ignored) {}

        // 构造函数测试：非带数字声调拼音
        try {
            new PinyinSelector('百', "bai", null);
            Assert.fail("PinyinSelector#PinyinSelector(int, String, Map) didn't throw IllegalArgumentException when i used invalid defaultPinyin.");
        } catch (IllegalArgumentException ignored) {}

        //构造函数测试：正常参数
        try {
            new PinyinSelector('百', "bai3", null);
        } catch (Exception e) {
            Assert.fail("PinyinSelector#PinyinSelector(int, String, Map) should not throw any Exception.");
        }
    }

    /**
     * {@link PinyinSelector#getPinyin(char[], int)} 方法测试
     */
    @Test
    public void testGetPinyin() {
        // 多音字拼音选择测试：万
        PinyinSelector selector = PinyinSelectorFactory.getSelector('万');
        Assert.assertNotNull("There is no PinyinSelector for chinese character `万`.", selector);
        // 错误目标字符索引位置测试
        try {
            selector.getPinyin("万俟卨".toCharArray(), 1);
            Assert.fail("PinyinSelector#getPinyin(char[], int) didn't throw IllegalArgumentException when i used invalid targetIndex.");
        } catch (IllegalArgumentException ignored) {}
        Assert.assertEquals("Select wrong pinyin for chinese character `万`.", "mo4",
                selector.getPinyin("万俟卨".toCharArray(), 0));
        Assert.assertEquals("Select wrong pinyin for chinese character `万`.", "wan4",
                selector.getPinyin("万一".toCharArray(), 0));

        //多音字拼音选择测试：麽
        selector = PinyinSelectorFactory.getSelector('麽');
        Assert.assertNotNull("There is no PinyinSelector for chinese character `麽`.", selector);
        Assert.assertEquals("Select wrong pinyin for chinese character `麽`.", "me5",
                selector.getPinyin("怎麽".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `麽`.", "me5",
                selector.getPinyin("什麽".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `麽`.", "mo2",
                selector.getPinyin("幺麽".toCharArray(), 1));

        //多音字拼音选择测试：子
        selector = PinyinSelectorFactory.getSelector('子');
        Assert.assertNotNull("There is no PinyinSelector for chinese character `子`.", selector);
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi3",
                selector.getPinyin("女子".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi3",
                selector.getPinyin("子嗣".toCharArray(), 0));
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi5",
                selector.getPinyin("个子".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi5",
                selector.getPinyin("沙子".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi5",
                selector.getPinyin("尖子".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi5",
                selector.getPinyin("钉子".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi5",
                selector.getPinyin("挑子".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `子`.", "zi5",
                selector.getPinyin("架子".toCharArray(), 1));
        //多音字拼音选择测试：的
        selector = PinyinSelectorFactory.getSelector('的');
        Assert.assertNotNull("There is no PinyinSelector for chinese character `的`.", selector);
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "de5",
                selector.getPinyin("那里的的的喀喀湖的景色很漂亮".toCharArray(), 2));
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "de5",
                selector.getPinyin("那里的的的喀喀湖的景色很漂亮".toCharArray(), 8));
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "di2",
                selector.getPinyin("那里的的的喀喀湖的景色很漂亮".toCharArray(), 3));
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "di2",
                selector.getPinyin("那里的的的喀喀湖的景色很漂亮".toCharArray(), 4));

        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "de5",
                selector.getPinyin("我的目的地是火车站".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "di4",
                selector.getPinyin("我的目的地是火车站".toCharArray(), 3));

        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "de5",
                selector.getPinyin("他的娇的的的宝宝在哭".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "de5",
                selector.getPinyin("他的娇的的的宝宝在哭".toCharArray(), 5));
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "di1",
                selector.getPinyin("他的娇的的的宝宝在哭".toCharArray(), 3));
        Assert.assertEquals("Select wrong pinyin for chinese character `的`.", "di1",
                selector.getPinyin("他的娇的的的宝宝在哭".toCharArray(), 4));

        //多音字拼音选择测试：乐
        selector = PinyinSelectorFactory.getSelector('乐');
        Assert.assertNotNull("There is no PinyinSelector for chinese character `乐`.", selector);
        Assert.assertEquals("Select wrong pinyin for chinese character `乐`.", "yue4",
                selector.getPinyin("独乐乐不如众乐乐".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `乐`.", "yue4",
                selector.getPinyin("独乐乐不如众乐乐".toCharArray(), 6));
        Assert.assertEquals("Select wrong pinyin for chinese character `乐`.", "le4",
                selector.getPinyin("独乐乐不如众乐乐".toCharArray(), 2));
        Assert.assertEquals("Select wrong pinyin for chinese character `乐`.", "le4",
                selector.getPinyin("独乐乐不如众乐乐".toCharArray(), 7));

        Assert.assertEquals("Select wrong pinyin for chinese character `乐`.", "yue4",
                selector.getPinyin("音乐使人快乐".toCharArray(), 1));
        Assert.assertEquals("Select wrong pinyin for chinese character `乐`.", "le4",
                selector.getPinyin("音乐使人快乐".toCharArray(), 5));


        //多音字拼音选择测试：钉
        selector = PinyinSelectorFactory.getSelector('钉');
        Assert.assertNotNull("There is no PinyinSelector for chinese character `钉`.", selector);
        Assert.assertEquals("Select wrong pinyin for chinese character `钉`.", "ding4",
                selector.getPinyin("铁板钉钉".toCharArray(), 2));
        Assert.assertEquals("Select wrong pinyin for chinese character `钉`.", "ding1",
                selector.getPinyin("铁板钉钉".toCharArray(), 3));
    }
}
