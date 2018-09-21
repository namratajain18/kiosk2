package com.cusbee.kiosk.controller.api.customer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.cusbee.kiosk.bean.OrderBean;
import com.cusbee.kiosk.bean.dto.IngredientDTO;
import com.cusbee.kiosk.bean.dto.MenueItemDTO;
import com.cusbee.kiosk.bean.dto.OrderDTO;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.controller.api.customer.payment.Encryption;
import com.cusbee.kiosk.entity.Locations;
import com.cusbee.kiosk.entity.OrderDetails;
import com.cusbee.kiosk.entity.Orders;
import com.cusbee.kiosk.entity.Payments;
import com.cusbee.kiosk.enums.CategoryType;
import com.cusbee.kiosk.enums.OrderStatus;
import com.cusbee.kiosk.enums.PaymentStatus;
import com.cusbee.kiosk.repository.MenuItemsRepository;
import com.cusbee.kiosk.repository.OrdersRepository;
import com.cusbee.kiosk.repository.PaymentsRepository;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by ahorbat on 12.02.17.
 */
@Api("Customer API")
@RestController
@RequestMapping(value = "/api/customer")
@PropertySource("classpath:application.properties")
public class CustomerOrderController {

	private final static Logger LOGGER = LoggerFactory.getLogger(CustomerOrderController.class);

	/*
	 * private static final String GRAND_CENTER_EMAIL =
	 * "tritipgrillnycgrand@gmail.com"; private static final String
	 * ROCKEFELLER_EMAIL = "tritipgrillnyc@gmail.com"; private static final String
	 * SAN_FRANCISCO_EMAIL = "sfmarket@buckhornbbq.com";
	 */

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private MenuItemsRepository menuItemsRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine htmlTemplateEngine;

	@Value("${email_1}")
	String GRAND_CENTER_EMAIL;

	@Value("${email_2}")
	String ROCKEFELLER_EMAIL;

	@Value("${email_3}")
	String SAN_FRANCISCO_EMAIL;

	@Value("${email_4}")
	String MARKET_STREET_EMAIL;

	@Value("${other_email}")
	String OTHER_EMAIL;

	@Value("${logo_loc_1}")
	String LOGO_LOC_1;

	@Value("${logo_loc_2}")
	String LOGO_LOC_2;

	@Value("${logo_loc_4}")
	String LOGO_LOC_4;

	@Value("${logo_loc_default}")
	String LOGO_LOC_DEFAULT;

	@Value("${tel_loc_1}")
	String TEL_LOC_1;

	@Value("${tel_loc_2}")
	String TEL_LOC_2;

	@Value("${tel_loc_4}")
	String TEL_LOC_4;

	@Value("${tel_loc_default}")
	String TEL_LOC_DEFAULT;

	@Value("${address_loc_1}")
	String ADDRESS_LOC_1;

	@Value("${address_loc_2}")
	String ADDRESS_LOC_2;

	@Value("${address_loc_4}")
	String ADDRESS_LOC_4;

	@Value("${address_loc_default}")
	String ADDRESS_LOC_DEFAULT;

	private static final String ACCOUNT_SID = "AC15460338c5deac47051b5bc5336f0982";
	private static final String AUTH_TOKEN = "063670cb8a2a711693e40b43783838c8";

	@ApiOperation(value = "This API service is acceptable only for CUSTOMER user and can be reached from KIOSK app. Service is saving a new order", notes = "No Notes please", response = OrderDTO.class)
	@Transactional
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public ResponseContainer<OrderBean> newOrder(@RequestBody OrderDTO orders,
			@RequestParam(value = "sms", required = true) boolean sms, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		try {
			LOGGER.info("ORDER_DTO OBTAINED: ", orders);
			/*
			 * orders.setPaymentData(
			 * "{\"Status\":\"APPROVED\",\"AmountApproved\":\"6.01\",\"AuthorizationCode\":\"EMV601\",\"CardHolder\":\"Demo/Verifone\",\"Cardholder\":\"Demo/Verifone\",\"AccountNumber\":\"************0128\",\"PaymentType\":\"MASTERCARD\",\"EntryMode\":\"SWIPE\",\"ErrorMessage\":\"\",\"Token\":\"1246726422\",\"TransactionDate\":\"9/10/2018 1:30:16 PM\",\"TransactionType\":\"SALE\",\"ResponseType\":\"SINGLE\",\"ValidationKey\":\"70469608-9f5b-47e6-ba40-17889f1f141e\",\"AdditionalParameters\":{\"SignatureData\":\"77,56^75,56^75,56^74,55^74,55^74,54^74,53^75,52^77,50^79,48^81,45^85,42^89,38^93,34^98,29^103,25^108,20^113,16^118,13^122,9^126,6^129,5^131,3^132,3^132,4^132,5^130,8^127,10^124,14^121,18^118,22^114,26^110,31^107,35^104,39^101,43^100,46^99,49^99,51^100,52^103,52^106,52^111,51^116,50^122,48^128,46^135,44^141,43^147,41^147,41^0,65535^~\",\"AmountDetails\":{\"UserTip\":\"0.00\",\"Cashback\":\"0.00\",\"Donation\":\"0.00\",\"Surcharge\":\"0.00\",\"Discount\":{\"Total\":\"0.00\"}},\"EMV\":{\"ApplicationInformation\":{\"Aid\":\"A0000000031010\",\"ApplicationLabel\":\"VISA VSDC37\",\"ApplicationExpiryDate\":\"12/31/2030\",\"ApplicationEffectiveDate\":\"\",\"ApplicationInterchangeProfile\":\"3C00\",\"ApplicationVersionNumber\":\"96\",\"ApplicationTransactionCounter\":\"EA\",\"ApplicationUsageControl\":\"\",\"ApplicationDisplayName\":\"VISA VSDC37\"},\"CardInformation\":{\"MaskedPan\":\"************0128\",\"PanSequenceNumber\":\"0\",\"CardExpiryDate\":\"12/30\"},\"ApplicationCryptogram\":{\"CryptogramType\":\"TC\",\"Cryptogram\":\"C8CA3D19415D1FD3\"},\"CvmResults\":\"5E0300\",\"IssuerApplicationData\":\"06010A03689804\",\"TerminalVerificationResults\":\"0800088040\",\"UnpredictableNumber\":\"26A2730B\",\"Amount\":{\"AmountAuthorized\":\"6.01\",\"AmountOther\":\"0.00\"},\"PosEntryMode\":\"05\",\"TerminalInformation\":{\"TerminalType\":\"22\",\"IfdSerialNumber\":\"84638790\",\"TerminalCountryCode\":\"0840\",\"TerminalID\":\"84638790\",\"TerminalActionCodeDefault\":\"DC4000A800\",\"TerminalActionCodeDenial\":\"0010000000\",\"TerminalActionCodeOnline\":\"DC4004F800\"},\"TransactionInformation\":{\"TransactionType\":\"00\",\"TransactionCurrencyCode\":\"840\",\"TransactionStatusInformation\":\"F800\"},\"CryptogramInformationData\":\"40\",\"PINStatement\":\"PIN Bypassed\",\"CvmMethod\":\"signature\",\"IssuerActionCodeDefault\":\"F040008800\",\"IssuerActionCodeDenial\":\"0010000000\",\"IssuerActionCodeOnline\":\"F040009800\",\"AuthorizationResponseCode\":\"00\"}}}"
			 * );
			 */ List<MenueItemDTO> menuItemDtoList = orders.getMenuItems();
			if (orders.getMenuItems() != null && !orders.getMenuItems().isEmpty()) {

				for (MenueItemDTO menueItemDTO : menuItemDtoList) {
					LOGGER.info("MENUID: ", menueItemDTO.getId());
					System.out.println("MENUID: " + menueItemDTO.getId());

					LOGGER.info("MENUNAME: ", menueItemDTO.getName());
					System.out.println("MENUNAME: " + menueItemDTO.getName());

					LOGGER.info("INGSIZE: ", menueItemDTO.getIngredients().size());
					System.out.println("INGSIZE: " + menueItemDTO.getIngredients().size());
					if (menueItemDTO.getIngredients() != null && !menueItemDTO.getIngredients().isEmpty()) {
						for (IngredientDTO ingredientDTO : menueItemDTO.getIngredients()) {
							System.out.println("INGPRICE: " + ingredientDTO.getPrice());
							System.out.println("INGNAME: " + ingredientDTO.getName());
							LOGGER.info("INGNAME: ", ingredientDTO.getName());
							LOGGER.info("DTO", ingredientDTO);
							System.out.println("DTO" + ingredientDTO);

						}
					}
				}
			}

			String warnMsg = null;
			if (!"kiosk".equalsIgnoreCase(orders.getSource())) {
				final Payments payment = paymentsRepository.findOne(orders.getPaymentId());

				System.out.println("=============> " + orders.getPackageType() + "  " + orders.getOrderDeliveryPrice()
						+ "  " + orders.getOrderDeliveryPrice() + "  " + orders.getOrderPriceSubTotal() + "  "
						+ orders);

				if (payment == null) {
					warnMsg = "There is something wrong with order's payment! It's not found!";
					LOGGER.info("COMPARE_ORDER_PRICE: " + warnMsg);
				} else if (orders.getOrderPrice() != ((double) payment.getAmount() / 100)) {
					warnMsg = String.join(" ", "There is something wrong with customer's order. Payment amount is ",
							String.valueOf(((double) payment.getAmount() / 100)), " but order full price is ",
							String.valueOf(orders.getOrderPrice()));
					LOGGER.info("COMPARE_ORDER_PRICE: " + warnMsg);
				} else {
					LOGGER.info("COMPARE_ORDER_PRICE: " + orders.getOrderPrice() + " != "
							+ ((double) payment.getAmount() / 100));
				}
			}

			Date from = DateTime.now().withTimeAtStartOfDay().toDate();
			Date to = DateTime.now().toDate();
			List<Orders> ordersList = ordersRepository.findAll();

			long activeOrders = ordersRepository.findByOrderDateBetween(from, to, new PageRequest(1, 50)).getContent()
					.stream()
					.filter(el -> el.getOrderStatus().getDescription().equals(OrderStatus.IN_PROGRESS.getDescription()))
					.count();
			// orders.setWarnMsg(warnMsg);
			Orders order = ordersRepository.save(createOrder(orders, activeOrders, ordersList));
			int code = HttpStatus.OK.value();
			String message;

			if (!sms) {
				try {
					LOGGER.error("BEFORE");
					LOGGER.error("httpRequest", httpRequest);
					LOGGER.error("httpResponse", httpResponse);
					LOGGER.error("order", order.getId());
					LOGGER.error("orders.getEmail()", orders.getEmail());
					final Locations location = menuItemsRepository.findById(orders.getMenuItems().get(0).getId())
							.getCategory().getMenus().getLocations();
					LOGGER.error("location", location);

					sendEmail(orders.getEmail(), httpRequest, httpResponse, order, orders, location, warnMsg);
					message = String.format("Order for %s with phone no %s created successfully", order.getName(),
							order.getPhone());
					LOGGER.error("orders.getEmail()", orders.getEmail());
					LOGGER.error("AFTER");
				} catch (MessagingException ex) {
					message = String.format(
							"Order for %s with phone no %s created successfully but email was not send to recipient %s with error message %s",
							order.getName(), order.getPhone(), orders.getEmail(),
							Throwables.getRootCause(ex).getMessage());
					code = HttpStatus.BAD_REQUEST.value();
				}
			} else {
				LOGGER.error(" BEFORE", order.getPaymentStatus());
				String body;
				if (order.getPaymentStatus() == PaymentStatus.PAYED_BY_CACHE) {
					body = String.format(
							"Thanks for the order, your order number is %s and your total bill is %s$. Please pay at the Pick Up counter while picking up the order.",
							order.getId(), order.getOrderPrice());
				} else {
					body = String.format(
							"Thanks for the order, your order number is %s and your total bill is %s$. Your order will be ready in 5-8 Minutes.",
							order.getId(), order.getOrderPrice());
				}
				sendSms(body, order.getPhone());

				message = String.format("Order for %s with phone no %s created successfully", order.getName(),
						order.getPhone());
				LOGGER.error(" AFTER", order.getPaymentStatus());
			}
			LOGGER.error("RESPONSE DONE");
			return ResponseContainer.Builder.<OrderBean>builder().data(OrderBean.toOrderBean(order)).code(code)
					.message(message).build();
		} catch (Exception e) {
			LOGGER.info("EXCEPTION: ", e);
			throw e;
		}
	}

	private Orders createOrder(OrderDTO orders, long activeOrders, List<Orders> ordersList) {
		LOGGER.error("BEFORE");
		Orders order = new Orders();
		order.setName(orders.getName());
		order.setEmail(orders.getEmail());
		order.setPhone(orders.getPhone());
		order.setOrderPrice(BigDecimal.valueOf(orders.getOrderPrice()));
		order.setPackageType(orders.getPackageType());
		order.setPaymentStatus(orders.getPaymentStatus());
		order.setOrderStatus(OrderStatus.IN_PROGRESS);
		order.setPaymentId(orders.getPaymentId());
		order.setOrderPriceTax(BigDecimal.valueOf(orders.getOrderPriceTax()));
		order.setCountDown(createCountDown(activeOrders).toDate());
		DateTime dt = new DateTime(orders.getOrderDate());
		order.setOrderDate(dt.plusHours(7).toDate());
		order.setTable(orders.getTable());
		order.setAddress(orders.getAddress());
		order.setSource(orders.getSource());
		order.setPaymentData(orders.getPaymentData());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(orders);
			byte[] orderdetail = baos.toByteArray();
			Blob blob = new SerialBlob(orderdetail);
			order.setOrderDetailsBlob(blob);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SerialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (ordersList != null && !ordersList.isEmpty()) {
			Orders lastOrder = ordersList.get(ordersList.size() - 1);
			DateTime lastOrderTime = new DateTime(lastOrder.getOrderDate());
			if (lastOrderTime.isBefore(new DateTime().now().withTimeAtStartOfDay())) {
				order.setOrderId(1);
			} else {
				order.setOrderId(Iterables.getLast(ordersList).getOrderId() + 1);
			}
		} else {
			order.setOrderId(1);
		}

		List<MenueItemDTO> menuItemDtoList = orders.getMenuItems();
		List<OrderDetails> orderDetailsList = new ArrayList<>();

		LOGGER.info("MENU ITEM LIST ", menuItemDtoList);

		groupByCategoryType(menuItemDtoList).forEach((categoryType, items) -> {
			List<MenueItemDTO> list = categoryType == CategoryType.STANDARD ? flatten(items) : items;

			List<OrderDetails> orderDetails = list.stream().map(item -> createOrderDetails(order, item, categoryType))
					.flatMap(List::stream).collect(Collectors.toList());

			orderDetailsList.addAll(orderDetails);
			LOGGER.error("orderDetailsList", orderDetailsList);
		});

		order.setOrderDetailsList(orderDetailsList);
		LOGGER.error("AFTER");
		return order;
	}

	private List<OrderDetails> createOrderDetails(Orders orderEntity, MenueItemDTO item, CategoryType type) {
		LOGGER.error("createOrderDetails ", orderEntity.getEmail());
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setItemId(item.getId());
		orderDetails.setCount(item.getCount());
		orderDetails.setServe(item.getServe());
		orderDetails.setSize(item.getSize());
		LOGGER.error("getExtras ", item.getExtras());
		if (item.getExtras() != null && !item.getExtras().isEmpty()) {
			StringBuilder extras = new StringBuilder();
			item.getExtras().forEach(e -> {
				extras.append(e.getName() + ",");
			});
			orderDetails.setExtras(extras.toString());
		}
		LOGGER.error("item.getInstructions() ", item.getInstructions());
		if (item.getInstructions() != null && !item.getInstructions().isEmpty()) {
			orderDetails.setInstructions(item.getInstructions());
		}

		if (item.getTemperature() != null && !item.getTemperature().isEmpty()) {
			orderDetails.setTemperature(item.getTemperature());
		}
		LOGGER.error("item.getModifiers() ", item.getModifiers());
		if (item.getModifiers() != null && !item.getModifiers().isEmpty()) {
			StringBuilder modifiers = new StringBuilder();
			item.getModifiers().forEach(e -> {
				modifiers.append(e.getName() + ",");
			});
			orderDetails.setModifiers(modifiers.toString());
		}

		/*
		 * orderDetails.setExtraIds(item.getExtras() .stream() .map(ExtrasDTO::getId)
		 * .map(String::valueOf) .collect(Collectors.joining(",")));
		 */
		orderDetails.setOrders(orderEntity);
		LOGGER.error("item.getModifiers() ", item.getModifiers());
		String options = item.getIngredients().stream().filter(type::isAppendable).map(IngredientDTO::getName)
				// .map(type.getStopWord()::concat)
				.collect(Collectors.joining(" ")).trim();

		/*
		 * StringBuilder extras = new StringBuilder(); if(!item.getExtras().isEmpty()){
		 * extras.append("Extras "); for(ExtrasDTO extra: item.getExtras()) {
		 * extras.append(extra. extras.append(extra.getName()); getName()); } }
		 */
		/*
		 * String optionsExtras = item.getExtras().stream()
		 * .filter(type::isExtraAppendable) .map(ExtrasDTO::getName)
		 * .map(CategoryType.EXTRA.getStopWord()::concat)
		 * .collect(Collectors.joining(" ")) .trim();
		 */

		String title = options.isEmpty() ? item.getName() : String.join(type.getBegin(), item.getName(), options);

		orderDetails.setTitle(title);
		if (item.getQuantity() > 1) {
			return IntStream.range(0, item.getQuantity()).mapToObj(iter -> cloneOrderDetails(orderDetails))
					.collect(Collectors.toList());
		}
		return Collections.singletonList(orderDetails);
	}

	private OrderDetails cloneOrderDetails(OrderDetails orderDetails) {
		OrderDetails target = new OrderDetails();
		BeanUtils.copyProperties(orderDetails, target);
		return target;
	}

	private DateTime createCountDown(long activeOrders) {
		DateTime dt = new DateTime(Date.from(Instant.now()));

		if (activeOrders <= 8) {
			return dt.plusMinutes(6);
		} else if (8 < activeOrders && activeOrders <= 10) {
			return dt.plusMinutes(9);
		} else if (10 < activeOrders && activeOrders <= 15) {
			return dt.plusMinutes(12);
		} else if (15 < activeOrders && activeOrders <= 20) {
			return dt.plusMinutes(15);
		} else if (20 < activeOrders && activeOrders <= 25) {
			return dt.plusMinutes(25);
		} else {
			return dt.plusMinutes(30);
		}
	}

	private void sendSms(String text, String number) {
		try {
			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

			// Build a filter for the MessageList
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("To", number)); // Add real number here
			params.add(new BasicNameValuePair("Body", text));
			// params.add(new BasicNameValuePair("To", "+380990469063")); //Add real number
			// here
			params.add(new BasicNameValuePair("From", "+19177463308"));

			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			Message message = messageFactory.create(params);
			System.out.println(message.getSid());
		} catch (TwilioRestException e) {
			System.out.println(e.getErrorMessage());
		}
	}

	private void sendEmail(String recipientEmail, HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			Orders orders, OrderDTO orderDTO, Locations locations, String warnMsg) throws MessagingException {
		LOGGER.error("SEND EMAIL BEFORE");
		final WebContext ctx = new WebContext(httpRequest, httpResponse, httpRequest.getServletContext());
		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		final MimeMessage serviceMimeMessage = javaMailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		final MimeMessageHelper serviceMessage = new MimeMessageHelper(serviceMimeMessage, "UTF-8");
		message.setSubject("New Kitchen Order " + locations.getName() + " # " + orders.getId());
		message.setFrom("noreply.tritipgrill@gmail.com");
		message.setTo(recipientEmail);
		serviceMessage.setSubject("New Kitchen Order " + locations.getName() + " # " + orders.getId());
		serviceMessage.setFrom("noreply.tritipgrill@gmail.com");
		String emailTo;

		/*
		 * if (StringUtils.containsIgnoreCase(locations.getName(), "grand center")) {
		 * emailTo = GRAND_CENTER_EMAIL; } else if
		 * (StringUtils.containsIgnoreCase(locations.getName(), "rock")) { emailTo =
		 * ROCKEFELLER_EMAIL; } else { emailTo = SAN_FRANCISCO_EMAIL; }
		 */

		if (locations.getId() == 1) {
			emailTo = GRAND_CENTER_EMAIL;
		} else if (locations.getId() == 2) {
			emailTo = ROCKEFELLER_EMAIL;
		} else if (locations.getId() == 3) {
			emailTo = SAN_FRANCISCO_EMAIL;
		} else if (locations.getId() == 4) {
			emailTo = MARKET_STREET_EMAIL;
		} else {
			emailTo = OTHER_EMAIL;
		}
		serviceMessage.setTo(emailTo);
		// Create the HTML body using Thymeleaf
		ctx.setVariable("restaurant", locations.getName());
		orders.getPackageType().getDescription();
		if (locations.getId() == 1) {
			ctx.setVariable("logo", LOGO_LOC_1);
			ctx.setVariable("tel", TEL_LOC_1);
			ctx.setVariable("address", ADDRESS_LOC_1);
		} else if (locations.getId() == 2) {
			ctx.setVariable("logo", LOGO_LOC_2);
			ctx.setVariable("tel", TEL_LOC_2);
			ctx.setVariable("address", ADDRESS_LOC_2);
		} else if (locations.getId() == 4) {
			ctx.setVariable("logo", LOGO_LOC_4);
			ctx.setVariable("tel", TEL_LOC_4);
			ctx.setVariable("address", ADDRESS_LOC_4);
		} else {
			ctx.setVariable("logo", LOGO_LOC_DEFAULT);
			ctx.setVariable("tel", TEL_LOC_DEFAULT);
			ctx.setVariable("address", ADDRESS_LOC_DEFAULT);
		}

		List<MenueItemDTO> menuItemDtoList = orderDTO.getMenuItems();
		for (MenueItemDTO menueItemDTO : menuItemDtoList) {

			LOGGER.info("MENUID: ", menueItemDTO.getId());
			LOGGER.info("MENUNAME: ", menueItemDTO.getName());
			LOGGER.info("INGSIZE: ", menueItemDTO.getIngredients().size());
			if (menueItemDTO.getIngredients() != null && !menueItemDTO.getIngredients().isEmpty()) {
				for (IngredientDTO ingredientDTO : menueItemDTO.getIngredients()) {
					LOGGER.info("INGNAME: ", ingredientDTO.getName());
					LOGGER.info("DTO", ingredientDTO);
				}
			}
		}

		/*
		 * DateTime systemDate = new DateTime(Date.from(Instant.now())); DateTime
		 * dateTimeUtcGmt = dt.withZone(systemDate.getZone() );
		 */
		ctx.setVariable("orderId", orders.getId());
		ctx.setVariable("location", locations.getName());
		ctx.setVariable("pickUp", orders.getPackageType().getDescription());
		ctx.setVariable("order", orderDTO);

		// String orderLink = "http://localhost:8080/kiosk/#/order/0TPRRVcxUVT";
		String orderLink = "http://159.203.79.162:8080/#/order/" + Encryption.encodeData(orderDTO.getOrderId() + "");
		ctx.setVariable("orderLink", orderLink);

		DateTime dt = new DateTime(orderDTO.getOrderDate());
		ctx.setVariable("orderDate", dt.plusHours(7).toDate());
		// ctx.setVariable("orderDate", dt.toDate());
		ctx.setVariable("warnMsg", warnMsg);

		ctx.setVariable("merchId", null);
		ctx.setVariable("transDate", null);
		ctx.setVariable("approvalCode", null);
		ctx.setVariable("nameOnCard", null);
		ctx.setVariable("cardNumber", null);
		ctx.setVariable("pinStatement", null);
		ctx.setVariable("terminalId", null);
		ctx.setVariable("appId", null);
		ctx.setVariable("appLabel", null);

		if (orders.getPaymentData() != null && !orders.getPaymentData().isEmpty()) {
			JSONObject root = new JSONObject(orders.getPaymentData());
			if (root != null) {
				ctx.setVariable("merchId", locations.getId());
				if (root.has("TransactionDate")) {
					ctx.setVariable("transDate", root.get("TransactionDate"));
				}
				// ctx.setVariable("refNum", "Reference Number: "+locations.getName());

				if (root.has("AuthorizationCode")) {
					ctx.setVariable("approvalCode", root.get("AuthorizationCode"));
				}
				if (root.has("Cardholder")) {
					ctx.setVariable("nameOnCard", root.get("Cardholder"));
				}
				if (root.has("AccountNumber")) {
					ctx.setVariable("cardNumber", root.get("AccountNumber"));
				}

				if (root.has("AdditionalParameters")) {
					JSONObject additionalParams = root.getJSONObject("AdditionalParameters");
					if (additionalParams != null) {
						if (additionalParams.has("EMV")) {
							JSONObject emv = additionalParams.getJSONObject("EMV");
							if (emv.has("PINStatement")) {

								ctx.setVariable("pinStatement", emv.get("PINStatement"));
							}

							if (emv.has("TerminalInformation")) {

								JSONObject terminalInformation = emv.getJSONObject("TerminalInformation");
								if (terminalInformation.has("TerminalID")) {

									ctx.setVariable("terminalId", terminalInformation.get("TerminalID"));
								}
							}

							if (emv.has("ApplicationInformation")) {

								JSONObject applicationInformation = emv.getJSONObject("ApplicationInformation");
								if (applicationInformation != null) {
									if (applicationInformation.has("Aid")) {
										ctx.setVariable("appId", applicationInformation.get("Aid"));

									}
									if (applicationInformation.has("ApplicationLabel")) {

										ctx.setVariable("appLabel", applicationInformation.get("ApplicationLabel"));
									}
								}
							}

						}

					}
				}
			}

		}

		final String htmlContent = htmlTemplateEngine.process("order-confirm", ctx);
		message.setText(htmlContent, true /* isHtml */);
		serviceMessage.setText(htmlContent, true /* isHtml */);
		javaMailSender.send(mimeMessage);
		javaMailSender.send(serviceMimeMessage);
		LOGGER.error("SEND EMAIL AFTER");
	}

	private Map<CategoryType, List<MenueItemDTO>> groupByCategoryType(List<MenueItemDTO> menuItems) {
		LOGGER.info("Menu Id: ", menuItems.get(0).getId());
		Function<Long, CategoryType> typeOf = id -> menuItemsRepository.findById(id).getCategory().getCategoryType();
		LOGGER.info("NEW LOG OBTAINED: ", typeOf);
		LOGGER.error("NEW LOG OBTAINED Error: ", typeOf);

		return menuItems.stream().collect(Collectors.groupingBy(typeOf.compose(MenueItemDTO::getId)));
	}

	private List<MenueItemDTO> flatten(List<MenueItemDTO> items) {
		return flatten(items, new ArrayList<>());
	}

	private List<MenueItemDTO> flatten(List<MenueItemDTO> items, List<MenueItemDTO> result) {
		items.forEach(item -> {
			List<IngredientDTO> ingredients = item.getIngredients();
			item.getSubMenuItems().forEach(submenu -> ingredients.addAll(submenu.getIngredients()));

			item.setIngredients(ingredients);
			result.add(item);
		});
		return result;
	}

}
