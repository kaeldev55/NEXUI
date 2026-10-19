
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
        if (args.length == 0) {
            sender.sendMessage("§b§lNEXUI §7» §fUI Framework");
            sender.sendMessage("§7Commands: §f/nexui load <project>");
            return true;
        }

        if (args[0].equalsIgnoreCase("load")) {
            if (args.length < 2) {
                sender.sendMessage("§cUsage: /nexui load <project>");
                return true;
            }

            try {
                UIProject project = UIProject.load(this, args[1]);

                sender.sendMessage("§b§lNEXUI §7» §aProject loaded!");
                sender.sendMessage("§7Name: §f" + project.getName());
                sender.sendMessage("§7ID: §f" + project.getId());
                sender.sendMessage(
                        "§7Components: §f" + project.getComponents().size()
                );
            } catch (IllegalArgumentException exception) {
                sender.sendMessage(
                        "§cNEXUI » " + exception.getMessage()
                );
            }

            return true;
        }

        sender.sendMessage("§cUnknown command. Use /nexui");
        return true;
    }

    @Override
    public void onDisable() {
        getLogger().info("NEXUI disabled.");
    }
}
