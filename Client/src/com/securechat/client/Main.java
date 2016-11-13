package com.securechat.client;

import com.securechat.client.network.NetworkClient;

public class Main {

	public static void main(String[] args) {
		
		
//		KeyPair pair = SecurityUtils.generateKeyPair();
//		
//		System.out.println("Public: " + Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
//		System.out.println("Private: " + new String(pair.getPrivate().getEncoded()));
//		
//		
//		long start = System.currentTimeMillis();
//		System.out.println("Encrypted: " + new String(SecurityUtils.encryptData("Hello".getBytes(), pair.getPublic())));
//		System.out.println(System.currentTimeMillis() - start);
////		SecurityUtils.hashData(input)
		
		new NetworkClient().connect("127.0.0.1", 1234);

	}

}
