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

import vavi.beans.AdvancedBinder;
import vavi.beans.Binder;


/**
 * Property.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
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
     * default value for this property.
     */
    String value() default "";

    /**
     * フィールドに値を代入する実装クラス
     */
    Class<? extends Binder> binder() default AdvancedBinder.class;

    /**
     * TODO アノテーションがメソッド指定の場合
     */
    class Util {

        private Util() {
        }

        /**
         * @param field {@link Property} annotated
         * @param args TODO not used???
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
            return name;
        }

        /**
         * @param field {@link Property} annotated
         */
        public static String getValue(Field field) {
            Property target = field.getAnnotation(Property.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }

            return target.value();
        }

        /**
         * @param field @{@link Property} annotated field.
         */
        public static <T> Binder getBinder(Field field) {
            Property target = field.getAnnotation(Property.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }

            try {
                Binder binder = target.binder().getDeclaredConstructor().newInstance();
                return binder;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}

/* */
