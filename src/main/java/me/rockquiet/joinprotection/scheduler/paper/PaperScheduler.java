package me.rockquiet.joinprotection.scheduler.paper;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.scheduler.PlatformScheduler;
import me.rockquiet.joinprotection.scheduler.PlatformTask;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PaperScheduler extends PlatformScheduler {

    private final AsyncScheduler asyncScheduler;

    public PaperScheduler(JoinProtection plugin) {
        super(plugin);
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
    }

    public void runTimerOnEntity(@NotNull Entity entity, @NotNull Consumer<? super PlatformTask> task, long delay, long period) {
        long finalDelay = Math.max(delay, 1L);
        new PaperTask(entity.getScheduler().runAtFixedRate(getPlugin(), scheduledTask -> task.accept(new PaperTask(scheduledTask)), null, finalDelay, period));
    }

    @Override
    public void runAsync(@NotNull Runnable runnable) {
        asyncScheduler.runNow(getPlugin(), scheduledTask -> runnable.run());
    }
}
