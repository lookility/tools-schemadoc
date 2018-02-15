package com.lookility.schemadoc.ui;

import java.util.Optional;

public interface SelectedPathChangedListener {

    public void changed(Optional<String> path);
}
