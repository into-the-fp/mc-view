package dev.entree.mcview.page;

public interface PageViewAction {
    class Nothing implements PageViewAction {
    }

    record SetPage(int page) implements PageViewAction {
    }

    class NextPage implements PageViewAction {
    }

    class PrevPage implements PageViewAction {

    }
}
