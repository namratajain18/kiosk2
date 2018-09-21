package com.cusbee.kiosk.controller.api.customer.payment;

import com.google.common.base.Throwables;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

// added by ash 5/15 coz of below
//sun.misc.BASE64Encoder -- which is currently used -- is available for Java <= 8
//java.util.Base64 -- a drop-in alternative -- is available in Java >= 8

//import java.util.Base64;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;

/**
 * Very basic encryption, AES or stronger encryption is better to use.
 *
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class Encryption {

    public static final int PREFIX_LENGTH = 12;

    private static Random rand = new Random(Date.from(Instant.now()).getTime());

    public String encrypt(String value) {
        BASE64Encoder encoder = new BASE64Encoder();

        byte[] salt = new byte[8];
          rand.nextBytes(salt);
        return encoder.encode(salt) + encoder.encode(value.getBytes());
    }

    public String decrypt(String value) {
        if (value.length() > PREFIX_LENGTH) {
            String cipher = value.substring(PREFIX_LENGTH);
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                return new String(decoder.decodeBuffer(cipher));
            } catch (IOException ex) {
                throw new RuntimeException(Throwables.getRootCause(ex).getMessage());
            }
        }
        return null;
    }
    
    //New Url change
    public static void main(String[] args) {
    	Encryption e = new Encryption();
    	String e1 = e.encrypt("Aamir");
		System.out.println(e1);
		System.out.println(e.decrypt(e1));
		/*0TP3RVRxUVT
		0TPRRVVGZVZ
		0TPRRVRk1WZ*/
		System.out.println(encodeData("538"));
		System.out.println(encodeData("241"));
		System.out.println(encodeData("380"));
		System.out.println(encodeData("532")); //0TPRRVcxUVT
		
	}
    
    public static String decodeData(String fourthLevel) {
		String second = decrUrlSafe(new StringBuilder(fourthLevel).reverse().toString());
		String firstRev = new StringBuilder(decr(second)).reverse().toString();
		String firstNumRev = decr(firstRev);
		String firstNum = new StringBuilder(firstNumRev).reverse().toString();
		return firstNum;
	}

	public static String encodeData(String oid) {
		String firstLevel = encr(new StringBuilder(oid).reverse().toString());
		String secondLevel = encr(new StringBuilder(firstLevel).reverse().toString());
		String thirdLevel = Base64.encodeBase64URLSafeString(secondLevel.getBytes());
		String fourthLevel = new StringBuilder(thirdLevel).reverse().toString();
		return fourthLevel;
	}
	
	public static String decrUrlSafe(String text) {
		byte[] bytesEncoded = Base64.decodeBase64(text);
		return new String(bytesEncoded);
	}
	
	public static String encr(String text) {
		byte[] bytesEncoded = Base64.encodeBase64(text.getBytes());
		return new String(bytesEncoded);
	}
	
	public static String decr(String text) {
		byte[] bytesEncoded = Base64.decodeBase64(text);
		return new String(bytesEncoded);
	}
}
