package com.example.distributed_storage_system.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.example.distributed_storage_system.constant.Constants.CONSISTENT_RING_SECTIONS;
import static com.example.distributed_storage_system.constant.Constants.HASHING_ALGORITHM;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@UtilityClass
public class CommonUtil {

    public static <T> List<T> nullSafeList(List<T> list) {
        return isEmpty(list) ? new ArrayList<>() : list;
    }

    public static <K, V> Map<K, V> nullSafeMap(Map<K, V> map) {
        return isEmpty(map) ? new HashMap<>() : map;
    }

    public static Integer getRingIndex(String string) {
        Integer ringIndex = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
            byte[] hashBytes = messageDigest.digest(string.getBytes());
            ringIndex = Math.abs(Arrays.hashCode(hashBytes)) % CONSISTENT_RING_SECTIONS;

        } catch (NoSuchAlgorithmException exception) {
            log.error("NoSuchAlgorithmException : exception : {}", exception.getMessage());
        }
        return ringIndex;
    }
}
