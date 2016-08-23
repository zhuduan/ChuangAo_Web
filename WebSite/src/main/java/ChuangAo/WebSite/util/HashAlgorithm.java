package ChuangAo.WebSite.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class HashAlgorithm {
	
	private static Logger logger = Logger.getLogger(includeTemplateUtil.class);
	
	private static class HashAlgorithmHolder {  
        private static final HashAlgorithm INSTANCE = new HashAlgorithm();  
    }  
	
	public static final HashAlgorithm getInstance() {  
        return HashAlgorithmHolder.INSTANCE; 
    }  
	
	private HashAlgorithm(){
		
	}
	
	//---没有使用随机盐，可能有一定安全性问题，但是这样就不用额外存储
	//---简单的使用一个凯撒密码类似物来生成混合盐
	public String getSalt(Integer userID,String userName,Long timestamp){
		//--
		char nameArray[] = userName.toCharArray();
		char timeArray[] = timestamp.toString().toCharArray();
		char idArray[] = userID.toString().toCharArray();
		int arrayLength = idArray.length + timeArray.length + nameArray.length;
		int idArrayCount =0;
		int timeArrayCount =0;
		int nameArrayCount =0;
		int i =0;
		char saltValue[] = new char[arrayLength];
		while(i<arrayLength){
			if(idArrayCount<idArray.length){
				saltValue[i]=idArray[idArrayCount];
				idArrayCount++;
				i++;
			}
			if(timeArrayCount<timeArray.length){
				saltValue[i]=timeArray[timeArrayCount];
				timeArrayCount++;
				i++;
			}
			if(nameArrayCount<nameArray.length){
				saltValue[i]=nameArray[nameArrayCount];
				nameArrayCount++;
				i++;
			}
		}
		logger.debug("salt: "+String.valueOf(saltValue));
		return String.valueOf(saltValue);
	}
	
	
	public String getMd5Hash(String hashContent,String salt){
		String resultStr = "error";
		try {
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			byte[] inputByteArray = hashContent.getBytes();  
			messageDigest.update(inputByteArray);  
			byte[] resultByteArray = messageDigest.digest(); 
			String nextHash = getStrFromByte(resultByteArray) + salt;
			byte[] nextByteArray = nextHash.getBytes();
			messageDigest.update(nextByteArray);
			resultStr = getStrFromByte(nextByteArray);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("md5: "+resultStr);
		return resultStr;
	}
	
	
	private String getStrFromByte(byte[] resultByteArray){
		StringBuffer stringBuffer = new StringBuffer();  
        for (byte b : resultByteArray){  
            int bt = b&0xff;  
            if (bt < 16){  
                stringBuffer.append(0);  
            }   
            stringBuffer.append(Integer.toHexString(bt));  
        }  
        return stringBuffer.toString();
	}
	
}
