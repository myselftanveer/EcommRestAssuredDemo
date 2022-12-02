package eComm;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.CreateOrderResponse;
import pojo.LoginRequest;
import pojo.LoginResponsePayload;
import pojo.OrderDetails;
import pojo.Orders;
import resources.Specification;

public class EcommApiTest extends Specification {

	static String token = "";
	static String userId = "";
	static String productId = "";
	static List<String> orderId;
	LoginRequest login;

	@Test(priority = 0)
	public void Login() throws FileNotFoundException {
		
		login = new LoginRequest();
		login.setUserEmail("qq@yopmail.com");
		login.setUserPassword("@A12345678a");
		RequestSpecification reqLogin = given().log().all().spec(request()).contentType(ContentType.JSON)
				.body(login);

		LoginResponsePayload response = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract()
				.response().as(LoginResponsePayload.class);

		System.out.println(response.getToken());
		System.out.println(response.getUserId());
		token = response.getToken();
		userId = response.getUserId();
	}

	@Test(priority = 1)
	public void AddProduct() throws FileNotFoundException {

		RequestSpecification createProduct = given().log().all().spec(request())
				.header("authorization", token).param("productName", "Shirt").param("productAddedBy", userId)
				.param("productCategory", "fashion").param("productSubCategory", "shirts")
				.param("productPrice", "12345").param("productDescription", "Originals").param("productFor", "men")
				.multiPart("productImage", new File("C://Users//Tanveer Shaikh//Desktop//Shirt.png"));

		String addProductRes = createProduct.when().post("/api/ecom/product/add-product").then().log().all().extract()
				.response().asString();

		JsonPath js = new JsonPath(addProductRes);
		productId = js.get("productId");

		System.out.println(productId);
	}

	@Test(priority = 2)
	public void CreateOrder() throws FileNotFoundException {

		OrderDetails details = new OrderDetails();
		details.setCountry("India");
		details.setProductOrderedId(productId);
		List<OrderDetails> orderDetail = new ArrayList<OrderDetails>();
		orderDetail.add(details);
		Orders order = new Orders();
		order.setOrders(orderDetail);

		RequestSpecification createOrder = given().log().all().spec(request()).header("authorization", token)
				.contentType(ContentType.JSON).body(order);
		CreateOrderResponse respOrder = createOrder.when().post("/api/ecom/order/create-order").then().log().all().extract()
				.response().as(CreateOrderResponse.class);
		
		System.out.println("==============="+respOrder.getOrders().get(0));
		orderId = respOrder.getOrders();
		
//		String respOrder = createOrder.when().post("/api/ecom/order/create-order").then().log().all().extract()
//				.response().asString();
//		System.out.println(">>>>>>>>>>>>" + respOrder);
//		JsonPath js = new JsonPath(respOrder);
//		orderId = js.get("orders[0]");
//		System.out.println("orderId" + orderId);
	}

	@Test(priority = 3)
	public void ViewOrderDeatils() throws FileNotFoundException {

		RequestSpecification viewOrder = given().log().all().spec(request()).queryParam("id", orderId)
				.header("authorization", token).contentType(ContentType.JSON);

		String viewOrderResponse = viewOrder.when().get("/api/ecom/order/get-orders-details").then().log().all()
				.extract().response().asString();
		System.out.println(viewOrderResponse);
	}

	@Test(priority = 4)
	public void DeleteOrder() throws FileNotFoundException {

		RequestSpecification deleteProdReq = given().log().all().spec(request()).header("authorization", token)
				.contentType(ContentType.JSON).pathParam("productId", productId);

		String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}")
				.then().log().all().extract().response().asString();

		JsonPath js1 = new JsonPath(deleteProductResponse);

		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
	}
}
