package com.rrpvm.subsidioninformator.objects;

import java.util.Comparator;

public class ComparatorUp implements Comparator<SubsidingRecivier> {
    @Override
    public int compare(SubsidingRecivier subsidingRecivier, SubsidingRecivier t1) {
        return subsidingRecivier.getPIB().compareTo(t1.getPIB());
    }
}
