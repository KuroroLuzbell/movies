package com.fv.movies.util;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;


public class Cast {
    private Cast() {}

    public static Integer getInteger(Object[] objResult, int intIndex) {
        return objResult[intIndex] != null ? (Integer) objResult[intIndex] : null;
    }
    public static Long getLong(Object[] objResult, int intIndex) {
        return objResult[intIndex] != null ? (Long) objResult[intIndex] : null;
    }

    public static String getString(Object[] objResult, int intIndex) {
        return objResult[intIndex] != null ? (String) objResult[intIndex] : null;
    }
    public static Character getCharacter(Object[] objResult, int intIndex) {
        return objResult[intIndex] != null ? (Character) objResult[intIndex] : null;
    }

    public static LocalDate getLocalDate(Object[] objResult, int intIndex) {
        if (objResult[intIndex] instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        return null;
    }

    public static Short getShort(Object[] objResult, int intIndex) {
        return objResult[intIndex] != null ? (Short) objResult[intIndex] : null;
    }

    public static BigDecimal getBigDecimal(Object[] objResult, int intIndex) {
        return objResult[intIndex] != null ? (BigDecimal) objResult[intIndex] : BigDecimal.ZERO;
    }

    public static Double getDouble(Object[] objResult, int intIndex) {
        return objResult[intIndex] != null ? (Double) objResult[intIndex] : null;
    }
}