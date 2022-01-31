package dev.entree.mcview;

public interface ViewAction {
    Nothing NOTHING = new Nothing();

    class Nothing implements ViewAction {
        private Nothing() {
        }
    }

    record Open(View view) implements ViewAction {
    }
}
