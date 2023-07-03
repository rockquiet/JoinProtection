package me.rockquiet.joinprotection;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageManager {

    public boolean messageEmpty(FileConfiguration config, String messagePath) {
        return !config.contains(messagePath) || config.getString(messagePath).isBlank();
    }

    public String messageWithPrefix(FileConfiguration config, String messagePath) {
        return StringUtils.replaceOnce(config.getString(messagePath), "%prefix%", config.getString("messages.prefix"));
    }

    public void sendMessage(FileConfiguration config, CommandSender sender, String messagePath) {
        if (messageEmpty(config, messagePath)) return;

        sender.sendMessage(MiniMessage.miniMessage().deserialize(messageWithPrefix(config, messagePath)));
    }

    public void sendMessage(FileConfiguration config, CommandSender sender, String messagePath, String placeholder, String replacePlaceholder) {
        if (messageEmpty(config, messagePath)) return;

        sender.sendMessage(MiniMessage.miniMessage().deserialize(StringUtils.replace(messageWithPrefix(config, messagePath), placeholder, replacePlaceholder)));
    }

    public void sendActionbar(FileConfiguration config, Player player, String messagePath) {
        if (messageEmpty(config, messagePath)) return;

        player.sendActionBar(MiniMessage.miniMessage().deserialize(messageWithPrefix(config, messagePath)));
    }

    public void sendActionbar(FileConfiguration config, Player player, String messagePath, String placeholder, String replacePlaceholder) {
        if (messageEmpty(config, messagePath)) return;

        player.sendActionBar(MiniMessage.miniMessage().deserialize(StringUtils.replace(messageWithPrefix(config, messagePath), placeholder, replacePlaceholder)));
    }
}
