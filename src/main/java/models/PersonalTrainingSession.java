package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;


@Entity
@Table(name = "personal_training_sessions")
public class PersonalTrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(
        name = "member_id", 
        referencedColumnName = "memberId",
        foreignKey = @ForeignKey(name = "FK_personalTrainingSession_member")
    )
    private Member member;

    @ManyToOne
    @JoinColumn(
        name = "trainer_id", 
        referencedColumnName = "trainerId",
        foreignKey = @ForeignKey(name = "FK_personalTrainingSession_trainer")
    )
    private Trainer trainer;

    @NotNull
    @Column(nullable = false)
    private int roomNum;

    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "dayOfWeek", column = @Column(name = "session_Day", nullable = false)),
        @AttributeOverride(name = "startTime", column = @Column(name = "session_StartTime", nullable = false)),
        @AttributeOverride(name = "endTime", column = @Column(name = "session_EndTime", nullable = false))
    })
    private Schedule sessionTime;

    public PersonalTrainingSession(Member member, Trainer trainer, int roomNum, String dayOfWeek, int startTime, int endTime) {
        this.member = member;
        this.trainer = trainer;
        this.roomNum = roomNum;
        this.sessionTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    public PersonalTrainingSession(Member member, Trainer trainer, int roomNum, DayOfWeek dayOfWeek, int startTime, int endTime) {
        this.member = member;
        this.trainer = trainer;
        this.roomNum = roomNum;
        this.sessionTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    // Getters and setters
    public Long getSessionId() { return sessionId; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public int getRoomNum() { return roomNum; }
    public void setRoomNum(int roomNum) { this.roomNum = roomNum; }

    public Schedule getSessionTime() { return sessionTime; }
    public void setSessionTime(String dayOfWeek, int startTime, int endTime) { 
        this.sessionTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    @Override
    public String toString() {
        return sessionId + "\t" + member.getMemberId() + "\t" + trainer.getTrainerId() + "\t" + roomNum + "\t" + sessionTime;
    }
}
