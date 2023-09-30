package me.trololo11.lifespluginseason3.cardstuff;

import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

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

    protected abstract void createDescription();

    public abstract String getName();

    protected abstract int getCustomModelData();

    public abstract CardType getCardType();

    public abstract float getPercentageChance();

    public ItemStack getCardItem(){

        ItemStack newItem = cardItem.clone();

        ItemMeta cardMeta = newItem.getItemMeta();

        cardMeta.setLocalizedName(cardMeta.getLocalizedName() + ","+ UUID.randomUUID());

        newItem.setItemMeta(cardMeta);
        return newItem;
    }

}
