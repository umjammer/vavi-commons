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
    /** */
    String url();

    /**
     * when true use {@link Class#getResourceAsStream(String)}, else use {@link URL#openStream()} 
     */
    boolean resource() default false;

    /** */
    class Util {

        /** */
        public static InputStream getInputStream(Object bean) throws IOException {
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }
            if (propsEntity.resource()) {
System.err.println(propsEntity.url());
                return bean.getClass().getResourceAsStream(propsEntity.url());
            } else {
                return new URL(propsEntity.url()).openStream();
            }
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
         * @return UTF-8 URL encoded 
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
