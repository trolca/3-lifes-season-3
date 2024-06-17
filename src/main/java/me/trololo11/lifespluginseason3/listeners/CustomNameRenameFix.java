package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class CustomNameRenameFix implements Listener {

    @EventHandler
    public void onAnvilRename(InventoryClickEvent e){
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.ANVIL) return;
        if(e.getSlot() != 2) return;
        if(e.getCurrentItem() == null) return;
        ItemStack item = e.getCurrentItem();
        if(Utils.getPrivateName(item) == null) return;
        if(Utils.getPrivateName(item).startsWith("revive_card")) return;
        Player player = (Player) e.getWhoClicked();

        player.sendMessage(ChatColor.RED + "Nie można zmieniać nazwy customowego itemu!");
        e.setCancelled(true);
    }
}
