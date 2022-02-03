package dev.entree.mcview;

import lombok.Data;

public interface ViewAction {
    Nothing NOTHING = new Nothing();

    class Nothing implements ViewAction {
        private Nothing() {
        }
    }

    @Data
    class Open implements ViewAction {
        private final View view;
    }
}
