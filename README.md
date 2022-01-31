# Pure minecraft chest view model

A pure dataset to express minecraft chest view.

## Initialize

```java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(BukkitView.viewListener(this), this);
    }
}
```

## SimpleView

```java
View subView = ...;

Map<Integer, ViewItem> map = new HashMap<>();
String title = "title";
int row = 1;
map.put(3, new ViewItem(
        new ItemStack(Material.DIAMOND),
        e -> {
            // this view item don't -- also shouldn't -- know how to open view,
            // just tell what view want to open.
            return new ViewAction.Open(subView);
        }
));
SimpleView view = new SimpleView(title, row, map);
BukkitView.openView(view, player);
```

## PageView

WIP

```java
List<Supplier<ViewItem>> items = ...;
List<Integer> slots = ...;
Map<Integer, Function<PageContext, PageViewControl>> controls = ...;
String title = "title";
int row = 6;
PageViewLayout layout = new PageViewLayout(title, row, items, slots, controls);
```

```java
// evaluate a single page from the layout
PageView view = PageView.from(1, layout);
BukkitView.openView(view, player);
```
