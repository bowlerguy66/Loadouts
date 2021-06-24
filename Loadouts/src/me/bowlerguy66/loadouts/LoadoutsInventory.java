package me.bowlerguy66.loadouts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.bowlerguy66.armorsets.ArmorSets;

public class LoadoutsInventory implements Listener {

	@EventHandler
	public void onLoadoutInvClick(InventoryClickEvent event) {
		if(!event.getView().getTitle().equals(Loadouts.CHEST_TITLE)) return;
		if(event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.CHEST) return;
				
		// Clicked in chest inventory in loadout menu
		
		if(event.getAction().toString().contains("PLACE")) {
			if(LoadoutsUtils.getEquipmentTypeFromSlot(event.getSlot()) != event.getCursor().getType().getEquipmentSlot()) {
				event.setCancelled(true);				
			}
		} else if(event.getAction() == InventoryAction.HOTBAR_SWAP) {
			if(LoadoutsUtils.getEquipmentTypeFromSlot(event.getSlot()) != event.getView().getBottomInventory().getItem(event.getHotbarButton()).getType().getEquipmentSlot()) {
				event.setCancelled(true);				
			}			
		}

	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		if(!event.getView().getTitle().equals(Loadouts.CHEST_TITLE)) return;
		if(event.getInventory() != null && event.getInventory().getType() != InventoryType.CHEST) return;
		int slot = (Integer) event.getRawSlots().toArray()[0];		
		// Sucky workaround, getInventory() is only returning top inventory so we need to know if the clicked inventory is the bottom one
		if(slot > event.getView().getTopInventory().getSize()) return;
		if(event.getNewItems().get(slot) == null || 
			LoadoutsUtils.getEquipmentTypeFromSlot(slot) != event.getNewItems().get(slot).getType().getEquipmentSlot()) {
			event.setCancelled(true);	
			return;
		}
	}
	
	@EventHandler
	public void onPlayerInvClick(InventoryClickEvent event) {
		if(!event.getView().getTitle().equals(Loadouts.CHEST_TITLE)) return;
		if(event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.CHEST) return;
		
		Inventory inv = event.getView().getTopInventory();
		
		// Clicked in player inventory in loadout menu
		if(event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
			int correctSlot = LoadoutsUtils.getSlotFromEquipmentType(event.getCurrentItem().getType().getEquipmentSlot());
			for(int i = 0; i < 6; i++) {
				int targSlot = i * 9 + correctSlot;
				if(targSlot > inv.getSize()) continue;
				if(inv.getItem(targSlot) != null && inv.getItem(targSlot).getType() != Material.AIR) continue;
				// Found open correct slot, move shifted item
				inv.setItem(targSlot, event.getCurrentItem());
				event.setCurrentItem(null);
				break;
			}
			event.setCancelled(true);
		}	
		
	}
	
	@EventHandler
	public void onSwapButtonClick(InventoryClickEvent event) {
		if(!event.getView().getTitle().equals(Loadouts.CHEST_TITLE)) return;
		if(event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.CHEST) return;
		if(!event.getAction().toString().contains("PICKUP")) return;
		if((event.getSlot() - 8) % 9 != 0) return;
				
		// Clicked the swap button in loadout menu

		event.setCancelled(true);
		
		Inventory inv = event.getClickedInventory();
		int row = (event.getSlot() - 8) / 9;

		ItemStack[] currentArmorContents = event.getWhoClicked().getEquipment().getArmorContents();
		ItemStack[] newArmorContents = new ItemStack[4];
				
		for(int i = 3; i >= 0; i--) {
			newArmorContents[3 - i] = inv.getItem(row * 9 + i);
			inv.setItem(row * 9 + i, currentArmorContents[3 - i]);
		}
		
		event.getWhoClicked().getEquipment().setArmorContents(newArmorContents);
		
		// Just in case armor sets is installed, it will update the players armor
		ArmorSets armorSets = (ArmorSets) Bukkit.getPluginManager().getPlugin("ArmorSets");
		if(armorSets != null) {
			armorSets.tickArmor(event.getWhoClicked());
		}
		
	}
	
	@EventHandler
	public void openInventory(InventoryOpenEvent event) {
		if(!event.getView().getTitle().equals(Loadouts.CHEST_TITLE)) return;
		Inventory inv = event.getView().getTopInventory();
		for(int i = 0; i < 6; i++) {
			int slot = i * 9 + 8;
			if(slot > inv.getSize()) return;
			if(inv.getItem(slot) != null && inv.getItem(slot).getType() == Material.AIR) event.getPlayer().getInventory().addItem(inv.getItem(slot));
			inv.setItem(slot, LoadoutsUtils.getApplyButton());
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent event) {
		if(!event.getView().getTitle().equals(Loadouts.CHEST_TITLE)) return;
		Inventory inv = event.getView().getTopInventory();		
		for(int i = 0; i < 6; i++) {
			int slot = i * 9 + 8;
			if(slot > inv.getSize()) return;
			inv.setItem(slot, new ItemStack(Material.AIR));
		}
	}
	
}
