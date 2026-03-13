package com.pomodoro.logic;

import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PomodoroTimer {

    private int durationMinutes = 25;
    private int secondsLeft;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private Task activeTask;

    private final Timer timer = new Timer(true);
    private TimerTask scheduledTimerTask;

    private Consumer<String> onTick;
    private Runnable onFinish;
    private Runnable onStatusChange;
    private Runnable onFocusMinuteAdded;

    // นับวินาทีเอาไว้เพิ่มเวลาตอนทำงานทุกการครบหกสิบวินาที
    private int secondsInCurrentMinute = 0;

    public PomodoroTimer(Consumer<String> onTick, Runnable onFinish, Runnable onStatusChange) {
        this.onTick = onTick;
        this.onFinish = onFinish;
        this.onStatusChange = onStatusChange;
        this.secondsLeft = durationMinutes * 60;
        updateTimerDisplay();
    }

    // กำหนดคำสั่งที่จะถูกเรียกใช้ทุกครั้งที่ใช้เวลาทำงานครบหนึ่งนาที
    public void setOnFocusMinuteAdded(Runnable onFocusMinuteAdded) {
        this.onFocusMinuteAdded = onFocusMinuteAdded;
    }

    // ทำงานทุกหนึ่งวินาทีเพื่อนับเวลาถอยหลังและเก็บสถิติ
    private void tick() {
        if (secondsLeft > 0) {
            secondsLeft--;
            updateTimerDisplay();
            if (activeTask != null) {
                secondsInCurrentMinute++;
                if (secondsInCurrentMinute >= 60) {
                    activeTask.addFocusMinutes(1);
                    secondsInCurrentMinute = 0;
                    if (onFocusMinuteAdded != null) {
                        onFocusMinuteAdded.run();
                    }
                }
            }
        } else {
            completeSession();
        }
    }

    // ทำงานตอนเวลานับถอยหลังหมด จากนั้นจะเปลี่ยนสถานะงานว่าเสร็จแล้วและแจ้งหน้าต่างผลลัพธ์
    private void completeSession() {
        clearRunningState();
        if (activeTask != null) {
            activeTask.setStatus(TaskStatus.DONE);
        }
        if (onFinish != null) {
            onFinish.run();
        }
        notifyStatusChange();
    }

    // ยกเลิกระบบนับเวลาถอยหลังที่กำลังทำงานค้างไว้อยู่
    private void cancelScheduledTask() {
        if (scheduledTimerTask != null) {
            scheduledTimerTask.cancel();
            scheduledTimerTask = null;
        }
    }

    // สร้างและรันตัวจับเวลาอันใหม่ที่จะทำงานทุกหนึ่งวินาที
    private void scheduleTimer() {
        cancelScheduledTask();
        scheduledTimerTask = new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        };
        timer.schedule(scheduledTimerTask, 1000, 1000);
    }

    // หยุดตัวจับเวลาล้างระบบว่าไม่ได้ทำงานหรือหยุดพักอยู่พร้อมกับคืนเวลาเดิม
    private void clearRunningState() {
        isRunning = false;
        isPaused = false;
        cancelScheduledTask();
    }

    // ส่งเวลาไปแสดงผลที่หน้าจอผ่านตัวรับ
    private void updateTimerDisplay() {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        if (onTick != null) {
            onTick.accept(String.format("%02d:%02d", minutes, seconds));
        }
    }

    public void start(Task task) {
        if (task == null) {
            return;
        }
        // ถ้ากดหยุดพักอยู่และเป็นงานเดิมก็ให้กลับมาทำต่อจากเวลาที่เหลือค้างไว้
        if (isPaused && this.activeTask == task) {
            resume();
            return;
        }
        clearRunningState();
        secondsInCurrentMinute = 0;
        this.activeTask = task;
        this.activeTask.setStatus(TaskStatus.IN_PROGRESS);
        // ถ้างานมีเวลาค้างเหลือไว้ให้ดึงมาทำต่อจากตรงนั้นอย่ารีเซ็ตใหม่
        if (task.getRemainingSeconds() > 0) {
            secondsLeft = task.getRemainingSeconds();
        } else {
            secondsLeft = durationMinutes * 60;
        }
        updateTimerDisplay();
        isRunning = true;
        isPaused = false;
        scheduleTimer();
        notifyStatusChange();
    }

    public void pause() {
        if (isRunning) {
            isRunning = false;
            isPaused = true;
            cancelScheduledTask();
            if (onFocusMinuteAdded != null) {
                onFocusMinuteAdded.run();
            }
            notifyStatusChange();
        }
    }

    public void resume() {
        if (isPaused && activeTask != null) {
            isRunning = true;
            isPaused = false;
            scheduleTimer();
            notifyStatusChange();
        }
    }

    public void stop() {
        if (activeTask != null) {
            activeTask.setRemainingSeconds(secondsLeft);
            activeTask.setStatus(TaskStatus.TODO);
            if (onFocusMinuteAdded != null) {
                onFocusMinuteAdded.run();
            }
        }
        clearRunningState();
        resetTime();
        secondsInCurrentMinute = 0;
        activeTask = null;
        notifyStatusChange();
    }

    private void notifyStatusChange() {
        if (onStatusChange != null) {
            onStatusChange.run();
        }
    }

    public void setDuration(int minutes) {
        this.durationMinutes = minutes;
        if (!isRunning && !isPaused) {
            resetTime();
        }
    }

    private void resetTime() {
        this.secondsLeft = durationMinutes * 60;
        updateTimerDisplay();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public Task getActiveTask() {
        return activeTask;
    }

    public void shutdown() {
        cancelScheduledTask();
        timer.cancel();
    }
}