package dev.nexui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class NexUIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NEXUI v0.1.0 enabled!");

        if (getCommand("nexui") != null) {
            getCommand("nexui").setExecutor(this::handleCommand);
        }
    }

    private boolean handleCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        sender.sendMessage(
                "§b§lNEXUI §7» §fUI Framework is running!"
        );
        sender.sendMessage(
                "§7Version: §f" + getDescription().getVersion()
        );
        return true;
    }

    @Override
    public void onDisable() {
        getLogger().info("NEXUI disabled.");
    }
}
