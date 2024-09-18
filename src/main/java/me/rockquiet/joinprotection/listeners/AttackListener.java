package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    public AttackListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        if (event.getEntity().equals(player)) return;

        final Config config = configManager.get();
        protectionHandler.cancelProtectionIfEnabled(
                player,
                Permissions.BYPASS_CANCEL_ON_ATTACK,
                config.cancel.onAttack,
                config.messages.protectionDeactivatedAttack
        );
    }
}
