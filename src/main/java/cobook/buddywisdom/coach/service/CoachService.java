package cobook.buddywisdom.coach.service;


import cobook.buddywisdom.coach.mapper.CoachMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachMapper coachMapper;
    public Integer deleteCoach(final long coachId) {
        int active_yn = 0;
        return coachMapper.deleteCoach(coachId, active_yn);
    }
}
