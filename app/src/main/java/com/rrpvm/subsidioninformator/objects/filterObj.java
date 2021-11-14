package com.rrpvm.subsidioninformator.objects;

public class filterObj<T> {
    public T object;
    public RecivierFilter.statement state;
    public filterObj(){
        this.state = RecivierFilter.statement.CLEAR;
    }
}
