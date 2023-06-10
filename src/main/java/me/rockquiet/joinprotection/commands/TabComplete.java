package me.rockquiet.joinprotection.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> results = new ArrayList<>();
        final List<String> completions = new ArrayList<>();

        if (args.length == 1 && command.getLabel().equalsIgnoreCase("joinprotection")) {
            if (sender.hasPermission("joinprotection.reload")) {
                results.add("reload");
            }
        }
        StringUtil.copyPartialMatches(args[0], results, completions);
        Collections.sort(completions);
        results.clear();
        results.addAll(completions);
        return results;
    }
}
