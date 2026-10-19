
package dev.nexui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class NexUIPlugin extends JavaPlugin {

    private UIRenderer renderer;

    @Override
    public void onEnable() {
        getLogger().info("NEXUI v0.1.0 enabled!");

        // Create the projects directory
        File projectsFolder = new File(
                getDataFolder(),
                "projects"
        );

        if (!projectsFolder.exists()
                && !projectsFolder.mkdirs()) {
            getLogger().severe(
                    "Could not create projects directory!"
            );
        }

        // Copy the example project if it does not exist
        if (getResource("projects/my-first-ui.yml") != null) {
            File exampleProject = new File(
                    projectsFolder,
                    "my-first-ui.yml"
            );

            if (!exampleProject.exists()) {
                saveResource(
                        "projects/my-first-ui.yml",
                        false
                );
            }
        } else {
            getLogger().warning(
                    "Example project resource not found."
            );
        }

        // Initialize the UI renderer
        renderer = new UIRenderer();

        // Register the command
        if (getCommand("nexui") != null) {
            getCommand("nexui").setExecutor(this::handleCommand);
        } else {
            getLogger().severe(
                    "Command 'nexui' is missing from plugin.yml!"
            );
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
            sender.sendMessage(
                    "§7Usage: §f/nexui load <project>"
            );
            return true;
        }

        if (args[0].equalsIgnoreCase("load")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(
                        "NEXUI: This command requires a player."
                );
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(
                        "§cUsage: /nexui load <project>"
                );
                return true;
            }

            Player player = (Player) sender;

            try {
                UIProject project = UIProject.load(
                        this,
                        args[1]
                );

                renderer.render(player, project);

                player.sendMessage(
                        "§b§lNEXUI §7» §aProject loaded!"
                );
                player.sendMessage(
                        "§7Name: §f" + project.getName()
                );
                player.sendMessage(
                        "§7Components: §f"
                                + project.getComponents().size()
                );
            } catch (IllegalArgumentException exception) {
                player.sendMessage(
                        "§cNEXUI » " + exception.getMessage()
                );
            }

            return true;
        }

        sender.sendMessage(
                "§cUnknown command. Use /nexui"
        );
        return true;
    }

    @Override
    public void onDisable() {
        getLogger().info("NEXUI disabled.");
    }
}
