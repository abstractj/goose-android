package org.abstractj.goose;

import java.io.Serializable;

public interface Callback<T> extends Serializable {

    void onSuccess(T data);

    void onFailure(Exception e);

}