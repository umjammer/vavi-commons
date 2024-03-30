/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * CharNormalizerJaTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
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

    @Test
    public void testToKatakana() throws Exception {
        assertEquals(fullKana, CharNormalizerJa.ToKatakana.normalize(fullHira));
    }

    @Test
    public void testToHiragana() throws Exception {
        assertEquals(fullHira, CharNormalizerJa.ToHiragana.normalize(fullKana));
    }

    /** */
    static final String halfANS = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    /** */
    static final String fullANS = "！\uff02＃＄％＆\uff07（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～";

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

    @Test
    public void testToHalf() throws Exception {
        assertEquals(halfANS, CharNormalizerJa.ToHalf.normalize(fullANS));
        assertEquals(halfDakuon, CharNormalizerJa.ToHalf.normalize(fullDakuon));
        assertEquals(halfHandakuon, CharNormalizerJa.ToHalf.normalize(fullHandakuon));
    }

    @Test
    public void testToFull() throws Exception {
        assertEquals(fullANS, CharNormalizerJa.ToFull.normalize(halfANS));
        assertEquals(fullDakuon, CharNormalizerJa.ToFull.normalize(halfDakuon));
        assertEquals(fullHandakuon, CharNormalizerJa.ToFull.normalize(halfHandakuon));
    }

    @Test
    public void testToFullANS() throws Exception {
        String result = CharNormalizerJa.ToFull.normalize(halfANS);
        for (int i = 0; i < halfANS.length(); i++) {
            assertEquals(Character.valueOf(fullANS.charAt(i)), Character.valueOf(result.charAt(i)));
        }
    }

    /** half-width katakana */
    static final String halfKanas = "｡｢｣･ｦｧｨｩｪｫｬｭｮｯｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝﾞﾟ";
    /** full-width katakana */
    static final String fullKanas = "。「」・ヲァィゥェォャュョッーアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワン゛゜";

    /** Full-width katakana → half-width katakana */
    @Test
    public void testToHalfKana() throws Exception {
        assertEquals(halfKanas, CharNormalizerJa.ToHalfKana.normalize(fullKanas));
    }

    /** Half-width katakana → full-width katakana */
    @Test
    public void testToFullKana() throws Exception {
        assertEquals(fullKanas, CharNormalizerJa.ToFullKana.normalize(halfKanas));
    }

    static final String fullTable =
            "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ" +
            "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ" +
            "０１２３４５６７８９" +
            "　（）“”’．，｛｝［］＿＆";
    private static final String halfTable =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789" +
            " ()\"\"'.,{}[]_&";

    @Test
    public void testToFullAns2() throws Exception {
//for (int i = 0; i < fullTable.length(); i++) {
// char c1 = fullTable.charAt(i);
// char c2 = CharNormalizerJa.ToFullAns2.normalize(halfTable).charAt(i);
// System.err.printf("[%d] %c, %x: %c, %x: %s%n", i, c1, (int) c1 & 0xffff, c2, (int) c2 & 0xffff, c1 == c2);
//}
        // because both "“" and "”" -> '"'
        char[] ca = fullTable.toCharArray();
        ca[66] = ca[65];
        String expect = new String(ca);
        assertEquals(expect, CharNormalizerJa.ToFullAns2.normalize(halfTable));
    }

    @Test
    public void testToHalfAns2() throws Exception {
        assertEquals(halfTable, CharNormalizerJa.ToHalfAns2.normalize(fullTable));
    }
}
