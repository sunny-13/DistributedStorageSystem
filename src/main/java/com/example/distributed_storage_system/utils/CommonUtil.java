package com.example.distributed_storage_system.utils;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

public class CommonUtil {

    public static <T> List<T> nullSafeList(List<T> list) {
        return isEmpty(list) ? new ArrayList<>() : list;
    }
}
