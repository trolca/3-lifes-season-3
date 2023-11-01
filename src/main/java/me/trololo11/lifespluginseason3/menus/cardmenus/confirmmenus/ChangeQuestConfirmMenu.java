package me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.menus.cardmenus.QuestSelectMenu;
import me.trololo11.lifespluginseason3.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ChangeQuestConfirmMenu extends ConfirmMenu {

    private Quest quest;
    private QuestSelectMenu questSelectMenu;
    private QuestManager questManager;

    public ChangeQuestConfirmMenu(Quest quest, QuestManager questManager, QuestSelectMenu questSelectMenu){
        this.quest = quest;
        this.questManager = questManager;
        this.questSelectMenu = questSelectMenu;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Czy na pewno zmienić?";
    }

    @Override
    protected Material getConfirmItem() {
        return Material.LIME_STAINED_GLASS_PANE;
    }

    @Override
    protected Material getCancelItem() {
        return Material.RED_STAINED_GLASS_PANE;
    }

    @Override
    protected ItemStack getIcon() {
        return Utils.createItem(quest.getIcon(), "&aCzy napewno zmienić questa &f["+quest.getName()+"&f]?", "skip-label");
    }

    @Override
    protected String getNameSoundIcon() {
        return "secret.vine_boom";
    }

    @Override
    protected void onConfirm(InventoryClickEvent e, Player player) {
        Random random = new Random();
        QuestType questType = quest.getQuestType();

        ArrayList<Quest> allQuests = questManager.getAllQuests();
        ArrayList<Quest> allThisTypesQuests = new ArrayList<>();

        //this only adds quests that are this type cus we can only change quests for the same type
        allQuests.forEach(quest1 -> { if(quest1.getQuestType() == questType) allThisTypesQuests.add(quest1); });

        Quest newQuest = allThisTypesQuests.get(random.nextInt(allThisTypesQuests.size()));

        player.getInventory().setItemInMainHand(null);

        try {
            questManager.changeQuest(quest, newQuest);
        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }

        player.closeInventory();

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            onlinePlayer.sendMessage(Utils.chat("&6" + player.getName() + " właśnie zamienił " + getQuestTypeName(questType) + " questa &f[" + quest.getName() + "&f]&6 na &f[" + newQuest.getName() + "&f]"));
            onlinePlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

            newQuest.setPlayerProgress(onlinePlayer, quest.getPlayerProgress(onlinePlayer));
            quest.removePlayerProgress(player);

        }
    }

    @Override
    protected void onCancel(InventoryClickEvent e, Player player) {
        questSelectMenu.open(player);
    }

    private String getQuestTypeName(QuestType questType){

        switch (questType){

            case DAILY -> {
                return "dziennego";
            }

            case WEEKLY -> {
                return "tygodniowego";
            }

            case CARD -> {
                return "card";
            }

            default -> {
                return "jakiegoś";
            }

        }

    }
}
