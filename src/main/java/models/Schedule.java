package models;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Schedule is an @Embeddable value object used to store “day + time range”:
 * - It’s NOT its own table; it gets inlined into whatever entity embeds it
 *   (e.g., ClassScheduleDetails, PersonalTrainingSessionDetails) as columns.
 * - dayOfWeek is stored as a string (e.g., "MONDAY") via @Enumerated(EnumType.STRING).
 * - startTime and endTime are LocalTime values built from simple integer hours.
 * - This class is reused anywhere we need a day/time window (class schedule,
 *   PT session details, room-conflict checks, trainer schedule view, etc.).
 *
 * DB NOTE:
 * - Because this is @Embeddable, it doesn’t define its own primary key, trigger,
 *   view, or index. It just contributes columns (like schedule_day, schedule_start_time,
 *   schedule_end_time) to the owning entity’s table. Any indexing is done on those
 *   columns in the parent entity or via queries.
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