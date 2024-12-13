package me.rockquiet.joinprotection.scheduler.spigot;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.scheduler.PlatformScheduler;
import me.rockquiet.joinprotection.scheduler.PlatformTask;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SpigotScheduler extends PlatformScheduler {

    private final BukkitScheduler scheduler;

    public SpigotScheduler(JoinProtection plugin) {
        super(plugin);
        this.scheduler = plugin.getServer().getScheduler();
    }

    private void runTaskTimer(@NotNull Consumer<? super PlatformTask> task, long delay, long period) {
        scheduler.runTaskTimer(getPlugin(), bukkitTask -> task.accept(new SpigotTask(bukkitTask)), delay, period);
    }

    @Override
    public void runTimerOnEntity(@NotNull Entity entity, @NotNull Consumer<? super PlatformTask> task, long delay, long period) {
        runTaskTimer(task, delay, period);
    }

    @Override
    public void runAsync(@NotNull Runnable runnable) {
        scheduler.runTaskAsynchronously(getPlugin(), runnable);
    }
}
