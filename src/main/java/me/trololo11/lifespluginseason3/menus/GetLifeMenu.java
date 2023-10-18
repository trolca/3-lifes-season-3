package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.tasks.GoldLifeRouletteTask;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

public class GetLifeMenu extends Menu {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private Random r = new Random();
    private GoldLifeRouletteTask goldLifeRouletteTask;
    private RecipesManager recipesManager;

    private int randomizedNum = -1;
    private final int chance = 445; //We write the chance in hundreds bcs we want the 0.1 perecentage. So 123 is 12.3%
    private int pointerIndex = -1; //The index of the pointer that shows if you won or not in the menu (-1 if its not active)
    public ItemStack currLife = null;
    private int randomizingStage = 0; // 0 - Not randomizing animation, 1 - randomizing animation, 2 - animation finished
    int[] rouletteSlots = {3,4,5,14,23,22,21,12};
    private boolean hasWon = false;

    //Global items so we can use them in the pointer function
    private final ItemStack goldPointer = Utils.createItem(Material.ORANGE_STAINED_GLASS_PANE, " ", "pointer");
    private final ItemStack normalPointer = Utils.createItem(Material.RED_STAINED_GLASS_PANE, " ", "pointer");
    private final ItemStack notGet = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "not-get");
    private final ItemStack get = Utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", "get");

    public GetLifeMenu(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Hazard";
    }

    @Override
    public int getSlots() {
        return 3*9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack setLife = Utils.createItem(Material.RED_STAINED_GLASS_PANE, "&cWybierz życie ze swojego ekwipunku", "set-item");
        ItemStack cantConfirm = Utils.createItem(Material.RED_DYE, "&cWybierz najpierw zycie!", "confirm-nonactive");
        ItemStack confirm = Utils.createItem(Material.GREEN_DYE, "&2Naciśnij tutaj by zacząć hazard", "confirm");
        ItemStack exit = Utils.createItem(Material.RED_DYE, "&c&lWyjście", "back");

        ItemStack afterFiller = hasWon ? Utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", "won-filler") :
                Utils.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", "lost-filler");

        for(int i=0; i < getSlots(); i++){
             if(randomizingStage != 2) inventory.setItem(i, filler);
             else inventory.setItem(i, afterFiller);
        }

        afterFiller = hasWon ? goldPointer : filler;

        for(int index : rouletteSlots){
            if(randomizingStage != 2) inventory.setItem(index, isGold(index) ? get : notGet);
            else inventory.setItem(index, afterFiller);
        }

        if(currLife == null) {
            inventory.setItem(13, setLife);
            inventory.setItem(16, cantConfirm);
            return;
        }

        switch (randomizingStage){
            case 0 -> { //Before the animation
                inventory.setItem(13, currLife);
                inventory.setItem(16, confirm);
                inventory.setItem(10, exit);
            }

            case 1 -> inventory.setItem(13, currLife); //During the animation


            case 2 -> { //After the animation

                if (hasWon) {
                    inventory.setItem(13, recipesManager.getGoldLifeItem());
                } else {
                    ItemMeta exitMeta = exit.getItemMeta();
                    exitMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Przegrałeś FFFF");
                    exit.setItemMeta(exitMeta);

                    inventory.setItem(13, exit);
                }
            }
        }



    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();


        switch (item.getType()){

            case SCUTE -> {
                if(item.getItemMeta().getLocalizedName().startsWith("life_item") || item.getItemMeta().getLocalizedName().startsWith("player_life") ) {
                    if (e.getClick() == ClickType.NUMBER_KEY) return;

                    player.getInventory().setItem(e.getSlot(), null);
                    currLife = item;
                    setMenuItems(player);
                }else if(item.getItemMeta().getLocalizedName().startsWith("gold_life") && e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.PLAYER){

                    player.closeInventory(); //We dont need to add the gold item for the player bcs the exit listener does that for us

                }

            }

            case GREEN_DYE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("confirm")) return;

                player.getInventory().setItemInMainHand(null);
                randomizingStage = 1;

                randomizedNum = r.nextInt(1000)+1; //We write the chance in hundreds bcs we want the 0.1 perecentage. So 123 is 12.3%

                int additionalSpins = randomizedNum/125;
                if(randomizedNum >= 503 && randomizedNum < 555 ) additionalSpins--;
                additionalSpins += ( 6 - ( (additionalSpins-1)*2 ) )+1;

                hasWon = randomizedNum >= chance;

                goldLifeRouletteTask = new GoldLifeRouletteTask(this, player, additionalSpins);
                goldLifeRouletteTask.runTaskAsynchronously(plugin);

                setMenuItems(player);

            }

            case RED_DYE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;

                player.closeInventory();
            }



        }

    }

    public void stoppedRandomizing(Player player){
        Bukkit.getScheduler().cancelTask(goldLifeRouletteTask.getTaskId());
        goldLifeRouletteTask.stop();
        randomizingStage = 2;

        if(hasWon){
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
            setMenuItems(player);
        }else{
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 2f, 1f);
            setMenuItems(player);
        }

    }

    public boolean isGold(int slot){

        switch (slot){

            case 4,5,14,23 ->{
                return true;
            }

            default -> {
                return false;
            }
        }

    }

    /**
     * The stage of the randomizing animation. <br>
     * The nums mean:
     * <ul>
     *     <li>0 - The animation hasn't started</li>
     *     <li>1 - The animation is playing right now</li>
     *     <li>2 - The animation has finished</li>
     * </ul>
     * @return The animation stage
     */
    public int getRandomizingStage() {
        return randomizingStage;
    }


    public void setPointer(int pointer, Player player){
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        int thisSlot = rouletteSlots[pointer];
        int lastSlot = rouletteSlots[this.pointerIndex == -1 ? 0 : this.pointerIndex];
        openInventory.setItem(thisSlot, isGold(thisSlot) ? goldPointer : normalPointer);
        openInventory.setItem(lastSlot, isGold(lastSlot) ? get : notGet );

        this.pointerIndex = pointer;
    }

    public void setPointerBrown(Player player){
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        ItemStack brownPointer = Utils.createItem(Material.BROWN_STAINED_GLASS_PANE, " ", "brown-pointer");

        openInventory.setItem(rouletteSlots[this.pointerIndex], brownPointer);
    }

    public GoldLifeRouletteTask getThisGoldLifeRouletteTask(){
        return goldLifeRouletteTask;
    }

    public boolean hasWon() {
        return hasWon;
    }



}
