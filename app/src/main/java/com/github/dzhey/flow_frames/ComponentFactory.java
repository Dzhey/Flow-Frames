package com.github.dzhey.flow_frames;

public interface ComponentFactory<T> {
    Object createComponent(T parent);
}
