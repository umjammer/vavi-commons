/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.util.Collection;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class ResourceListTest {

    @Test
    void test() {
        Collection<String> list = ResourceList.getResources(Pattern.compile(".+\\.properties"));
list.forEach(System.out::println);
    }


    /**
     * list the resources that match args[0]
     *
     * @param args
     *            args[0] is the pattern to match, or list all resources if
     *            there are no args
     */
    public static void main(final String[] args){
        Pattern pattern;
        if(args.length < 1){
            pattern = Pattern.compile(".*");
        } else{
            pattern = Pattern.compile(args[0]);
        }
        Collection<String> list = ResourceList.getResources(pattern);
list.forEach(System.out::println);
    }
}
