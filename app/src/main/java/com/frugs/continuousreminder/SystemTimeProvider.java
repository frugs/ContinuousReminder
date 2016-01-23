package com.frugs.continuousreminder;

import android.os.SystemClock;

public class SystemTimeProvider implements TimeProvider {
    @Override
    public long getCurrentSystemTimeMillis() {
        return SystemClock.elapsedRealtime();
    }
}
