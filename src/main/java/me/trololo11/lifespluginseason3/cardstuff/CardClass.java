package me.trololo11.lifespluginseason3.cardstuff;

import me.trololo11.lifespluginseason3.managers.CardManager;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class represents a custom item of a card that
 * has special abilities that can do a lot of stuff. <br>
 * <b>Every CardClass should be registered in {@link CardManager} to get recognised by this plugin</b>
 * This class stores:
 * <ul>
 *     <li>The name of this card</li>
 *     <li>The {@link CardType} of this card</li>
 *     <li>The custom model data of this card</li>
 *     <li>The chance to get this card stored in percentage</li>
 *     <li>The item of a card</li>
 * </ul>
 */
public abstract class CardClass {

    private ItemStack cardItem;
    protected ArrayList<String> description = new ArrayList<>();

    public CardClass(){
        createDescription();
        createItem();
    }


    private void createItem(){
        cardItem = new ItemStack(Material.PAPER);
        ItemMeta cardMeta = cardItem.getItemMeta();

        cardMeta.setDisplayName(Utils.chat(getName()));
        cardMeta.setLore(description);
        cardMeta.setCustomModelData(getCustomModelData());
        cardMeta.setLocalizedName(getCardType().toString().toLowerCase());

        cardItem.setItemMeta(cardMeta);
    }

    /**
     * Creates the description for the card item. <br>
     * <b>You should use the {@link CardClass#description} array list!</b>
     */
    protected abstract void createDescription();

    public abstract String getName();

    protected abstract int getCustomModelData();

    public abstract CardType getCardType();

    public abstract int getPercentageChance();

    public ItemStack getCardItem(){

        ItemStack newItem = cardItem.clone();

        ItemMeta cardMeta = newItem.getItemMeta();

        cardMeta.setLocalizedName(cardMeta.getLocalizedName() + ","+ UUID.randomUUID());

        newItem.setItemMeta(cardMeta);
        return newItem;
    }

}
