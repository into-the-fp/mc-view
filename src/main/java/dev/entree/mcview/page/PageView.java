package dev.entree.mcview.page;

import dev.entree.mcview.View;
import dev.entree.mcview.ViewAction;
import dev.entree.mcview.ViewItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public record PageView(PageViewLayout layout, Map<Integer, ViewItem> items, PageContext context) implements View {

    public static PageView from(int page, PageViewLayout layout) {
        Map<Integer, ViewItem> items = new HashMap<>();
        int contentSize = layout.slots().size();
        int count = layout.items().size();
        int maxPage = count / contentSize + Math.min(count % contentSize, 1);
        int coercedPage = Math.max(Math.min(page, maxPage), 1);
        PageContext ctx = new PageContext(maxPage, coercedPage);
        List<Supplier<ViewItem>> subItemList = pagingList(contentSize, coercedPage, layout.items());
        // Contents
        for (int i = 0; i < subItemList.size(); i++) {
            int slot = layout.slots().get(i);
            items.put(slot, subItemList.get(i).get());
        }
        // Controls
        for (Map.Entry<Integer, Function<PageContext, PageViewControl>> pair : layout.controls().entrySet()) {
            PageViewControl control = pair.getValue().apply(ctx);
            items.put(pair.getKey(), new ViewItem(
                    control.item(),
                    event -> {
                        PageViewAction action = control.onClick().apply(event);
                        if (action instanceof PageViewAction.SetPage) {
                            PageViewAction.SetPage setPage = (PageViewAction.SetPage) action;
                            return new ViewAction.Open(from(setPage.page(), layout));
                        } else if (action instanceof PageViewAction.NextPage && ctx.canNext()) {
                            return new ViewAction.Open(from(ctx.page() + 1, layout));
                        } else if (action instanceof PageViewAction.PrevPage && ctx.canPrev()) {
                            return new ViewAction.Open(from(ctx.page() - 1, layout));
                        } else {
                            return ViewAction.NOTHING;
                        }
                    }
            ));
        }
        return new PageView(layout, items, ctx);
    }

    public PageView withPage(int page) {
        return from(page, layout());
    }

    public static <T> List<T> pagingList(int elementSize, int page, List<T> list) {
        int start = (page - 1) * elementSize;
        int end = page * elementSize;
        return list.isEmpty()
                ? Collections.emptyList()
                : list.subList(
                Math.min(Math.max(start, 0), list.size()),
                Math.min(Math.max(end, 0), list.size()));
    }
}
