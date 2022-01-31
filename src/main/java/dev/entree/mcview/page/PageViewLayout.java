package dev.entree.mcview.page;

import dev.entree.mcview.ViewItem;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public record PageViewLayout(String title, int row, List<Supplier<ViewItem>> items, List<Integer> slots,
                             Map<Integer, Function<PageContext, PageViewControl>> controls) {
}
