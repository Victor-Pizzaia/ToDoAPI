package org.pizzaia.todo.util;

import java.time.LocalDate;

public class DateUtils {
    private DateUtils() {}
    public static boolean isEqualOrFutureDate(LocalDate date) {
        return date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now());
    }
}
