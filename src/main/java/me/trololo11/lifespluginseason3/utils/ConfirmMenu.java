package me.trololo11.lifespluginseason3.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A basic menu used for confirming the specified actions. <br>
 * Creates a 9-slot menu where the player has 2 options yes or no and in the middle a custom item.
 */
public abstract class ConfirmMenu extends Menu{

    /**
     * The menu name.
     * @return The menu name
     */
    public abstract String getMenuName();

    /**
     * Gets the item that is going to show as the confirm item.
     * @return The material of this item.
     */
    protected abstract Material getConfirmItem();

    /**
     * Gets the item that is going to show as the cancel item.
     * @return The material of this item.
     */
    protected abstract Material getCancelItem();

    /**
     * The item that is going to be showed in the middle of the inventory.
     */
    protected abstract ItemStack getIcon();

    protected abstract String getNameSoundIcon();

    /**
     * The action that'll happen when a player clicks on the confirm option.
     * @param e The event of the click
     * @param player The player that clicked on the option
     */
    protected abstract void onConfirm(InventoryClickEvent e, Player player);

    /**
     * The action that'll happen when a player clicks on the cancel option.
     * @param e The event of the click
     * @param player The player that clicked on the option
     */
    protected abstract void onCancel(InventoryClickEvent e, Player player);

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack yesItem = Utils.createItem(getConfirmItem(), "&aTak", "yes");
        ItemStack noItem = Utils.createItem(getCancelItem(), "&cNie", "no");

        for(int i=0; i < getSlots(); i++){
            inventory.setItem(i, filler);
        }

        inventory.setItem(2, yesItem);
        inventory.setItem(4, getIcon());
        inventory.setItem(6, noItem);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        String localizedName = e.getCurrentItem().getItemMeta().getLocalizedName();
        Material material = e.getCurrentItem().getType();
        Player player = (Player) e.getWhoClicked();

        if(material == getConfirmItem() && localizedName.equalsIgnoreCase("yes")) onConfirm(e, player);
        else if(material == getCancelItem() && localizedName.equalsIgnoreCase("no")) onCancel(e, player);
        else if(material == getIcon().getType() && localizedName.equalsIgnoreCase(getIcon().getItemMeta().getLocalizedName())){

            if(getNameSoundIcon() == null) return;

            player.stopAllSounds();
            player.playSound(player, getNameSoundIcon(), 1f, 1f);

        }


    }
}
