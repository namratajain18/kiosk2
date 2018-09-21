package com.cusbee.kiosk.controller.api.customer.payment;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.arraycopy;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
public class AccountNumberMask {
    private List<UnmaskRange> ranges;
    private char maskCharacter;

    public AccountNumberMask(List<UnmaskRange> ranges, char maskCharacter) {
        this.ranges = ranges;
        this.maskCharacter = maskCharacter;
    }

    public String mask(String accountNumber) {
        if (accountNumber == null) {
            throw new RuntimeException("account number is null");
        }
        char[] characters = accountNumber.toCharArray();
        char[] newCharacters = new char[characters.length];

        Arrays.fill(newCharacters, 0, newCharacters.length, maskCharacter);
        for (UnmaskRange range : ranges) {
            if (range.getPositionType() == UnmaskRange.BEGINNINGTYPE) {
                arraycopy(
                    characters,
                    0,
                    newCharacters,
                    0,
                    range.getLength()
                );
            } else {
                arraycopy(
                    characters,
                    characters.length - range.getLength(),
                    newCharacters,
                    newCharacters.length - range.getLength(),
                    range.getLength()
                );
            }
        }

        return String.valueOf(newCharacters);
    }
}
