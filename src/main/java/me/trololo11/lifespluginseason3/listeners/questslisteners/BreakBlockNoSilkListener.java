package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakBlockNoSilkListener extends QuestListener {


    public BreakBlockNoSilkListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        ItemStack breakTool = p.getInventory().getItemInMainHand();
        if(breakTool.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) return;
        checkTarget(e.getBlock().getType(), p);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.BREAK_BLOCKS_NO_SILK;
    }
}
