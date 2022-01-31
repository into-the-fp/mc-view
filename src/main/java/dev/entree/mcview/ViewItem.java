package dev.entree.mcview;

import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;


public record ViewItem(ItemStack item, Function<ClickEvent, ViewAction> onClick) {
    public static ViewItem only(ItemStack item) {
        return new ViewItem(item, ignore -> ViewAction.NOTHING);
    }

    public static ViewItem consumer(ItemStack item, Consumer<ClickEvent> onClick) {
        return new ViewItem(item, e -> {
            onClick.accept(e);
            return ViewAction.NOTHING;
        });
    }

    public ItemStack getItem() {
        return new ItemStack(item);
    }
}
