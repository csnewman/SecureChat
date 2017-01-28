package com.securechat.basicsecurity;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.securechat.api.common.implementation.IImplementationFactory;
import com.securechat.api.common.implementation.ImplementationMarker;
import com.securechat.api.common.plugins.InjectInstance;
import com.securechat.api.common.security.IAsymmetricKeyEncryption;
import com.securechat.api.common.storage.IByteReader;
import com.securechat.api.common.storage.IByteWriter;

public class RSAEncryption implements IAsymmetricKeyEncryption {
	public static final ImplementationMarker MARKER = new ImplementationMarker(BasicSecurityPlugin.NAME,
			BasicSecurityPlugin.VERSION, "rsa_encryption", "1.0.0");
	@InjectInstance
	private IImplementationFactory factory;
	private PublicKey pubKey;
	private PrivateKey priKey;
	private Cipher cipher;

	public RSAEncryption() {
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("Failed to crypt data", e);
		}
	}

	@Override
	public void generate() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(4096);
			KeyPair pair = generator.generateKeyPair();
			pubKey = pair.getPublic();
			priKey = pair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to generate key pair", e);
		}
	}

	@Override
	public void load(byte[] publicKey, byte[] privateKey) {
		try {
			pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to load public key", e);
		}

		try {
			priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to load private key", e);
		}
	}

	@Override
	public byte[] getPublickey() {
		return new X509EncodedKeySpec(pubKey.getEncoded()).getEncoded();
	}

	@Override
	public byte[] getPrivatekey() {
		return new PKCS8EncodedKeySpec(priKey.getEncoded()).getEncoded();
	}

	@Override
	public byte[] encrypt(byte[] data) {
		try {
			int count = (int) Math.ceil((double) data.length / 501d);
			IByteWriter out = IByteWriter.get(factory, MARKER.getId());
			out.writeInt(data.length);

			for (int i = 0; i < count; i++) {
				int start = i * 501;
				int size = data.length - start < 501 ? data.length - start : 501;

				byte[] temp = new byte[size];
				System.arraycopy(data, start, temp, 0, size);

				cipher.init(Cipher.ENCRYPT_MODE, pubKey);
				temp = cipher.doFinal(temp);
				out.writeArray(temp);
			}

			return out.toByteArray();
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException("Failed to encrypt data", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] data) throws IOException {
		try {
			IByteReader in = IByteReader.get(factory, MARKER.getId(), data);
			int length = in.readInt();
			int count = (int) Math.ceil((double) length / 501d);

			byte[] result = new byte[length];

			for (int i = 0; i < count; i++) {
				int start = i * 501;

				byte[] temp = in.readArray();
				cipher.init(Cipher.DECRYPT_MODE, priKey);
				temp = cipher.doFinal(temp);

				System.arraycopy(temp, 0, result, start, temp.length);
			}

			return result;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException("Failed to decrypt data", e);
		}
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
