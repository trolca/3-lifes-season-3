package me.trololo11.lifespluginseason3.listeners.cardlisteners;

import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.menus.cardmenus.QuestSelectMenu;
import me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus.SkipQuestConfirmMenu;
import me.trololo11.lifespluginseason3.utils.QuestSelectFunction;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SkipQuestsCardListener implements Listener {

    private QuestManager questManager;

    public SkipQuestsCardListener(QuestManager questManager){
        this.questManager = questManager;
    }

    private QuestSelectFunction questSelectFunction = (((quest, player, questSelectMenu) -> new SkipQuestConfirmMenu(quest, questSelectMenu).open(player) ) );

    //Daily card quests
    @EventHandler
    public void onInteractDaily(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!Utils.checkCardItem(item, CardType.DAILY_SKIP)) return;



        new QuestSelectMenu("&c&lWybierz dzienny quest do pominięcia", QuestType.DAILY,  questManager,"&7&o(Klknij by pominąć)", questSelectFunction ).open(player);
    }

    @EventHandler
    public void onInteractWeekly(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!Utils.checkCardItem(item, CardType.WEEKLY_SKIP)) return;

        new QuestSelectMenu("&e&lWybierz tygodniowy quest do pominięcia", QuestType.WEEKLY, questManager,"&7&o(Kliknij by pominąć)", questSelectFunction).open(player);
    }
}
