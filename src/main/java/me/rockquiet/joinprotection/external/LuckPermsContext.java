package me.rockquiet.joinprotection.external;

import me.rockquiet.joinprotection.ProtectionHandler;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LuckPermsContext implements ContextCalculator<Player> {

    private final ProtectionHandler protectionHandler;

    public LuckPermsContext(ProtectionHandler protectionHandler) {
        this.protectionHandler = protectionHandler;
    }

    @Override
    public void calculate(@NonNull Player target, @NonNull ContextConsumer consumer) {
        consumer.accept("joinprotection", protectionHandler.hasProtection(target.getUniqueId()) ? "true" : "false");
    }

    @Override
    public @NonNull ContextSet estimatePotentialContexts() {
        return ImmutableContextSet.builder()
                .add("joinprotection", "true")
                .add("joinprotection", "false")
                .build();
    }
}
