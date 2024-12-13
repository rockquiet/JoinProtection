package me.rockquiet.joinprotection.scheduler.spigot;

import me.rockquiet.joinprotection.scheduler.PlatformTask;
import org.bukkit.scheduler.BukkitTask;

public class SpigotTask implements PlatformTask  {

    private final BukkitTask task;

    public SpigotTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        task.cancel();
    }
}
