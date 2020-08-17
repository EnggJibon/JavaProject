package com.kmcj.karte.util;

public class SpecialSqlLikeOperator {
    private static final String[] SQL_LIKE_SPECIAL_OPERATOR = {"%", "_", "[", "]", "^"};

    public static String verify(String sql) {
        for (String specialChar : SQL_LIKE_SPECIAL_OPERATOR) {
            sql = sql.replace(specialChar, "\\" + specialChar);
        }
        return sql;
    }
}
