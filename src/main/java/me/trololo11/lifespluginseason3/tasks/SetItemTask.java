package me.trololo11.lifespluginseason3.tasks;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Sets a specified item in specifed slot.
 */
public class SetItemTask extends BukkitRunnable {

    private PlayerInventory inventory;
    private int slot;
    private ItemStack item;

    public SetItemTask(PlayerInventory inventory, int slot, ItemStack item){
        this.inventory = inventory;
        this.slot = slot;
        this.item = item;
    }

    @Override
    public void run() {
        inventory.setItem(slot, item);
    }
}
