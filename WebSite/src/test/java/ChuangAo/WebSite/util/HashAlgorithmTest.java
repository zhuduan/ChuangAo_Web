package ChuangAo.WebSite.util;

import org.junit.Test;

import static org.junit.Assert.*;


public class HashAlgorithmTest {

	@Test
	public void getSaltTest(){
		Long time = 1234560L;
		String name = "hi";
		Integer id = 987;
		String result = "91h82i734560";
		assertEquals(result, HashAlgorithm.getInstance().getSalt(id, name, time));
	}
	
	@Test
	public void getMD5(){
		String salt = "91h82i734560";
		Long time = 1234560L;
		String name = "hi";
		Integer id = 987;
		String email = "23333@163.com";
		String hash = HashAlgorithm.getInstance().getMd5Hash(email, salt);
		String newHash = HashAlgorithm.getInstance().getMd5Hash(email, 
				HashAlgorithm.getInstance().getSalt(id, name, time));
		assertEquals(hash,newHash);
	}
	
}
