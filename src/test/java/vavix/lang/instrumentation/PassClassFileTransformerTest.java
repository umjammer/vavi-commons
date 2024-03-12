/*
 * Copyright (c) 2011 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * PassClassFileTransformerTest.
 * <p>
 * <pre>
 * -javaagent:../../vavi/vavi-commons-sandbox/vavi-instrumentation.jar
 * -Dvavix.lang.instrumentation.VaviInstrumentation.5=vavix.lang.instrumentation.PropertiesClassFileTransformer
 * -Dvavix.lang.instrumentation.VaviInstrumentation.6=vavix.lang.instrumentation.PropertiesClassFileTransformer
 * -D$vavix.lang.instrumentation.VaviInstrumentation.2=vavix.lang.instrumentation.PropertiesClassFileTransformer
 * -D$vavix.lang.instrumentation.VaviInstrumentation.4=vavix.lang.instrumentation.ToStringClassFileTransformer
 * -Dvavix.lang.instrumentation.ToStringClassFileTransformer.4.pattern=org/benf/cfr/reader/entities/constantpool/ConstantPoolEntryString
 * -Dvavix.lang.instrumentation.ToStringClassFileTransformer.4.body="{ return vavi.util.StringUtil.paramStringDeep(this,1); }"
 * </pre>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2011/08/23 umjammer initial version <br>
 */
public class PassClassFileTransformerTest {

    @Test
    public void test01() {
        assertEquals("jp.noids.image.scaling.gui.l", PassClassFileTransformer.normalize("jp.noids.image.scaling.gui.MugenSampleDialog$N$l"));
    }
}
