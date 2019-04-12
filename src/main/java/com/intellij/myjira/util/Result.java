package com.intellij.myjira.util;

public interface Result<T> {

    boolean isValid();


    T get();

}
