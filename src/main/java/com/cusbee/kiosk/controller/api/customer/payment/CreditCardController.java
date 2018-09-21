package com.cusbee.kiosk.controller.api.customer.payment;

import com.cusbee.kiosk.bean.dto.CreditCardDTO;
import com.cusbee.kiosk.controller.ResponseContainer;
import com.cusbee.kiosk.entity.CreditCards;
import com.cusbee.kiosk.repository.CreditCardRepository;
import com.cusbee.kiosk.repository.UsersRepository;
import com.google.common.base.Throwables;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Api("Credit Card API")
@RestController
@RequestMapping(value = "/api/customer")
public class CreditCardController {

    @Autowired
    private CreditCardRepository cardRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CreditCardValidator cardValidator;

    @Autowired
    private Encryption encryption;

    @ApiOperation(
        value = "Gets credit card",
        notes = "Returns saved credit card that found by passed identifier",
        authorizations = {
            @Authorization(value = "ROLE_CUSTOMER")
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Credit card is found and returned", response = CreditCardDTO.class),
        @ApiResponse(code = 404, message = "Credit card is not found", response = CreditCardDTO.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Void.class)
    })
    @ApiImplicitParams(value = {
        @ApiImplicitParam(
            name = "credit-card-id",
            value = "The ID of credit card to be returned",
            required = true,
            paramType = "path",
            dataType = "long"
        )
    })
    @RequestMapping(value = "/credit-card/{credit-card-id}", method = RequestMethod.GET)
    public ResponseContainer<CreditCard> getCreditCard(@PathVariable("credit-card-id") long id) {
        int code = 200;
        String message = StringUtils.EMPTY;
        CreditCards creditCard = null;

        try {
            creditCard = cardRepository.findCreditCard(id, encryption);
            if (creditCard == null) {
                code = 404;
                message = "Unable to find credit card";
            }
        } catch (Exception ex) {
            code = 500;
            message = Throwables.getRootCause(ex).getMessage();
        }

        return ResponseContainer.Builder.<CreditCard>builder()
            .message(message)
            .code(code)
            .data(creditCard)
            .build();
    }

    @ApiOperation(
        value = "Gets all credit cards of user",
        notes = "Returns all credit cards are registered to current user",
        authorizations = {
            @Authorization(value = "ROLE_CUSTOMER")
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Credit cards are found and returned", response = CreditCardDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized access", response = CreditCardDTO.class, responseContainer = "List"),
        @ApiResponse(code = 500, message = "Internal server error", response = Void.class, responseContainer = "List")
    })
    @ApiImplicitParams(value = {
        @ApiImplicitParam(
            name = "user-id",
            value = "The ID of current user",
            required = true,
            paramType = "query",
            dataType = "long"
        )
    })
    @RequestMapping(value = "/credit-card/by-user", method = RequestMethod.GET)
    public ResponseContainer<List<CreditCards>> getAllUserCreditCards(@RequestParam("user-id") Long userId) {
        int code = 200;
        String message = StringUtils.EMPTY;
        List<CreditCards> creditCards = Collections.emptyList();

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userId.equals(usersRepository.findByUsername(user.getUsername()).getId())) {
                creditCards = cardRepository.findAllUserCreditCards(userId, encryption);
            } else {
                code = 401;
                message = "Unauthorized access.";
            }
        } catch (Exception ex) {
            code = 500;
            message = Throwables.getRootCause(ex).getMessage();
        }

        return ResponseContainer.Builder.<List<CreditCards>>builder()
            .message(message)
            .code(code)
            .data(creditCards)
            .build();
    }

    @ApiOperation(
        value = "Creates a new credit card for user",
        notes = "Creates a new credit card and then assign it to current authenticated user",
        authorizations = {
            @Authorization(value = "ROLE_CUSTOMER")
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Credit card is created", response = CreditCardDTO.class),
        @ApiResponse(code = 406, message = "The passed credit card information is not valid", response = CreditCardDTO.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Void.class)
    })
    @ApiImplicitParams(value = {
        @ApiImplicitParam(
            name = "creditCardDto",
            value = "The information about new credit card",
            required = true,
            paramType = "body",
            dataType = "CreditCardDTO"
        )
    })
    @RequestMapping(value = "/credit-card", method = RequestMethod.POST)
    public ResponseContainer<CreditCards> saveCreditCard(@RequestBody @Valid CreditCardDTO creditCardDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseContainer.Builder.<CreditCards>builder()
                .code(406)
                .message(result.getAllErrors().stream().map(ObjectError::getCode).collect(Collectors.joining("\n")))
                .build();
        } else {
            int code = 200;
            String message = StringUtils.EMPTY;
            CreditCards savedCreditCard = null;
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String ending = encryption.encrypt(creditCardDto.getCreditCardNumber()).substring(Encryption.PREFIX_LENGTH);

            CreditCards creditCard = null;
            try {
                if (cardRepository.findByPanEndingWith(ending).isEmpty()) {
                    creditCard = CreditCards.Builder.builder()
                        .nameOnCard(creditCardDto.getCreditCardHolderName())
                        .pan(creditCardDto.getCreditCardNumber())
                        .encryption(encryption)
                        .expirationMonth(Integer.parseInt(creditCardDto.getCreditCardExpMonth()))
                        .expirationYear(Integer.parseInt(creditCardDto.getCreditCardExpYear()))
                        .userId(usersRepository.findByUsername(user.getUsername()).getId())
                        .build();
                } else {
                    throw new RuntimeException("The credit card number should be unique");
                }
                savedCreditCard = cardRepository.saveCreditCard(creditCard, encryption);
            } catch (Exception ex) {
                code = 500;
                message = Throwables.getRootCause(ex).getMessage();
            }
            return ResponseContainer.Builder.<CreditCards>builder()
                .message(message)
                .code(code)
                .data(savedCreditCard)
                .build();
        }
    }

    @ApiOperation(
        value = "Deletes credit card",
        notes = "Deletes saved credit card that found by passed identifier",
        authorizations = {
            @Authorization(value = "ROLE_CUSTOMER")
        }
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Credit card is found and deleted", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Void.class)
    })
    @ApiImplicitParams(value = {
        @ApiImplicitParam(
            name = "credit-card-id",
            value = "The ID of credit card to be deleted",
            required = true,
            paramType = "path",
            dataType = "long"
        )
    })
    @RequestMapping(value = "/credit-card/{credit-card-id}", method = RequestMethod.DELETE)
    public ResponseContainer<Void> deleteCreditCard(@PathVariable("credit-card-id") Long id) {
        int code = 200;
        String message = "Credit card is successfully deleted";

        try {
            cardRepository.delete(id);
        } catch (Exception ex) {
            code = 500;
            message = Throwables.getRootCause(ex).getMessage();
        }

        return ResponseContainer.Builder.<Void>builder()
            .code(code)
            .message(message)
            .build();
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.addValidators(cardValidator);
    }
}
