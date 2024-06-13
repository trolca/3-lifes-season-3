package me.trololo11.lifespluginseason3.menus.questawardmenus;

import me.trololo11.lifespluginseason3.LifesPlugin;
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

/**
 * Menu that allows to collect the daily awards from.
 * @see QuestsAwardsManager
 */
public class LifeShardAwardsMenu extends Menu {
    private QuestsMenu questsMenu;
    private RecipesManager recipesManager;
    private QuestsAwardsManager questsAwardsManager; //TODO: add big reward card shards
    private QuestManager questManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public LifeShardAwardsMenu(QuestsMenu previousQuestMenu, RecipesManager recipesManager,QuestManager questManager, QuestsAwardsManager questsAwardsManager){
        this.questsMenu = previousQuestMenu;
        this.recipesManager = recipesManager;
        this.questManager = questManager;
        this.questsAwardsManager = questsAwardsManager;
    }

    @Override
    public String getMenuName() {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Nagrody z questów dziennych";
     }

    @Override
    public int getSlots() {
        return 5*9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");
        ItemStack lifeShard1 = recipesManager.getLifeShardItem();
        ItemStack lifeShard2 = recipesManager.getLifeShardItem();
        ItemStack awardTaken = Utils.createItem(Material.BARRIER, "&4To już zostało odebrane!", "award-taken");
        ItemStack cardItem = Utils.createItem(Material.GHAST_TEAR, "&3Kawałek karty oddania", "card-shard");
        ItemStack completed = Utils.createItem(Material.LIME_STAINED_GLASS_PANE, " ", "completed");
        ItemStack notCompleted = Utils.createItem(Material.RED_STAINED_GLASS_PANE, " ", "not-completed");

        for(int i = 0; i <getSlots(); i++){
            inventory.setItem(i, filler);
        }

        int questsPerAward = questsAwardsManager.getQuestsPerAward(QuestType.DAILY);
        int playerFinishedQuests = questManager.getPlayerFinishedQuests(player, QuestType.DAILY);
        byte howManyTaken = questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.DAILY);
        int howMuchShouldTake = playerFinishedQuests >= questManager.getActiveDailyQuests().size() ? (questsAwardsManager.getMaxAmountOfAwards(QuestType.DAILY)-1) : playerFinishedQuests/questsPerAward;
        int maxAmountOfAwards = questsAwardsManager.getMaxAmountOfAwards(QuestType.DAILY)-1;


        ItemMeta lifeShardTakeMeta = lifeShard1.getItemMeta();
        lifeShardTakeMeta.setDisplayName(Utils.chat("&cKilknij by mnie odebrać!"));
        lifeShardTakeMeta.addEnchant(Enchantment.MENDING, 1, true);
        lifeShardTakeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        Utils.setPrivateName(lifeShardTakeMeta, "lifes-shard-take");

        ItemMeta lifeShardStandardMeta = lifeShard1.getItemMeta();
        lifeShardStandardMeta.setDisplayName(Utils.chat("&4Zrób jeszcze <num> questy by odebrać!"));
        Utils.setPrivateName(lifeShardStandardMeta, "lifes-shard-not-take");

        ItemMeta otherMeta = lifeShard1.getItemMeta();
        otherMeta.setDisplayName(ChatColor.DARK_RED + "Odbierz najpierw poprzednie nagrody!" );
        Utils.setPrivateName(otherMeta, "life-shard-blocked");

        int activeQuestsSize = questManager.getCorrespondingQuestArray(QuestType.DAILY).size();

        lifeShard1 = QuestUtils.getAwardItem(lifeShard1, awardTaken, lifeShardStandardMeta.clone(), lifeShardTakeMeta, otherMeta.clone(), howMuchShouldTake, howManyTaken,
                activeQuestsSize, playerFinishedQuests, 1, maxAmountOfAwards, questsPerAward);

       lifeShard2 = QuestUtils.getAwardItem(lifeShard2, awardTaken, lifeShardStandardMeta.clone(), lifeShardTakeMeta, otherMeta.clone(), howMuchShouldTake, howManyTaken,
               activeQuestsSize, playerFinishedQuests, 2, maxAmountOfAwards, questsPerAward);

        if(howManyTaken == 2){
            lifeShardTakeMeta.setDisplayName(Utils.chat("&bKliknij by mnie odebrać!"));
            Utils.setPrivateName(lifeShardTakeMeta, "card-shard-take");
            cardItem.setItemMeta(lifeShardTakeMeta);
        }else if(howManyTaken < 2){
            lifeShardStandardMeta.setDisplayName(Utils.chat("&3Odbierz jeszcze "+ ( maxAmountOfAwards-howManyTaken  ) +
                    ( howManyTaken == 1 ? " nagrodę" : " nagrody" ) +" by odebrać!"));
            Utils.setPrivateName(lifeShardStandardMeta, "card-shard-blocked");
            cardItem.setItemMeta(lifeShardStandardMeta);
        }else{
            cardItem = awardTaken;
        }


        inventory.setItem(4, cardItem);
        inventory.setItem(38, lifeShard1);
        inventory.setItem(42, lifeShard2);

        for(int i = 20; i < 25; i++){

            if(i < 22) inventory.setItem(i, howManyTaken >= 1 ? completed : notCompleted);
            else inventory.setItem(i, howManyTaken >= 2 ? completed : notCompleted);
        }

        inventory.setItem(13, howManyTaken >= 2 ? completed : notCompleted);
        inventory.setItem(29, howManyTaken >= 1 ? completed : notCompleted);
        inventory.setItem(33, howManyTaken >= 2 ? completed : notCompleted);
        inventory.setItem(8, back);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (item.getType()){
            case RED_DYE -> {
                if(!Utils.isPrivateNameEqual(item, "back")) return;

                questsMenu.open(player);
            }

            case GOLD_NUGGET -> {
                if(!Utils.isPrivateNameEqual(item, "lifes-shard-take")) return;

                if(Utils.isInventoryFull(player.getInventory())){
                    player.sendMessage(ChatColor.RED + "Twój ekwipunek jest pełny i nie można dodać itemu!");
                    return;
                }

                questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.DAILY, (byte) (questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.DAILY)+1) );
                player.getInventory().addItem(recipesManager.getLifeShardItem());
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                setMenuItems(player);

                plugin.getPlayerStats(player).dailyShardsRedeemed++;

            }

            case GHAST_TEAR -> {
                if(!Utils.isPrivateNameEqual(item, "card-shard-take")) return;

                questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.DAILY, (byte) (questsAwardsManager.getAwardsTakenForPlayer(player, QuestType.DAILY)+1 ));
                player.getInventory().addItem(new ItemStack(Material.GHAST_TEAR));
                player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                player.closeInventory();
            }


        }
    }



}
