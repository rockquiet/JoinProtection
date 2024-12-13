package me.rockquiet.joinprotection.scheduler.paper;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.rockquiet.joinprotection.scheduler.PlatformTask;

public class PaperTask implements PlatformTask {

    private final ScheduledTask task;

    public PaperTask(ScheduledTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        task.cancel();
    }
}
