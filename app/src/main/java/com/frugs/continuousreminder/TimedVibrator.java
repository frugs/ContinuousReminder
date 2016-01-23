package com.frugs.continuousreminder;

public class TimedVibrator implements TimerListener {

    private final VibrationController mVibrationController;

    public TimedVibrator(VibrationController vibrationController) {
        this.mVibrationController = vibrationController;
    }

    @Override
    public void onTimer() {
        mVibrationController.vibrate();
    }
}
