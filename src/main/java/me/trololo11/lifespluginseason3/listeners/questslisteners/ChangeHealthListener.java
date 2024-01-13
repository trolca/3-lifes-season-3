package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.ArrayList;

public class ChangeHealthListener extends QuestListener {
    public ChangeHealthListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onChangeHealth(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player p) checkCustomTarget((int) p.getHealth(), p);

    }

    @EventHandler
    public void onHealHeath(EntityRegainHealthEvent e) {
        if(e.getEntity() instanceof Player p) checkCustomTarget((int) p.getHealth(), p);
    }
    protected void checkCustomTarget(Integer target, Player p) {
        ArrayList<Quest> checkQuests = questManager.getListenerTypesQuests().get(getThisListenerType());
        for (Quest quest : checkQuests) {


            if (( (Integer) quest.getTargets().get(0) >= target || quest.getTargets() == null || quest.getTargets().isEmpty()) && !quest.hasFinished(p)) {

                quest.setPlayerProgress(p, quest.getPlayerProgress(p) + 1);

            }
        }
    }


    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.PLAYER_HEART;
    }
}
