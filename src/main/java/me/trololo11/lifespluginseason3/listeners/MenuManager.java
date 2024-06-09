package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * This class detects if a inventory that player clicks if an instance of {@link Menu}
 * and if it is then it cancels the event and calls the {@link Menu#handleMenu(InventoryClickEvent)} function
 */
public class MenuManager implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){

        ItemStack item = e.getCurrentItem();
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu) {

            e.setCancelled(true);

            if (item == null) return;
            if (!item.hasItemMeta()) return;
            if (Utils.getPrivateName(item) == null) return;
            if(!(e.getWhoClicked() instanceof Player)) return;

            Menu menu = (Menu) holder;
            menu.handleMenu(e);

        }

    }
}
