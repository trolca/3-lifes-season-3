package me.trololo11.lifespluginseason3.menus.questawardmenus;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.managers.QuestsAwardsManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.menus.QuestsMenu;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.QuestUtils;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeeklyQuestsAwardsMenu extends Menu {

    private QuestsMenu questsMenu;
    private RecipesManager recipesManager;
    private QuestManager questManager;
    private QuestsAwardsManager questsAwardsManager;

    public WeeklyQuestsAwardsMenu(QuestsMenu questsMenu, RecipesManager recipesManager, QuestManager questManager, QuestsAwardsManager questsAwardsManager){
        this.questsMenu = questsMenu;
        this.questManager = questManager;
        this.recipesManager= recipesManager;
        this.questsAwardsManager = questsAwardsManager;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Nagrody z questów tygodniowych";
    }

    @Override
    public int getSlots() {
        return 5*9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");
        ItemStack reviveCardTemplate = recipesManager.getReviveShardItem();
        ItemStack reviveCardShard1;
        ItemStack reviveCardShard2;
        ItemStack reviveCardShard3;
        ItemStack awardTaken = Utils.createItem(Material.BARRIER, "&4To już zostało odebrane!", "award-taken");
        ItemStack cardItem = Utils.createItem(Material.GHAST_TEAR, "&3Kawałek karty oddania", "card-shard");
        ItemStack completed = Utils.createItem(Material.LIME_STAINED_GLASS_PANE, " ", "completed");
        ItemStack notCompleted = Utils.createItem(Material.RED_STAINED_GLASS_PANE, " ", "not-completed");

        for(int i = 0; i <getSlots(); i++){
            inventory.setItem(i, filler);
        }

        int questsPerAward = questManager.getQuestsPerAwards(QuestType.WEEKLY);
        int playerFinishedQuests = questManager.getPlayerFinishedQuests(player, QuestType.WEEKLY);
        byte howManyTaken = questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.WEEKLY);
        int howMuchShouldTake = playerFinishedQuests >= questManager.getActiveWeeklyQuests().size() ? (questsAwardsManager.getMaxAmountOfAwards(QuestType.WEEKLY)-1) : playerFinishedQuests/questsPerAward;
        int maxAmountOfAwards = questsAwardsManager.getMaxAmountOfAwards(QuestType.WEEKLY)-1;


        int allQuestsSize = questManager.getActiveWeeklyQuests().size();

        ItemMeta takeMeta = reviveCardTemplate.getItemMeta();
        takeMeta.setDisplayName(ChatColor.AQUA + "Kliknij by mnie odebrać!");
        takeMeta.addEnchant(Enchantment.MENDING, 1, false);
        takeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        takeMeta.setLocalizedName("revive-shard-take");

        ItemMeta normalMeta = reviveCardTemplate.getItemMeta();
        normalMeta.setDisplayName(ChatColor.AQUA + "Zrób jeszcze <num> questy by odebrać!");
        normalMeta.setLocalizedName("revive-shard-blocked");

        reviveCardShard1 = QuestUtils.getAwardItem(reviveCardTemplate.clone(), awardTaken, normalMeta.clone(), takeMeta.clone(), howMuchShouldTake, howManyTaken,
                allQuestsSize, playerFinishedQuests, 1, maxAmountOfAwards, questsPerAward);

        reviveCardShard2 = QuestUtils.getAwardItem(reviveCardTemplate.clone(), awardTaken, normalMeta.clone(), takeMeta.clone(), howMuchShouldTake, howManyTaken,
                allQuestsSize, playerFinishedQuests, 2, maxAmountOfAwards, questsPerAward);

        reviveCardShard3 = QuestUtils.getAwardItem(reviveCardTemplate.clone(), awardTaken, normalMeta.clone(), takeMeta.clone(), howMuchShouldTake, howManyTaken,
                allQuestsSize, playerFinishedQuests, 3, maxAmountOfAwards, questsPerAward);

        ItemMeta cardMeta = cardItem.getItemMeta();

        if(howManyTaken == 3){
            cardMeta.setDisplayName(Utils.chat("&bKliknij by mnie odebrać!"));
            cardMeta.setLocalizedName("card-shard-take");
            cardItem.setItemMeta(cardMeta);
        }else if(howManyTaken < 3){
            cardMeta.setDisplayName(Utils.chat("&3Odbierz jeszcze "+ ( maxAmountOfAwards-howManyTaken  ) +
                    ( howManyTaken == 2 ? " nagrode" : " nagrody" ) +" by odebrać!"));
            cardMeta.setLocalizedName("card-shard-blocked");
            cardItem.setItemMeta(cardMeta);
        }else{
            cardItem = awardTaken;
        }

        inventory.setItem(8, back);
        inventory.setItem(4, cardItem);
        inventory.setItem(38, reviveCardShard1);
        inventory.setItem(40, reviveCardShard2);
        inventory.setItem(42, reviveCardShard3);


        for(int i=20; i < 25; i++){

            if(i < 22){
                inventory.setItem(i, howManyTaken >= 1 ? completed : notCompleted);
            }else if(i == 22){
                inventory.setItem(i, howManyTaken >= 2 ? completed : notCompleted);
            }else{
                inventory.setItem(i, howManyTaken >= 3 ? completed : notCompleted);
            }

        }

        inventory.setItem(13, howManyTaken >= 3 ? completed : notCompleted);
        inventory.setItem(29, howManyTaken >= 1 ? completed : notCompleted);
        inventory.setItem(31, howManyTaken >= 2 ? completed : notCompleted);
        inventory.setItem(33, howManyTaken >= 3 ? completed : notCompleted);


    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (item.getType()){

            case RED_DYE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;

                questsMenu.open(player);
            }

            case IRON_NUGGET -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("revive-shard-take")) return;

                questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.WEEKLY, (byte) (questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.WEEKLY)+1) );
                player.getInventory().addItem(recipesManager.getReviveShardItem());
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                setMenuItems(player);
            }

            case GHAST_TEAR -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("card-shard-take")) return;

                questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.WEEKLY, questsAwardsManager.getMaxAmountOfAwards(QuestType.WEEKLY));
                player.getInventory().addItem(new ItemStack(Material.GHAST_TEAR));
                player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                setMenuItems(player);
            }

        }
    }
}
