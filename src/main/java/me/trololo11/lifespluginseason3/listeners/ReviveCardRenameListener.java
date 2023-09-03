package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ReviveCardRenameListener implements Listener {

    private LifesManager lifesManager;

    public ReviveCardRenameListener(LifesManager lifesManager){
        this.lifesManager = lifesManager;
    }
    @EventHandler
    public void onOpen(InventoryOpenEvent e){
        if(!(e.getPlayer() instanceof Player)) return;
        if(e.getInventory().getType() != InventoryType.ANVIL) return;
        Player player = (Player) e.getPlayer();
        ItemStack reviveCard = player.getInventory().getItemInMainHand();
        if(isBadItem(reviveCard)) return;

        AnvilInventory inventory = (AnvilInventory) e.getInventory();

        ItemMeta itemMeta = reviveCard.getItemMeta();

        itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Nazwa gracza: ");

        reviveCard.setItemMeta(itemMeta);
        inventory.setItem(0, reviveCard);
        player.getInventory().setItemInMainHand(null);
    }

    @EventHandler
    public void onOpenClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(e.getClickedInventory() == null && e.getHotbarButton() == -1) return;
        if(e.getHotbarButton() == -1 && e.getClickedInventory().getType() == InventoryType.ANVIL) return;
        if(e.getView().getTopInventory().getType() != InventoryType.ANVIL) return;
        Player player = (Player) e.getWhoClicked();
        int slot = e.getHotbarButton() == -1 ? e.getSlot() : e.getHotbarButton();

        ItemStack reviveCard = e.getHotbarButton() == -1 ? e.getCurrentItem() : player.getInventory().getItem(slot);
        if(isBadItem(reviveCard)) return;

        AnvilInventory anvilInventory = (AnvilInventory) e.getInventory();

        if(anvilInventory.getItem(0) != null){
            e.setCancelled(true);
            return;
        }

        Inventory clickedInventory = e.getHotbarButton() == -1 ? e.getClickedInventory() : player.getInventory();

        ItemMeta itemMeta = reviveCard.getItemMeta();

        itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Nazwa gracza: ");

        reviveCard.setItemMeta(itemMeta);

        anvilInventory.setItem(0, reviveCard);
        e.setCancelled(true);
        clickedInventory.setItem(slot, null);
    }

    @EventHandler
    public void onSubmitClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.ANVIL) return;
        if(e.getSlot() != 2) return;
        if(e.getCurrentItem() == null) return;
        AnvilInventory inventory = (AnvilInventory) e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        ItemStack reviveCard = inventory.getItem(0);
        if(isBadItem(reviveCard)) return;

        String[] args = inventory.getRenameText().split(" ");

        ItemMeta itemMeta = reviveCard.getItemMeta();

        itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Karta odrodzenia");

        if(args.length < 3){
            reviveCard.setItemMeta(itemMeta);

            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Niepoprawny format tekstu!");
            leaveLogic(reviveCard, player, true);
            return;
        }

        String name = args[2];
        OfflinePlayer deadPlayer = null;

        for(OfflinePlayer checkDeadPlayer : lifesManager.getDeadPlayers()){

            if(checkDeadPlayer.getName().equalsIgnoreCase(name)){
                deadPlayer = checkDeadPlayer;
                break;
            }
        }

        if(deadPlayer == null){
            reviveCard.setItemMeta(itemMeta);
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Ten gracz nie jest martwy!");
            leaveLogic(reviveCard, player, true);
            return;
        }

        ArrayList<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.set(0, ChatColor.GRAY + "Przypisany gracz: "+ deadPlayer.getName());
        itemMeta.setLore(lore);
        String oldName = itemMeta.getLocalizedName().split("_")[2].split(":")[1];
        itemMeta.setLocalizedName(itemMeta.getLocalizedName().replace(oldName, deadPlayer.getName()));

        reviveCard.setItemMeta(itemMeta);

        e.setCancelled(true);
        player.closeInventory();
        Utils.addSafelyItem(reviveCard, player);
        player.sendMessage(ChatColor.GREEN + "Pomyślnie przypisano "+ deadPlayer.getName() + " do tej karty!");
    }

    @EventHandler
    public void onLeaveClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.ANVIL) return;
        if(e.getSlot() != 0) return;

        Player player = (Player) e.getWhoClicked();
        ItemStack reviveCard = e.getCurrentItem();
        if(isBadItem(reviveCard)) return;

        if(e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT || e.getClick() == ClickType.NUMBER_KEY){
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        leaveLogic(reviveCard, player, true);

    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent e){
        if(!(e.getPlayer() instanceof Player)) return;
        if(e.getInventory().getType() != InventoryType.ANVIL) return;
        AnvilInventory inventory = (AnvilInventory) e.getInventory();
        Player player = (Player) e.getPlayer();
        ItemStack reviveCard = inventory.getItem(0);
        if(isBadItem(reviveCard)) return;
        leaveLogic(reviveCard, player, false);
    }

    public void leaveLogic(ItemStack reviveCard, Player player, boolean closeInv){
        ItemMeta itemMeta = reviveCard.getItemMeta();

        itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Karta odrodzenia");

        reviveCard.setItemMeta(itemMeta);
        if(closeInv) player.closeInventory();
    }


    private boolean isBadItem(ItemStack item){
        if(item == null) return true;
        if(item.getType() == Material.AIR) return true;
        if(!item.hasItemMeta()) return true;
        if(!item.getItemMeta().hasLocalizedName()) return true;

        return !item.getItemMeta().getLocalizedName().startsWith("revive_card");
    }


}
