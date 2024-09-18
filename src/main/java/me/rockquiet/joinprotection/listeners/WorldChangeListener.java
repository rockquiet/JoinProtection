package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.Objects;

public class WorldChangeListener implements Listener {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    public WorldChangeListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission(Permissions.USE_WORLD)) return;

        if (!protectionHandler.isEnabledInWorld(player.getWorld()) && !player.hasPermission(Permissions.BYPASS_WORLD_LIST)) {
            return;
        }

        protectionHandler.startWorldProtection(player);

        // clear mob targets
        final Config config = configManager.get();
        if (config.modules.disableEntityTargeting.enabled) {
            final double horizontalRange = config.modules.disableEntityTargeting.horizontalRange;
            final double verticalRange = config.modules.disableEntityTargeting.verticalRange;

            player.getNearbyEntities(horizontalRange, verticalRange, horizontalRange).forEach(entity -> {
                if (entity instanceof Mob mob && Objects.equals(mob.getTarget(), player)) {
                    mob.setTarget(null);
                }
            });
        }
    }
}
