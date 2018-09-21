package com.cusbee.kiosk.controller.api.admin.order;

import static com.cusbee.kiosk.bean.OrderBean.toBeanList;
import static com.cusbee.kiosk.bean.OrderBean.toBeanListWithDetails;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cusbee.kiosk.bean.OrderBean;
import com.cusbee.kiosk.bean.OrderDetailsBean;
import com.cusbee.kiosk.bean.dto.ExtrasDTO;
import com.cusbee.kiosk.bean.dto.MenueItemDTO;
import com.cusbee.kiosk.bean.dto.OrderDTO;
import com.cusbee.kiosk.controller.PageContainer;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.controller.api.customer.payment.Encryption;
import com.cusbee.kiosk.entity.Categories;
import com.cusbee.kiosk.entity.Locations;
import com.cusbee.kiosk.entity.MenuItems;
import com.cusbee.kiosk.entity.OrderDetails;
import com.cusbee.kiosk.entity.Orders;
import com.cusbee.kiosk.enums.KitchenStatus;
import com.cusbee.kiosk.enums.OrderStatus;
import com.cusbee.kiosk.repository.CategoriesRepository;
import com.cusbee.kiosk.repository.IngredientsRepository;
import com.cusbee.kiosk.repository.MenuItemsRepository;
import com.cusbee.kiosk.repository.OrderDetailsRepository;
import com.cusbee.kiosk.repository.OrdersRepository;
import com.google.common.collect.Lists;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

/**
 * Created by ahorbat on 12.02.17.
 */
@RestController
@RequestMapping(value = "/api/admin")
public class OrderController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private MenuItemsRepository menuItemsRepository;
    
    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Autowired
    private TokenStore tokenStore;

    //private static final String ACCOUNT_SID = "AC15460338c5deac47051b5bc5336f0982";
    //private static final String AUTH_TOKEN = "PNdd74791d8c5688023c24eeb584625ac7";
    private static final String ACCOUNT_SID = "AC15460338c5deac47051b5bc5336f0982";
    private static final String AUTH_TOKEN = "063670cb8a2a711693e40b43783838c8";

    @RequestMapping(value = "/order/all", method = RequestMethod.GET)
    public ResponseContainer<List<OrderBean>> orderList(
                            @RequestParam(value = "today", required = true) boolean today,
                            @RequestParam(value = "perPage", required = true) int perPage,
                            @RequestParam(value = "page",  required = true) int page) {

        ResponseContainer<List<OrderBean>> response = new ResponseContainer<>();


       /* if (today){
            orders = ordersRepository.findByOrderDateBetween(DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().toDate(), new PageRequest(page, perPage));
        } else*/
        List<Orders> orders = ordersRepository.findAll();
        //orders = ordersRepository.findAll((new PageRequest(page, perPage)));

        List<Orders> reversedList = Lists.reverse(orders);
        List<Orders> filteredByInProgress = reversedList.stream().filter(el ->
        {System.out.println(" =====> " + el.getOrderStatus().name().equals("IN_PROGRESS"));
                        return el.getOrderStatus().name().equals("IN_PROGRESS");
        }
        ).collect(Collectors.toList());

        filteredByInProgress.stream().forEach(el ->  {
            el.setOrderDetailsList(orderDetailsRepository.findByOrders(el));
        });
        response.setData(toBeanListWithDetails(filteredByInProgress));
        response.setCode(HttpStatus.OK.value());
        response.setPageContainer(new PageContainer(500,0));
        return response;
    }

    @RequestMapping(value = "/order/{orderId}" ,method = RequestMethod.PUT)
    public ResponseContainer<OrderBean> updateOrderById(@PathVariable("orderId") long orderId, OrderDTO newOrder){
        ResponseContainer<OrderBean> response = new ResponseContainer<>();
        Orders oldOrder = ordersRepository.findById(orderId);
        if(oldOrder != null) {
            List<MenueItemDTO> newOrderMenuItems = newOrder.getMenuItems();
            oldOrder.setName(newOrder.getName());
            oldOrder.setEmail(newOrder.getEmail());
            oldOrder.setPhone(newOrder.getPhone());
            oldOrder.setOrderPrice(BigDecimal.valueOf(newOrder.getOrderPrice()));
            oldOrder.setPackageType(newOrder.getPackageType());
            oldOrder.setPaymentStatus(newOrder.getPaymentStatus());
            List<OrderDetails> orderDetailsList = new ArrayList<>();

            newOrderMenuItems.stream().forEach(el ->{
                OrderDetails details = new OrderDetails();
                details.setItemId(el.getId());
                details.setCount(el.getCount());
                details.setExtraIds(el.getExtras().stream()
                        .map(ExtrasDTO::getId)
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
                details.setOrders(oldOrder);
                orderDetailsList.add(details);
            });

            ordersRepository.save(oldOrder);
            response.setData(OrderBean.toOrderBean(oldOrder));
            return response;
        } else {
            response.setCode(404);
            response.setMessage("Order with id: " + orderId + " was not found");
            return  response;
        }
    }

    @RequestMapping(value = "/order/postpone/{orderId}" , method = RequestMethod.POST)
    public ResponseContainer<OrderBean> postPoneOrderPreparationTime(
                                                            @PathVariable("orderId") long orderId,
                                                            @RequestParam(value = "minute") int minute) {
        ResponseContainer<OrderBean> response = new ResponseContainer<>();
        Orders order = ordersRepository.findById(orderId);
        if(order != null) {
            DateTime dt = new DateTime(order.getCountDown());
            order.setCountDown(dt.plusMinutes(5).toDate());
            ordersRepository.save(order);
            response.setData(OrderBean.toOrderBean(order));
            response.setMessage("Cooking time for order " + order.getId() + " postponed for " + minute + " minutes");
            response.setCode(HttpStatus.OK.value());
            return response;
        } else {
            response.setMessage("Order with id: " + orderId + " was not found in the system");
            response.setCode(HttpStatus.NOT_FOUND.value());
            return response;
        }
    }

    @RequestMapping(value = "/order/change-status/{orderId}", method = RequestMethod.POST)
    public ResponseContainer<OrderBean> changeOrderStatus(
                                                            @PathVariable("orderId") long orderId,
                                                            @RequestParam(value = "status") String status){
        ResponseContainer<OrderBean> response = new ResponseContainer<>();
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            Orders order = ordersRepository.findById(orderId);
            if (order != null) {
                boolean allComplete = orderDetailsRepository.findByOrders(order).stream().filter(el ->
                        el.getItemStatus().equals(KitchenStatus.IN_PROGRESS)).collect(Collectors.toList()).isEmpty();

                /*if(allComplete) {
                    order.setOrderStatus(orderStatus);
                    ordersRepository.save(order);
                    response.setData(OrderBean.toOrderBean(order));
                    response.setMessage("Order for " + order.getName() + " changed status to " + orderStatus.getDescription());
                    response.setCode(HttpStatus.OK.value());
                    sendSms(order.getPhone(), order.getId().toString());
                    return response;
                } else {
                    response.setMessage("Not all menue items were prepared yet");
                    response.setCode(HttpStatus.CONFLICT.value());
                    return response;
                }*/
                order.setOrderStatus(orderStatus);
                ordersRepository.save(order);
                response.setData(OrderBean.toOrderBean(order));
                response.setMessage("Order for " + order.getName() + " changed status to " + orderStatus.getDescription());
                response.setCode(HttpStatus.OK.value());
                sendSms(order.getPhone(), order.getId().toString());
                return response;

            } else {
                response.setMessage("Order with id: " + orderId + " was not found in the system");
                response.setCode(HttpStatus.NO_CONTENT.value());
                return response;
            }

        } catch (Exception e) {
            response.setMessage("Order status: " + status + " is incorrect");
            response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
            return response;
        }

    }

    private void sendSms(String number, String orderId) {
        try {
            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", "Your Order #"+orderId+" is Ready. Thank you for Ordering Online."));
            params.add(new BasicNameValuePair("To", number)); //Add real number here
            //params.add(new BasicNameValuePair("To", "+380990469063")); //Add real number here
            params.add(new BasicNameValuePair("From", "+19177463308"));

            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            System.out.println(message.getSid());
        }
        catch (TwilioRestException e) {
            System.out.println(e.getErrorMessage());
        }
    }

    @RequestMapping(value = "/order-details/{orderId}", method = RequestMethod.GET)
    public ResponseContainer<List<OrderDetailsBean>> orderDetailsById(@PathVariable("orderId") long orderId,
                                                                      @RequestParam(value = "byTab", required = false) boolean byTab
                                                                      ) {
        ResponseContainer<List<OrderDetailsBean>> response = new ResponseContainer<>();
        Orders order = ordersRepository.findById(orderId);
        if(byTab){
            switch (getUserRole()){
                case "SALAD": {
                    return filterItemsByCategory("sal", response,order);
                }
                case "BURGER": return filterItemsByCategory("bur", response,order);
                case "SANDWICH": return filterItemsByCategory("sand", response,order);
                default: {
                    response.setData(OrderDetailsBean.toBeanList(orderDetailsRepository.findByOrders(order)));
                    return response;
                }
            }
        } else {
            response.setData(OrderDetailsBean.toBeanList(orderDetailsRepository.findByOrders(order)));
            return response;
        }
    }

    public static void main(String[] args) {
		OrderController c = new OrderController();
		c.orderDetailsById("0TPRRlNGZVZ");
	}
    LobHandler lob = new DefaultLobHandler();
    
    @RequestMapping(value = "/order/{orderId}/details", method = RequestMethod.GET)
    public ResponseContainer<OrderDTO> orderDetailsById(@PathVariable("orderId") String orderId) {
    	
        String oId = Encryption.decodeData(orderId);
        
        Orders order = ordersRepository.findById(Long.parseLong(oId));
        if(order == null) return new ResponseContainer<OrderDTO>();
        OrderDTO dto = new OrderDTO();
        //dto.setWarnMsg("");
        //ByteArrayInputStream baos = new ByteArrayInputStream();
        ObjectInputStream oos;
        
		try {
			Blob b = order.getOrderDetailsBlob();//row.get("REQUEST_MESSAGE");//cast with (Blob) if required. Blob from resultSet as rs.getBlob(index).
			if(b != null) {
				InputStream bis = b.getBinaryStream();
				ObjectInputStream ois = new ObjectInputStream(bis);	
				dto = (OrderDTO) ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        if(dto!=null) {
        	dto.setLogo("http://174.138.58.149/tri-tip-landing/img/logo.png");
    		dto.setRestaurant("Tri-Tip Grill");
        	if(!CollectionUtils.isEmpty(dto.getMenuItems())) {
        		Long firstItemId = dto.getMenuItems().get(0).getId();
        		MenuItems menuItem = menuItemsRepository.findById(firstItemId);
            	Locations loc = menuItem.getCategory().getMenus().getLocations();
            	if(loc.getId() == 1) {
            		dto.setRestaurant("Fuddruckers");
            		dto.setLogo("https://d38o1hjtj2mzwt.cloudfront.net/com.splickit.fuddruckers/web/brand-assets/logo.png");
            	} else if(loc.getId() == 4) {
            		 dto.setRestaurant("Buckhorn Grill");
            		 dto.setLogo("https://buckhorngrill.com/wp-content/uploads/2016/01/Logo-14-14-14.png");
            	}
            	dto.setLocation(loc.getName());
        	}
        	if(dto.getOrderDate() != null) {
        		dto.setOrderDate(new DateTime(dto.getOrderDate()).plusHours(7).toDate());
        	}
        }
        
        ResponseContainer<OrderDTO> response = new ResponseContainer<OrderDTO>();
        response.setData(dto);
        return response;
    }
    
    private ResponseContainer<List<OrderDetailsBean>> filterItemsByCategory(
                                                            String category,
                                                            ResponseContainer<List<OrderDetailsBean>> response,
                                                            Orders order) {

        Categories catObj = categoriesRepository.findByNameContaining(category).stream().findAny().orElseGet(null);
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrders(order);
        List<MenuItems> menueItems = menuItemsRepository.findByCategory(catObj);
        List<OrderDetails> updatedODetails = new ArrayList<>();
        menueItems.stream().forEach(menu -> {
            for(OrderDetails oDetails : orderDetailsList){
                if(oDetails.getItemId().equals(menu.getId())){
                    updatedODetails.add(oDetails);
                }
            }
        });
        response.setData(OrderDetailsBean.toBeanList(updatedODetails));
        return response;
    }

    private String getUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))){
            return "ADMIN";
        } else if (auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_SALAD"))){
            return "SALAD";
        } else if (auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_SANDWICH"))){
            return "SANDWICH";
        } else if(auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_BURGER"))) {
            return "BURGER";
        } else {
            return "NO_USER_ROLE";
        }

    }

    @RequestMapping(value = "/order/{orderId}",method = RequestMethod.DELETE)
    public ResponseContainer<String> removeOrder(@PathVariable("orderId") Long orderId) {
        ResponseContainer<String> response = new ResponseContainer<>();
        try{
            ordersRepository.delete(orderId);
            response.setData("Success");
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Order id: " + orderId + " removed successfully");
        } catch (Exception e) {
            response.setData(e.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Order id: " + orderId + " not removed");
        }
        return response;
    }

    @RequestMapping(value = "/order-history/{cellPhone}")
    public ResponseContainer<List<OrderBean>> orderHistory(@PathVariable("cellPhone") String cellPhone){
        ResponseContainer<List<OrderBean>> response = new ResponseContainer<>();
        List<Orders> orders = ordersRepository.findTop5ByPhoneOrderByIdDesc(cellPhone);
        response.setData(toBeanList(orders));
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @RequestMapping(value = "/order-history")
    public ResponseContainer<List<OrderBean>> orderHistoryByDateRange(@RequestParam(value = "from", required = true) String from,
                                                                      @RequestParam(value = "to", required = true) String to,
                                                                      @RequestParam(value = "perPage", required = true) int perPage,
                                                                      @RequestParam(value = "page",  required = true) int page,
                                                                      @RequestParam(value = "dineIn", required = false) Boolean dineIn) {
        ResponseContainer<List<OrderBean>> response = new ResponseContainer<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date fromDate = df.parse(from);
            Date toDate = df.parse(to);
            Page<Orders> orders = ordersRepository.findByOrderDateBetween(fromDate, toDate,new PageRequest(page, perPage));
            List<OrderBean> orderResponseList = toBeanList(orders.getContent());
            //Removed filter based on client requirement
           /* List<OrderBean> filteredListByDone = orderResponseList.stream().filter(el ->
                    el.getOrderStatus().name().equals("DONE")).collect(Collectors.toList());*/

            if(dineIn != null) {
                if (dineIn) {
                	orderResponseList.sort((o1, o2) -> o2.getType().compareTo(o1.getType()));
                } else {
                	orderResponseList.sort((o1, o2) -> o1.getType().compareTo(o2.getType()));
                }
            }

            response.setData(orderResponseList);
            response.setCode(HttpStatus.OK.value());
            response.setPageContainer(new PageContainer(orders.getTotalElements(),orders.getTotalPages()));
            return response;
        } catch (ParseException e) {
            response.setMessage(e.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }

    @RequestMapping(value = "/order-search/{query}")
    public ResponseContainer<List<OrderBean>> searchByParams(@PathVariable("query") String query,
                                                             @RequestParam(value = "perPage", required = true) int perPage,
                                                             @RequestParam(value = "page",  required = true) int page,
                                                             @RequestParam(value = "dineIn", required = false) Boolean dineIn){
        ResponseContainer<List<OrderBean>> response = new ResponseContainer<>();
        Page<Orders> orders;
        if(StringUtil.isNumeric(query)) {
            orders = ordersRepository.findByIdLike(Long.parseLong(query), new PageRequest(page, perPage));
        } else {
            orders = ordersRepository.findByNameOrPhoneLike("%" + query + "%",new PageRequest(page, perPage));
        }
        List<OrderBean> orderResponseList = toBeanList(orders.getContent());
        if(dineIn != null) {
            if (dineIn) {
                orderResponseList.sort((o1, o2) -> o2.getType().compareTo(o1.getType()));
            } else orderResponseList.sort((o1, o2) -> o1.getType().compareTo(o2.getType()));
        }
        response.setData(orderResponseList);
        response.setPageContainer(new PageContainer(orders.getTotalElements(),orders.getTotalPages()));
        return response;
    }
    
    //Mobile url related change
    @RequestMapping(value = "/order/mobile/{orderId}/{number}", method = RequestMethod.POST)
    public ResponseContainer<OrderBean> sendEmailOrSmsWithOrder(
                                                            @PathVariable("orderId") long orderId,
                                                            @PathVariable("number") String number){
        ResponseContainer<OrderBean> response = new ResponseContainer<>();
        try {
           // OrderStatus orderStatus = OrderStatus.valueOf(status);
            Orders order = ordersRepository.findById(orderId);
            if (order != null) {
                boolean allComplete = orderDetailsRepository.findByOrders(order).stream().filter(el ->
                        el.getItemStatus().equals(KitchenStatus.IN_PROGRESS)).collect(Collectors.toList()).isEmpty();

                response.setMessage("Order for " + order.getName() + " sent sms" );
                response.setCode(HttpStatus.OK.value());
                sendSmsWithOrder(number, order.getId().toString());
                return response;

            } else {
                response.setMessage("Order with id: " + orderId + " was not found in the system");
                response.setCode(HttpStatus.NO_CONTENT.value());
                return response;
            }

        } catch (Exception e) {
            response.setMessage("Order status: " + ""/*status*/ + " is incorrect");
            response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
            return response;
        }

    }
    
    private void sendSmsWithOrder(String number, String orderId) {
        try {
            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String orderLink = "http://159.203.79.162/tri-tip-landing/order/"+Encryption.encodeData(orderId+"");
            params.add(new BasicNameValuePair("Body", "Please click on the below link to see order detail"+"\n"+orderLink));
            params.add(new BasicNameValuePair("To", number)); //Add real number here
            //params.add(new BasicNameValuePair("To", "+380990469063")); //Add real number here
            params.add(new BasicNameValuePair("From", "+19177463308"));

            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            System.out.println(message.getSid());
        }
        catch (TwilioRestException e) {
            System.out.println(e.getErrorMessage());
        }
    }
    
    
    @RequestMapping(value = "/order/mobile/{orderId}/{email}", method = RequestMethod.POST)
    public ResponseContainer<OrderBean> sendEmailWithOrder(
                                                            @PathVariable("orderId") long orderId,
                                                            @PathVariable("email") String email){
        ResponseContainer<OrderBean> response = new ResponseContainer<>();
        try {
           // OrderStatus orderStatus = OrderStatus.valueOf(status);
            Orders order = ordersRepository.findById(orderId);
            if (order != null) {
                boolean allComplete = orderDetailsRepository.findByOrders(order).stream().filter(el ->
                        el.getItemStatus().equals(KitchenStatus.IN_PROGRESS)).collect(Collectors.toList()).isEmpty();

                response.setMessage("Order for " + order.getName() + " sent email" );
                response.setCode(HttpStatus.OK.value());
               //sendEmailWithOrder(order.getId().toString(),email);
                return response;

            } else {
                response.setMessage("Order with id: " + orderId + " was not found in the system");
                response.setCode(HttpStatus.NO_CONTENT.value());
                return response;
            }

        } catch (Exception e) {
            response.setMessage("Order status: " + ""/*status*/ + " is incorrect");
            response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
            return response;
        }

    }
    
}
