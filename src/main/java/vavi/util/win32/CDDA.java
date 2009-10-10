/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.IOException;
import java.io.InputStream;

import vavi.io.LittleEndianDataInputStream;


/**
 * CDDA format.
 * 
 * <pre>
 * 
 *  off len dsc
 *  00  04  &quot;fmt &quot;
 *  04  04  chank length (*1)
 * 
 *  00  02  CDDA file version. Currently equals 1. If it has
 *          other value, following data may be out of date.
 *  02  02  Number of track.
 *  04  04  CD disc serial number (the one stored in CDPLAYER.INI)
 *  08  04  Beginning of the track in HSG format.
 *  0C  04  Length of the track in HSG format.
 *  10  04  Beginning of the track in Red-Book format.
 *  14  04  Length of the track in Red-Book format.
 *  
 * </pre>
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020424 nsano initial version <br>
 *          0.10 020507 nsano repackage <br>
 *          0.11 020507 nsano refine <br>
 *          0.10 020707 nsano refine <br>
 *          0.20 030209 nsano new RIFF compatible <br>
 *          0.30 030711 nsano add setChildData() <br>
 *          0.31 030711 nsano deprecate setChildData() <br>
 */
public class CDDA extends RIFF {

    /** Gets extension. */
    public static String getExtention() {
        return "cda";
    }

    /** for debug */
    protected void printData() {
        System.err.println("---- data ----");
    }

    /** Gets beginning of the track in MSF format. */
    public MSF getBeginMSF() {
        return ((fmt) chunks.firstElement()).getBeginMSF();
    }

    /** Gets length of the track in MSF format. */
    public MSF getLengthMSF() {
        return ((fmt) chunks.firstElement()).getLengthMSF();
    }

    //-------------------------------------------------------------------------

    /** */
    public class fmt extends Chunk {

        /** CDA file version */
        private int version;
        /** Number of track */
        private int number;
        /** CD disc serial number */
        private int serial;
        /** Beginning of the track */
        private int begin;
        /** Length of the track */
        private long lengthOfFrame;
        /** Beginning of the track in MSF format */
        private MSF beginMSF;
        /** Length of the track in MSF format */
        private MSF lengthMSF;

        /** Gets beginning of the track in frame. */
        public int getBegin() {
            return begin;
        }

        /** Gets length of the track in frame. */
        public long getLengthOfFrame() {
            return lengthOfFrame;
        }

        /** Gets beginning of the track in MSF format. */
        public MSF getBeginMSF() {
            return beginMSF;
        }

        /** Gets length of the track in MSF format. */
        public MSF getLengthMSF() {
            return lengthMSF;
        }

        /** for debug */
        protected void printData() {
            System.err.println("version:\t"  + version      );
            System.err.println("number:\t"   + number       );
            System.err.println("serial:\t\t" + serial       );
            System.err.println("begin:\t\t"  + begin        );
            System.err.println("length:\t\t" + lengthOfFrame);
            System.err.println("beginMSF:\t" + beginMSF     );
            System.err.println("lengthMSF:\t"+ lengthMSF    );
        }

        /**
         * Creates a CDA object from a stream.
         * 24 bytes
         */
        public void setData(InputStream is) throws IOException {

            LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

            version       = ledis.readShort();
            number        = ledis.readShort();
            serial        = ledis.readInt();
            begin         = ledis.readInt();
            lengthOfFrame = ledis.readInt() & 0xffffffffL;

            int m, s, f, d;
            f = is.read();
            s = is.read();
            m = is.read();
            d = is.read();
            beginMSF = new MSF(m, s, f);

            f = is.read();
            s = is.read();
            m = is.read();
            d = is.read();
            lengthMSF = new MSF(m, s, f);
        }
    }
}

/* */
