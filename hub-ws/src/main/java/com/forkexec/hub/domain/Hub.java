package com.forkexec.hub.domain;


import com.forkexec.hub.domain.exceptions.*;
import com.forkexec.hub.ws.*;
import com.forkexec.hub.ws.cli.CreditCardClient;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.rst.ws.*;
import com.forkexec.rst.ws.cli.RestaurantClient;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hub
 * <p>
 * A restaurants hub server.
 */
public class Hub {
    private AtomicReference<UDDINaming> uddi = new AtomicReference<>();

    private QCPointsHandler pointsHandler;

    private ConcurrentHashMap<String, List<CartItem>> carts = new ConcurrentHashMap<>();

    private AtomicInteger currentOrderId = new AtomicInteger();

    private Pattern userPattern = Pattern.compile("\\S+");

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Hub() {
    }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Hub INSTANCE = new Hub();
    }

    public static synchronized Hub getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Uddi Lookups
     */

    public void setUddi(String uddiURL, int n) throws UddiLookupException {
        try {
            uddi.set(new UDDINaming(uddiURL));
        } catch (UDDINamingException e) {
            throw new UddiLookupException("Unable to create Uddi client", e);
        }
        pointsHandler = new QCPointsHandler(uddi.get(), n);
    }

    private Map<String, RestaurantClient> getRestaurantClients() {
        try {
            return uddi.get().listRecords("T28_Restaurant%").stream()
                .collect(Collectors.toMap(UDDIRecord::getOrgName, r -> new RestaurantClient(r.getUrl())));
        } catch (UDDINamingException e) {
            throw new UddiLookupException("Unable to lookup Restautant clients", e);
        }
    }

    private CreditCardClient getCreditCardClient() {
        try {
            return new CreditCardClient(uddi.get().lookup("CC"));
        } catch (UDDINamingException e) {
            throw new UddiLookupException("Unable to lookup Credit Card client", e);
        }
    }

    /**
     * Create and check views
     */

    private Food createFood(Menu menu, String restaurantId) {
        Food food = new Food();

        FoodId foodId = new FoodId();
        foodId.setMenuId(menu.getId().getId());
        foodId.setRestaurantId(restaurantId);
        food.setId(foodId);

        food.setEntree(menu.getEntree());
        food.setPlate(menu.getPlate());
        food.setDessert(menu.getDessert());
        food.setPrice(menu.getPrice());
        food.setPreparationTime(menu.getPreparationTime());

        return food;
    }

    private MenuInit createMenuInit(FoodInit foodInit) {
        MenuInit menuInit = new MenuInit();

        menuInit.setQuantity(foodInit.getQuantity());

        Menu menu = new Menu();
        MenuId menuId = new MenuId();
        menuId.setId(foodInit.getFood().getId().getMenuId());
        menu.setId(menuId);
        menu.setEntree(foodInit.getFood().getEntree());
        menu.setPlate(foodInit.getFood().getPlate());
        menu.setDessert(foodInit.getFood().getDessert());
        menu.setPrice(foodInit.getFood().getPrice());
        menu.setPreparationTime(foodInit.getFood().getPreparationTime());

        menuInit.setMenu(menu);

        return menuInit;
    }

    private FoodOrderItem createFoodOrderItem(CartItem item) {
        FoodOrderItem foodOrderItem = new FoodOrderItem();

        FoodId foodId = new FoodId();
        foodId.setRestaurantId(item.getRestaurantId());
        foodId.setMenuId(item.getMenuId());
        foodOrderItem.setFoodId(foodId);

        foodOrderItem.setFoodQuantity(item.getQuantity());

        return foodOrderItem;
    }

    private boolean checkFoodId(FoodId foodId) {
        return foodId != null && foodId.getMenuId() != null && foodId.getRestaurantId() != null
            && foodId.getMenuId().length() != 0 && foodId.getRestaurantId().length() != 0;
    }

    private boolean checkFoodInit(FoodInit foodInit) {
        return foodInit != null && foodInit.getFood() != null && checkFoodId(foodInit.getFood().getId());
    }

    /**
     * Impl
     */

    public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitException {
        if (initialFoods == null || !initialFoods.stream().allMatch(this::checkFoodInit))
            throw new InvalidInitException();
        for (Map.Entry<String, RestaurantClient> entry : getRestaurantClients().entrySet()) {
            try {
                entry.getValue().ctrlInit(
                    initialFoods.stream().filter(f -> f.getFood().getId().getRestaurantId().equals(entry.getKey()))
                        .map(this::createMenuInit).collect(Collectors.toList()));
            } catch (com.forkexec.rst.ws.BadInitFault_Exception e) {
                throw new InvalidInitException();
            }
        }
    }

    public void ctrlInitUserPoints(int startPoints) throws InvalidInitException {
        pointsHandler.ctrlInit(startPoints);
    }

    public void ctrlClear() {
        carts = new ConcurrentHashMap<>();
        currentOrderId.set(0);
        pointsHandler.ctrlClear();
        getRestaurantClients().values().forEach(RestaurantClient::ctrlClear);
    }

    public void activateAccount(String userEmail) throws InvalidUserIdException {
        pointsHandler.activateUser(userEmail);
    }

    private int convertToPoints(int ammount) throws InvalidMoneyException {
        switch (ammount) {
            case 10:
                return 1000;
            case 20:
                return 2100;
            case 30:
                return 3300;
            case 50:
                return 5500;
            default:
                throw new InvalidMoneyException();
        }
    }

    public void loadAccount(String userEmail, int moneyToAdd, String creditCardNumber) throws InvalidCreditCardException, InvalidMoneyException, InvalidUserIdException {
        if (userEmail == null || !userPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        if (!getCreditCardClient().validateNumber(creditCardNumber))
            throw new InvalidCreditCardException();

        pointsHandler.addPoints(userEmail, convertToPoints(moneyToAdd));
    }

    public List<Food> searchMenus(String description, Comparator<Food> comparator) throws InvalidTextException {
        Stream.Builder<Stream<Food>> allFoods = Stream.builder();

        for (Map.Entry<String, RestaurantClient> entry : getRestaurantClients().entrySet()) {
            try {
                allFoods.accept(entry.getValue().searchMenus(description).stream().map(m -> createFood(m, entry.getKey())));
            } catch (BadTextFault_Exception e) {
                throw new InvalidTextException();
            }
        }

        return allFoods.build().flatMap(Function.identity()).sorted(comparator).collect(Collectors.toList());
    }

    private int getFoodPrice(FoodId foodId) throws InvalidFoodIdException {
        if (!checkFoodId(foodId))
            throw new InvalidFoodIdException();

        RestaurantClient restaurantClient = getRestaurantClients().get(foodId.getRestaurantId());
        if (restaurantClient == null)
            throw new InvalidFoodIdException();

        MenuId menuId = new MenuId();
        menuId.setId(foodId.getMenuId());

        try {
            return restaurantClient.getMenu(menuId).getPrice();
        } catch (BadMenuIdFault_Exception e) {
            throw new InvalidFoodIdException();
        }
    }

    public void addFoodToCart(String userEmail, FoodId foodId, int foodQuantity) throws InvalidFoodIdException, InvalidFoodQuantityException, InvalidUserIdException {
        if (userEmail == null || !userPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        if (!carts.containsKey(userEmail))
            carts.put(userEmail, new CopyOnWriteArrayList<>());

        if (!checkFoodId(foodId))
            throw new InvalidFoodIdException();

        if (foodQuantity < 1)
            throw new InvalidFoodQuantityException();

        carts.get(userEmail).add(new CartItem(foodId.getRestaurantId(), foodId.getMenuId(), getFoodPrice(foodId), foodQuantity));
    }

    public void clearCart(String userEmail) throws InvalidUserIdException {
        if (userEmail == null || !userPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        carts.remove(userEmail);
    }

    public FoodOrder orderCart(String userEmail) throws EmptyCartException, InvalidUserIdException, NotEnoughPointsException, InvalidFoodQuantityException {
        int balance = pointsHandler.pointsBalance(userEmail);

        if (!carts.containsKey(userEmail) || carts.get(userEmail).isEmpty())
            throw new EmptyCartException();

        List<CartItem> cart = new ArrayList<>(carts.get(userEmail));

        int totalPrice = cart.stream().mapToInt(CartItem::getPrice).sum();
        if (balance < totalPrice)
            throw new NotEnoughPointsException();

        carts.get(userEmail).clear();

        Map<String, RestaurantClient> restaurants = getRestaurantClients();
        for (CartItem item : cart) {
            MenuId menuId = new MenuId();
            menuId.setId(item.getMenuId());
            try {
                restaurants.get(item.getRestaurantId()).orderMenu(menuId, item.getQuantity());
            } catch (BadMenuIdFault_Exception | BadQuantityFault_Exception e) {
                throw new IllegalStateException();
            } catch (InsufficientQuantityFault_Exception e) {
                throw new InvalidFoodQuantityException();
            }
        }

        try {
            pointsHandler.spendPoints(userEmail, cart.stream().mapToInt(CartItem::getPrice).sum());
        } catch (InvalidMoneyException e) {
            throw new IllegalStateException();
        }

        FoodOrder order = new FoodOrder();

        FoodOrderId foodOrderId = new FoodOrderId();
        foodOrderId.setId(Integer.toString(currentOrderId.getAndAdd(1)));
        order.setFoodOrderId(foodOrderId);

        cart.forEach(i -> order.getItems().add(createFoodOrderItem(i)));

        return order;
    }

    public int accountBalance(String userEmail) throws InvalidUserIdException {
        return pointsHandler.pointsBalance(userEmail);
    }

    public Food getFood(FoodId foodId) throws InvalidFoodIdException {
        if (!checkFoodId(foodId))
            throw new InvalidFoodIdException();

        RestaurantClient restaurantClient = getRestaurantClients().get(foodId.getRestaurantId());
        if (restaurantClient == null)
            throw new InvalidFoodIdException();

        MenuId menuId = new MenuId();
        menuId.setId(foodId.getMenuId());

        try {
            return createFood(restaurantClient.getMenu(menuId), foodId.getRestaurantId());
        } catch (BadMenuIdFault_Exception e) {
            throw new InvalidFoodIdException();
        }
    }

    public List<FoodOrderItem> cartContents(String userEmail) throws InvalidUserIdException {
        if (userEmail == null || !userPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        List<FoodOrderItem> contents = new ArrayList<>();

        if (carts.containsKey(userEmail))
            carts.get(userEmail).forEach(i -> contents.add(createFoodOrderItem(i)));

        return contents;
    }
}
