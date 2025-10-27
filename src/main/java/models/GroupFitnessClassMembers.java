package models;

import jakarta.persistence.*;

@Entity
@Table(name = "group_fitness_class_members")
public class GroupFitnessClassMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classMemberId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", referencedColumnName = "classId", foreignKey = @ForeignKey(name = "FK_groupFitnessClass_class"))
    private GroupFitnessClass groupFitnessClass;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "memberId", foreignKey = @ForeignKey(name = "FK_groupFitnessClass_member"))
    private Member member;

    public GroupFitnessClassMembers() {
    }

    public GroupFitnessClassMembers(GroupFitnessClass groupFitnessClass, Member member) {
        this.groupFitnessClass = groupFitnessClass;
        this.member = member;
    }

    // --- SAFE trigger ---
    @PrePersist
    public void onPrePersist() {
        if (groupFitnessClass != null) {
            groupFitnessClass.incrementMembers();
        }
    }

    @PreRemove
    public void onPreRemove() {
        if (groupFitnessClass != null) {
            groupFitnessClass.decrementMembers();
        }
    }

    // --- Getters and Setters ---
    public Long getClassMemberId() {
        return classMemberId;
    }

    public GroupFitnessClass getGroupFitnessClass() {
        return groupFitnessClass;
    }

    public void setGroupFitnessClass(GroupFitnessClass groupFitnessClass) {
        this.groupFitnessClass = groupFitnessClass;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
