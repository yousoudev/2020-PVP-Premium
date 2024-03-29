package com.Sagacious_.KitpvpStats.bounty.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.Bounty;
import com.Sagacious_.KitpvpStats.bounty.BountyManager;
import com.Sagacious_.KitpvpStats.util.ItemUtil;

public class TargetGUI {
	private int size;
	private short color;
	private String display;
	private List<String> lore;
	private String title;
	public HashMap<UUID, Integer> s = new HashMap<UUID, Integer>();
	private BountyManager bm = Core.getInstance().getBountyManager();
	
	public TargetGUI() {
		FileConfiguration conf = bm.conf;
		size = conf.getInt("tracker-gui-size");
		color = Short.valueOf((short)conf.getInt("tracker-gui-glass-color"));
		display = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-gui-display"));
		lore = bm.translateList(conf.getStringList("tracker-gui-lore"));
		title = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-gui-title"));
	}

	public void openInventory(Player p, int page) {
		s.put(p.getUniqueId(), page);
		Inventory inv = Bukkit.createInventory(null, size, title);
		
		int start = page*18;
		List<Bounty> s = bm.getBounties();
		for(int i = 0; i < size-9; i++) {
			if(i+start<s.size()) {
				Bounty b = s.get(i+start);
				List<String> l = new ArrayList<String>();
				for(String f : lore) {
					f=f.replace("%bountied%", Bukkit.getOfflinePlayer(b.getUuidTarget()).getName())
					.replace("%bountier%", Bukkit.getOfflinePlayer(b.getUuidOwner()).getName()).replace("%amount%", ""+b.getAmount()).replace("%timeleft%", ""+b.getTimeLeft());
				   l.add(f);
				}
				inv.setItem(i, ItemUtil.createSkullItem(Bukkit.getOfflinePlayer(b.getUuidTarget()).getName(), display.replace("%bountied%", Bukkit.getOfflinePlayer(b.getUuidTarget()).getName())
						.replace("%bountier%", Bukkit.getOfflinePlayer(b.getUuidOwner()).getName()).replace("%amount%", ""+b.getAmount()).replace("%timeleft%", ""+b.getTimeLeft()), l, b.getUuidTarget().equals(p.getUniqueId())));
			}
		}
		for(int i = size-9; i < size; i++) {
			if((i==size-4&&(page+1)*18<s.size())||(i==size-6&&page>0)) {
				inv.setItem(i, ItemUtil.createItem(Material.ARROW, i==size-6?"�8< �6Previous page":"�6Next page �8>"));
		}
			if(inv.getItem(i)==null) {
				inv.setItem(i, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 1, (short)color, "�7     "));
			}
		}
		p.openInventory(inv);
	}
}
