package me.rockquiet.joinprotection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    public void check(JoinProtection plugin) throws IOException {
        URL obj = new URL("https://api.github.com/repos/rockquiet/joinprotection/releases/latest");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
            plugin.getLogger().info("Unable to check for updates...");
            return;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String line;
        StringBuilder response = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();

        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

        String latestVersion = jsonResponse.get("tag_name").getAsString().replaceFirst("^v", "");
        String currentVersion = plugin.getDescription().getVersion();

        if (!currentVersion.equalsIgnoreCase(latestVersion)) {
            plugin.getLogger().info("An update is available! Latest version: " + latestVersion);
        }
    }
}
