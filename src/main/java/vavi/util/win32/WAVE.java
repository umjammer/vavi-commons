/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import vavi.io.LittleEndianDataInputStream;
import vavi.util.StringUtil;


/**
 * WAVE format.
 * 
 * <pre>
 *  off len dsc
 * </pre>
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020707 nsano initial version <br>
 *          1.00 030121 nsano refactoring <br>
 *          1.10 030711 nsano add setChildData() <br>
 *          1.11 030711 nsano deprecate setChildData() <br>
 */
public class WAVE extends RIFF {

    /** */
    private fmt header;

    /**
     * @return Returns the format.
     */
    public fmt getHeader() {
        return header;
    }

    /**
     * @param format The format to set.
     */
    public void setHeader(fmt format) {
        this.header = format;
    }

    /** Gets extension. */
    public static final String getExtention() {
        return "wav";
    }

    /** for debug */
    protected void printData() {
        System.err.println("---- data ----");
    }

    /** TODO now construction */
    private boolean dealBigSize = false;

    //-------------------------------------------------------------------------

    /** */
    public class fmt extends Chunk {

        /** The format id */
        private int formatId;
        /** Number of channel */
        private int numberChannels;
        /** Sampling rate */
        private int samplingRate;
        /** bps */
        private int bytesPerSecond;
        /** Size of the block */
        private int blockSize;
        /** Sampling bits */
        private int samplingBits;
        /** Size of extended data */
        private int sizeofExtended;
        /** Extended data buffer */
        private byte[] extended;

        /** Gets format id. @see pcmTypes#key */
        public int getFormatId() {
            return formatId;
        }

        /** Sets format id. @see pcmTypes#key */
        public void setFormatId(int formatId) {
            this.formatId = formatId;
        }

        /** Gets number of channels. */
        public int getNumberChannels() {
            return numberChannels;
        }

        /** Sets number of channels. */
        public void setNumberChannels(int numberChannels) {
            this.numberChannels = numberChannels;
        }

        /** Gets sampling rate. */
        public int getSamplingRate() {
            return samplingRate;
        }

        /** Sets sampling rate. */
        public void setSamplingRate(int samplingRate) {
            this.samplingRate = samplingRate;
        }

        /** Gets bps. */
        public int getBytesPerSecond() {
            return bytesPerSecond;
        }

        /** Sets bps. */
        public void setBytesPerSecond(int bytesPerSecond) {
            this.bytesPerSecond = bytesPerSecond;
        }

        /** Gets block size. */
        public int getBlockSize() {
            return blockSize;
        }

        /** Sets block size. */
        public void setBlockSize(int blockSize) {
            this.blockSize = blockSize;
        }

        /** Gets sampling bits. */
        public int getSamplingBits() {
            return samplingBits;
        }

        /** Sets sampling bits. */
        public void setSamplingBits(int samplingBits) {
            this.samplingBits = samplingBits;
        }

        /** Gets extended data. */
        public byte[] getExtended() {
            return extended;
        }

        /** Sets extended data. */
        public void setExtended(byte[] extended) {
            this.extended = extended;
        }

        /** for debug */
        protected void printData() {
            String key = "format.id." + StringUtil.toHex4(formatId);
            String type = pcmTypes.get(key);
            System.err.println("formatId:\t" + (type == null ? StringUtil.toHex4(formatId) : type));
            System.err.println("numberChannels:\t" + numberChannels);
            System.err.println("samplingRate:\t"   + samplingRate  );
            System.err.println("bytesPerSecond:\t" + bytesPerSecond);
            System.err.println("blockSize:\t"      + blockSize     );
            System.err.println("samplingBits:\t"   + samplingBits  );
            System.err.println("sizeofExtended:\t" + sizeofExtended);
            System.err.println("expanded:\t" + ((extended == null) ? "null" : "\n" + StringUtil.getDump(extended)));
        }

        /** */
        public void setData(InputStream is) throws IOException {
            @SuppressWarnings("resource")
            LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

            formatId       = ledis.readShort();
            numberChannels = ledis.readShort();
            samplingRate   = ledis.readInt() & 0xffff;
//          int samplingRate1 = leis.readShort();
//System.err.println("l1: " + samplingRate1);
//          int samplingRate2 = leis.readShort();
//System.err.println("l2: " + samplingRate2);
            bytesPerSecond = ledis.readInt();
            blockSize      = ledis.readShort();
            samplingBits   = ledis.readShort();
            if (getLength() > 16) {
                sizeofExtended = ledis.readShort();
                extended = new byte[sizeofExtended];
                int l = 0;
                while (l < sizeofExtended) {
                    l += ledis.read(extended, l, sizeofExtended - l);
                }
            }
        }
    }

    //-------------------------------------------------------------------------

    /** */
    public class data extends Chunk {

        /** buffer for data */
        private byte[] wave;

        /** */
        private MappedByteBuffer buffer;

        /**
         * Gets data.
         * getData() はオーバライドできない．
         */
        public byte[] getWave() {
            if (dealBigSize && buffer != null) {
                return buffer.array();
            } else {
                return wave;
            }
        }

        /** for debug */
        protected void printData() {
            System.err.println("WAV file");
        }

        /** */
        public void setData(InputStream is) throws IOException {
            if (dealBigSize && is instanceof FileInputStream) {
                FileChannel inputChannel = FileInputStream.class.cast(is).getChannel();
                buffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) inputChannel.size());
            } else {
                wave = new byte[(int) getLength()];
                int l = 0;
                while (l < getLength()) {
                    int r = is.read(wave, l, (int) getLength() - l);
                    if (r < 0) {
                        throw new EOFException();
                    }
                    l += r;
                }
            }
        }
    }

    /** */
    public class fact extends Chunk {
        int fileSize;
        /** for debug */
        protected void printData() {
            System.err.println("fileSize: " + fileSize);
        }
        /** */
        public void setData(InputStream is) throws IOException {
            @SuppressWarnings("resource")
            LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);
            fileSize = ledis.readInt();
        }
    }

    //-------------------------------------------------------------------------

    /** */
    public void writeTo(OutputStream os) throws IOException {
    }

    //-------------------------------------------------------------------------

    /** PCM types table */
    private static Hashtable<String, String> pcmTypes = new Hashtable<>();

    /**  */
    static {
        try {
            // props
            final String path = "wave.properties";
            Properties props = new Properties();
            props.load(WAVE.class.getResourceAsStream(path));

            // format id
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (key.startsWith("format.id.")) {
                    String value = props.getProperty(key);
//System.err.println(key + " = " + value);
                    pcmTypes.put(key, value);
                }
            }
        } catch (Exception e) {
System.err.println(e);
            System.exit(1);
        }
    }
}

/* */
