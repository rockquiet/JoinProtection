package me.rockquiet.joinprotection.commands;

import me.rockquiet.joinprotection.JoinProtection;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinProtectionCommand implements CommandExecutor {

    private final JoinProtection joinProtection;

    public JoinProtectionCommand(JoinProtection joinProtection) {
        this.joinProtection = joinProtection;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1 || !args[0].equalsIgnoreCase("reload")) {
            return false;
        }

        if ((sender instanceof Player player) && !player.hasPermission("joinprotection.reload")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(joinProtection.getConfig().getString("messages.noPerms")
                            .replace("%prefix%", joinProtection.getConfig().getString("messages.prefix"))
                    )
            );
            return false;
        }

        joinProtection.reloadConfig();
        sender.sendMessage(MiniMessage.miniMessage().deserialize(joinProtection.getConfig().getString("messages.reload")
                        .replace("%prefix%", joinProtection.getConfig().getString("messages.prefix"))
                )
        );
        return false;
    }
}
