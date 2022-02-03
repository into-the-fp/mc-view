package dev.entree.mcview.page;

import dev.entree.mcview.View;
import dev.entree.mcview.ViewAction;
import dev.entree.mcview.ViewItem;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
public class PageView implements View {
       private final PageViewLayout layout;
       private final Map<Integer, ViewItem> items;
       private final PageContext context;

    public static PageView from(int page, PageViewLayout layout) {
        Map<Integer, ViewItem> items = new HashMap<>();
        int contentSize = layout.getSlots().size();
        int count = layout.getItems().size();
        int maxPage = count / contentSize + Math.min(count % contentSize, 1);
        int coercedPage = Math.max(Math.min(page, maxPage), 1);
        PageContext ctx = new PageContext(maxPage, coercedPage);
        List<Supplier<ViewItem>> subItemList = pagingList(contentSize, coercedPage, layout.getItems());
        // Contents
        for (int i = 0; i < subItemList.size(); i++) {
            int slot = layout.getSlots().get(i);
            items.put(slot, subItemList.get(i).get());
        }
        // Controls
        for (Map.Entry<Integer, Function<PageContext, PageViewControl>> pair : layout.getControls().entrySet()) {
            PageViewControl control = pair.getValue().apply(ctx);
            items.put(pair.getKey(), new ViewItem(
                    control.getItem(),
                    event -> {
                        PageViewAction action = control.getOnClick().apply(event);
                        if (action instanceof PageViewAction.SetPage) {
                            PageViewAction.SetPage setPage = (PageViewAction.SetPage) action;
                            return new ViewAction.Open(from(setPage.getPage(), layout));
                        } else if (action instanceof PageViewAction.NextPage && ctx.canNext()) {
                            return new ViewAction.Open(from(ctx.getPage() + 1, layout));
                        } else if (action instanceof PageViewAction.PrevPage && ctx.canPrev()) {
                            return new ViewAction.Open(from(ctx.getPage() - 1, layout));
                        } else {
                            return ViewAction.NOTHING;
                        }
                    }
            ));
        }
        return new PageView(layout, items, ctx);
    }

    public PageView withPage(int page) {
        return from(page, getLayout());
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
