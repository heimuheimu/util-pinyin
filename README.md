# util-pinyin: 中文汉字拼音转换工具，支持常用多音字转换。

<a href="https://lgtm.com/projects/g/heimuheimu/util-pinyin/alerts/"><img alt="Total alerts" src="https://img.shields.io/lgtm/alerts/g/heimuheimu/util-pinyin.svg?logo=lgtm&logoWidth=18"/></a>

## 使用要求
* JDK 版本：1.8+ 

## util-jdbc 特色：
* 体积小、速度快、多音字识别准确率高

## Maven 配置
```xml
    <dependency>
        <groupId>com.heimuheimu</groupId>
        <artifactId>util-pinyin</artifactId>
        <version>1.0</version>
    </dependency>
```

### 示例代码

将文本中的中文转换为拼音：
```java
    public class ChineseSentenceConverter {
    
        public static void main(String[] args) throws Exception {
            // 带有数字声调的拼音转换
            // 输出内容："liang3 sui4 neng2 shu3 shu4 de5 xiao3 hai2 yi3 shuo4 jian4 bu4 xian1 le5 。"
            System.out.println(PinyinUtil.toPinyinWithToneNumber("两岁能数数的小孩已数见不鲜了。"));
            
            // 带有符号声调的拼音转换
            // 输出内容："liǎng suì néng shǔ shù de xiǎo hái yǐ shuò jiàn bù xiān le 。"
            System.out.println(PinyinUtil.toPinyinWithToneMark("两岁能数数的小孩已数见不鲜了。"));
            
            // 不带声调的拼音转换
            // 输出内容："liang sui neng shu shu de xiao hai yi shuo jian bu xian le 。"
            System.out.println(PinyinUtil.toPinyinWithoutTone("两岁能数数的小孩已数见不鲜了。"));
        }
    }
```

获得单个中文汉字的拼音数组：
```java
    public class PinyinFetcher {
    
        private static final PinyinDictionary PINYIN_DICTIONARY = PinyinDictionaryFactory.getDictionary();
    
        public static void main(String[] args) throws Exception {
            // 获得汉字对应的带有数字声调拼音数组，如果为多音字，数组的长度将大于 1，如果找不到拼音，会返回 null
            String[] pinyinArray = PINYIN_DICTIONARY.getPinyinWithToneNumber('的');
            
            // 将带有数字声调的拼音转换为带有符号声调的拼音
            String pinyinWithToneMark = PINYIN_DICTIONARY.toPinyinWithToneMark(pinyinArray[0]);
            
            // 移除拼音中的数字声调，返回不带声调的拼音
            String pinyinWithoutTone = PINYIN_DICTIONARY.removeToneNumber(pinyinArray[0]);
        }
    }
```