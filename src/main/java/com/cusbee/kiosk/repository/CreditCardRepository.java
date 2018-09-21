package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.controller.api.customer.payment.Encryption;
import com.cusbee.kiosk.entity.CreditCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CreditCardRepository extends JpaRepository<CreditCards, Long> {

    List<CreditCards> findAllByUserId(Long userId);

    CreditCards findByPan(String pan);

    List<CreditCards> findByPanEndingWith(String panEnding);

    @Transactional
    default CreditCards saveCreditCard(CreditCards card, Encryption encryption) {
        CreditCards saved = save(card);
        saved.setEncryption(encryption);
        return saved;
    }

    default CreditCards findCreditCard(Long creditCardId, Encryption encryption) {
        CreditCards card = findOne(creditCardId);
        card.setEncryption(encryption);
        return card;
    }

    default CreditCards findCreditCard(String cardNumber, Encryption encryption) {
        String pan = encryption.encrypt(cardNumber);
        CreditCards card = findByPan(pan);
        card.setEncryption(encryption);
        return card;
    }

    default List<CreditCards> findAllUserCreditCards(Long userId, Encryption encryption) {
        List<CreditCards> cards = findAllByUserId(userId);
        cards.forEach(card -> card.setEncryption(encryption));
        return cards;
    }
}
