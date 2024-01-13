package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.GameEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.GenericGameEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class SusSandBreakListener extends QuestListener {

    private HashMap<Player, Boolean> isInteracting = new HashMap<>();
    private HashMap<Player, ArrayList<Block>> blockSeen = new HashMap<>();

    public SusSandBreakListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onEvent(GenericGameEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        GameEvent gameEvent = e.getEvent();
        Player player = (Player) e.getEntity();

        if(gameEvent == GameEvent.ITEM_INTERACT_START){
            Block playerBlock = player.getTargetBlockExact(5);
            isInteracting.put(player, true);

            if(playerBlock != null && (playerBlock.getType() == Material.SUSPICIOUS_SAND || playerBlock.getType() == Material.SUSPICIOUS_GRAVEL) ){
                addIfDoesntContain(playerBlock, player);
            }

        }else if(gameEvent == GameEvent.ITEM_INTERACT_FINISH){

            isInteracting.put(player, false);

            ArrayList<Block> blocks = blockSeen.get(player);

            if(blocks == null) return;

            for(Block block : blocks){
                if(block.getType() == Material.SAND || block.getType() == Material.GRAVEL){
                    checkTarget(null, player);
                }
            }

            blockSeen.remove(player);

        }

    }

    @EventHandler
    public void onLook(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(!isInteracting.getOrDefault(player, false)) return;

        Block playerBlock = player.getTargetBlockExact(5);

        if(playerBlock != null && (playerBlock.getType() == Material.SUSPICIOUS_SAND || playerBlock.getType() == Material.SUSPICIOUS_GRAVEL) ){
            addIfDoesntContain(playerBlock, player);
        }

    }

    private void addIfDoesntContain(Block block, Player player){

        ArrayList<Block> blocks = blockSeen.get(player);
        if(blocks == null) blocks = new ArrayList<>();

        for(Block block1 : blocks){
            if(block1.getLocation().equals(block.getLocation())) return;
        }


        blocks.add(block);
        blockSeen.put(player, blocks);

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.MINE_SUS_BLOCK;
    }
}
