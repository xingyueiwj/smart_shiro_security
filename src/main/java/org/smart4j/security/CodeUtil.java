package org.smart4j.security;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public final class CodeUtil {
    public static String md5(String source){
        return DigestUtils.md5Hex(source);
    }
}
