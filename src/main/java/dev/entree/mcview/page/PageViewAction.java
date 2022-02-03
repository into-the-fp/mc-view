package dev.entree.mcview.page;

import lombok.Data;

public interface PageViewAction {
    class Nothing implements PageViewAction {
    }

    @Data
    class SetPage implements PageViewAction {
        private final int page;
    }

    class NextPage implements PageViewAction {
    }

    class PrevPage implements PageViewAction {

    }
}
