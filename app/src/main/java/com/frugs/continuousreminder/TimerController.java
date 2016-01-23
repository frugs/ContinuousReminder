package com.frugs.continuousreminder;

public interface TimerController {

    void start();

    void stop();

    void setTimerValue(long value);
}
