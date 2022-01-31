package dev.entree.mcview.page;

public record PageContext(int maxPage, int page) {

    public boolean canNext() {
        return page() < maxPage();
    }

    public boolean canPrev() {
        return page() >= 2;
    }
}
