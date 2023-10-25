package me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus;

import me.trololo11.lifespluginseason3.menus.cardmenus.QuestSelectMenu;
import me.trololo11.lifespluginseason3.utils.ConfirmMenu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SkipQuestConfirmMenu extends ConfirmMenu {

    private Quest quest;
    private QuestSelectMenu questSelectMenu;

    public SkipQuestConfirmMenu(Quest quest, QuestSelectMenu questSelectMenu){
        this.quest = quest;
        this.questSelectMenu = questSelectMenu;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Czy napewno pominąć?";
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
        return Utils.createItem(quest.getIcon(), "&aCzy napewno pominąć &f["+quest.getName()+"&f]?", "skip-label");
    }

    @Override
    protected void onConfirm(InventoryClickEvent e, Player player) {
        player.sendMessage(Utils.chat("&ePomyślnie pominięto quest &f["+quest.getName()+"&f]"));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        quest.setSilentPlayerProgress(player, quest.getMaxProgress());
        player.getInventory().setItemInMainHand(null);
        player.closeInventory();
    }

    @Override
    protected void onCancel(InventoryClickEvent e, Player player) {
        questSelectMenu.open(player);
    }
}
