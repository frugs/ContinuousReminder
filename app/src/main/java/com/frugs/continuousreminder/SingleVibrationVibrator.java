package com.frugs.continuousreminder;

import android.os.Vibrator;

public class SingleVibrationVibrator implements VibrationController {
    private static final long DURATION = 700;

    private final Vibrator mVibrator;

    public SingleVibrationVibrator(Vibrator mVibrator) {
        this.mVibrator = mVibrator;
    }

    @Override
    public void vibrate() {
        mVibrator.vibrate(DURATION);
    }
}
