/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.event;

import java.io.Serializable;
import javax.swing.event.EventListenerList;


/**
 * 汎用イベント機構のの基本実装クラスです．
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class GenericSupport implements Serializable {

    /** The generic listeners */
    private EventListenerList listenerList = new EventListenerList();

    /** GenericListener を追加します． */
    public void addGenericListener(GenericListener l) {
        listenerList.add(GenericListener.class, l);
    }

    /** GenericListener を削除します． */
    public void removeGenericListener(GenericListener l) {
        listenerList.remove(GenericListener.class, l);
    }

    /** 汎用イベントを発行します． */
    public void fireEventHappened(GenericEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == GenericListener.class) {
                ((GenericListener) listeners[i + 1]).eventHappened(ev);
            }
        }
    }
}

/* */
