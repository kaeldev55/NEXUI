
package dev.nexui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class NexUIPlugin extends JavaPlugin {

    private GuiManager guiManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        guiManager = new GuiManager(this);
        getServer().getPluginManager().registerEvents(guiManager, this);

        if (getCommand("nexui") != null) {
            getCommand("nexui").setExecutor(this::handleCommand);
        }

        getLogger().info("NEXUI enabled!");
    }

    private boolean handleCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (args.length == 0) {
            sender.sendMessage("§b§lNEXUI §7» §fGunakan /nexui gui <nama>");
            return true;
        }

        if (args[0].equalsIgnoreCase("gui")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Command ini hanya untuk pemain.");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("§cGunakan: /nexui gui <nama>");
                return true;
            }

            guiManager.open(player, args[1]);
            return true;
        }

        sender.sendMessage("§b§lNEXUI §7» §fUI Framework is running!");
        return true;
    }

    @Override
    public void onDisable() {
        getLogger().info("NEXUI disabled.");
    }
}
