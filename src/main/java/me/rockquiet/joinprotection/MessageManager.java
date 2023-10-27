package me.rockquiet.joinprotection;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageManager {

    private final JoinProtection plugin;
    private final MiniMessage msg;

    public MessageManager(JoinProtection plugin) {
        this.plugin = plugin;
        this.msg = MiniMessage.miniMessage();
    }

    public boolean messageEmpty(FileConfiguration config, String messagePath) {
        return !config.contains(messagePath) || config.getString(messagePath).isBlank();
    }

    public String messageWithPrefix(FileConfiguration config, String messagePath) {
        return config.getString(messagePath).replaceFirst("%prefix%", config.getString("messages.prefix"));
    }

    public void sendMessage(FileConfiguration config, CommandSender sender, String messagePath) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().sender(sender).sendMessage(msg.deserialize(messageWithPrefix(config, messagePath)));
    }

    public void sendMessage(FileConfiguration config, CommandSender sender, String messagePath, String placeholder, String replacePlaceholder) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().sender(sender).sendMessage(msg.deserialize(messageWithPrefix(config, messagePath).replace(placeholder, replacePlaceholder)));
    }

    public void sendActionbar(FileConfiguration config, Player player, String messagePath) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().player(player).sendActionBar(msg.deserialize(messageWithPrefix(config, messagePath)));
    }

    public void sendActionbar(FileConfiguration config, Player player, String messagePath, String placeholder, String replacePlaceholder) {
        if (messageEmpty(config, messagePath)) return;

        plugin.adventure().player(player).sendActionBar(msg.deserialize(messageWithPrefix(config, messagePath).replace(placeholder, replacePlaceholder)));
    }
}
