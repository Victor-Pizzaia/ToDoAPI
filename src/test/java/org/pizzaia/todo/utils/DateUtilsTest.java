package org.pizzaia.todo.utils;

import org.junit.jupiter.api.Test;
import org.pizzaia.todo.util.DateUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {
    @Test
    public void shouldReturnTrueForFutureDates() {
        LocalDate date = LocalDate.of(2100, 1, 1);
        assertThat(DateUtils.isEqualOrFutureDate(date)).isTrue();
    }

    @Test
    public void shouldReturnFalseForPastDates() {
        LocalDate date = LocalDate.of(2010, 1, 1);
        assertThat(DateUtils.isEqualOrFutureDate(date)).isFalse();
    }

    @Test
    public void shouldReturnTrueForCurrentDate() {
        LocalDate date = LocalDate.now();
        assertThat(DateUtils.isEqualOrFutureDate(date)).isTrue();
    }
}
