package me.bowlerguy66.loadouts;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class LoadoutsUtils {

	public static ItemStack getApplyButton() {
		ItemStack i = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(ChatColor.GREEN + "Click to apply loadout");
		m.setLore(Arrays.asList("", ChatColor.GRAY + "Swaps out current items with ones in loadout"));
		i.setItemMeta(m);
		return i;
	}
	
	public static boolean validInv(Inventory inv) {
		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < 6; y++) {
				int slot = y * 9 + x;
				if(slot > inv.getSize()) continue;
				ItemStack item = inv.getItem(slot);
				if(item == null) continue;
				if(getEquipmentTypeFromSlot(slot) != item.getType().getEquipmentSlot()) return false;
			}
		}
		return true;
	}
	
	public static EquipmentSlot getEquipmentTypeFromSlot(int slot) {
		int num = slot % 9;
		switch(num) {
		case 0:
			return EquipmentSlot.HEAD;
		case 1:
			return EquipmentSlot.CHEST;
		case 2:
			return EquipmentSlot.LEGS;
		case 3:
			return EquipmentSlot.FEET;
		}
		return EquipmentSlot.HAND;
	}
	
	public static int getSlotFromEquipmentType(EquipmentSlot slot) {
		switch(slot) {
		case HEAD:
			return 0;
		case CHEST:
			return 1;
		case LEGS:
			return 2;
		case FEET:
			return 3;
		default:
			return 4;
		}
	}

}
