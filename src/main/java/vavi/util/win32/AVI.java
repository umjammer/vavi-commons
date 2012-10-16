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
 * AVI format.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030120 nsano initial version <br>
 *          1.00 030122 nsano new specification complient <br>
 *          1.01 030606 nsano change LIST <br>
 *          1.10 030711 nsano add setChildData() <br>
 *          1.11 030711 nsano deprecate setChildData() <br>
 */
public class AVI extends RIFF {

    /** Gets extension. */
    public static String getExtention() {
        return "avi";
    }
    
    /** for debug */
    protected void printData() {
        System.err.println("---- data ----");
    }

    //-------------------------------------------------------------------------

    public class LIST extends vavi.util.win32.LIST {
        /** */
        public class LIST2 extends vavi.util.win32.LIST {
            /** */
            public class strh extends Chunk {
            }
            /** */
            public class strf extends Chunk {
            }
            /** */
            public class strn extends Chunk {
            }
        }
        /** */
        public class avih extends Chunk {
            /**
             * the period between video frames,
             * indicates the overall timing for the file
             */
            int microSecPerFrame;
            /** the approximate maximum data rate of the file */
            int maxBytesPerSec;
            /** */
            int reserved1;
            /** */
            int flags;
            
            /** Indicates the AVI file has an vdx1 chunk */
            static final int AVIF_HASINDEX = 0;
            /**
             * Indicates the index should be used to determine the order of
             * presentation of the data
             */
            static final int AVIF_MUSTUSEINDEX = 0;
            /** Indicates the AVI file is interleaved */
            static final int AVIF_ISINTERLEAVED = 0;
            /**
             * Indicates the AVI file is a specially allocated file used for
             * capturing real-time video
             */
            static final int AVIF_WASCAPTUREFILE = 0;
            /** Indicates the AVI file contains copyrighted data */
            static final int AVIF_COPYRIGHTED = 0;
            
            /** */
            int totalFrames;
            /** */
            int initialFrames;
            /** */
            int streams;
            /** */
            int suggestedBufferSize;
            /** */
            int width;
            /** */
            int height;
            /** */
            int scale;
            /** */
            int rate;
            /** */
            int start;
            /** */
            int length;
            
            /** for debug */
            protected void printData() {
                System.err.println(this);
            }
            
            /** */
            public void setData(InputStream is) throws IOException {
                LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);
                
                microSecPerFrame = ledis.readInt();
                maxBytesPerSec = ledis.readInt();
                reserved1 = ledis.readInt();
                flags = ledis.readInt();
                totalFrames = ledis.readInt();
                initialFrames = ledis.readInt();
                streams = ledis.readInt();
                suggestedBufferSize = ledis.readInt();
                width = ledis.readInt();
                height = ledis.readInt();
                scale = ledis.readInt();
                rate = ledis.readInt();
                start = ledis.readInt();
                length = ledis.readInt();
            }
        }
        /** */
        class XXdb extends Chunk {
        }
        /** */
        public class _00db extends XXdb {
        }
    }

    /** */
    public class JUNK extends Chunk {
    }
}

/* */
