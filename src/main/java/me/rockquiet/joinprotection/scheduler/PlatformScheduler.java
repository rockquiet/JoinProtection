package me.rockquiet.joinprotection.scheduler;

import me.rockquiet.joinprotection.JoinProtection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class PlatformScheduler {

    private final JoinProtection plugin;

    protected PlatformScheduler(JoinProtection plugin) {
        this.plugin = plugin;
    }

    public JoinProtection getPlugin() {
        return plugin;
    }

    public abstract void runTimerOnEntity(@NotNull Entity entity, @NotNull Consumer<? super PlatformTask> task, long delay, long period);

    public abstract void runAsync(@NotNull Runnable runnable);
}
