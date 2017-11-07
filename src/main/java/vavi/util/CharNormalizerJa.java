/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.lang.String;
import java.lang.StringBuilder;


/**
 * 日本語文字の平仮名とカタカナの相互変換と，
 * 半角と全角文字の相互変換を行います．
 *
 * @author Takashi Okamoto
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.20 T.O original version <br>
 *          0.30 021120 nsano refine <br>
 */
public enum CharNormalizerJa implements CharNormalizer {
    /**
     * 平仮名をカタカナに変換変換する。
     */
    ToKatakana() {
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                char code = str.charAt(i);
                if ((code >= 0x3041) && (code <= 0x3093)) {
                    // 平仮名のときカタカナに変換
                    ret.append((char) (code + 0x60));
                } else {
                    // 平仮名以外は、そのまま
                    ret.append(code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * カタカナを平仮名に変換変換する。
     * ただし、次の文字は、変換できない(ひらがなに対応する文字がない)
     * <UL>
     *  <LI>ヴ(0x30f4)</LI>
     *  <LI>ヵ(0x30f5)</LI>
     *  <LI>ヶ(0x30f6)</LI>
     * </UL>
     */
    ToHiragana {
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                char code = str.charAt(i);
                if ((code >= 0x30a1) && (code <= 0x30f3)) {
                    // カタカナのとき平仮名に変換
                    ret.append((char) (code - 0x60));
                } else {
                    // カタカナ以外は、そのまま
                    ret.append(code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * 全角英数記号を半角に変換する。
     */
    ToHalfANS {
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0;i < str.length(); i++) {
                int code = str.charAt(i);
                if ((code >= 0xff01) && (code <= 0xff5e)) {
                    ret.append((char) (code - 0xfee0));
                } else {
                    ret.append((char) code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * 全角文字を可能な限り半角文字に変換する。
     */
    ToHalf {
        public String normalize(String str) {
            return ToHalfKana.normalize(ToHalfANS.normalize(str));
        }
    },
    /**
     * 半角文字を可能な限り全角文字に変換する。
     */
    ToFull {
        public String normalize(String str) {
            return ToFullKana.normalize(ToFullANS.normalize(str));
        }
    },
    /**
     * 半角英数記号を全角に変換する。
     */
    ToFullANS {
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                int code = str.charAt(i);
                if ((code >= 0x21) && (code <= 0x7e)) {
                    ret.append((char) (code + 0xfee0));
//Debug.println((int) ret.charAt(i));
                } else {
                    ret.append((char) code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * 全角文字を半角文字に変換する。
     */
    ToHalfKana {
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {

                int base = -1;
                int type = -1;
                char code = str.charAt(i);

                if (code != '#') {
                    for (int t = 1; t < 4; t++) {
                        for (int j = 0; j < kanaTableJa.length; j++) {
                            if (code == kanaTableJa[j].charAt(t)) {
                                base = j;
                                type = t;
                                break;
                            }
                        }
                        if (type != -1) {
                            break;
                        }
                    }
                }

                switch (type) {
                case 1:
                    ret.append(kanaTableJa[base].charAt(0));
                    break;
                case 2:
                    ret.append(kanaTableJa[base].charAt(0));
                    ret.append('ﾞ');
                    break;
                case 3:
                    ret.append(kanaTableJa[base].charAt(0));
                    ret.append('ﾟ');
                    break;
                default:
                    ret.append(code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * 半角カナを全角カタカナに変換する。<p>
     *
     * 濁点、半濁点が含まれる場合、前の文字に統合<br>
     * 例「バ」→「バ」(「ハ゛」とならない)<br>
     */
    ToFullKana {
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();
            int prevBase = 0;

            for (int i = 0; i < str.length(); i++) {
    //      int base = -1;
                char code = str.charAt(i);

                if ((code == 'ﾞ') && (kanaTableJa[prevBase].charAt(2) != '#')) {
                    // 濁点を受理し、1つ前の文字が濁点付与可能な文字の場合

                    // 1文字削除
                    ret.deleteCharAt(ret.length() - 1);

                    // 濁点文字追加
                    ret.append(kanaTableJa[prevBase].charAt(2));

                    prevBase = 0;
                    continue;
                } else if ((code == 'ﾟ') && (kanaTableJa[prevBase].charAt(2) != '#')) {
                    // 半濁点を受理し、1つ前の文字が濁点付与可能な文字の場合

                    // 1文字削除
                    ret.deleteCharAt(ret.length() - 1);

                    // 半濁点文字追加
                    ret.append(kanaTableJa[prevBase].charAt(3));

                    prevBase = 0;
                    continue;
                }

                boolean flag = false;

                for (int j = 0; j < kanaTableJa.length; j++) {
                    // テ−ブルを走査
                    if (code == kanaTableJa[j].charAt(0)) {
                        // 文字が見つかれば追加
                        flag = true;
                        ret.append(kanaTableJa[j].charAt(1));
                        prevBase = j;
                        break;
                    }
                }
                if (!flag) {
                    ret.append(code);
                    prevBase = 0;
                }
            }

            return ret.toString();
        }
    },
    /** */
    ToFullAns2 {
        /** */
        public String normalize(String str) {
            for (int i = 0; i < halfTable.length(); i++) {
                str = str.replace(halfTable.charAt(i), fullTable.charAt(i));
            }

            return str;
        }
    },
    /** */
    ToHalfAns2 {
        /** */
        public String normalize(String str) {
            for (int i = 0; i < fullTable.length(); i++) {
                str = str.replace(fullTable.charAt(i), halfTable.charAt(i));
            }

            return str;
        }
    };

    /** */
    private static final String kanaTableJa[] = {
        "｡。##",
        "｢「##",
        "｣」##",
        "･・##",
        "ｦヲ##",
        "ｧァ##",
        "ｨィ##",
        "ｩゥ##",
        "ｪェ##",
        "ｫォ##",
        "ｬャ##",
        "ｭュ##",
        "ｮョ##",
        "ｯッ##",
        "ｰー##",
        "ｱア##",
        "ｲイ##",
        "ｳウ##",
        "ｴエ##",
        "ｵオ##",
        "ｶカガ#",
        "ｷキギ#",
        "ｸクグ#",
        "ｹケゲ#",
        "ｺコゴ#",
        "ｻサザ#",
        "ｼシジ#",
        "ｽスズ#",
        "ｾセゼ#",
        "ｿソゾ#",
        "ﾀタダ#",
        "ﾁチヂ#",
        "ﾂツヅ#",
        "ﾃテデ#",
        "ﾄトド#",
        "ﾅナ##",
        "ﾆニ##",
        "ﾇヌ##",
        "ﾈネ##",
        "ﾉノ##",
        "ﾊハバパ",
        "ﾋヒビピ",
        "ﾌフブプ",
        "ﾍヘベペ",
        "ﾎホボポ",
        "ﾏマ##",
        "ﾐミ##",
        "ﾑム##",
        "ﾒメ##",
        "ﾓモ##",
        "ﾔヤ##",
        "ﾕユ##",
        "ﾖヨ##",
        "ﾗラ##",
        "ﾘリ##",
        "ﾙル##",
        "ﾚレ##",
        "ﾛロ##",
        "ﾜワ##",
        "ﾝン##",
        "ﾞ゛##",
        "ﾟ゜##"
    };

    /** */
    private static final String fullTable =
        "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ" +
        "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ" +
        "０１２３４５６７８９" +
        "　（）“”’．，｛｝［］＿＆";

    /** */
    private static final String halfTable =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789" +
            " ()\"\"'.,{}[]_&";
}

/* */
