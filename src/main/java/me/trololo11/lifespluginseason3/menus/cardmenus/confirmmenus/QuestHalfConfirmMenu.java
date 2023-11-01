package me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus;

import me.trololo11.lifespluginseason3.menus.cardmenus.QuestSelectMenu;
import me.trololo11.lifespluginseason3.menus.QuestsMenu;
import me.trololo11.lifespluginseason3.utils.ConfirmMenu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class QuestHalfConfirmMenu extends ConfirmMenu {

    private Quest quest;
    private QuestSelectMenu questSelectMenu;

    public QuestHalfConfirmMenu(Quest quest, QuestSelectMenu questSelectMenu){
        this.quest = quest;
        this.questSelectMenu = questSelectMenu;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.GREEN + "Czy napewno zmniejszyć?";
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
        return Utils.createItem(quest.getIcon(), "&2Czy napewno zmienjszyć wymagania questa &f["+quest.getName()+"&f]?", "skip-label");
    }

    @Override
    protected String getNameSoundIcon() {
        return "secret.sus_music";
    }

    @Override
    protected void onConfirm(InventoryClickEvent e, Player player) {
        quest.setHalfed(true);
        player.getInventory().setItemInMainHand(null);
        player.closeInventory();

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
            onlinePlayer.sendMessage(Utils.chat("&a"+player.getName()+" właśnie zmniejszył wymagania quest &f["+quest.getName()+"&f]&a dwukrotnie!"));
            onlinePlayer.playSound(onlinePlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

            //We set the same progress to the quests for all of the players to check
            //if a player has finished a quest because of this halfing
            quest.setPlayerProgress(onlinePlayer, quest.getPlayerProgress(onlinePlayer));

            //We set items in everyone's open menu with the quests so that the label that quest is halfed shows instantly
            if(onlinePlayer.getOpenInventory().getTopInventory().getHolder() instanceof QuestsMenu questsMenu){
                questsMenu.setMenuItems(player);
            }

        }

    }

    @Override
    protected void onCancel(InventoryClickEvent e, Player player) {
        questSelectMenu.open(player);
    }
}
