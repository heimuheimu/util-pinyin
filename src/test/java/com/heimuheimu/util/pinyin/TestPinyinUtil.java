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

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link PinyinUtil} 单元测试类。
 *
 * @author heimuheimu
 */
public class TestPinyinUtil {

    /**
     * 含有多音字的中文语句数组
     */
    private static final String[] CHINESE_SENTENCE_ARRAY = new String[] {
            "我不喜欢抽雪茄烟，但我喜欢吃番茄。",
            "这种弹弓弹力很强。",
            "听到这个噩耗，小刘颤栗，小陈颤抖。",
            "他扒下皮鞋，就去追扒手。",
            "我收集的材料散失了，散文没法写了。",
            "两岁能数数的小孩已数见不鲜了。",
            "人参苗长得参差不齐，还让人参观吗。",
            "今天召开的会计工作会议一会儿就要结束了。",
            "他用簸箕簸米。",
            "敌人的恐吓吓不倒他。",
            "肥胖并不都是因为心宽体胖，而是缺少锻炼。",
            "边伺候他边窥伺动静。",
            "好逸恶劳、好为人师的做法都不好。"
    };

    /**
     * 和 {@link #CHINESE_SENTENCE_ARRAY} 对应的带有数字声调的拼音语句数组
     */
    private static final String[] EXPECTED_PINYIN_WITH_TONE_NUMBER_ARRAY = new String[] {
            "wo3 bu4 xi3 huan1 chou1 xue3 jia1 yan1 ， dan4 wo3 xi3 huan1 chi1 fan1 qie2 。",
            "zhe4 zhong3 dan4 gong1 tan2 li4 hen3 qiang2 。",
            "ting1 dao4 zhe4 ge4 e4 hao4 ， xiao3 liu2 zhan4 li4 ， xiao3 chen2 chan4 dou3 。",
            "ta1 ba1 xia4 pi2 xie2 ， jiu4 qu4 zhui1 pa2 shou3 。",
            "wo3 shou1 ji2 de5 cai2 liao4 san4 shi1 le5 ， san3 wen2 mei2 fa3 xie3 le5 。",
            "liang3 sui4 neng2 shu3 shu4 de5 xiao3 hai2 yi3 shuo4 jian4 bu4 xian1 le5 。",
            "ren2 shen1 miao2 zhang3 de2 cen1 ci1 bu4 qi2 ， hai2 rang4 ren2 can1 guan1 ma5 。",
            "jin1 tian1 zhao4 kai1 de5 kuai4 ji4 gong1 zuo4 hui4 yi4 yi1 hui4 er2 jiu4 yao4 jie2 shu4 le5 。",
            "ta1 yong4 bo4 ji1 bo3 mi3 。",
            "di2 ren2 de5 kong3 he4 xia4 bu4 dao3 ta1 。",
            "fei2 pang4 bing4 bu4 dou1 shi4 yin1 wei4 xin1 kuan1 ti3 pan2 ， er2 shi4 que1 shao3 duan4 lian4 。",
            "bian1 ci4 hou4 ta1 bian1 kui1 si4 dong4 jing4 。",
            "hao4 yi4 wu4 lao2 、 hao4 wei2 ren2 shi1 de5 zuo4 fa3 dou1 bu4 hao3 。"
    };

    /**
     * 和 {@link #CHINESE_SENTENCE_ARRAY} 对应的带有符号声调的拼音语句数组
     */
    private static final String[] EXPECTED_PINYIN_WITH_TONE_MARK_ARRAY = new String[] {
            "wǒ bù xǐ huān chōu xuě jiā yān ， dàn wǒ xǐ huān chī fān qié 。",
            "zhè zhǒng dàn gōng tán lì hěn qiáng 。",
            "tīng dào zhè gè è hào ， xiǎo liú zhàn lì ， xiǎo chén chàn dǒu 。",
            "tā bā xià pí xié ， jiù qù zhuī pá shǒu 。",
            "wǒ shōu jí de cái liào sàn shī le ， sǎn wén méi fǎ xiě le 。",
            "liǎng suì néng shǔ shù de xiǎo hái yǐ shuò jiàn bù xiān le 。",
            "rén shēn miáo zhǎng dé cēn cī bù qí ， hái ràng rén cān guān ma 。",
            "jīn tiān zhào kāi de kuài jì gōng zuò huì yì yī huì ér jiù yào jié shù le 。",
            "tā yòng bò jī bǒ mǐ 。",
            "dí rén de kǒng hè xià bù dǎo tā 。",
            "féi pàng bìng bù dōu shì yīn wèi xīn kuān tǐ pán ， ér shì quē shǎo duàn liàn 。",
            "biān cì hòu tā biān kuī sì dòng jìng 。",
            "hào yì wù láo 、 hào wéi rén shī de zuò fǎ dōu bù hǎo 。"
    };

    /**
     * 和 {@link #CHINESE_SENTENCE_ARRAY} 对应的不带声调的拼音语句数组
     */
    private static final String[] EXPECTED_PINYIN_WITHOUT_TONE_ARRAY = new String[] {
            "wo bu xi huan chou xue jia yan ， dan wo xi huan chi fan qie 。",
            "zhe zhong dan gong tan li hen qiang 。",
            "ting dao zhe ge e hao ， xiao liu zhan li ， xiao chen chan dou 。",
            "ta ba xia pi xie ， jiu qu zhui pa shou 。",
            "wo shou ji de cai liao san shi le ， san wen mei fa xie le 。",
            "liang sui neng shu shu de xiao hai yi shuo jian bu xian le 。",
            "ren shen miao zhang de cen ci bu qi ， hai rang ren can guan ma 。",
            "jin tian zhao kai de kuai ji gong zuo hui yi yi hui er jiu yao jie shu le 。",
            "ta yong bo ji bo mi 。",
            "di ren de kong he xia bu dao ta 。",
            "fei pang bing bu dou shi yin wei xin kuan ti pan ， er shi que shao duan lian 。",
            "bian ci hou ta bian kui si dong jing 。",
            "hao yi wu lao 、 hao wei ren shi de zuo fa dou bu hao 。"
    };

    /**
     * 对 {@link PinyinUtil#toPinyinWithToneNumber(String)}、 {@link PinyinUtil#toPinyinWithToneMark(String)} 和
     * {@link PinyinUtil#toPinyinWithoutTone(String)} 三个方法进行测试。
     */
    @Test
    public void testToPinyin() {
        int j = 12;
        System.out.println(CHINESE_SENTENCE_ARRAY[j]);
        System.out.println(PinyinUtil.toPinyinWithToneNumber(CHINESE_SENTENCE_ARRAY[j]));
        System.out.println(PinyinUtil.toPinyinWithToneMark(CHINESE_SENTENCE_ARRAY[j]));
        System.out.println(PinyinUtil.toPinyinWithoutTone(CHINESE_SENTENCE_ARRAY[j]));

        System.out.println();
        for (int i = 0; i < CHINESE_SENTENCE_ARRAY.length; i++) {
            String sentence = CHINESE_SENTENCE_ARRAY[i];

            String expectedPinyinWithToneNumber = EXPECTED_PINYIN_WITH_TONE_NUMBER_ARRAY[i];
            Assert.assertEquals("Invalid pinyin for chinese sentence: `" + sentence + "`.",
                    expectedPinyinWithToneNumber, PinyinUtil.toPinyinWithToneNumber(sentence));

            String expectedPinyinWithToneMark = EXPECTED_PINYIN_WITH_TONE_MARK_ARRAY[i];
            Assert.assertEquals("Invalid pinyin for chinese sentence: `" + sentence + "`.",
                    expectedPinyinWithToneMark, PinyinUtil.toPinyinWithToneMark(sentence));

            String expectedPinyinWithoutTone = EXPECTED_PINYIN_WITHOUT_TONE_ARRAY[i];
            Assert.assertEquals("Invalid pinyin for chinese sentence: `" + sentence + "`.",
                    expectedPinyinWithoutTone, PinyinUtil.toPinyinWithoutTone(sentence));
        }
    }
}
