package dev.entree.mcview;


import java.util.Map;

public record SimpleView(String title, int row, Map<Integer, ViewItem> items) implements View {
}
