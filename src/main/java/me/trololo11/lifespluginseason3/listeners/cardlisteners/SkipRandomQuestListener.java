package me.trololo11.lifespluginseason3.listeners.cardlisteners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class SkipRandomQuestListener implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private QuestManager questManager;
    private DatabaseManager databaseManager;
    private Random random = new Random();

    public SkipRandomQuestListener(QuestManager questManager, DatabaseManager databaseManager){
        this.questManager = questManager;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) throws SQLException {
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!Utils.checkCardItem(item, CardType.RANDOM_SKIP)) return;

        ArrayList<Quest> allQuests = new ArrayList<>(questManager.getAllActiveQuests());

        allQuests.removeIf(quest -> quest.hasFinished(player));

        if(allQuests.isEmpty()){
            player.sendMessage(ChatColor.RED + "Musisz miec przynajmiej 1 questa nie skończonego by użyć tej karty!");
            return;
        }

        Quest quest = allQuests.get(random.nextInt(allQuests.size()));


        player.getInventory().setItemInMainHand(null);

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
            onlinePlayer.sendMessage(Utils.chat("&eQuest &f["+quest.getName()+"&f] &ezostał właśnie pominięty przez "+ player.getName() + "!"));
            onlinePlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            quest.setSilentPlayerProgress(onlinePlayer, quest.getMaxProgress());
        }

        databaseManager.setQuestProgressForAll(quest, quest.getMaxProgress());
        databaseManager.addSkippedQuest(quest);
        plugin.addSkippedQuest(quest);

    }
}
