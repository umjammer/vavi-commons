/*
 * Copyright (c) 2002 by Naohide Sano, All right reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import vavi.io.LittleEndianDataInputStream;
import vavi.util.StringUtil;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.getLogger;


/**
 * This is a class for handling .lnk.
 *
 * <pre>
 * 00h   4  'L' 00 00 00 magic
 * 04h  16  GUID
 * 14h   4  flags
 * 18h   4  file attributes
 * 1ch   8  time 1
 * 24h   8  time 2
 * 2ch   8  time 3
 * 34h   4  file length
 * 38h   4  icon number
 * 3ch   4  ShowWnd value
 * 40h   4  hot key
 * 44h   8  unknown, always 0
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020507 nsano initial version <br>
 */
public class LinkFile {

    private static final Logger logger = getLogger(LinkFile.class.getName());

    private long guid;
    private int flags;
    private int attributes;
    private long time1;
    private long time2;
    private long time3;
    private int length;
    private int iconNumber;
    private int showWnd;
    private int hotKey;

    // flags
    private static final int FLAG_THE_SHELL_ITEM_ID_LIST_IS_PRESENT = 0x01;
    private static final int FLAG_POINT_TO_A_FILE_OR_DIRECTORY = 0x02;
    private static final int FLAG_HAS_A_DESCRIPTION_STRING = 0x04;
    private static final int FLAG_HAS_A_RELATIVE_PATH_STRING = 0x08;
    private static final int FLAG_HAS_A_WORKING_DIRECTORY = 0x10;
    private static final int FLAG_HAS_COMMAND_LINE_ARGUMENTS = 0x20;
    private static final int FLAG_HAS_A_CUSTOM_ICON = 0x40;

    // attributes
    protected static final int ATTRIBUTE_TARGET_IS_READ_ONLY = 0x01;
    protected static final int ATTRIBUTE_TARGET_IS_HIDDEN = 0x02;
    protected static final int ATTRIBUTE_TARGET_IS_A_SYSTEM_FILE = 0x04;
    protected static final int ATTRIBUTE_TARGET_IS_A_VOLUME_LABEL = 0x08;
    protected static final int ATTRIBUTE_TARGET_IS_A_DIRECTORY = 0x10;
    protected static final int ATTRIBUTE_TARGET_HAS_BEEN_MODIFIED_SINCE_LAST_BACKUP = 0x20;
    protected static final int ATTRIBUTE_TARGET_IS_ENCRIPTED = 0x40;
    protected static final int ATTRIBUTE_TARGET_IS_NORMAL = 0x80;
    protected static final int ATTRIBUTE_TARGET_IS_TEMPORARY = 0x100;
    protected static final int ATTRIBUTE_TARGET_IS_SPARSE_FILE = 0x200;
    protected static final int ATTRIBUTE_TARGET_HAS_REPARSE_FILE = 0x400;
    protected static final int ATTRIBUTE_TARGET_IS_COMPRESSED = 0x800;
    protected static final int ATTRIBUTE_TARGET_IS_OFFLINE = 0x1000;

    // showWnd
    protected static final int SW_HIDE = 0;
    protected static final int SW_NORMAL = 1;
    protected static final int SW_SHOWMINIMIZED = 2;
    protected static final int SW_SHOWMAXIMIZED = 3;
    protected static final int SW_SHOWNOACTIVATE = 4;
    protected static final int SW_SHOW = 5;
    protected static final int SW_MINIMIZE = 6;
    protected static final int SW_SHOWMINNOACTIVE = 7;
    protected static final int SW_SHOWNA = 8;
    protected static final int SW_RESTORE = 9;
    protected static final int SW_SHOWDEFAULT = 10;

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_NO_ROOT_DIRECTORY = 1;
    public static final int TYPE_REMOVABLE = 2;
    public static final int TYPE_FIXED = 3;
    public static final int TYPE_REMOTE = 4;
    public static final int TYPE_CD_ROM = 5;
    public static final int TYPE_RAM_DRIVE = 6;

    public static final int FLAG_AVAILABLE_ON_A_LOCAL_VOLUME = 0x01;
    public static final int FLAG_AVAILABLE_ON_A_NETWORK_SHARE = 0x02;

    /** */
    private String path;

    /** */
    public String getPath() {
        return path;
    }

    /** */
    public static LinkFile readFrom(InputStream is) throws IOException {

        LinkFile lf = new LinkFile();

        LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

        // File Header
        int b1 = is.read();
        int b2 = is.read();
        int b3 = is.read();
        int b4 = is.read();
        if (b1 != 'L' || b2 != 0 || b3 != 0 || b4 != 0) {
            throw new IllegalArgumentException("unknown header");
        }

        b1            = ledis.readInt();
        b2            = ledis.readInt();
        b3            = ledis.readInt();
        b4            = ledis.readInt();
        lf.guid       = b1 + b2 + b3 + b4;
        lf.flags      = ledis.readInt();
        lf.attributes = ledis.readInt();
        lf.time1      = ledis.readLong();
        lf.time2      = ledis.readLong();
        lf.time3      = ledis.readLong();
        lf.length     = ledis.readInt();
        lf.iconNumber = ledis.readInt();
        lf.showWnd    = ledis.readInt();
        lf.hotKey     = ledis.readInt();
        ledis.readInt();
        ledis.readInt();

logger.log(DEBUG, lf);

        // Shell item ID list
        if ((lf.flags & FLAG_THE_SHELL_ITEM_ID_LIST_IS_PRESENT) != 0) {
logger.log(DEBUG, "---- the shell item id list ----");
            int length = ledis.readShort();
logger.log(DEBUG, ": " + length);

            int len;
            while (true) {
                len = ledis.readShort();
                if (len == 0) {
                    break;
                }
                len -= 2;
logger.log(DEBUG, " len: " + len);
                byte[] buf = new byte[len];
                int l = 0;
                while (l < len) {
                    l += ledis.read(buf, l, len - l);
                }
if (buf[0] == 0x4c && buf[1] == 0x50) {
 boolean f2 = false;
 LittleEndianDataInputStream is2 =
  new LittleEndianDataInputStream(new ByteArrayInputStream(buf));
 is2.skipBytes(26);
 while (true) {
  String s2 = readString(is2);
  if (s2 == null && f2) {
   break;
  } else {
logger.log(DEBUG, " s2: " + s2);
  }
  s2 = readString(is2);
  if (s2 == null) {
   f2 = true;
  }
 }
} else {
 logger.log(DEBUG, StringUtil.getDump(buf));
}
            }

        }

        // has been read
        int read = 0;

        // File location info
        if ((lf.flags & FLAG_POINT_TO_A_FILE_OR_DIRECTORY) != 0) {
logger.log(DEBUG, "---- file location info ----");
            int length = ledis.readInt();
logger.log(DEBUG, ": " + length);

            int offset = ledis.readInt(); // 0x1c
            int flags = ledis.readInt();
            int offsetOfLocalVolumeInfo = ledis.readInt();
            int offsetOfBasePathnameOnLocalSystem = ledis.readInt();
            int offsetOfNetworkVolumeInfo = ledis.readInt();
            int offsetOfRemainingPathname = ledis.readInt();
logger.log(DEBUG, " offset: " + offset);
logger.log(DEBUG, " flags: " + flags);
logger.log(DEBUG, " offsetOfLocalVolumeInfo: " + offsetOfLocalVolumeInfo);
logger.log(DEBUG, " offsetOfBasePathnameOnLocalSystem: " + offsetOfBasePathnameOnLocalSystem);
logger.log(DEBUG, " offsetOfNetworkVolumeInfo: " + offsetOfNetworkVolumeInfo);
logger.log(DEBUG, " offsetOfRemainingPathname: " + offsetOfRemainingPathname);
            read += 4 * 7;

            if ((flags & FLAG_AVAILABLE_ON_A_LOCAL_VOLUME) != 0) {

                int s1_length = ledis.readInt();
                int s1_type = ledis.readInt();
                int s1_serial = ledis.readInt();
                int s1_offset = ledis.readInt(); // 0x10
                read += 4 * 4;
                byte[] buf = readAsciiz(is);
                read += buf.length + 1;
                String s1_label = new String(buf, "JISAutoDetect");
logger.log(DEBUG, " s1_length: " + s1_length);
logger.log(DEBUG, " s1_type: " + s1_type);
logger.log(DEBUG, " s1_serial: %08x".formatted(s1_serial));
logger.log(DEBUG, " s1_offset: " + s1_offset);
logger.log(DEBUG, " s1_label: " + s1_label);

                buf = readAsciiz(is);
                read += buf.length + 1;
                String s2_label = new String(buf, "JISAutoDetect");
logger.log(DEBUG, " s2_label: " + s2_label);

                lf.path = s1_label + (!s1_label.isEmpty() ? "\\" : "") + s2_label;
            }
            if ((flags & FLAG_AVAILABLE_ON_A_NETWORK_SHARE) != 0) {
                int s1_length = ledis.readInt();
                int s1_unknown = ledis.readInt();  // 0x02
                int s1_offset = ledis.readInt();   // 0x14
                int s1_unknown2 = ledis.readInt(); // 0
                int s1_unknown3 = ledis.readInt(); // 0x20000
                read += 4 * 5;
                byte[] buf = readAsciiz(is);
                read += buf.length + 1;
                String s1_name = new String(buf, "JISAutoDetect");
logger.log(DEBUG, " s1_length: " + s1_length);
logger.log(DEBUG, " s1_unknown: " + s1_unknown);
logger.log(DEBUG, " s1_offset: " + s1_offset);
logger.log(DEBUG, " s1_unknown2: " + s1_unknown2);
logger.log(DEBUG, " s1_unknown3: %08x".formatted(s1_unknown3));
logger.log(DEBUG, " s1_name: " + s1_name);

                buf = readAsciiz(is);
                read += buf.length + 1;
                String s2_label = new String(buf, "JISAutoDetect");
logger.log(DEBUG, " s2_label: " + s2_label);

                lf.path = s1_name + "\\" + s2_label;
            }

logger.log(DEBUG, " length - read: " + (length - read));
            ledis.skipBytes(length - read);
        }

        // Description string
        if ((lf.flags & FLAG_HAS_A_DESCRIPTION_STRING) != 0) {
logger.log(DEBUG, "---- description string ----");
logger.log(DEBUG, ": " + readString(ledis));
        }

        // Relative path string
        if ((lf.flags & FLAG_HAS_A_RELATIVE_PATH_STRING) != 0) {
logger.log(DEBUG, "---- relative path string ----");
logger.log(DEBUG, ": " + readString(ledis));
        }

        // Working directory string
        if ((lf.flags & FLAG_HAS_A_WORKING_DIRECTORY) != 0) {
logger.log(DEBUG, "---- working directory string ----");
logger.log(DEBUG, ": " + readString(ledis));
        }

        // Command line string
        if ((lf.flags & FLAG_HAS_COMMAND_LINE_ARGUMENTS) != 0) {
logger.log(DEBUG, "---- command line string ----");
logger.log(DEBUG, ": " + readString(ledis));
        }

        // Icon filename string
        if ((lf.flags & FLAG_HAS_A_CUSTOM_ICON) != 0) {
logger.log(DEBUG, "---- icon filename string ----");
logger.log(DEBUG, ": " + readString(ledis));
        }

        // Extra stuff
        int e1_length = ledis.readInt();
logger.log(DEBUG, "---- extra stuff: " + e1_length + " ----");
        byte[] buf = new byte[e1_length];
        int l = 0;
        while (l < e1_length) {
            l += is.read(buf, l, e1_length - l);
        }
if (e1_length != 0) {
 logger.log(DEBUG, StringUtil.getDump(buf));
}
        return lf;
    }

    /** */
    private static String readString(LittleEndianDataInputStream leis)
        throws IOException {

        int length = leis.readShort() * 2;
        if (length == 0) {
            return null;
        }
logger.log(DEBUG, "string len: " + length);
        byte[] buf = new byte[length];
        int l = 0;
        while (l < length) {
            l += leis.read(buf, l, length - l);
        }
        String result = new String(buf, StandardCharsets.UTF_16LE);
//        String result = CharConverter.toUTF8(buf);
        return result;
    }

    /** */
    private static byte[] readAsciiz(InputStream is)
        throws IOException {

        Vector<Integer> chars = new Vector<>();

        while (true) {
            int c = is.read();
            if (c == 0) {
                break;
            }
            chars.addElement(c);
        }

        byte[] buf = new byte[chars.size()];
        for (int i = 0; i < chars.size(); i++) {
            buf[i] = (byte) chars.elementAt(i).intValue();
        }

        return buf;
    }

    /** */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sb.append(super.toString()).append(" {");
        sb.append("guid: ").append(guid).append(", ");
        sb.append("flags: ").append(flags).append(", ");
        sb.append("attributes: ").append(attributes).append(", ");
        sb.append("time1: ").append(time1).append(": ").append("%02x".formatted(time1)).append(", ");
        sb.append("time1: ").append(sdf.format(new Date(DateUtil.filetimeToLong(time1)))).append(", ");
        sb.append("time2: ").append(time2).append(": ").append("%02x".formatted(time2)).append(", ");
        sb.append("time2: ").append(sdf.format(new Date(DateUtil.filetimeToLong(time2)))).append(", ");
        sb.append("time3: ").append(time3).append(": ").append("%16x".formatted(time3)).append(", ");
        sb.append("time3: ").append(sdf.format(new Date(DateUtil.filetimeToLong(time3)))).append(", ");
        sb.append("length: ").append(length).append(", ");
        sb.append("iconNumber: ").append(iconNumber).append(", ");
        sb.append("showWnd: ").append(showWnd).append(", ");
        sb.append("hotKey: ").append(hotKey).append("}");
        return sb.toString();
    }
}
