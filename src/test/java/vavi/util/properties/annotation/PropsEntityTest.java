/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * PropsEntityTest.
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2010/10/08 nsano initial version <br>
 */
@PropsEntity(url = "classpath:vavi/util/properties/annotation/propsEntityTest.properties")
public class PropsEntityTest {

    @Property(name = "data1")
    private String data1;

    @Property(name = "data2")
    private int data2;

    @Property
    private String data3;

    public String toString() {
        return "data1: " + data1 +
               ", data2: " + data2 +
               ", data3: " + data3;
    }

    public static void main(String[] args) throws Exception {
        PropsEntityTest bean = new PropsEntityTest();
        PropsEntity.Util.bind(bean);
        System.err.println(bean);
    }

    @Test
    public void test01() throws Exception {
        PropsEntityTest bean = new PropsEntityTest();
        PropsEntity.Util.bind(bean);
        assertEquals("Sano Naohide", bean.data1);
        assertEquals(40, bean.data2);
        assertEquals("Umjammer", bean.data3);
    }

    @PropsEntity(url = "file://${HOME}/.vavi-commons")
    public static class Test2 {
        @Property(name = "test.data")
        private String data;
    }

    @Test
    public void test02() throws Exception {
        File file = new File(System.getenv("HOME"), ".vavi-commons");
        FileWriter fw = new FileWriter(file);
        fw.write("test.data=Hello\n");
        fw.close();
        Test2 bean = new Test2();
        PropsEntity.Util.bind(bean);
        assertEquals("Hello", bean.data);
        fw.close();
        file.delete();
    }

    /** just test regex */
    @Test
    public void test03() {
        Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
        Matcher matcher = pattern.matcher("file://${HOME}/.vavi-commons");
        if (matcher.find()) {
            assertEquals("${HOME}", matcher.group());
        } else {
            fail("no match");
        }
    }

    @PropsEntity(url = "file://${user.dir}/local.properties.sample")
    public static class Test4 {
        @Property(name = "dir.lib")
        private String data1;
    }

    @Test
    public void test04() throws Exception {
        Test4 bean = new Test4();
        PropsEntity.Util.bind(bean);
        assertEquals("lib", bean.data1);
    }

    @PropsEntity(url = "file://${user.dir}/local.properties.{1}") // {1} this is not proper usage! just for test
    public static class Test5 {
        @Property(name = "{0}") // {0} this is not proper usage! just for test
        private String data1;
    }

    @Test
    public void test05() throws Exception {
        Test5 bean = new Test5();
        PropsEntity.Util.bind(bean, "dir.lib", "sample");
        assertEquals("lib", bean.data1);
    }
}

/* */
