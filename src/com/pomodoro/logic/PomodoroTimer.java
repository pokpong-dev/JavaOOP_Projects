package com.pomodoro.logic;

import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;

import javax.swing.Timer;
import java.util.function.Consumer;

public class PomodoroTimer {
    private int durationMinutes = 25;
    private int secondsLeft;
    private boolean isRunning = false;
    private boolean isPaused = false;

    private Task activeTask;
    private Timer timer;

    private Consumer<String> onTick;
    private Runnable onFinish;
    private Runnable onStatusChange;

    private int sessionSeconds = 0;

    public PomodoroTimer(Consumer<String> onTick, Runnable onFinish, Runnable onStatusChange) {
        this.onTick = onTick;
        this.onFinish = onFinish;
        this.onStatusChange = onStatusChange;
        this.secondsLeft = durationMinutes * 60;

        timer = new Timer(1000, e -> tick());
        updateTime();
    }

    private void tick() {
        if (secondsLeft > 0) {
            secondsLeft--;
            updateTime();

            if (activeTask != null) {
                sessionSeconds++;
                if (sessionSeconds >= 60) {
                    activeTask.addFocusMinutes(1);
                    sessionSeconds = 0;
                }
            }
        } else {
            completeSession();
        }
    }

    private void completeSession() {
        stopTimerInternal();
        if (activeTask != null) {
            activeTask.setStatus(TaskStatus.DONE);
        }
        if (onFinish != null) onFinish.run();
        notifyStatusChange();
    }

    private void stopTimerInternal() {
        isRunning = false;
        isPaused = false;
        timer.stop();
    }

    private void updateTime() {
        int m = secondsLeft / 60;
        int s = secondsLeft % 60;
        if (onTick != null) {
            onTick.accept(String.format("%02d:%02d", m, s));
        }
    }

    public void start(Task task) {
        if (task == null) return;

        if (isPaused && this.activeTask == task) {
            resume();
            return;
        }

        stop();
        this.activeTask = task;
        this.activeTask.setStatus(TaskStatus.IN_PROGRESS);

        isRunning = true;
        isPaused = false;
        timer.start();
        notifyStatusChange();
    }

    public void pause() {
        if (isRunning) {
            isRunning = false;
            isPaused = true;
            timer.stop();
            notifyStatusChange();
        }
    }

    public void resume() {
        if (isPaused && activeTask != null) {
            isRunning = true;
            isPaused = false;
            timer.start();
            notifyStatusChange();
        }
    }

    public void stop() {
        stopTimerInternal();
        resetTime();
        sessionSeconds = 0;
        notifyStatusChange();
    }

    private void notifyStatusChange() {
        if (onStatusChange != null) onStatusChange.run();
    }

    public void setDuration(int minutes) {
        this.durationMinutes = minutes;
        if (!isRunning && !isPaused) {
            resetTime();
        }
    }

    private void resetTime() {
        this.secondsLeft = durationMinutes * 60;
        updateTime();
    }

    public boolean isRunning() { return isRunning; }
    public boolean isPaused() { return isPaused; }
    public Task getActiveTask() { return activeTask; }
}
