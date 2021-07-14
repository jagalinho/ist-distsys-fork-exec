package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.*;
import com.forkexec.rst.ws.cli.RestaurantClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Base class for testing a Park Load properties from test.properties
 */
public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";

	protected static final String MENU1_ID = "Big Muk";
	protected static final String MENU1_ENTREE = "4 douradinhos de frango";
	protected static final String MENU1_PLATE = "Hambúrguer Muk";
	protected static final int MENU1_PRICE = 500;
	protected static final int MENU1_PREPARATION_TIME = 7;
	protected static final String SHARED_DESSERT = "Gelado";
	protected static final int MENU1_QUANTITY = 1;

	protected static final String MENU2_ID = "Pizza Party";
	protected static final String MENU2_ENTREE = "Grissini";
	protected static final String MENU2_PLATE = "Pizza s/ Ananás";
	protected static final String MENU2_DESSERT = "Ananás";
	protected static final int MENU2_PRICE = 1000;
	protected static final int MENU2_PREPARATION_TIME = 20;
	protected static final int MENU2_QUANTITY = 2;

	protected static final String MENU3_ID = "Oriental Explosion";
	protected static final String MENU3_ENTREE = "2 Crepes";
	protected static final String MENU3_PLATE = "Pato à Pequim + Arroz Chau-chau";
	protected static final int MENU3_PRICE = 800;
	protected static final int MENU3_PREPARATION_TIME = 10;
	protected static final int MENU3_QUANTITY = 1;

	protected static final String MENU4_ID = "Dieta";
	protected static final String MENU4_ENTREE = "Azeitonas";
	protected static final String MENU4_PLATE = "Salada Mista com orégãos, queijo Mozzarela de búfala, Rúcula, Ananás e Tomate Cherry";
	/** palavra do MENU4_PLATE para testar a pesquisa */
	protected static final String MENU4_PLATE_PARTIAL = "orégãos";
	protected static final String MENU4_DESSERT = "Maça/Uvas";
	protected static final int MENU4_PRICE = 455;
	protected static final int MENU4_PREPARATION_TIME = 5;
	protected static final int MENU4_QUANTITY = 11;

	protected static final String SHARED_WORD = "Ananás";

	protected static final String UNKNOWN_MENU_ID = "Unknown";
	protected static final String UNKNOWN_MENU_ENTREE = "Plumbus";

	protected static Properties testProps;

	protected static RestaurantClient client;

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

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new RestaurantClient(uddiURL, wsName);
		} else {
			client = new RestaurantClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
	}

	@AfterClass
	public static void cleanup() {
	}

	protected static MenuInit createMenuInit(Menu menu, int quantity) {
		MenuInit menuinit = new MenuInit();

		menuinit.setMenu(menu);
		menuinit.setQuantity(quantity);

		return menuinit;
	}

	protected static Menu createMenu(String id, String entree, String plate, String dessert, int price, int preparationTime) {
		Menu menu = new Menu();

		MenuId menuId = new MenuId();
		menuId.setId(id);

		menu.setId(menuId);
		menu.setEntree(entree);
		menu.setPlate(plate);
		menu.setDessert(dessert);
		menu.setPrice(price);
		menu.setPreparationTime(preparationTime);

		return menu;
	}

	protected static void restaurantTestInit() throws BadInitFault_Exception {
		List<MenuInit> menus = new ArrayList<>();
		menus.add(createMenuInit(createMenu(MENU1_ID,
				MENU1_ENTREE,
				MENU1_PLATE,
				SHARED_DESSERT,
				MENU1_PRICE,
				MENU1_PREPARATION_TIME),
				MENU1_QUANTITY));
		menus.add(createMenuInit(createMenu(MENU2_ID,
				MENU2_ENTREE,
				MENU2_PLATE,
				MENU2_DESSERT,
				MENU2_PRICE,
				MENU2_PREPARATION_TIME),
				MENU2_QUANTITY));
		menus.add(createMenuInit(createMenu(MENU3_ID,
				MENU3_ENTREE,
				MENU3_PLATE,
				SHARED_DESSERT,
				MENU3_PRICE,
				MENU3_PREPARATION_TIME),
				MENU3_QUANTITY));
		menus.add(createMenuInit(createMenu(MENU4_ID,
				MENU4_ENTREE,
				MENU4_PLATE,
				MENU4_DESSERT,
				MENU4_PRICE,
				MENU4_PREPARATION_TIME),
				MENU4_QUANTITY));
		client.ctrlInit(menus);
	}

	protected static void restaurantTestClear() {
		client.ctrlClear();
	}

    @Before
    public void setUp() throws BadInitFault_Exception {
        restaurantTestInit();
    }

    @After
    public void tearDown() {
        restaurantTestClear();
    }

	private static boolean checkMenu(Menu menu, String id, String entree, String plate, String dessert, int price, int preparationTime) {
		return menu.getId().getId().equals(id) &&
				menu.getEntree().equals(entree) &&
				menu.getPlate().equals(plate) &&
				menu.getDessert().equals(dessert) &&
				menu.getPrice() == price &&
				menu.getPreparationTime() == preparationTime;
	}

	protected static void checkMenuOrder(MenuOrder order, int quantity, String id) {
		assertEquals(order.getMenuId().getId(), id);
		assertEquals(order.getMenuQuantity(), quantity);
		assertNotNull(order.getId() /*MenuOrderId*/);
		assertNotNull(order.getId().getId() /* string */);
		assertFalse(order.getId().getId().trim().isEmpty());
	}

	protected static boolean isMenu1(Menu menu) {
		return checkMenu(menu, MENU1_ID, MENU1_ENTREE, MENU1_PLATE, SHARED_DESSERT, MENU1_PRICE, MENU1_PREPARATION_TIME);
	}

	protected static boolean isMenu2(Menu menu) {
		return checkMenu(menu, MENU2_ID, MENU2_ENTREE, MENU2_PLATE, MENU2_DESSERT, MENU2_PRICE, MENU2_PREPARATION_TIME);
	}

	protected static boolean isMenu3(Menu menu) {
		return checkMenu(menu, MENU3_ID, MENU3_ENTREE, MENU3_PLATE, SHARED_DESSERT, MENU3_PRICE, MENU3_PREPARATION_TIME);
	}
	protected static boolean isMenu4(Menu menu) {
		return checkMenu(menu, MENU4_ID, MENU4_ENTREE, MENU4_PLATE, MENU4_DESSERT, MENU4_PRICE, MENU4_PREPARATION_TIME);
	}
}
