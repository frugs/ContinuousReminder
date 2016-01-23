package com.frugs.continuousreminder;

import java.util.concurrent.Executor;

public class VibratorProxy implements VibrationController {

    private final VibrationController mVibrator;
    private final Executor mExecutor;

    public VibratorProxy(VibrationController vibrator, Executor executor) {
        mVibrator = vibrator;
        mExecutor = executor;
    }

    @Override
    public void vibrate() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mVibrator.vibrate();
            }
        });
    }
}
