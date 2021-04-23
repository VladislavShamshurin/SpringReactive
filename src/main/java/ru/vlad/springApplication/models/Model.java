package ru.vlad.springApplication.models;


public interface Model<K> {

    K getId();

    void setId(K id);
}
