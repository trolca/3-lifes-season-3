package me.trololo11.lifespluginseason3.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {
    protected Inventory inventory;

    public abstract String getMenuName();

    public abstract int getSlots();

    //let each menu decide what items are to be placed in the inventory menu
    public abstract void setMenuItems(Player player);

    public abstract void handleMenu(InventoryClickEvent e);


    public void open(Player player) {

        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems(player);

        player.openInventory(inventory);
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
