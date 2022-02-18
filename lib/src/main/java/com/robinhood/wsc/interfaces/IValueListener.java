package com.robinhood.wsc.interfaces;

public interface IValueListener<T> {
    void value(T result);
    void failed();
}
