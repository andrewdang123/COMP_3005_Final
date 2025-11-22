package models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * DateOfBirth is an @Embeddable value object:
 * - It gets stored inline as three columns (day, month, year) in the owning entity
 *   (e.g., Member), rather than as its own table.
 * - Uses Bean Validation (@Min/@Max) to enforce valid ranges for day (1–31)
 *   and month (1–12) at the model level.
 */

@Embeddable
public class DateOfBirth {

    @Min(1) @Max(31)
    private int day;

    @Min(1) @Max(12)
    private int month;

    private int year;

    public DateOfBirth() {} // Required by JPA

    public DateOfBirth(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // Getters and setters
    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}