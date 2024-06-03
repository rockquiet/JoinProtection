package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {

    private final ProtectionHandler protectionHandler;

    public AttackListener(ProtectionHandler protectionHandler) {
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        if (event.getEntity().equals(player)) return;

        protectionHandler.cancelProtectionIfEnabled(
                player,
                "joinprotection.bypass.cancel-on-attack",
                "cancel.on-attack",
                "messages.protectionDeactivatedAttack"
        );
    }
}
