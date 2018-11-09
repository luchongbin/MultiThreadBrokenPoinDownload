package com.aspsine.multithreaddownload.util;

import java.util.List;

/**
 * Created by luchongbin on 2018/10/30.
 */
public class ListUtils {

    public static final boolean isEmpty(List list) {
        return !(list != null && list.size() > 0);
    }
}
