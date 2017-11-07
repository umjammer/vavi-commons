/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.event;

import java.util.EventListener;


/**
 * 汎用のリスナインターフェースです．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
@FunctionalInterface
public interface GenericListener extends EventListener {

    /**
     * イベントが発行された時に呼ばれます．
     *
     * @param ev 汎用イベント
     */
    void eventHappened(GenericEvent ev);
}

/* */
