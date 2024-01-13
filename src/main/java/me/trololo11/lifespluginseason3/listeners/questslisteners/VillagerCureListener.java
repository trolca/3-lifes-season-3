package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class VillagerCureListener extends QuestListener {

    private HashMap<ZombieVillager, Player> playerZombiesStartCure = new HashMap<>();
    private ArrayList<ZombieVillager> allZombieVillagers = new ArrayList<>();

    public VillagerCureListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onTransform(EntityTransformEvent e) throws SQLException {
        if(e.getEntity().getType() != EntityType.ZOMBIE_VILLAGER) return;
        if(e.getTransformReason() != EntityTransformEvent.TransformReason.CURED) return;
        ZombieVillager zombieVillager = (ZombieVillager) e.getEntity();
        if(!allZombieVillagers.contains(zombieVillager)) return;

        checkTarget(null, playerZombiesStartCure.get(zombieVillager));
        allZombieVillagers.remove(zombieVillager);
        playerZombiesStartCure.remove(zombieVillager);


    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if(e.getEntity().getType() != EntityType.ZOMBIE_VILLAGER) return;
        ZombieVillager zombieVillager = (ZombieVillager) e.getEntity();
        if(!allZombieVillagers.contains(zombieVillager)) return;

        allZombieVillagers.remove(zombieVillager);
        playerZombiesStartCure.remove(zombieVillager);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){

        if(e.getRightClicked().getType() != EntityType.ZOMBIE_VILLAGER) return;
        ZombieVillager entity = (ZombieVillager) e.getRightClicked();
        if(!entity.hasPotionEffect(PotionEffectType.WEAKNESS)) return;

        Player player = e.getPlayer();

        playerZombiesStartCure.put(entity, player);
        allZombieVillagers.add(entity);

        playerZombiesStartCure.put(entity, player);


    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.VILLAGER_CURE;
    }
}
