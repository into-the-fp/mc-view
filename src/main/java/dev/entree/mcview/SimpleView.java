package dev.entree.mcview;


import lombok.Data;

import java.util.Map;

@Data
public class SimpleView implements View {
    private final String title;
    private final int row;
    private final Map<Integer, ViewItem> items;
}
