/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


/**
 * Converts between Japanese Hiragana and Katakana,
 * Full-Width and Half-Width also.
 *
 * @author Takashi Okamoto
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.20 T.O original version <br>
 *          0.30 021120 nsano refine <br>
 */
@Locales(countries = "Japan", languages = "Japanese")
public enum CharNormalizerJa implements CharNormalizer {

    /**
     * Converts hiragana to katakana.
     */
    ToKatakana() {
        @Override
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                char code = str.charAt(i);
                if ((code >= 0x3041) && (code <= 0x3093)) {
                    // when char is hiragana, converts to katakana
                    ret.append((char) (code + 0x60));
                } else {
                    // else hiragana, remains
                    ret.append(code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * Converts katakana to hiragana.
     * but follows will be excluded (no corresponding hiragana)
     * <UL>
     *  <LI>ヴ(0x30f4)</LI>
     *  <LI>ヵ(0x30f5)</LI>
     *  <LI>ヶ(0x30f6)</LI>
     * </UL>
     */
    ToHiragana {
        @Override
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                char code = str.charAt(i);
                if ((code >= 0x30a1) && (code <= 0x30f3)) {
                    // when char is katakana, converts to hiragana
                    ret.append((char) (code - 0x60));
                } else {
                    // else katakana, remains
                    ret.append(code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * Converts full-width alphanumeric symbols to half-width.
     */
    ToHalfANS {
        @Override
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
     * Converts full-width numbers to half-width numbers.
     */
    ToHalfDigit {
        @Override
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0;i < str.length(); i++) {
                int code = str.charAt(i);
                if ((code >= 0xff10) && (code <= 0xff19)) {
                    ret.append((char) (code - 0xfee0));
                } else {
                    ret.append((char) code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * Convert full-width characters to half-width characters whenever possible.
     */
    ToHalf {
        @Override
        public String normalize(String str) {
            return ToHalfKana.normalize(ToHalfANS.normalize(str));
        }
    },
    /**
     * Convert half-width characters to full-width characters whenever possible.
     */
    ToFull {
        @Override
        public String normalize(String str) {
            return ToFullKana.normalize(ToFullANS.normalize(str));
        }
    },
    /**
     * Convert half-width alphanumeric symbols to full-width.
     */
    ToFullANS {
        @Override
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                int code = str.charAt(i);
                if ((code >= 0x21) && (code <= 0x7e)) {
                    ret.append((char) (code + 0xfee0));
//logger.log(Level.TRACE, (int) ret.charAt(i));
                } else {
                    ret.append((char) code);
                }
            }

            return ret.toString();
        }
    },
    /**
     * Convert full-width characters to half-width characters.
     */
    ToHalfKana {
        @Override
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
     * Convert half-width kana to full-width katakana.
     * <p>
     * If a voiced or handakuten character is included, it will be merged into the previous character.<br>
     * e.g.「バ」→「バ」(not 「ハ゛」)<br>
     */
    ToFullKana {
        @Override
        public String normalize(String str) {

            StringBuilder ret = new StringBuilder();
            int prevBase = 0;

            for (int i = 0; i < str.length(); i++) {
    //      int base = -1;
                char code = str.charAt(i);

                if ((code == 'ﾞ') && (kanaTableJa[prevBase].charAt(2) != '#')) {
                    // if a voiced mark is accepted and the previous character is a character
                    // that can be given a voiced mark

                    // delete 1 character
                    ret.deleteCharAt(ret.length() - 1);

                    // added voiced characters
                    ret.append(kanaTableJa[prevBase].charAt(2));

                    prevBase = 0;
                    continue;
                } else if ((code == 'ﾟ') && (kanaTableJa[prevBase].charAt(2) != '#')) {
                    // If a handakuten is accepted and the previous character is a character
                    // that can be given a voiced mark

                    // delete 1 character
                    ret.deleteCharAt(ret.length() - 1);

                    // added handakuten characters
                    ret.append(kanaTableJa[prevBase].charAt(3));

                    prevBase = 0;
                    continue;
                }

                boolean flag = false;

                for (int j = 0; j < kanaTableJa.length; j++) {
                    // scan table
                    if (code == kanaTableJa[j].charAt(0)) {
                        // add characters if found
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
    /**
     * Converts by {@link #halfTable}
     */
    ToFullAns2 {
        /** */
        @Override
        public String normalize(String str) {
            for (int i = 0; i < halfTable.length(); i++) {
                str = str.replace(halfTable.charAt(i), fullTable.charAt(i));
            }

            return str;
        }
    },
    /**
     * Converts by {@link #fullTable}
     */
    ToHalfAns2 {
        /** */
        @Override
        public String normalize(String str) {
            for (int i = 0; i < fullTable.length(); i++) {
                str = str.replace(fullTable.charAt(i), halfTable.charAt(i));
            }

            return str;
        }
    };

    /** @see "kanaTableJa.txt" */
    private static final String[] kanaTableJa;

    /** @see "ans2.properties" */
    private static final String fullTable;

    /** @see "ans2.properties" */
    private static final String halfTable;

    static {
        //
        List<String> kanaTableJaList = new ArrayList<>();
        Scanner s = new Scanner(CharNormalizerJa.class.getResourceAsStream("kanaTableJa.txt"));
        while (s.hasNextLine()) {
            String l = s.nextLine();
            kanaTableJaList.add(l);
        }
        kanaTableJa = kanaTableJaList.toArray(new String[0]);
        //
        try {
            Properties p = new Properties();
            p.load(CharNormalizerJa.class.getResourceAsStream("ans2.properties"));
            fullTable = p.getProperty("fullTable");
            halfTable = p.getProperty("halfTable");
//logger.log(Level.TRACE, "fullTable: " + fullTable.length());
//logger.log(Level.TRACE, "halfTable: " + halfTable.length());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
