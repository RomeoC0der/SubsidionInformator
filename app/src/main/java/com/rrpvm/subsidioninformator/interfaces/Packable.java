package com.rrpvm.subsidioninformator.interfaces;

import android.content.Context;

public interface Packable {
    void importFromJSON(Context ctx);
    void exportToJSON(Context ctx);
}
