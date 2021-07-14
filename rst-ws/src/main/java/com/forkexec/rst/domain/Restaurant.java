package com.forkexec.rst.domain;


import com.forkexec.rst.domain.exceptions.*;
import com.forkexec.rst.ws.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Restaurant
 * <p>
 * A restaurant server.
 */
public class Restaurant {
    private Map<String, MenuItem> menus = new ConcurrentHashMap<>();

    private AtomicInteger currentOrderId = new AtomicInteger();

    private Pattern searchTextPattern = Pattern.compile("\\S+");

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Restaurant() {
    }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Restaurant INSTANCE = new Restaurant();
    }

    public static synchronized Restaurant getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private MenuItem createMenuItem(MenuInit m) {
        return new MenuItem(m.getMenu().getId().getId(), m.getMenu().getEntree(), m.getMenu().getPlate(), m.getMenu().getDessert(), m.getMenu().getPrice(), m.getMenu().getPreparationTime(), m.getQuantity());
    }

    private Menu createMenu(MenuItem item) {
        Menu menu = new Menu();

        MenuId id = new MenuId();
        id.setId(item.getId());
        menu.setId(id);

        menu.setEntree(item.getEntree());
        menu.setPlate(item.getPlate());
        menu.setDessert(item.getDessert());
        menu.setPrice(item.getPrice());
        menu.setPreparationTime(item.getPreparationTime());

        return menu;
    }

    private boolean checkMenuId(MenuId menuId) {
        return menuId != null && menuId.getId() != null && menuId.getId().length() != 0;
    }

    private boolean checkMenuInit(MenuInit menuInit) {
        return menuInit != null && menuInit.getMenu() != null && checkMenuId(menuInit.getMenu().getId());
    }

    public void ctrlInit(List<MenuInit> initialMenus) throws BadInitException {
		if (initialMenus == null || !initialMenus.stream().allMatch(this::checkMenuInit))
			throw new BadInitException();
	    menus = initialMenus.stream().collect(Collectors.toConcurrentMap(m -> m.getMenu().getId().getId(), this::createMenuItem));
    }

    public void ctrlClear() {
        menus = new ConcurrentHashMap<>();
        currentOrderId.set(0);
    }

    public Menu getMenu(MenuId menuId) throws BadMenuIdException {
        if (!checkMenuId(menuId))
            throw new BadMenuIdException();
        MenuItem item = menus.get(menuId.getId());
        if (item == null)
            throw new BadMenuIdException();

        return createMenu(item);
    }

    private boolean searchMenuItem(MenuItem item, String desc) {
        return item.getEntree().contains(desc) || item.getPlate().contains(desc) || item.getDessert().contains(desc);
    }

    public List<Menu> searchMenus(String descriptionText) throws BadTextException {
        if (descriptionText == null || !searchTextPattern.matcher(descriptionText).matches())
            throw new BadTextException();
        return menus.values().stream().filter(i -> searchMenuItem(i, descriptionText)).map(this::createMenu).collect(Collectors.toList());
    }

    private MenuOrder createMenuOrder(MenuItem item, int id, int quantity) {
        MenuOrder order = new MenuOrder();

        MenuId menuId = new MenuId();
        menuId.setId(item.getId());
        order.setMenuId(menuId);

        MenuOrderId orderId = new MenuOrderId();
        orderId.setId(Integer.toString(id));
        order.setId(orderId);

        order.setMenuQuantity(quantity);

        return order;
    }

    public MenuOrder orderMenu(MenuId menuId, int quantity) throws BadMenuIdException, BadQuantityException, InsufficientQuantityException {
        if (!checkMenuId(menuId))
            throw new BadMenuIdException();
        MenuItem item = menus.get(menuId.getId());
        if (item == null)
            throw new BadMenuIdException();

        if (quantity < 1)
            throw new BadQuantityException();
        item.subtractStock(quantity);

        return createMenuOrder(item, currentOrderId.getAndAdd(1), quantity);
    }

}
