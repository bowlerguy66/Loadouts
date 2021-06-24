package me.bowlerguy66.loadouts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Loadouts extends JavaPlugin {

	public static String FILE_LOCATION = "plugins/Loadouts/loadoutlocs.yml";
	public static String CHEST_TITLE = ChatColor.of(new Color(100, 0, 0)) + "Loadouts";
	
	FileLoading fileLoader;
	HashMap<UUID, Location> loadoutLoc;
	
	@Override
	public void onEnable() {
		
		this.fileLoader = new FileLoading("");
		this.loadoutLoc = new HashMap<UUID, Location>();
		
		Bukkit.getPluginManager().registerEvents(new LoadoutsInventory(), this);
		
		FileConfiguration file = fileLoader.getFile(FILE_LOCATION);
		if(file.contains("loadoutlocs")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "loading from file");
			for(String s : file.getStringList("loadoutlocs")) {
				String[] args = s.split(";");
				String[] locargs = args[1].split(",");
				Location loc = new Location(Bukkit.getWorld(UUID.fromString(locargs[0])), Integer.parseInt(locargs[1]), Integer.parseInt(locargs[2]), Integer.parseInt(locargs[3]));
				Bukkit.getConsoleSender().sendMessage("Loaded loc for: " + args[0]);
				loadoutLoc.put(UUID.fromString(args[0]), loc);
			}
		}
		
	}
	
	@Override
	public void onDisable() {
		
		List<String> serializedLocs = new ArrayList<String>();
		for(UUID uuid : loadoutLoc.keySet()) {
			Location l = loadoutLoc.get(uuid);
			serializedLocs.add(uuid.toString()+";"+l.getWorld().getUID().toString()+","
												  +l.getBlockX()+","
												  +l.getBlockY()+","
												  +l.getBlockZ());
		}
		fileLoader.getFile(FILE_LOCATION).set("loadoutlocs", serializedLocs);
		fileLoader.saveFile(FILE_LOCATION);
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		if(cmd.getName().equalsIgnoreCase("loadout") || cmd.getName().equalsIgnoreCase("ld")) {
			
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
				return true;
			}
			
			Player p = (Player) sender;
			Location loc = loadoutLoc.get(p.getUniqueId());
			
			if(loc == null) {
				p.sendMessage(ChatColor.RED + "You haven't set a loadout location yet! Set one with /setloadoutloc");
				return true;
			}
						
			if(!(loc.getBlock().getState() instanceof Chest)) {
				p.sendMessage(ChatColor.RED + "The block at your loadout location isn't a chest!");
				return true;
			}
			
			Inventory inv = ((Chest) loc.getBlock().getState()).getInventory();
			p.openInventory(inv);
			
		}
		
		if(cmd.getName().equalsIgnoreCase("setloadoutloc")) {
		
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
				return true;
			}
			
			Player p = (Player) sender;
			Block target = p.getTargetBlockExact(25, FluidCollisionMode.NEVER);
		
			if(target == null || !(target.getState() instanceof Chest)) {
				sender.sendMessage(ChatColor.RED + "You must be looking at a chest!");
				return true;
			}
			
			Chest chest = (Chest) target.getState();
			chest.setCustomName(CHEST_TITLE);
			chest.update();
			loadoutLoc.put(p.getUniqueId(), target.getLocation());
			p.sendMessage(ChatColor.GREEN + "Set loadout location!");
			
		}
		
		return false;
	}
	
}
