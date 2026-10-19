
package dev.nexui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class GuiManager implements Listener {

    private final NexUIPlugin plugin;
    private final Map<String, String> openMenus = new HashMap<>();

    public GuiManager(NexUIPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, String menuName) {
        File file = new File(
                plugin.getDataFolder(),
                "guis/" + menuName + ".yml"
        );

        if (!file.isFile()) {
            player.sendMessage("§cNEXUI: GUI tidak ditemukan: " + menuName);
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        int size = config.getInt("size", 27);

        if (size < 9 || size > 54 || size % 9 != 0) {
            player.sendMessage("§cNEXUI: Ukuran GUI harus 9–54 dan kelipatan 9.");
            return;
        }

        String title = config.getString("title", "&8NEXUI");
        title = title.replace("&", "§");

        Inventory inventory = Bukkit.createInventory(
                null, size, title
        );

        ConfigurationSection items = config.getConfigurationSection("items");

        if (items != null) {
            for (String key : items.getKeys(false)) {
                String path = "items." + key + ".";
                int slot = config.getInt(path + "slot", -1);

                if (slot < 0 || slot >= size) continue;

                Material material = Material.matchMaterial(
                        config.getString(path + "material", "STONE")
                );

                if (material == null || material.isAir()) continue;

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {
                    String name = config.getString(
                            path + "name", "&fItem"
                    );
                    meta.setDisplayName(name.replace("&", "§"));

                    java.util.List<String> lore =
                            config.getStringList(path + "lore");
                    lore.replaceAll(line -> line.replace("&", "§"));

                    if (!lore.isEmpty()) meta.setLore(lore);
                    item.setItemMeta(meta);
                }

                inventory.setItem(slot, item);
            }
        }

        openMenus.put(player.getUniqueId().toString(), menuName);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String menuName = openMenus.get(player.getUniqueId().toString());
        if (menuName == null) return;

        File file = new File(
                plugin.getDataFolder(),
                "guis/" + menuName + ".yml"
        );

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String title = config.getString("title", "&8NEXUI")
                .replace("&", "§");

        if (!event.getView().getTitle().equals(title)) return;

        event.setCancelled(true);

        if (event.getRawSlot() < 0 ||
                event.getRawSlot() >= event.getView().getTopInventory().getSize()) {
            return;
        }

        String action = config.getString(
                "items." + event.getRawSlot() + ".action", ""
        );

        if (action.equalsIgnoreCase("CLOSE")) {
            player.closeInventory();
        } else if (action.startsWith("MESSAGE:")) {
            player.sendMessage(action.substring(8).replace("&", "§"));
        } else if (action.startsWith("OPEN_GUI:")) {
            open(player, action.substring(9));
        }
    }
}
