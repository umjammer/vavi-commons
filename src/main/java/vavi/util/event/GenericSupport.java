/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 汎用イベント機構のの基本実装クラスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030318 nsano initial version <br>
 */
public class GenericSupport implements Serializable {

    /** The generic listeners */
    private List<GenericListener> listeners = new ArrayList<>();

    /** GenericListener を追加します． */
    public void addGenericListener(GenericListener l) {
        listeners.add(l);
    }

    /** GenericListener を削除します． */
    public void removeGenericListener(GenericListener l) {
        listeners.remove(l);
    }

    /** 汎用イベントを発行します． */
    public void fireEventHappened(GenericEvent ev) {
        for (GenericListener listener : listeners) {
            listener.eventHappened(ev);
        }
    }
}
