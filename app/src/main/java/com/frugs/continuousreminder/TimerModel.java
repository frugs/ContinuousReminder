package com.frugs.continuousreminder;

import java.util.ArrayList;
import java.util.List;

public class TimerModel {

    private final TimeProvider mTimeProvider;
    private List<TimerListener> mListeners;

    private boolean mStarted = false;
    private long mStartTime = 0;
    private long mTimerValue = 0;

    public TimerModel(List<TimerListener> mListeners, TimeProvider timeProvider) {
        mTimeProvider = timeProvider;
        this.mListeners = new ArrayList<>(mListeners);
    }

    public void setTimerValue(long value) {
        mTimerValue = value;
    }

    public void start() {
        mStarted = true;
        mStartTime = mTimeProvider.getCurrentSystemTimeMillis();
    }

    public void stop() {
        mStarted = false;
    }

    public void update() {
        if (mStarted) {
            long currentTime = mTimeProvider.getCurrentSystemTimeMillis();
            if (currentTime - mStartTime > mTimerValue) {
                for (TimerListener listener : mListeners) {
                    listener.onTimer();
                }
                mStartTime = currentTime;
            }
        }
    }
}
