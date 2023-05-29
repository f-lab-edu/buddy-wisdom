package cobook.buddywisdom.mentee.service;

import cobook.buddywisdom.mentee.mapper.MenteeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class MenteeService {

    private final MenteeMapper menteeMapper;
    public Integer deleteMentee(final long menteeId) {
        int active_yn = 0;
        return menteeMapper.deleteMentee(menteeId, active_yn);
    }
}