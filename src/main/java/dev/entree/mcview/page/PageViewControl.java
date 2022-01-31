package dev.entree.mcview.page;

import dev.entree.mcview.ClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public record PageViewControl(ItemStack item, Function<ClickEvent, PageViewAction> onClick) {
    public static PageViewControl just(ItemStack item) {
        return new PageViewControl(item, ignore -> new PageViewAction.Nothing());
    }
}
