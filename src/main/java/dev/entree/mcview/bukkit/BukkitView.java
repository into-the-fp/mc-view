package dev.entree.mcview.bukkit;

import dev.entree.mcview.*;
import dev.entree.mcview.page.PageView;
import dev.entree.mcview.page.PageViewLayout;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class BukkitView {
    public static void openPageView(PageView view, Player player) {
        PageViewLayout layout = view.layout();
        ViewHolder holder = new ViewHolder();
        holder.setView(view);
        Inventory inv = Bukkit.createInventory(holder, layout.row() * 9, layout.title());
        holder.setInventory(inv);
        for (Map.Entry<Integer, ViewItem> pair : view.items().entrySet()) {
            inv.setItem(pair.getKey(), pair.getValue().getItem());
        }
        player.openInventory(inv);
    }

    public static void openSimpleView(SimpleView view, Player player) {
        ViewHolder holder = new ViewHolder();
        holder.setView(view);
        Inventory inv = Bukkit.createInventory(holder, view.row() * 9, view.title());
        holder.setInventory(inv);
        for (Map.Entry<Integer, ViewItem> pair : view.items().entrySet()) {
            inv.setItem(pair.getKey(), pair.getValue().getItem());
        }
        player.openInventory(inv);
    }

    public static void openView(View view, Player player) {
        if (view instanceof PageView) {
            openPageView(((PageView) view), player);
        } else if (view instanceof SimpleView) {
            openSimpleView(((SimpleView) view), player);
        }
    }

    public static Listener viewListener(Plugin plugin) {
        return new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                Inventory topInv = e.getView().getTopInventory();
                ViewHolder holder = topInv.getHolder() instanceof ViewHolder ? ((ViewHolder) topInv.getHolder()) : null;
                View view = holder != null ? holder.getView() : null;
                if (holder == null || view == null) {
                    return;
                }
                Player p = (Player) e.getWhoClicked();
                if (view instanceof PageView) {
                    PageView pageView = ((PageView) view);
                    ViewItem viewItem = pageView.items().get(e.getRawSlot());
                    if (viewItem != null) {
                        ViewAction action = viewItem.onClick().apply(new ClickEvent(p));
                        if (action instanceof ViewAction.Open) {
                            ViewAction.Open open = (ViewAction.Open) action;
                            Bukkit.getScheduler().runTask(plugin, () -> openView(open.view(), p));
                        }
                    }
                    e.setCancelled(true);
                } else if (view instanceof SimpleView) {
                    SimpleView simpleView = (SimpleView) view;
                    ViewItem viewItem = simpleView.items().get(e.getRawSlot());
                    if (viewItem != null) {
                        ViewAction action = viewItem.onClick().apply(new ClickEvent(p));
                        if (action instanceof ViewAction.Open) {
                            ViewAction.Open open = (ViewAction.Open) action;
                            Bukkit.getScheduler().runTask(plugin, () -> openView(open.view(), p));
                        }
                    }
                    e.setCancelled(true);
                }
            }

            @EventHandler
            public void onDrag(InventoryDragEvent e) {
                Inventory topInv = e.getView().getTopInventory();
                ViewHolder holder = topInv.getHolder() instanceof ViewHolder ? ((ViewHolder) topInv.getHolder()) : null;
                if (holder == null) {
                    return;
                }
                View view = holder.getView();
                if (view instanceof PageView) {
                    PageView pageView = ((PageView) view);
                    if (e.getRawSlots().stream()
                            .anyMatch(a -> pageView.items().get(a) != null)) {
                        e.setCancelled(true);
                    }
                } else if (view instanceof SimpleView) {
                    SimpleView simpleView = ((SimpleView) view);
                    if (e.getRawSlots().stream()
                            .anyMatch(a -> simpleView.items().get(a) != null)) {
                        e.setCancelled(true);
                    }
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {

            }
        };
    }
}
