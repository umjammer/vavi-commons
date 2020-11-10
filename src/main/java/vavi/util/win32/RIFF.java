/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.win32;


/**
 * Resource Interchange File Format.
 *
 * <pre>
 * off len dsc
 * 00  04  "RIFF"
 * 04  04  chunk length (*1)
 * 08  04  chunk type
 * 0c (*1) chunk data ...
 * </pre>
 *
 * @target    1.1
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020424 nsano initial version <br>
 *          0.10 020507 nsano repackage <br>
 *          0.11 020507 nsano refine <br>
 *          0.10 020707 nsano refine <br>
 *          1.00 030121 nsano refactoring <br>
 *          1.01 030606 nsano show error in #readFrom <br>
 *          1.10 030711 nsano add setChildData() <br>
 *          1.11 030711 nsano deprecate setChildData() <br>
 */
public class RIFF extends MultipartChunk {

}

/* */
