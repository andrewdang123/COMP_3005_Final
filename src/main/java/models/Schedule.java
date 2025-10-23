package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Embeddable
public class Schedule {

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Min(0) @Max(24)
    private int startTime;

    @Min(0) @Max(24)
    private int endTime;

    public Schedule() {} // Required by JPA

    public Schedule(DayOfWeek dayOfWeek, int startTime, int endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }

    public int getEndTime() { return endTime; }
    public void setEndTime(int endTime) { this.endTime = endTime; }

}


enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DayOfWeek fromString (String input) {
        if (input == null) return null;
        String normalized = input.trim().toUpperCase();

        switch (normalized) {
            case "MON": case "MONDAY": return MONDAY;
            case "TUE": case "TUESDAY": return TUESDAY;
            case "WED": case "WEDNESDAY": return WEDNESDAY;
            case "THU": case "THURSDAY": return THURSDAY;
            case "FRI": case "FRIDAY": return FRIDAY;
            case "SAT": case "SATURDAY": return SATURDAY;
            case "SUN": case "SUNDAY": return SUNDAY;
            default: throw new IllegalArgumentException("Unknown Day: " + input);
        }
    }
}