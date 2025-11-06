package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.DayOfWeek;
import java.time.LocalTime;

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