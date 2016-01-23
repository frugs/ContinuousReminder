package com.frugs.continuousreminder;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends WearableActivity {

    private static final int DEFAULT_INTERVAL_SECONDS = 25;

    private BoxInsetLayout mContainerView;
    private TextView mSwitchLabelTextView;
    private TextView mIntervalPickerTextView;

    private TimerController mTimerController;
    private ScheduledExecutorService mTimerRunnerExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mSwitchLabelTextView = (TextView) findViewById(R.id.switch_label);
        mIntervalPickerTextView = (TextView) findViewById(R.id.interval_picker_label);

        final Executor mainThreadExecutor = new Executor() {
            @Override
            public void execute(@NonNull Runnable command) {
                runOnUiThread(command);
            }
        };

        mTimerRunnerExecutor = new ScheduledThreadPoolExecutor(1);

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        final VibrationController vibrationController =
                new VibratorProxy(new SingleVibrationVibrator(vibrator), mainThreadExecutor);

        final TimerListener timedVibrator = new TimedVibrator(vibrationController);
        final TimerModel timerModel =
                new TimerModel(Collections.singletonList(timedVibrator), new SystemTimeProvider());
        mTimerController = new TimerRunner(mTimerRunnerExecutor, timerModel);

        final Switch timerSwitch = (Switch) findViewById(R.id.timer_switch);
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTimerController.start();
                    mSwitchLabelTextView.setText(R.string.switch_on);
                } else {
                    mTimerController.stop();
                    mSwitchLabelTextView.setText(R.string.switch_off);
                }
            }
        });

        final NumberPicker intervalPicker = (NumberPicker) findViewById(R.id.interval_picker);
        intervalPicker.setMinValue(0);
        intervalPicker.setMaxValue(999);
        intervalPicker.setValue(DEFAULT_INTERVAL_SECONDS);
        intervalPicker.setWrapSelectorWheel(false);
        intervalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mTimerController.setTimerValue(newVal * 1000);
            }
        });

        mTimerController.setTimerValue(DEFAULT_INTERVAL_SECONDS * 1000);
    }

    @Override
    protected void onDestroy() {
        mTimerController.stop();
        mTimerRunnerExecutor.shutdown();
        super.onDestroy();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mSwitchLabelTextView.setTextColor(getResources().getColor(android.R.color.white));
            mIntervalPickerTextView.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            mContainerView.setBackground(null);
            mSwitchLabelTextView.setTextColor(getResources().getColor(android.R.color.black));
            mIntervalPickerTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }
}
