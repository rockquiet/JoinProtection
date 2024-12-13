package me.rockquiet.joinprotection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.module.ModuleDescriptor.Version;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UpdateChecker {

    @SuppressWarnings("deprecation")
    public UpdateChecker(JoinProtection plugin) {
        plugin.getScheduler().runAsync(() -> {
            try {
                String currentVersion = plugin.getDescription().getVersion();

                HttpClient client = HttpClient.newHttpClient();

                JsonObject jsonResponse = JsonParser.parseString(
                        client.send(
                                HttpRequest.newBuilder()
                                        .uri(URI.create("https://api.github.com/repos/rockquiet/joinprotection/releases/latest"))
                                        .setHeader("User-Agent", "JoinProtection " + currentVersion)
                                        .timeout(Duration.ofSeconds(30))
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString()
                        ).body()
                ).getAsJsonObject();

                Version latest = Version.parse(jsonResponse.get("tag_name").getAsString().replaceFirst("^[Vv]", ""));
                Version current = Version.parse(currentVersion);
                int compare = latest.compareTo(current);

                if (compare > 0) {
                    plugin.getLogger().info("An update is available! Latest version: " + latest + ", you are using: " + current);
                } else if (compare < 0) {
                    plugin.getLogger().warning("You are running a newer version of the plugin than released. If you are using a development build, please report any bugs on the project's GitHub.");
                }
            } catch (IOException | InterruptedException e) {
                plugin.getLogger().warning("Unable to check for updates.");
            }
        });
    }
}
