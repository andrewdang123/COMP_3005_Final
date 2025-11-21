package services;

import models.LatestHealthMetricDTO;
import org.hibernate.Session;

import java.util.List;

public class MemberService {

    private Session session;

    public MemberService(Session session) {
        this.session = session;
    }

    // View
    public List<LatestHealthMetricDTO> getLatestHealthMetrics() {
        return session.createQuery(
                "SELECT new models.LatestHealthMetricDTO(m.memberId, m.name, hm.currentWeight, hm.currentBmi, hm.timestamp) "
                        +
                        "FROM HealthMetric hm " +
                        "JOIN hm.member m " +
                        "WHERE hm.timestamp = (" +
                        "    SELECT MAX(h.timestamp) FROM HealthMetric h WHERE h.member = m" +
                        ")",
                LatestHealthMetricDTO.class).getResultList();
    }
}
