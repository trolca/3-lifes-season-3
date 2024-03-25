package me.trololo11.lifespluginseason3.listeners.cardlisteners;

import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.menus.cardmenus.QuestSelectMenu;
import me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus.ChangeQuestConfirmMenu;
import me.trololo11.lifespluginseason3.utils.QuestSelectFunction;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ChangeQuestCardUseListener implements Listener {
    private QuestManager questManager;

    private QuestSelectFunction questSelectFunction;

    public ChangeQuestCardUseListener(QuestManager questManager){
        this.questManager = questManager;
        questSelectFunction = (quest, player, questSelectMenu) -> new ChangeQuestConfirmMenu(quest, questManager, questSelectMenu).open(player);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!Utils.checkCardItem(item, CardType.QUEST_CHANGE)) return;

        new QuestSelectMenu("&6&lZmień globalnie questa na innego", null, questManager,"&7&o(Kliknij by zmienić)", questSelectFunction).open(player);
    }
}
