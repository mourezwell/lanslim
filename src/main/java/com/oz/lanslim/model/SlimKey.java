package com.oz.lanslim.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DHParameterSpec;

import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.SlimLogger;

public class SlimKey implements Serializable {
	
	private static final String WRAPPING_ALGORITHM = "DES"; //$NON-NLS-1$
	private static final byte[] WRAPPING_SECRET = "LANSLIMS".getBytes(); //$NON-NLS-1$
	private static final String KEY_ALGORITHM = "DH"; //$NON-NLS-1$
	private static final BigInteger DH_G = new BigInteger("9225573972510520240049214520007542512642077837648200700856538266029811352575842095981243932539326746692234554205940988813129395100984963317994132347021715"); //$NON-NLS-1$ 
	private static final BigInteger DH_P = new BigInteger("9615739199612046998463417864283920837611590440425871904620972591765278936099774460553656895895774759288985432277228909410815151466194655845433884529182089"); //$NON-NLS-1$
	private static final int DH_L = 510; 
	
	private static Key privateK = null;
	private static Cipher unwrapper = null;

	private String encodedK = null;
	private transient Cipher coder = null;
	private transient Cipher decoder = null;

	private SlimKey(String pEncoded, Cipher pCoder, Cipher pDecoder) {
		encodedK = pEncoded;
		coder = pCoder;
		decoder = pDecoder;
	}
	
	public synchronized static SlimKey generateKey() throws SlimException {
		try {
	        KeyPairGenerator lGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
	        lGenerator.initialize(new DHParameterSpec(DH_P, DH_G, DH_L));
	        KeyPair lPair = lGenerator.generateKeyPair();
	        
	        SecretKeyFactory lWrapKeyFactory = SecretKeyFactory.getInstance(WRAPPING_ALGORITHM);
	        DESKeySpec lWrapSecretKeySpec = new DESKeySpec(WRAPPING_SECRET);
	        SecretKey lWrapSecretKey = lWrapKeyFactory.generateSecret(lWrapSecretKeySpec);
		    Cipher lWrapper = Cipher.getInstance(WRAPPING_ALGORITHM);
		    lWrapper.init(Cipher.WRAP_MODE, lWrapSecretKey);
		    byte[] lWrapped = lWrapper.wrap(lPair.getPublic());
		    String lEncoded = toHexString(lWrapped);

		    privateK = lPair.getPrivate();
		    unwrapper = Cipher.getInstance(WRAPPING_ALGORITHM);
		    unwrapper.init(Cipher.UNWRAP_MODE, lWrapSecretKey);
		    
	        SlimKey lKey = new SlimKey(lEncoded, null, null);
		    
	        return lKey;
		}
		catch (GeneralSecurityException lE) {
			throw new SlimException(Externalizer.getString("LANSLIM.137", //$NON-NLS-1$
					SlimLogger.shortFormatException(lE)));
		}
	}
	
	public synchronized static SlimKey fromString(String pKey) throws SlimException {

		if (privateK == null) {
			return null;
		}
		try {
			byte[] lEncodedKey = fromHexString(pKey);
		    Key lPubK = unwrapper.unwrap(lEncodedKey, KEY_ALGORITHM, Cipher.PUBLIC_KEY);
		    
		    KeyAgreement keyAg = KeyAgreement.getInstance(KEY_ALGORITHM);
		    keyAg.init(privateK);
		    keyAg.doPhase(lPubK, true);
		    byte[] lSecret = keyAg.generateSecret();
		    
	        SecretKeyFactory lSecretKeyFactory = SecretKeyFactory.getInstance(WRAPPING_ALGORITHM);
	        DESKeySpec lSecretKeySpec = new DESKeySpec(lSecret);
	        SecretKey lSecretKey = lSecretKeyFactory.generateSecret(lSecretKeySpec);
		    
		    Cipher lCoder = Cipher.getInstance(WRAPPING_ALGORITHM);
		    lCoder.init(Cipher.ENCRYPT_MODE, lSecretKey);

		    Cipher lDecoder = Cipher.getInstance(WRAPPING_ALGORITHM);
		    lDecoder.init(Cipher.DECRYPT_MODE, lSecretKey);
	
		    SlimKey lKey = new SlimKey(pKey, lCoder, lDecoder);
	
		    return lKey;
		}
		catch (GeneralSecurityException lE) {
			throw new SlimException(Externalizer.getString("LANSLIM.138", //$NON-NLS-1$
					SlimLogger.shortFormatException(lE)));
		}
	}

	public String encodeMsg(String pMsg) throws SlimException {
		try {
			byte[] lArray = coder.doFinal(pMsg.getBytes());
			return toHexString(lArray);
		}
		catch (GeneralSecurityException lE) {
			throw new SlimException(Externalizer.getString("LANSLIM.139", //$NON-NLS-1$
					SlimLogger.shortFormatException(lE)));
		}
	}
	
	public String decodeMsg(String pMsg) throws SlimException {
		try {
			byte[] lArray = fromHexString(pMsg);
			byte[] lDecArray = decoder.doFinal(lArray);
			return new String(lDecArray);
		}
		catch (GeneralSecurityException lE) {
			throw new SlimException(Externalizer.getString("LANSLIM.140", //$NON-NLS-1$
					SlimLogger.shortFormatException(lE)));
		}
	}
	
    public String toString() {
    	return encodedK;
	}

    
    /* Converts a byte to hex digit and writes to the supplied buffer */
    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        		'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /* Converts a byte array to hex string */
    private static String toHexString(byte[] block) {
    	
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
             byte2hex(block[i], buf);
        } 
        return buf.toString(); 
    }

    private static byte[] fromHexString(String hexString) {
    	
        int len = hexString.length() / 2;
        byte[] lRes = new byte[len];
        for (int i = 0; i < len; i++) {
        	lRes[i] = (byte)(16 * hexCharValue(hexString.charAt(2*i)) 
        		+ hexCharValue(hexString.charAt(2*i+1)));
        } 
        return lRes; 
    }

    private static byte hexCharValue(char pChar) {
    	
        if (pChar < 58) {
        	return (byte)(pChar - 48);
        }
    	return (byte)(pChar - 55);
    }

}
