package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class TotemUseListener extends QuestListener {
    public TotemUseListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onUse(EntityDamageEvent e) {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getEntity();
        double currHealth = player.getHealth() - e.getDamage();
        if(currHealth < 0.5){
            if(player.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING
                    && player.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING)  return;

            checkTarget(null, player);
        }

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.TOTEM_USE;
    }
}
