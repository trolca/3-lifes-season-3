package me.trololo11.lifespluginseason3.listeners.cardlisteners;

import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.menus.cardmenus.QuestSelectMenu;
import me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus.QuestHalfConfirmMenu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestSelectFunction;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class RequirementsCardListener implements Listener {

    private QuestSelectFunction questSelectFunction;
    private Function<Quest, Boolean> questFilter;
    private QuestManager questManager;

    public RequirementsCardListener(QuestManager questManager){
        this.questManager = questManager;
        questSelectFunction =  ( (quest, player, questSelectMenu) -> new QuestHalfConfirmMenu(quest, questSelectMenu).open(player));
        questFilter = (quest -> !quest.isHalfed() && quest.getMaxProgress() > 1);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!Utils.checkCardItem(item, CardType.REQUIREMENTS_REDUCE)) return;

        new QuestSelectMenu("&2&lWybierz questa do zmniejszenia", null, questManager,"&7&o(Kliknij by zmniejszyc wymagania)" , questSelectFunction, questFilter).open(player);
    }
}
