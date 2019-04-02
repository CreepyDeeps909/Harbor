package mykyta.Harbor.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import mykyta.Harbor.Config;
import mykyta.Harbor.Util;

public class Sleeping implements CommandExecutor {

    private Inventory gui;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();
            Config config = new Config();
            ArrayList<Player> sleeping = Util.sleeping.get(world);
            int slots = Math.min(54, ((sleeping.size() - 1) / 9 + 1) * 9);
            gui = Bukkit.createInventory(player, slots, config.getString("gui.sleeping"));
            
            if (sleeping.size() > 0) sleeping.forEach(p -> {
                ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setDisplayName(ChatColor.GRAY + p.getName());
                /*ArrayList<String> lore = new ArrayList<String>();
                lore.add("Custom head");
                skull.setLore(lore);
                */
                meta.setOwner(p.getName());
                item.setItemMeta(meta);
                gui.setItem(sleeping.indexOf(p), item);
            });

            player.openInventory(gui);
        }
        else {
            Config config = new Config();
            if (args.length < 1)  {
                ArrayList<String> list = new ArrayList<String>();
                Bukkit.getServer().getWorlds().forEach(w -> {list.add("&o" + w.getName());});
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.miscellaneous.prefix") + "Specify a world name. Possible worlds are " + String.join("&r, ", list) + "&r."));
            }
            else {
                World w = Bukkit.getServer().getWorld(args[0]);
                if (w == null) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.miscellaneous.prefix") + "The specified world doesn't exist."));
                else {
                    ArrayList<String> list = new ArrayList<String>();
                    Util.sleeping.get(Bukkit.getServer().getWorld(args[0])).forEach(p -> {list.add("&o" + p.getName());});
                    if (list.size() > 0 && list.size() != 1) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.miscellaneous.prefix") + "In &o" + args[0] + "&r, the currently sleeping players are " + String.join("&r, ", list) + "&r."));
                    else if (list.size() == 1) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.miscellaneous.prefix") + "In &o" + args[0] + "&r, " + list.get(0) + "&r is the only player sleeping."));
                    else Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.miscellaneous.prefix") + "There aren't any currently sleeping players in &o" + args[0] + "&r."));
                }
            }
        }
        return true;
    }
}