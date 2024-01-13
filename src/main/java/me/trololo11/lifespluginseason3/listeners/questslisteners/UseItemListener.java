package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseItemListener extends QuestListener {
    public UseItemListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() == Material.AIR && player.getInventory().getItemInOffHand().getType() == Material.AIR) return;
        Material material = player.getInventory().getItemInMainHand().getType() == Material.AIR ? player.getInventory().getItemInOffHand().getType() : player.getInventory().getItemInMainHand().getType();

        checkTarget(material, player);

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.USE_ITEM;
    }
}
