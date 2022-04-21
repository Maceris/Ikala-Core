package com.ikalagaming.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains tests for the hashing algorithm.
 *
 * @author Ches Burks
 *
 */
public class TestHasher {
	/**
	 * Creates a hash for passwords and tests collisions and matching.
	 */
	@Test
	public void testPasswordHashing() {
		try {
			List<String> hashes = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				String hash = Hasher.createHash("p\r\nassw0Rd!");
				Assertions.assertFalse(hashes.contains(hash));
				hashes.add(hash);
			}

			// Test password validation
			boolean failure = false;
			for (int i = 0; i < 500; ++i) {
				String password = "" + i;
				String hash = Hasher.createHash(password);
				String secondHash = Hasher.createHash(password);
				Assertions.assertFalse(hash.equals(secondHash));
				
				String wrongPassword = "" + (i + 1);
				Assertions.assertFalse(Hasher.validatePassword(wrongPassword, hash));
				Assertions.assertTrue(Hasher.validatePassword(password, hash));
			}
		}
		catch (Exception ex) {
			Assertions.fail(ex);
		}
	}

}
