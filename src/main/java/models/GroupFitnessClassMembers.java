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
 * GroupFitnessClassMembers is the join table between Member and GroupFitnessClass:
 * - classMemberId is the primary key; the DB creates a PK index on this column.
 * - class_id is a @ManyToOne FK to GroupFitnessClass; queries like
 *   “all members in a given class” use the FK index on class_id.
 * - member_id is a @ManyToOne FK to Member; queries like
 *   “all classes for a given member” use the FK index on member_id.
 *
 * “Trigger”-style logic:
 * - @PrePersist runs before a new row is inserted:
 *   • Prevents duplicate registrations of the same Member in the same class.
 *   • Enforces capacity by checking currentMembers vs capacity.
 *   • Calls groupFitnessClass.incrementMembers() so the in-memory counter matches
 *     the number of join rows.
 * - @PreRemove runs before a row is deleted:
 *   • Calls groupFitnessClass.decrementMembers() to keep the count in sync.
 *
 * Together, these lifecycle callbacks act like application-level triggers that:
 * - enforce business rules (no duplicates, no over-capacity),
 * - and keep GroupFitnessClass.currentMembers consistent with the membership table.
 * (The database can also have a trigger on this table to enforce the same logic at the DB level.)
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
