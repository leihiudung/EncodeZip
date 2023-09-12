package com.bvt.encodezip.util;

import cn.hutool.core.lang.hash.MurmurHash;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.util
 * @className: HashUtils
 * @author: Tom
 * @description: TODO
 */
public class HashUtils {

    public static final String CryptCode = "abcgo";
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String getMd5(CharSequence str) {
        return DigestUtils.md5DigestAsHex(str.toString().getBytes());
    }

    public static long getMurmurHash32(String str) {
        int i = MurmurHash.hash32(str);
        long num = i < 0 ? Integer.MAX_VALUE - (long) i : i;
        return num;
    }

    public static String getBC(CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    public static boolean matchBC(CharSequence rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(getBC("123456"));
    }
}
