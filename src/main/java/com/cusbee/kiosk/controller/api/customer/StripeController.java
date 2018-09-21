package com.cusbee.kiosk.controller.api.customer;

import com.cusbee.kiosk.bean.StripeBean;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.controller.api.customer.payment.Encryption;
import com.cusbee.kiosk.entity.CreditCards;
import com.cusbee.kiosk.entity.Orders;
import com.cusbee.kiosk.entity.Payments;
import com.cusbee.kiosk.enums.PaymentStatus;
import com.cusbee.kiosk.repository.CreditCardRepository;
import com.cusbee.kiosk.repository.MenuItemsRepository;
import com.cusbee.kiosk.repository.OrdersRepository;
import com.cusbee.kiosk.repository.PaymentsRepository;
import com.cusbee.kiosk.utils.Matcher;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.Token;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

/**
 * Created by ahorbat on 05.04.17.
 */
@RestController
@RequestMapping(value = "/api/customer/payment")
@PropertySource("classpath:application.properties")
public class StripeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripeController.class);
    private static final String DEFAULT_CURRENCY = "usd";

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private Encryption encryption;

    @Autowired
    private CreditCardRepository cardRepository;

    @Value( "${stripe.api.key}" )
    String STRIPE_API_KEY;
    
    @Value( "${stripe.api.key_2}" )
    String STRIPE_API_KEY_ROCKFELLER;
    
    @Value( "${stripe.api.key_1}" )
    String STRIPE_API_KEY_GRAND;
    
    @Value( "${stripe.api.key_3}" )
    String STRIPE_API_KEY_SAN_FRAN;
    
    @Value( "${stripe.api.key_4}" )
    String STRIPE_API_KEY_MARKET_ST;
    
    
    //private static final String STRIPE_API_KEY = "sk_live_MCJlPpO3GNEKsOV2yseJRdmy";
   // private static final String STRIPE_API_KEY_GRAND = "sk_live_MCJlPpO3GNEKsOV2yseJRdmy";
   // private static final String STRIPE_API_KEY_ROCKFELLER = "pk_live_zlrGXf81VttlUOA7ihcq7WLj";
    //private static final String STRIPE_API_KEY_SAN = "sk_live_MCJlPpO3GNEKsOV2yseJRdmy";
    //private static final String STRIPE_API_KEY_SAN_TEST = "sk_test_GrurLAjSFESllYBSOb2KATZs";
    // static final String STRIPE_API_KEY = "sk_test_GrurLAjSFESllYBSOb2KATZs";
    //private static final String DEVELOPERS_KEY = "sk_test_Ggwpv4hCbh4XVTrNj862dewE";
    
    @RequestMapping(value = "/refund/{orderId}", method = RequestMethod.GET)
    public ResponseContainer<Payments> refund(@PathVariable("orderId") Long orderId) {
        Orders order = ordersRepository.findById(orderId);
        if (order == null) {
            return createEmptyResponse(format("Order with id: %d does not exist", orderId));
        } else {
            if (order.getPaymentId() == null) {
                return createEmptyResponse(format("Payment for order id: %d is not set", orderId));
            }
            Payments payment = paymentsRepository.findOne(order.getPaymentId());
            if (payment == null) {
                return createEmptyResponse(format(
                    "Payment for order id: %d with payment id: %d does not exist",
                    orderId,
                    order.getPaymentId()
                ));
            } else {
                final long locationId = menuItemsRepository.findById(order.getOrderDetailsList().get(0).getItemId())
                    .getCategory().getMenus().getLocations().getId();
                if (locationId == 1) {
                	System.out.println(locationId+" "+STRIPE_API_KEY_GRAND);
                	Stripe.apiKey = STRIPE_API_KEY_GRAND; 
        		} else if (locationId == 2) {
        			System.out.println(locationId+" "+STRIPE_API_KEY_ROCKFELLER);
        			 Stripe.apiKey = STRIPE_API_KEY_ROCKFELLER;
        		} else if (locationId == 3) {
        			System.out.println(locationId+" "+STRIPE_API_KEY_SAN_FRAN);
        			Stripe.apiKey = STRIPE_API_KEY_SAN_FRAN;
        		} else if (locationId == 4) {
        			System.out.println(locationId+" "+STRIPE_API_KEY_MARKET_ST);
        			Stripe.apiKey = STRIPE_API_KEY_MARKET_ST;
        		}  else {
        			System.out.println(locationId+" "+STRIPE_API_KEY);
        			Stripe.apiKey = STRIPE_API_KEY;
        		}
                
               // Stripe.apiKey = STRIPE_API_KEY;
                Map<String, Object> refundParams = ImmutableMap.of("charge", payment.getRefund());
                try {
                    Refund.create(refundParams);
                    return createRefundResponse(payment, 200, "Successful", null);
                } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException ex) {
                    return createRefundResponse(payment, 404, ex.getMessage(), ex);
                }
            }

        }
    }

    // @formatter:off
    @ApiOperation(
        value = "Performs charge operation by using Stripe API",
        notes =
            "In case if credit card ID is empty, the full information about user's credit card should be present in stripe bean, " +
            "otherwise the saved credit card will be used.",
        response = StripeBean.class
    )
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public ResponseContainer<Payments> charge(@RequestBody StripeBean stripeBean) {
        LOGGER.info("charge CALLED");
        CreditCards creditCard = stripeBean.getCardId() == null
                ? null
                : cardRepository.findCreditCard(stripeBean.getCardId(), encryption);

        String cardNumber = stripeBean.getNumber();
        if (creditCard != null)
        {
            cardNumber = creditCard.getPan();
        }
        
        Long locationId = stripeBean.getLocId();
        System.out.println(stripeBean.getLocId());
		if (locationId == null) {
			locationId = 0L;
		}
        //long locationId = 0 ;
    /*    if (purchaseId != null) {
            Orders order = ordersRepository.findById(purchaseId);
            if (order != null) {
            	System.out.println(order.getId());
            	  locationId = menuItemsRepository.findById(order.getOrderDetailsList().get(0).getItemId())
            			.getCategory().getMenus().getLocations().getId();
            }
        }*/
            if (locationId == 1) {
            	 System.out.println(locationId+" "+STRIPE_API_KEY_GRAND);
            	Stripe.apiKey = STRIPE_API_KEY_GRAND; 
    		} else if (locationId == 2) {
    			System.out.println(locationId+" "+STRIPE_API_KEY_ROCKFELLER);
    			 Stripe.apiKey = STRIPE_API_KEY_ROCKFELLER;
    		} else if (locationId == 3) {
    			System.out.println(locationId+" "+STRIPE_API_KEY_SAN_FRAN);
    			Stripe.apiKey = STRIPE_API_KEY_SAN_FRAN;
    		} else if (locationId == 4) {
    			System.out.println(locationId+" "+STRIPE_API_KEY_MARKET_ST);
    			Stripe.apiKey = STRIPE_API_KEY_MARKET_ST;
    		}  else {
    			System.out.println(locationId+" "+STRIPE_API_KEY);
    			Stripe.apiKey = STRIPE_API_KEY;
    		}

        RequestOptions options = RequestOptions.builder()
            .setIdempotencyKey(stripeBean.getKey())
//            .setApiKey(STRIPE_API_KEY_SAN)
            .setApiKey(Stripe.apiKey)
            .build();

        Map<String, Object> cardMap = ImmutableMap.<String, Object>builder()
            .put("number",    cardNumber)
            .put("exp_month", creditCard == null ? stripeBean.getExpMonth() : creditCard.getExpirationMonth())
            .put("exp_year",  creditCard == null ? stripeBean.getExpYear()  : creditCard.getExpirationYear())
            .build();

        Map<String, Object> chargeMap = ImmutableMap.<String, Object>builder()
            .put("amount",   stripeBean.getAmount())
            .put("currency", DEFAULT_CURRENCY)
            .put("card",     cardMap)
            .build();

        Payments payment = null;
        try {
        	 LOGGER.info("API KEY"  + Stripe.apiKey);
             System.out.println("API KEY"  + Stripe.apiKey);
        	Charge charge = createCharge(cardMap, stripeBean);
            LOGGER.info("Charge is being save: {}", charge);

            payment = Payments.Builder.builder()
                .amount(stripeBean.getAmount())
                .status(PaymentStatus.PAYED_BY_CARD)
                .comment("Successful")
                .refund(charge.getId())
                .card(creditCard)
                .cardNumber(encryption.encrypt(cardNumber))
                .name(stripeBean.getCardHolderName())
                .build();

            Payments saved = paymentsRepository.save(payment);
            Long orderId = stripeBean.getOrderId();
            if (orderId != null) {
                Orders order = ordersRepository.findById(orderId);
                if (order != null) {
                    order.setPaymentId(saved.getId());
                    ordersRepository.save(order);
                }
            }
            String savedCardNumber = StringUtils.defaultString(saved.getCardNumber());
            if (!savedCardNumber.isEmpty()) {
                hideCardNumberIfPossible(saved, savedCardNumber);
            }
            return ResponseContainer.Builder.<Payments>builder()
                .data(saved)
                .message("Payed successfully")
                .code(200)
                .build();
        } catch (Throwable ex) { // NOSONAR;
            LOGGER.error(Throwables.getRootCause(ex).getMessage(), ex);

            Payments failedPayment = payment;
            return Matcher.when(AuthenticationException.class::isInstance, this::onAuthenticationError)
                .or(CardException.class::isInstance,           this::onCreditCardError)
                .or(RateLimitException.class::isInstance,      err -> onStripeError(err, "Too many requests made to the API too quickly: %s "))
                .or(InvalidRequestException.class::isInstance, err -> onStripeError(err, "Invalid parameters were supplied to Stripe's API: %s "))
                .or(APIConnectionException.class::isInstance,  err -> onStripeError(err, "Network communication with Stripe failed: %s"))
                .or(StripeException.class::isInstance,         err -> onStripeError(err, "General exception: %s"))
                .either(this::onInternalServerError)
                .match(ex)
                .map(pair -> createChargeResponse(chargeMap, failedPayment, pair.getKey(), pair.getValue(), ex))
                .orElse(null);
        }
    }
    // @formatter:on

    
    public Charge createCharge(Map<String, Object> cardMap, StripeBean stripeBean)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, CardException, APIException {
    	 LOGGER.info("API KEY"  + Stripe.apiKey);
         System.out.println("API KEY"  + Stripe.apiKey);
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("number", cardMap.get("number"));
        cardParams.put("exp_month", cardMap.get("exp_month"));
        cardParams.put("exp_year", cardMap.get("exp_year"));
        cardParams.put("cvc", stripeBean.getCvv());
        LOGGER.info("Recived info for token: " + cardParams + " key: " + Stripe.apiKey);
        System.out.println("Recived info for token: " + cardParams + " key: " + Stripe.apiKey);
        Map<String, Object> tokenParams = new HashMap<String, Object>();
        tokenParams.put("card", cardParams);
        LOGGER.info("Request for token");
        System.out.println("Request for token");
        Token t = Token.create(tokenParams, Stripe.apiKey);
        LOGGER.info("Recieved token " + t);
        System.out.println("Recieved token " + t);
        Map<String, Object> chargeMap2 = ImmutableMap.<String, Object>builder()
                .put("amount",   stripeBean.getAmount())
                .put("currency", DEFAULT_CURRENCY)
                .put("source", t.getId())
                .build();
        LOGGER.error("\n\nRequesting charge map:"+ chargeMap2);
        System.out.println("\n\nRequesting charge map:"+ chargeMap2);
        Charge charge = Charge.create(chargeMap2, Stripe.apiKey);
        LOGGER.error("created charge data" + charge);
        System.out.println("created charge data" + charge);
        return charge;
    }
    // @formatter:on
    
 
    /*private Token createToken(RequestOptions options) {
        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("number", "4242424242424242");
        cardParams.put("exp_month", 9);
        cardParams.put("exp_year", 2019);
        cardParams.put("cvc", "314");
        tokenParams.put("card", cardParams);
        //tokenParams.put("Stripe.apiKey","sk_live_ORe2OdzncvajyoOfnE2Bfges");
        Token t = null;
        try {
            t = Token.create(tokenParams, STRIPE_API_KEY);
            System.out.println(t);
        }catch(Exception e) {
            LOGGER.error("Error:" + e);
        }
        return t;
    }*/
    private void hideCardNumberIfPossible(Payments saved, String savedCardNumber) {
        String cardNumber = encryption.decrypt(savedCardNumber);
        int group = 4;
        int index = cardNumber.length() - group;
        String formattedNumber = cardNumber.substring(0, index).replaceAll("\\d", "*") + cardNumber.substring(index);

        saved.setCardNumber(String.join(" ", Splitter.fixedLength(group).split(formattedNumber)));
    }

    private Pair<Integer, String> onCreditCardError(Throwable cause) {
        String format = format("Status is: %s Message is: %%s", CardException.class.cast(cause).getCode());
        return onStripeError(cause, format);
    }

    private Pair<Integer, String> onAuthenticationError(Throwable cause) {
        return onError(cause, 401, "Authentication with Stripe's API failed: %s");
    }

    private Pair<Integer, String> onStripeError(Throwable cause, String format) {
        return onError(cause, 404, format);
    }

    private Pair<Integer, String> onInternalServerError(Throwable cause) {
        return onError(cause, 500, "Not stripe fatal exception: %s");
    }

    private Pair<Integer, String> onError(Throwable cause, Integer code, String format) {
        return Pair.of(code, format(format, cause.getMessage()));
    }

    private ResponseContainer<Payments> createEmptyResponse(String message) {
        return ResponseContainer.Builder.<Payments>builder()
            .message(message)
            .code(404)
            .build();
    }

    private ResponseContainer<Payments> createRefundResponse(Payments payment, Integer code, String message, Throwable ex) {
        payment.setStatus(ex == null ? PaymentStatus.REFUND : PaymentStatus.REFUND_FAILED);
        payment.setDate(Date.from(Instant.now()));
        payment.setComment(message);

        Payments saved = paymentsRepository.save(payment);
        String savedCardNumber = StringUtils.defaultString(saved.getCardNumber());
        if (!savedCardNumber.isEmpty()) {
            hideCardNumberIfPossible(saved, savedCardNumber);
        }
        return ResponseContainer.Builder.<Payments>builder()
            .data(saved)
            .code(code)
            .message(ex == null ? "Successfully refund" : format("Failed: %s", Throwables.getRootCause(ex).getMessage()))
            .build();
    }

    private ResponseContainer<Payments> createChargeResponse(Map<String, Object> chargeMap, Payments payment, Integer code, String message, Throwable ex) {
        Payments defaultPayment = Optional.ofNullable(payment).orElse(Payments.Builder.builder()
            .cardNumber(((Map) chargeMap.get("card")).get("number").toString())
            .amount(Integer.parseInt(chargeMap.get("amount").toString()))
            .date(Date.from(Instant.now()))
            .build()
        );
        defaultPayment.setStatus(PaymentStatus.FAILED);
        defaultPayment.setComment(message);

        return ResponseContainer.Builder.<Payments>builder()
            .data(paymentsRepository.save(defaultPayment))
            .code(code)
            .message(ex.getMessage())
            .build();
    }
}
