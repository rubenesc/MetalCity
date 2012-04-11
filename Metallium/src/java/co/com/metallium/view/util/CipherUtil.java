/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util;

import com.metallium.utils.framework.utilities.LogHelper;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
   
public class CipherUtil extends java.lang.Object implements java.io.Serializable 
{	
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	public static final String DES_ENCRYPTION_SCHEME = "DES";
	public static final String	OLD_DEFAULT_ENCRYPTION_KEY	= "This is a fairly long phrase used to encrypt";
	private static final String	UNICODE_FORMAT= "UTF8";
	private static final String clazz = CipherUtil.class.getName();
	
	private static KeySpec				oldKeySpec;
	private static SecretKeyFactory		oldKeyFactory;
	private static Cipher				oldDecryptCipher;
	private static Cipher				oldEncryptCipher;
	
	
	public static final String AES_ENCRYPTION_SCHEME = "AES";
	public static final String	DEFAULT_ENCRYPTION_KEY	= "metallium encrypt key";
	
	private static SecretKeySpec		skeySpec;	
	private static Cipher				decryptCipher;
	private static Cipher				encryptCipher;


	private static void init() throws EncryptionException
	{		
		initOld(DESEDE_ENCRYPTION_SCHEME, OLD_DEFAULT_ENCRYPTION_KEY);		
		init(AES_ENCRYPTION_SCHEME, DEFAULT_ENCRYPTION_KEY);
	}

	private static void init(String encryptionScheme, String encryptionKey ) throws EncryptionException
	{	
		if (skeySpec != null && decryptCipher != null && encryptCipher != null) {
			return;
		}
		
		if (encryptionKey == null )
			throw new IllegalArgumentException( "encryption key was null" );
		if (encryptionKey.trim().length() < 16)
			throw new IllegalArgumentException(
				"encryption key was less than 16 characters" );

		try
		{			
			insertBouncyCastleProvider();
			
			KeyGenerator kgen = KeyGenerator.getInstance(encryptionScheme);
            kgen.init(128); // 192 and 256 bits may not be available
            
            byte[] keyAsBytes = DEFAULT_ENCRYPTION_KEY.getBytes( UNICODE_FORMAT );                        
            skeySpec = new SecretKeySpec(keyAsBytes, encryptionScheme);
            
            // Instantiate the encrypt cipher
            encryptCipher = Cipher.getInstance("AES", "BC");
            encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			LogHelper.makeLog("Initialized encryption keyfactory/cipher");
			
			// Instantiate the decrypt cipher
			decryptCipher = Cipher.getInstance( encryptionScheme, "BC" );
			decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec);
			LogHelper.makeLog("Initialized encryption keyfactory/cipher");
		}
		catch (InvalidKeyException e)
		{			
			throw new EncryptionException( e );
		}
		catch (UnsupportedEncodingException e)
		{	
			throw new EncryptionException( e );
		}
		catch (NoSuchAlgorithmException e)
		{ 	
			throw new EncryptionException( e );
		}
		catch (NoSuchPaddingException e)
		{ 	
			throw new EncryptionException( e );
		} catch (NoSuchProviderException e) {
			throw new EncryptionException( e );
		}  catch (NullPointerException e) {			
			throw new EncryptionException( e );
		} catch (SecurityException e) {
			throw new EncryptionException( e );
		}  catch (Exception e) {
			throw new EncryptionException( e );
		}

	}
	
	public static void insertBouncyCastleProvider() {
		
        int insertionPoint = 1;
        
        Provider[] providers = Security.getProviders();

        if (providers != null) {
            
            insertionPoint = (providers.length+1);
            
            LogHelper.makeLog("Found " + providers.length + " existing security providers: searching for inserted BouncyCastle provider");
            
            for (int i = 0; i < providers.length; i++) {
            	LogHelper.makeLog("providers["+i+"]="+providers[i]);
            	            	
            	if (providers[i] instanceof BouncyCastleProvider) {
                    LogHelper.makeLog("BouncyCastle security provider already exists at position " + (i+1) + ": no insertion required");
                    insertionPoint = -1;
                    break;
                }
                LogHelper.makeLog("Did not match providers["+i+"]="+providers[i]);
            }
        }

        if (insertionPoint > 0) {
            LogHelper.makeLog("Inserting BouncyCastle security provider at position: " + insertionPoint);
            Security.insertProviderAt(new BouncyCastleProvider(), insertionPoint);
            LogHelper.makeLog("After Inserting BouncyCastle security provider.");
        }		
    }
	
	public static String encrypt(String unencryptedString) {
		if ( unencryptedString == null || unencryptedString.trim().length() == 0 )
			throw new IllegalArgumentException("unencrypted string was null or empty" );

		try
		{ 
			init();			
			byte[] cleartext = unencryptedString.getBytes( UNICODE_FORMAT );			
			byte[] ciphertext = encryptCipher.doFinal( cleartext );
			
			BASE64Encoder base64encoder = new BASE64Encoder();			
			return base64encoder.encode( ciphertext );
		}
		catch (Exception e) {
			LogHelper.makeLog("Exception in encrypt while encrypting string: " + unencryptedString, e);
		}
		return null;
	}

    public static String encodeStandardBase64(String clearText) {

        if (clearText == null || clearText.trim().length() == 0) {
            throw new IllegalArgumentException("the input string is null or empty");
        }
        String encodedText = new String(Base64.encodeBase64(clearText.getBytes()));
        return encodedText;
    }


	public static String decrypt(String encryptedString)	{
		if (encryptedString == null || encryptedString.trim().length() <= 0)
			throw new IllegalArgumentException( "encrypted string was null or empty" );

		try {
			init();			
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] cleartext = base64decoder.decodeBuffer( encryptedString );
			byte[] ciphertext = decryptCipher.doFinal( cleartext );

			return bytes2String( ciphertext );
		}
		catch (Exception e) {
			LogHelper.makeLog("Exception in decrypt while decrypting string: " + encryptedString, e);
		}
		return null;
	}
	
	public static class EncryptionException extends Exception {
		public EncryptionException(Throwable t) {
			super(t);
		}
	}
	
	// ************** All methods below this are to be phased ou 03/17/2009 ***/
	private static void initOld( String encryptionScheme, String encryptionKey ) throws EncryptionException
	{

		if (oldKeyFactory != null && oldDecryptCipher != null && oldEncryptCipher != null) {
			return;
		}
		if ( encryptionKey == null )
			throw new IllegalArgumentException( "encryption key was null" );
		if ( encryptionKey.trim().length() < 24 )
			throw new IllegalArgumentException(
				"encryption key was less than 24 characters" );

		try
		{
			LogHelper.makeLog("Initializing encryption keyfactory/cipher");
			
			byte[] keyAsBytes = encryptionKey.getBytes( UNICODE_FORMAT );

			if ( encryptionScheme.equals( DESEDE_ENCRYPTION_SCHEME) )
			{
				oldKeySpec = new DESedeKeySpec( keyAsBytes );
			}
			else if ( encryptionScheme.equals( DES_ENCRYPTION_SCHEME ) )
			{
				oldKeySpec = new DESKeySpec( keyAsBytes );
			}
			else
			{
				throw new IllegalArgumentException( "Encryption scheme not supported: "
					+ encryptionScheme );
			}

			oldKeyFactory = SecretKeyFactory.getInstance( encryptionScheme );
			SecretKey secretkey = oldKeyFactory.generateSecret( oldKeySpec );

			oldDecryptCipher = Cipher.getInstance( encryptionScheme );
			oldDecryptCipher.init(Cipher.DECRYPT_MODE, secretkey);
			
			oldEncryptCipher = Cipher.getInstance( encryptionScheme );
			oldEncryptCipher.init(Cipher.ENCRYPT_MODE, secretkey);
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptionException( e );
		}
		catch (UnsupportedEncodingException e)
		{
			throw new EncryptionException( e );
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptionException( e );
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptionException( e );
		}
		catch (java.security.spec.InvalidKeySpecException e) {
			throw new EncryptionException(e);
		}

	}

	public static String encryptOld031709( String unencryptedString ) 
	{
		if ( unencryptedString == null || unencryptedString.trim().length() == 0 )
			throw new IllegalArgumentException("unencrypted string was null or empty" );

		try
		{
			init();
			//SecretKey key = oldKeyFactory.generateSecret( oldKeySpec );
			//cipher.init( Cipher.ENCRYPT_MODE, key );
			byte[] cleartext = unencryptedString.getBytes( UNICODE_FORMAT );
			byte[] ciphertext = oldEncryptCipher.doFinal( cleartext );

			BASE64Encoder base64encoder = new BASE64Encoder();
			return base64encoder.encode( ciphertext );
		}
		catch (Exception e)
		{
			oldKeyFactory = null;
			//e.printStackTrace();
			LogHelper.makeLog("Exception in encryptOld031709 while encrypting string: " + unencryptedString, e);
		}
		return null;
	}

	public static String decryptOld031709( String encryptedString ) 
	{
		if ( encryptedString == null || encryptedString.trim().length() <= 0 )
			throw new IllegalArgumentException( "encrypted string was null or empty" );

		try
		{
			init();
			//SecretKey key = oldKeyFactory.generateSecret( oldKeySpec );
			//cipher.init( Cipher.DECRYPT_MODE, key );
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] cleartext = base64decoder.decodeBuffer( encryptedString );
			byte[] ciphertext = oldDecryptCipher.doFinal( cleartext );

			return bytes2String( ciphertext );
		}
		catch (Exception e)
		{
			oldKeyFactory = null;
			//e.printStackTrace();
			LogHelper.makeLog("Exception in decryptOld031709 while decrypting string: " + encryptedString, e);
		}
		return null;
	}

	private static String bytes2String( byte[] bytes )
	{
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			stringBuffer.append( (char) bytes[i] );
		}
		return stringBuffer.toString();
	}
	

	
	private static String encryptString(String encryptString) 
	{	
		String alphabeth = "HuIdtcrYl90]1p2!8:zUVh)?WX[EgTZ-@ }m#$%PfaFq56SC*,QRbBy.on<GOx{K34JvL>(M;weNADs7";	
		String vigenereTable = alphabeth + alphabeth;	
		int alphabethLength = alphabeth.length();	

		int nIndex = encryptString.length();	
		int nIndex2 = 0, nChar1 = 0, nChar2 = 0;	
		String workString = "";	
		char encodeKey = 'J';	
		while (nIndex > 0) {		
			nIndex--;		
			nChar1 = getIndex(encodeKey);		
			nChar2 = getIndex(encryptString.charAt(nIndex2));		
			if (nChar2 != -1 ) {			
				workString = workString + vigenereTable.charAt(nChar2 + nChar1);			
				encodeKey = vigenereTable.charAt(nChar2 + nChar1);			
			}		
			else			
				workString = workString + encryptString.charAt(nIndex2);		
			nIndex2++;		
		}		
		return workString;	
	}	
	
	
	public static String encryptOld(String encryptString){		
		if (encryptString==null || "".equals(encryptString)) return "";
		try{
			sun.misc.BASE64Encoder encoder =new sun.misc.BASE64Encoder();// Class.forName("sun.misc.BASE64Encoder" ).newInstance();
	        return encoder.encode( encryptString(encryptString).getBytes() );
		}catch(Exception e){
			//e.printStackTrace();
			LogHelper.makeLog("Exception in encryptOld while encrypting string: " + encryptString, e);
			return encryptString;
		}	
	}
	public static String olddecrypt(String decryptString){	
		if (decryptString==null || "".equals(decryptString)) return "";
		java.io.ByteArrayInputStream oByteArrayInputStream = new java.io.ByteArrayInputStream(decryptString.getBytes());
		try{
			sun.misc.BASE64Decoder decoder =new sun.misc.BASE64Decoder();// Class.forName("sun.misc.BASE64Decoder" ).newInstance();
	        return decryptString(new String(decoder.decodeBuffer(oByteArrayInputStream)));
		}catch(Exception e){
			//e.printStackTrace();
			LogHelper.makeLog("Exception in olddecrypt while decrypting string: " + decryptString, e);
			return decryptString;
		}	
	}
	
	private static String decryptString(String decryptString) {	
		String alphabeth = "HuIdtcrYl90]1p2!8:zUVh)?WX[EgTZ-@ }m#$%PfaFq56SC*,QRbBy.on<GOx{K34JvL>(M;weNADs7";	
		String vigenereTable = alphabeth + alphabeth;	
		int alphabethLength = alphabeth.length();	
		int nIndex = decryptString.length();	
		int nIndex2 = 0, nChar1 = 0, nChar2 = 0;	
		String workString = "";	char decodeKey = 'J';	
		while (nIndex > 0) {		
			nIndex--;		
			nChar1 = getIndex(decodeKey);		
			nChar2 = getIndex(decryptString.charAt(nIndex2));		
			if (nChar2 != -1 ) {			
				workString = workString + vigenereTable.charAt(nChar2 - nChar1 + alphabethLength);	
				decodeKey = decryptString.charAt(nIndex2);			
			}		
			else			
				workString = workString + decryptString.charAt(nIndex2);		
			nIndex2++;		
		}		
		return workString;	
	}	
	
	private static int getIndex(char cIndex) {	
		String alphabeth = "HuIdtcrYl90]1p2!8:zUVh)?WX[EgTZ-@ }m#$%PfaFq56SC*,QRbBy.on<GOx{K34JvL>(M;weNADs7";	
		String vigenereTable = alphabeth + alphabeth;	
		int alphabethLength = alphabeth.length();	

		int nIndex = 0;	
		while (nIndex < (alphabethLength)) {		
		if (cIndex== vigenereTable.charAt(nIndex))			
		break;		nIndex++;		
		}	
		if (nIndex < (alphabethLength))		
		return nIndex;	
		else		
		return -1;	
	}



	
	//public char getCodeKey() {	return codeKey;	}

}
