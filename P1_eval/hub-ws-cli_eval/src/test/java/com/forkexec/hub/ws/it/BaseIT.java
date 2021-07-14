package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.cli.HubClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Base class for testing a Park Load properties from test.properties
 */
public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;

	protected static HubClient client;
	protected static String RESTAURANT1_ID;
	protected static String RESTAURANT2_ID;
	protected static String FOOD1_RESTAURANTID;
	protected static FoodId FOOD1_ID;
	protected static String FOOD2_RESTAURANTID;
	protected static FoodId FOOD2_ID;
	protected static String FOOD3_RESTAURANTID;
	protected static FoodId FOOD3_ID;

	protected static final String COMMON_DESCRIPTION = "Entrada";
	protected static final String UNKNOWN_DESCRIPTION = "Unknown";

	protected static final String FOOD1_MENUID = "Menu1";
	protected static final String FOOD1_ENTREE = COMMON_DESCRIPTION + "2";
	protected static final String FOOD1_PLATE = "Hambúrguer Muk";
	protected static final String FOOD1_PLATE_WORD = "Muk";
	protected static final String FOOD1_DESSERT = "Gelado";
	protected static final int FOOD1_PRICE = 500;
	protected static final int FOOD1_PREPARATION_TIME = 17;
	protected static final int FOOD1_QUANTITY = 1;

	protected static final String FOOD2_MENUID = "Menu2";
	protected static final String FOOD2_ENTREE = "2x " + COMMON_DESCRIPTION + "2";
	protected static final String FOOD2_PLATE = "Pizza s/ Ananás";
	protected static final String FOOD2_DESSERT = "Ananás";
	protected static final int FOOD2_PRICE = 1000;
	protected static final int FOOD2_PREPARATION_TIME = 20;
	protected static final int FOOD2_QUANTITY = 2;

	protected static final String FOOD3_MENUID = "Menu3";
	protected static final String FOOD3_ENTREE = COMMON_DESCRIPTION + "3";
	protected static final String FOOD3_PLATE = "Pato à Pequim + Arroz Chau-chau";
	protected static final String FOOD3_DESSERT = "Gelado Frito";
	protected static final int FOOD3_PRICE = 800;
	protected static final int FOOD3_PREPARATION_TIME = 10;
	protected static final int FOOD3_QUANTITY = 1;

	protected static final String UNKNOWN_FOOD_ID = "Unknown";
	protected static final String UNKNOWN_FOOD_ENTREE = "Plumbus";

	protected static final String VALID_USER = "sd.test@tecnico.ulisboa";
	protected static final String VALID_USER2 = "sd.test2@tecnico.ulisboa";
	protected static final String UNKNOWN_USER = "Unknown@ist.utl";
	/** "cada utilizador comeca com um saldo de 100 pontos de oferta" */
	protected static final int DEFAULT_USER_POINTS = 100;
	protected static final int USER_POINTS = 1000;
	protected static final String VALID_CC = "4156321808160885";
	protected static final String VALID_CC2 = "3530552847191833";
	protected static final String VALID_CC3 = "6011462234369318";
	protected static final String VALID_CC4 = "341321025768761";
	protected static final String VALID_CC5 = "3589741211455524302";
	protected static final String INVALID_CC = "4156321808160881";

	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		Locale.setDefault(Locale.US);
		testProps = new Properties();
		try {
			testProps.load(BaseIT.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file %s", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		final String uddiEnabled = testProps.getProperty("uddi.enabled");
		final String verboseEnabled = testProps.getProperty("verbose.enabled");

		final String uddiURL = testProps.getProperty("uddi.url");
		final String wsName = testProps.getProperty("ws.name");
		final String wsURL = testProps.getProperty("ws.url");
		RESTAURANT1_ID = testProps.getProperty("rst.ws.name1");
		RESTAURANT2_ID = testProps.getProperty("rst.ws.name2");
		FOOD1_RESTAURANTID = RESTAURANT1_ID;
		FOOD2_RESTAURANTID = RESTAURANT2_ID;
		FOOD3_RESTAURANTID = RESTAURANT1_ID;
		FOOD1_ID = buildFoodId(FOOD1_MENUID, FOOD1_RESTAURANTID);
		FOOD2_ID = buildFoodId(FOOD2_MENUID, FOOD2_RESTAURANTID);
		FOOD3_ID = buildFoodId(FOOD3_MENUID, FOOD3_RESTAURANTID);

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new HubClient(uddiURL, wsName);
		} else {
			client = new HubClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
	}

	protected static FoodInit createFoodInit(Food food, int quantity) {
		FoodInit foodInit = new FoodInit();

		foodInit.setFood(food);
		foodInit.setQuantity(quantity);

		return foodInit;
	}

	protected static Food createFood(String menuId, String restaurantId, String entree, String plate, String dessert, int price, int preparationTime) {
		Food food = new Food();

		FoodId foodId = new FoodId();
		foodId.setMenuId(menuId);
		foodId.setRestaurantId(restaurantId);

		food.setId(foodId);
		food.setEntree(entree);
		food.setPlate(plate);
		food.setDessert(dessert);
		food.setPrice(price);
		food.setPreparationTime(preparationTime);

		return food;
	}

	protected static void hubTestInit() throws InvalidInitFault_Exception {
		List<FoodInit> foods = new ArrayList<>();
		foods.add(createFoodInit(
				createFood(
					FOOD1_MENUID,
					FOOD1_RESTAURANTID,
					FOOD1_ENTREE,
					FOOD1_PLATE,
					FOOD1_DESSERT,
					FOOD1_PRICE,
					FOOD1_PREPARATION_TIME),
				FOOD1_QUANTITY));
		foods.add(createFoodInit(
				createFood(
					FOOD2_MENUID,
					FOOD2_RESTAURANTID,
					FOOD2_ENTREE,
					FOOD2_PLATE,
					FOOD2_DESSERT,
					FOOD2_PRICE,
					FOOD2_PREPARATION_TIME),
				FOOD2_QUANTITY));
		foods.add(createFoodInit(
				createFood(
					FOOD3_MENUID,
					FOOD3_RESTAURANTID,
					FOOD3_ENTREE,
					FOOD3_PLATE,
					FOOD3_DESSERT,
					FOOD3_PRICE,
					FOOD3_PREPARATION_TIME),
				FOOD3_QUANTITY));
		client.ctrlInitFood(foods);
		/* can not call ctrlInitUserPoints here, otherwise we cant test the default user balance */
		//client.ctrlInitUserPoints(USER_POINTS);
	}

	protected static void hubTestClear() {
		client.ctrlClear();
	}

	@Before
	public void setUp() throws InvalidInitFault_Exception {
	    hubTestInit();
	}

	@After
	public void tearDown() {
	    hubTestClear();
	}

	@AfterClass
	public static void cleanup() {
	}

	// Helpers --------------------------------------------------------

	private static boolean checkFood(Food food, String menuId, String restaurantId, String entree, String plate, String dessert, int price, int preparationTime) {
		return food.getId().getMenuId().equals(menuId) &&
				food.getId().getRestaurantId().equals(restaurantId) &&
				food.getEntree().equals(entree) &&
				food.getPlate().equals(plate) &&
				food.getDessert().equals(dessert) &&
				food.getPrice() == price &&
				food.getPreparationTime() == preparationTime;
	}

	protected static boolean isFood1(Food food) {
		return checkFood(food, FOOD1_MENUID, FOOD1_RESTAURANTID, FOOD1_ENTREE, FOOD1_PLATE, FOOD1_DESSERT, FOOD1_PRICE, FOOD1_PREPARATION_TIME);
	}

	protected static boolean isFood2(Food food) {
		return checkFood(food, FOOD2_MENUID, FOOD2_RESTAURANTID, FOOD2_ENTREE, FOOD2_PLATE, FOOD2_DESSERT, FOOD2_PRICE, FOOD2_PREPARATION_TIME);
	}

	protected static boolean isFood3(Food food) {
		return checkFood(food, FOOD3_MENUID, FOOD3_RESTAURANTID, FOOD3_ENTREE, FOOD3_PLATE, FOOD3_DESSERT, FOOD3_PRICE, FOOD3_PREPARATION_TIME);
	}

	protected static FoodId buildFoodId(String menuId, String restaurantId) {
		FoodId foodId = new FoodId();
		foodId.setMenuId(menuId);
		foodId.setRestaurantId(restaurantId);
		return foodId;
	}
}
