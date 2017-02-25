package com.securechat.javasecurity;

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
import java.util.concurrent.locks.ReentrantLock;

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
	public static final ImplementationMarker MARKER = new ImplementationMarker(JavaSecurityPlugin.NAME,
			JavaSecurityPlugin.VERSION, "rsa_encryption", "1.0.0");
	private static final int blockSize = 116, keySize = 1024;
	
	@InjectInstance
	private IImplementationFactory factory;
	private PublicKey pubKey;
	private PrivateKey priKey;
	private Cipher cipher;
	private ReentrantLock lock;

	public RSAEncryption() {
		try {
			lock = new ReentrantLock();
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("Failed to crypt data", e);
		}
	}

	@Override
	public void generate() {
		try {
			lock.lock();
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(keySize);
			KeyPair pair = generator.generateKeyPair();
			pubKey = pair.getPublic();
			priKey = pair.getPrivate();
			lock.unlock();
		} catch (NoSuchAlgorithmException e) {
			lock.unlock();
			throw new RuntimeException("Failed to generate key pair", e);
		}
	}

	@Override
	public void load(byte[] publicKey, byte[] privateKey) {
		lock.lock();
		try {
			if (publicKey != null)
				pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			lock.unlock();
			throw new RuntimeException("Failed to load public key", e);
		}

		try {
			if (privateKey != null)
				priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			lock.unlock();
			throw new RuntimeException("Failed to load private key", e);
		}
		lock.unlock();
	}

	@Override
	public byte[] getPublickey() {
		lock.lock();
		byte[] data = new X509EncodedKeySpec(pubKey.getEncoded()).getEncoded();
		lock.unlock();
		return data;
	}

	@Override
	public byte[] getPrivatekey() {
		lock.lock();
		byte[] data = new PKCS8EncodedKeySpec(priKey.getEncoded()).getEncoded();
		lock.unlock();
		return data;
	}

	@Override
	public byte[] encrypt(byte[] data) {
		try {
			lock.lock();
			int count = (int) Math.ceil((double) data.length / (double) blockSize);
			IByteWriter out = IByteWriter.get(factory, MARKER.getId());
			out.writeInt(data.length);

			for (int i = 0; i < count; i++) {
				int start = i * blockSize;
				int size = data.length - start < blockSize ? data.length - start : blockSize;

				byte[] temp = new byte[blockSize];
				System.arraycopy(data, start, temp, 0, size);

				cipher.init(Cipher.ENCRYPT_MODE, pubKey);
				temp = cipher.doFinal(temp);
				out.writeArray(temp);
				out.writeInt(size);
			}

			lock.unlock();
			return out.toByteArray();
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			lock.unlock();
			throw new RuntimeException("Failed to encrypt data", e);
		}
	}

	@Override
	public byte[] decrypt(byte[] data) throws IOException {
		try {
			lock.lock();
			IByteReader in = IByteReader.get(factory, MARKER.getId(), data);
			int length = in.readInt();
			int count = (int) Math.ceil((double) length / (double) blockSize);

			byte[] result = new byte[length];

			for (int i = 0; i < count; i++) {
				int start = i * blockSize;

				byte[] temp = in.readArray();
				cipher.init(Cipher.DECRYPT_MODE, priKey);
				temp = cipher.doFinal(temp);

				System.arraycopy(temp, 0, result, start, in.readInt());
			}

			lock.unlock();
			return result;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			lock.unlock();
			throw new RuntimeException("Failed to decrypt data", e);
		}
	}

	@Override
	public ImplementationMarker getMarker() {
		return MARKER;
	}

}
