package com.frugs.continuousreminder;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerRunner implements TimerController {

    private final ScheduledExecutorService mExecutorService;
    private final TimerModel mModel;
    private final Runnable mUpdateTimerTask;

    public TimerRunner(ScheduledExecutorService executorService, TimerModel model) {
        mExecutorService = executorService;
        mModel = model;
        mUpdateTimerTask = new UpdateTimerTask(mModel);
    }

    @Override
    public void start() {
        mExecutorService.scheduleAtFixedRate(mUpdateTimerTask, 0, 1, TimeUnit.SECONDS);
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mModel.start();
            }
        });
    }

    @Override
    public void stop() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mModel.stop();
            }
        });
    }

    @Override
    public void setTimerValue(final long value) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mModel.setTimerValue(value);
            }
        });
    }

    private class UpdateTimerTask implements Runnable {
        private final TimerModel mModel;

        public UpdateTimerTask(TimerModel model) {
            mModel = model;
        }

        @Override
        public void run() {
            mModel.update();
        }
    }
}
