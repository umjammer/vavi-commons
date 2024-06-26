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
import java.util.Objects;
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
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020707 nsano initial version <br>
 *          1.00 030121 nsano refactoring <br>
 *          1.10 030711 nsano add setChildData() <br>
 *          1.11 030711 nsano deprecate setChildData() <br>
 */
public class WAVE extends RIFF {

    /**
     * @return Returns the format.
     */
    public fmt getHeader() {
        return findChildOf(fmt.class);
    }

    //----

    /** Represents wave format data. */
    public static class fmt extends Chunk {

        static final String ID = "fmt ";

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
        public String toString() {
             String key = "format.id." + String.format("%04x", formatId);
             String type = pcmTypes.getProperty(key);
             return "formatId:\t" + (type == null ? String.format("%04x", formatId) : type) +
                ", numberChannels:\t" + numberChannels +
                ", samplingRate:\t"   + samplingRate +
                ", bytesPerSecond:\t" + bytesPerSecond +
                ", blockSize:\t"      + blockSize +
                ", samplingBits:\t"   + samplingBits +
                ", sizeofExtended:\t" + sizeofExtended +
                ", expanded:\t" + ((extended == null) ? "null" : "\n" + StringUtil.getDump(extended));
        }

        @Override
        public void setData(InputStream is) throws IOException {
            LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

            formatId       = ledis.readShort() & 0xffff;
            numberChannels = ledis.readShort();
            samplingRate   = ledis.readInt();
            bytesPerSecond = ledis.readInt();
            blockSize      = ledis.readShort();
            samplingBits   = ledis.readShort();
            if (getLength() > 16) {
                sizeofExtended = ledis.readShort();
                extended = new byte[sizeofExtended];
                ledis.readFully(extended);
            }
        }
    }

    //----

    /** use DataBuffer map for data handler */
    public static final String WAVE_DATA_DEAL_BIG_SIZE_KEY = "vavi.util.win32.WAVE.data.dealBigSize";

    /** to stop parsing before loading data or not */
    public static final String WAVE_DATA_NOT_LOAD_KEY = "vavi.util.win32.WAVE.data.notLoadData";

    private static boolean isDealBigSize() {
        return (boolean) Objects.requireNonNullElse(context.get().get(WAVE_DATA_DEAL_BIG_SIZE_KEY), false);
    }

    private static boolean isNotLoadData() {
        return (boolean) Objects.requireNonNullElse(context.get().get(WAVE_DATA_NOT_LOAD_KEY), false);
    }

    /**
     * Represents wave data.
     * <p>
     * system properties
     * <ul>
     *  <li>vavi.util.win32.WAVE.data.dealBigSize ... use DataBuffer map for data handler</li>
     *  <li>vavi.util.win32.WAVE.data.notLoadData ... to stop parsing before loading data or not</li>
     * </ul>
     */
    public static class data extends Chunk {

        /** buffer for data */
        private byte[] wave;

        /** */
        private MappedByteBuffer buffer;

        /**
         * Gets data.
         * getData() cannot be overridden
         */
        public byte[] getWave() {
            if (isDealBigSize() && buffer != null) {
                return buffer.array();
            } else {
                return wave;
            }
        }

        @Override
        public void setData(InputStream is) throws IOException {
            if (isNotLoadData()) {
                throw new ChunkParseStopException();
            }
            if (isDealBigSize() && is instanceof FileInputStream) {
                FileChannel inputChannel = ((FileInputStream) is).getChannel();
                buffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) inputChannel.size());
            } else {
                wave = new byte[getLength()];
                int l = 0;
                while (l < getLength()) {
                    int r = is.read(wave, l, getLength() - l);
                    if (r < 0) {
                        throw new EOFException();
                    }
                    l += r;
                }
            }
        }
    }

    /** */
    public static class fact extends Chunk {
        public int sampleCount;
        /** for debug */
        public String toString() {
            return "sampleCount: " + sampleCount;
        }
        @Override
        public void setData(InputStream is) throws IOException {
            LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);
            sampleCount = ledis.readInt();
        }
    }

    /** */
    public static class LIST extends MultipartChunk {

        public static class ISFT extends Chunk {
        }
    }

    //----

    @Override
    public void writeTo(OutputStream os) throws IOException {
    }

    //----

    /** PCM types table */
    private static final Properties pcmTypes = new Properties();

    static {
        try {
            pcmTypes.load(WAVE.class.getResourceAsStream("wave.properties"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
