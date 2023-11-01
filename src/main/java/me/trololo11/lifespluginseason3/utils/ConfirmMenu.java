package me.trololo11.lifespluginseason3.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ConfirmMenu extends Menu{

    public abstract String getMenuName(Player player);

    protected abstract Material getConfirmItem();

    protected abstract Material getCancelItem();

    protected abstract ItemStack getIcon();

    protected abstract String getNameSoundIcon();

    protected abstract void onConfirm(InventoryClickEvent e, Player player);

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
