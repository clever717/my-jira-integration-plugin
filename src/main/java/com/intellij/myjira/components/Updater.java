package com.intellij.myjira.components;

import java.util.List;

public interface Updater<T> {

    void update(List<T> t);

    void update(T t);

}
