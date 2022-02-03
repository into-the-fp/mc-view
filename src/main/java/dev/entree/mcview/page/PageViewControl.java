package dev.entree.mcview.page;

import dev.entree.mcview.ClickEvent;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

@Data
public class PageViewControl {
    private final ItemStack item;
    private final Function<ClickEvent, PageViewAction> onClick;

    public static PageViewControl just(ItemStack item) {
        return new PageViewControl(item, ignore -> new PageViewAction.Nothing());
    }
}
