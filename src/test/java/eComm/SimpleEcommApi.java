package eComm;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponsePayload;
import pojo.OrderDetails;
import pojo.Orders;

public class SimpleEcommApi {

	public static void main(String[] args) {
		
		RequestSpecification req =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		
		LoginRequest login =new LoginRequest();
		login.setUserEmail("qq@yopmail.com");
		login.setUserPassword("@A12345678a");
		
		RequestSpecification reqLogin = given().log().all().spec(req).body(login);
		LoginResponsePayload response = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response().as(LoginResponsePayload.class);
		
		System.out.println(response.getToken()); 
		System.out.println(response.getUserId());
		String token =response.getToken();
		String userId = response.getUserId();
		
//		Add Product
		
		RequestSpecification addProduct =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization", token).build();
		
		RequestSpecification createProduct = given().log().all().spec(addProduct).param("productName","qwerty").param("productAddedBy", userId).param("productCategory", "fashion")
		.param("productSubCategory", "shirts").param("productPrice", "12345").param("productDescription", "Originals")
		.param("productFor", "men").multiPart("productImage",new File("C://Users//Tanveer Shaikh//Downloads//TestCafe_Certificate.jpg"));
		
		String addProductRes = createProduct.when().post("/api/ecom/product/add-product").
		then().log().all().extract().response().asString();
		
		JsonPath js =new JsonPath(addProductRes);
		String productId = js.get("productId");
		
		System.out.println(productId);
		
//		Create Order
		
		RequestSpecification createOrderReq =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).setContentType(ContentType.JSON).build();
		OrderDetails details = new OrderDetails();
		details.setCountry("India");
		details.setProductOrderedId(productId);
		List<OrderDetails> orderDetail = new ArrayList();
		orderDetail.add(details);
		Orders order = new Orders();
		order.setOrders(orderDetail);
		
		RequestSpecification createOrder = given().log().all().spec(createOrderReq).body(order);
		String respOrder= createOrder.when().post("/api/ecom/order/create-order")
		.then().log().all().extract().response().asString();
		System.out.println(respOrder);
		
//Delete Product

		RequestSpecification deleteProdBaseReq=	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.addHeader("authorization", token).setContentType(ContentType.JSON)
		.build();

		RequestSpecification deleteProdReq =given().log().all().spec(deleteProdBaseReq).pathParam("productId",productId);

		String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all().
		extract().response().asString();

		JsonPath js1 = new JsonPath(deleteProductResponse);

		Assert.assertEquals("Product Deleted Successfully",js1.get("message"));

	}

}
