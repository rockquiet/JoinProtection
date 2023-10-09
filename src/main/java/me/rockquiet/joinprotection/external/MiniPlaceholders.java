package me.rockquiet.joinprotection.external;

import io.github.miniplaceholders.api.Expansion;
import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.entity.Player;

public class MiniPlaceholders {

    private final JoinProtection plugin;
    private final ProtectionHandler protectionHandler;

    public MiniPlaceholders(JoinProtection plugin,
                            ProtectionHandler protectionHandler) {
        this.plugin = plugin;
        this.protectionHandler = protectionHandler;
    }

    public void registerExpansion() {
        final Expansion expansion = Expansion.builder("joinprotection")
                .filter(Player.class)
                .audiencePlaceholder("status", (audience, queue, ctx) -> {
                    final Player player = (Player) audience;
                    if (protectionHandler.hasProtection(player.getUniqueId())) {
                        return Tag.selfClosingInserting(Component.text(plugin.getConfig().getString("integration.miniplaceholders.status.protected")));
                    } else {
                        return Tag.selfClosingInserting(Component.text(plugin.getConfig().getString("integration.miniplaceholders.status.not-protected")));
                    }
                })
                .build();

        expansion.register();
    }
}
