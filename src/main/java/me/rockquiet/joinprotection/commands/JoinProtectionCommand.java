package me.rockquiet.joinprotection.commands;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinProtectionCommand implements CommandExecutor {

    private final JoinProtection joinProtection;
    private final MessageManager messageManager;

    public JoinProtectionCommand(JoinProtection joinProtection,
                                 MessageManager messageManager) {
        this.joinProtection = joinProtection;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
            return false;
        }

        FileConfiguration config = joinProtection.getConfig();

        if ((sender instanceof Player player) && !player.hasPermission("joinprotection.reload")) {
            messageManager.sendMessage(config, player, "messages.noPerms");
            return false;
        }

        joinProtection.reloadConfig();
        messageManager.sendMessage(config, sender, "messages.reload");
        return false;
    }
}
