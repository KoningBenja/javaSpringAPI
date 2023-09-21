package com.createment.springone;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TableData {
    Map<Integer, Map<String, String>> data;
    String tableName;

    TableData() {
        data = new HashMap<>();
        tableName = "";
    }
}
