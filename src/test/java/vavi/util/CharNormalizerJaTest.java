/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * CharNormalizerJaTest.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 0xxxxx nsano initial version <br>
 */
public class CharNormalizerJaTest {

    /** */
    static final String halfKana = "ｱｲｳｴｵ";
    /** */
    static final String fullHira = "あいうえお";
    /** */
    static final String fullKana = "アイウエオ";
    /** */
    static final String halfDakuon = "ｶﾞｷﾞｸﾞｹﾞｺﾞｻﾞｼﾞｽﾞｾﾞｿﾞﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞﾊﾞﾋﾞﾌﾞﾍﾞﾎﾞ";
    /** */
    static final String fullDakuon = "ガギグゲゴザジズゼゾダヂヅデドバビブベボ";
    /** */
    static final String halfHandakuon = "ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ";
    /** */
    static final String fullHandakuon = "パピプペポ";

    /** */
    @Test
    public void testToKatakana() throws Exception {
        assertEquals(fullKana, CharNormalizerJa.ToKatakana.normalize(fullHira));
    }

    /** */
    @Test
    public void testToHiragana() throws Exception {
        assertEquals(fullHira, CharNormalizerJa.ToHiragana.normalize(fullKana));
    }

    /** */
    static final String halfANS = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    /** */
    static final String fullANS = "！\uff02＃＄％＆\uff07（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～";

    /** */
    @Test
    public void testToHalfANS() throws Exception {
//  System.err.println(CharNormalizerJa.ToHalfANS.normalize(fullANS));
//  System.err.println(CharNormalizerJa.ToFullANS.normalize(halfANS));
//  for (int i = 0; i < fullANS.length(); i++) {
//   System.err.println(StringUtil.toHex4(fullANS.charAt(i)) + ": " + fullANS.charAt(i));
//  }
        String result = CharNormalizerJa.ToHalfANS.normalize(fullANS);
        for (int i = 0; i < fullANS.length(); i++) {
            assertEquals(Character.valueOf(halfANS.charAt(i)), Character.valueOf(result.charAt(i)));
        }
    }

    /** */
    @Test
    public void testToHalf() throws Exception {
        assertEquals(halfANS, CharNormalizerJa.ToHalf.normalize(fullANS));
        assertEquals(halfDakuon, CharNormalizerJa.ToHalf.normalize(fullDakuon));
        assertEquals(halfHandakuon, CharNormalizerJa.ToHalf.normalize(fullHandakuon));
    }

    /** */
    @Test
    public void testToFull() throws Exception {
        assertEquals(fullANS, CharNormalizerJa.ToFull.normalize(halfANS));
        assertEquals(fullDakuon, CharNormalizerJa.ToFull.normalize(halfDakuon));
        assertEquals(fullHandakuon, CharNormalizerJa.ToFull.normalize(halfHandakuon));
    }

    /** */
    @Test
    public void testToFullANS() throws Exception {
        String result = CharNormalizerJa.ToFull.normalize(halfANS);
        for (int i = 0; i < halfANS.length(); i++) {
            assertEquals(Character.valueOf(fullANS.charAt(i)), Character.valueOf(result.charAt(i)));
        }
    }

    /** 半角カタカナ */
    static final String halfKanas = "｡｢｣･ｦｧｨｩｪｫｬｭｮｯｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝﾞﾟ";
    /** 全角カタカナ */
    static final String fullKanas = "。「」・ヲァィゥェォャュョッーアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワン゛゜";

    /** 全角カタカナ → 半角カタカナ */
    @Test
    public void testToHalfKana() throws Exception {
        assertEquals(halfKanas, CharNormalizerJa.ToHalfKana.normalize(fullKanas));
    }

    /** 半角カタカナ → 全角カタカナ */
    @Test
    public void testToFullKana() throws Exception {
        assertEquals(fullKanas, CharNormalizerJa.ToFullKana.normalize(halfKanas));
    }
}

/* */
