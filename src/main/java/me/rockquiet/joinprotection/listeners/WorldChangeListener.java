package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.Objects;

public class WorldChangeListener implements Listener {

    private final JoinProtection plugin;
    private final ProtectionHandler protectionHandler;

    public WorldChangeListener(JoinProtection plugin, ProtectionHandler protectionHandler) {
        this.plugin = plugin;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("joinprotection.use-world")) return;

        if (!protectionHandler.isEnabledInWorld(player.getWorld()) && !player.hasPermission("joinprotection.bypass.world-list")) {
            return;
        }

        protectionHandler.startWorldProtection(player);

        // clear mob targets
        final FileConfiguration config = plugin.getConfig();
        if (config.getBoolean("modules.disable-entity-targeting.enabled")) {
            final double horizontalRange = config.getDouble("modules.disable-entity-targeting.horizontal-range");
            final double verticalRange = config.getDouble("modules.disable-entity-targeting.vertical-range");

            player.getNearbyEntities(horizontalRange, verticalRange, horizontalRange).forEach(entity -> {
                if (entity instanceof Mob mob && Objects.equals(mob.getTarget(), player)) {
                    mob.setTarget(null);
                }
            });
        }
    }
}
