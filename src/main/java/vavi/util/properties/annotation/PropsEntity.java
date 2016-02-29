/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vavi.beans.DefaultBinder;


/**
 * PropsEntity. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2010/10/08 nsano initial version <br>
 */
@java.lang.annotation.Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropsEntity {

    /** 
     * If you use <code>classpath</code> schema, set <code>-Djava.protocol.handler.pkgs=vavi.net.protocol</code>
     * 
     * <code>${Foo}</code> is replaced with <code>System.getProperty("Foo")</code> or <code>System.getenv("Foo")</code>.
     * <p>
     * ex.
     * <pre>
     *  "/foo.properties" 
     *  "classpath:your/package/foo.properties" 
     * </pre>
     * </p>
     * @see {@link vavi.net.protocol.classpath.Handler}
     */
    String url();

    /** */
    class Util {

        /** replacing key pattern */
        private static final Pattern pattern = Pattern.compile("\\$\\{[\\w\\.]+\\}"); 

        /** */
        public static InputStream getInputStream(Object bean) throws IOException {
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            String url = replaceWithEnvOrProps(propsEntity.url());

//System.err.println("url: finally: " + url);
            return new URL(url).openStream();
        }

        /**
         * Replaces <code>${Foo}</code> with <code>System.getProperty("Foo")</code> or <code>System.getenv("Foo")</code>.
         */
        private static String replaceWithEnvOrProps(String url) {
//System.err.println("url: origin: " + url);
            Matcher matcher = pattern.matcher(url);
            while (matcher.find()) {
               String key = matcher.group();
               String name = key.substring(2, key.length() - 1);
               String value = System.getenv(name);
               if (value == null) {
                   value = System.getProperty(name);
                   if (value == null) {
                       System.err.println(key + " is not replaceable");
                       continue;
                   }
//else {
// System.err.println("url: replaced with props: " + value);
//}
               }
//else {
// System.err.println("url: replaced with env: " + value);
//}
               url = url.replace(key, value);
//System.err.println("url: replace: " + key + ": " + url);
            }
            return url;
        }

        /**
         * @return {@link PropsEntity} annotated fields
         */
        public static Set<Field> getPropertyFields(Object bean) {
            //
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            //
            Set<Field> propertyFields = new HashSet<Field>(); 

            for (Field field : bean.getClass().getDeclaredFields()) {
                Property property = field.getAnnotation(Property.class);
                if (property != null) {
                    propertyFields.add(field);
                }
            }

            return propertyFields;
        }

        /**
         * Entry point.
         */
        public static void bind(Object bean) throws IOException {
            //
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            Properties props = new Properties();
            props.load(PropsEntity.Util.getInputStream(bean));

            DefaultBinder binder = new DefaultBinder();

            //
            for (Field field : getPropertyFields(bean)) {
                String name = Property.Util.getName(field);
                String value = props.getProperty(name);
System.err.println("value: " + name + ", " + value);
                binder.bind(bean, field, field.getType(), value, value); // TODO elseValue is used for type String
            }
        }
    }
}

/* */
