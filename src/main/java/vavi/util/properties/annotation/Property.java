/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;


/**
 * Property. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2010/09/30 nsano initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * Property name. 
     * When this is not set, the field name will be used.
     * <code>{#}</code> (# is 0, 1, 2...) will be replaced by parameters (String...) of {@link PropsEntity.Util#bind(Object, String...)}
     */
    String name() default "";

    /**
     * TODO アノテーションがメソッド指定の場合 
     */
    class Util {

        /**
         * @param field {@link Property} annotated
         * @return When {@link Property#name()} is not set, the field name will be return.
         */
        public static String getName(Field field, String... args) {
            Property target = field.getAnnotation(Property.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }
            String name = target.name();
            if (name.isEmpty()) {
                name = field.getName();
            }
            
//System.err.println("before: " + name);
            name = replaceWithArgs(name, args);
            
//System.err.println("after: " + name);
            return name;
        }

        /**
         * <code>{#}</code> (# is 0, 1, 2...) is replaced by parameters args.
         */
        private static String replaceWithArgs(String name, String... args) {
            for (int i = 0; i < args.length; i++) {
                String key = "{" + i + "}";
                name = name.replace(key, args[i]);
//System.err.println("replace: " + name + ", " + key + ", " + args[i]);
            }
            return name;
        }
    }
}

/* */
