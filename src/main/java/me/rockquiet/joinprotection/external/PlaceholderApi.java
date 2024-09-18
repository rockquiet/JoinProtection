package me.rockquiet.joinprotection.external;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderApi extends PlaceholderExpansion {

    private final JoinProtection plugin;
    private final ProtectionHandler protectionHandler;

    public PlaceholderApi(JoinProtection plugin,
                          ProtectionHandler protectionHandler) {
        this.plugin = plugin;
        this.protectionHandler = protectionHandler;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "joinprotection";
    }

    @Override
    public @NotNull String getAuthor() {
        return "rockquiet";
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        if (params.equalsIgnoreCase("status")) {
            if (protectionHandler.hasProtection(player.getUniqueId())) {
                return plugin.config().integration.placeholderapi.status.protectionActive();
            } else {
                return plugin.config().integration.placeholderapi.status.notProtected();
            }
        }

        return null;
    }
}
