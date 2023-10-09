package me.trololo11.lifespluginseason3.listeners.cardlisteners;

import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.managers.CardManager;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.menus.PlayerReviveMenu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class GiveLifeCardUseListener implements Listener {

    private CardManager cardManager;
    private LifesManager lifesManager;

    public GiveLifeCardUseListener(CardManager cardManager, LifesManager lifesManager){
        this.cardManager = cardManager;
        this.lifesManager = lifesManager;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!Utils.checkCardItem(item, CardType.LIFE_GIVE)) return;

        if(lifesManager.getDeadPlayers().isEmpty()){
            player.sendMessage(ChatColor.RED + "Nikt jeszcze nie żyje!");
            return;
        }

        new PlayerReviveMenu(lifesManager).open(player);

    }
}
