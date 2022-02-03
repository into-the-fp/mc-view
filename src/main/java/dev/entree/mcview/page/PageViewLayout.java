package dev.entree.mcview.page;

import dev.entree.mcview.ViewItem;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
public class PageViewLayout {
    private final String title;
    private final int row;
    private final List<Supplier<ViewItem>> items;
    private final List<Integer> slots;
    private final Map<Integer, Function<PageContext, PageViewControl>> controls;
}
