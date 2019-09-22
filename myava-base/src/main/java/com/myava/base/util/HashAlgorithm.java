package com.myava.base.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash算法
 *
 * @author biao
 */
@Slf4j
public class HashAlgorithm {

    private static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            log.error("MD5 algorithm init error.");
        }
    }

    /**
     * hash算法
     * @param key
     * @return
     */
    public static long hash(String key) throws NoSuchAlgorithmException {
        if (md5 == null) {
            md5 = MessageDigest.getInstance("MD5");
        }
        md5.reset();
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();
        long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF);
        return res & 0xffffffffL;
    }
}
