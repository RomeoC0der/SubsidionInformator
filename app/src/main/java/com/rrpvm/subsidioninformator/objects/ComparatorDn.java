package com.rrpvm.subsidioninformator.objects;

import java.util.Comparator;

public class ComparatorDn implements Comparator<SubsidingRecivier> {
    @Override
    public int compare(SubsidingRecivier subsidingRecivier, SubsidingRecivier t1) {
        return t1.getPIB().compareTo(subsidingRecivier.getPIB());
    }
}

