package me.rockquiet.joinprotection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    public void check(JoinProtection plugin) throws IOException {
        URL url = new URL("https://github.com/rockquiet/joinprotection/releases/latest");
        HttpURLConnection con;
        con = (HttpURLConnection) url.openConnection();
        con.setInstanceFollowRedirects(false);

        if (con.getHeaderField("Location") == null) {
            plugin.getLogger().info("Unable to check for updates...");
            return;
        }

        String[] split = con.getHeaderField("Location").split("/");
        String latestVersion = (split[split.length - 1]).replaceFirst("^v", "");
        String currentVersion = plugin.getDescription().getVersion();

        if (!currentVersion.equalsIgnoreCase(latestVersion)) {
            plugin.getLogger().info("An update is available! Latest version: " + latestVersion);
        }
    }
}
