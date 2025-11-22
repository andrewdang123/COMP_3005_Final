package models;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;

/**
 * GroupFitnessClassMembers is the join table linking members to fitness classes.
 * - classMemberId is the PK; class_id and member_id are FK indexes used for fast
 *   lookups like “members in a class” or “classes for a member.”
 *
 * Trigger-like callbacks:
 * - @PrePersist blocks duplicates, enforces class capacity, and increments the
 *   class’s member count.
 * - @PreRemove decrements the count when someone is removed.
 *
 * These callbacks act as application-level triggers to enforce rules and keep
 * currentMembers accurate.
 */

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

    /**
     * PrePersist “trigger”:
     * - Check if this member is already in the class (prevents duplicate join rows).
     * - Check if the class is already at capacity.
     * - If both checks pass, increment currentMembers on the parent class.
     * - Throwing RuntimeException here cancels the insert and rolls back the transaction.
     */

    @PrePersist
    public void onPrePersist() {
        if (groupFitnessClass != null) {
            boolean alreadyExists = groupFitnessClass.getMembers().stream()
                    .anyMatch(m -> m != this && m.getMember().equals(member));

            if (alreadyExists) {
                throw new RuntimeException("Cannot add member: '" + member.getName()
                        + "' is already registered in class '"
                        + groupFitnessClass.getClassName() + "'.");
            }

            if (groupFitnessClass.getCurrentMembers() >= groupFitnessClass.getCapacity()) {
                throw new RuntimeException("Cannot add member: Class '"
                        + groupFitnessClass.getClassName()
                        + "' is already full (capacity "
                        + groupFitnessClass.getCapacity() + ").");
            }

            // Safe increment
            groupFitnessClass.incrementMembers();
        }
    }

    /**
     * PreRemove “trigger”:
     * - Before this membership row is deleted, decrement the parent class’s
     *   currentMembers counter so it stays in sync.
     */

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
