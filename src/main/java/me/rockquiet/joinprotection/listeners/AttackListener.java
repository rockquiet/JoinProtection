package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {

    private final JoinProtection plugin;
    private final ProtectionHandler protectionHandler;

    public AttackListener(JoinProtection joinProtection,
                          ProtectionHandler protectionHandler) {
        this.plugin = joinProtection;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        if (!protectionHandler.hasProtection(player.getUniqueId()) || player.hasPermission("joinprotection.bypass.cancel-on-attack")) {
            return;
        }

        if (event.getEntity().equals(player)) return;

        if (plugin.getConfig().getBoolean("cancel.on-attack")) {
            protectionHandler.cancelProtection(player, "messages.protectionDeactivatedAttack");
        }
    }
}
