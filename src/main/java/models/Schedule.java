package models;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Schedule is an @Embeddable value object for “day + time range.”
 * - Not its own table—its fields become columns inside the parent entity.
 * - Stores dayOfWeek (string), startTime, and endTime.
 * - Reused for class schedules, PT sessions, and conflict checks.
 */

@Embeddable
public class Schedule {

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Min(0)
    @Max(24)
    private LocalTime startTime;

    @Min(0)
    @Max(24)
    private LocalTime endTime;

    public Schedule() {
    } // Required by JPA

    public Schedule(DayOfWeek dayOfWeek, int startTime, int endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = LocalTime.of(startTime, 0);
        this.endTime = LocalTime.of(endTime, 0);
    }

    public Schedule(String dayOfWeek, int startTime, int endTime) {
        this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        this.startTime = LocalTime.of(startTime, 0);
        this.endTime = LocalTime.of(endTime, 0);
    }

    // Getters and setters
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = LocalTime.of(startTime, 0);
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = LocalTime.of(endTime, 0);
    }

    public String toString() {
        return dayOfWeek.toString() + " " + startTime.toString() + " - " + endTime.toString();
    }
}