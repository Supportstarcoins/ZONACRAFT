package net.minecraftforge.event.entity.player;

import java.util.List;
import net.minecraft.item.ItemStack;

public class ItemTooltipEvent {
    public ItemStack itemStack;
    public List toolTip;
    public boolean showAdvancedItemTooltips;
}
