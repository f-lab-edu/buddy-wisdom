package cobook.buddywisdom.cancellation.service;

import static cobook.buddywisdom.global.vo.MessageTemplate.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.cancellation.vo.DirectionType;
import cobook.buddywisdom.cancellation.domain.CancelRequest;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.exception.ConfirmedCancelRequestException;
import cobook.buddywisdom.cancellation.exception.NotFoundCancelRequestException;
import cobook.buddywisdom.cancellation.mapper.CancelRequestMapper;
import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CancelRequestService {

	private final CancelRequestMapper cancelRequestMapper;
	private final CoachScheduleService coachScheduleService;
	private final MenteeScheduleService menteeScheduleService;
	private final CancelRequestEventService cancelRequestEventService;

	public List<CancelRequestResponseDto> getCancelRequest(long memberId, DirectionType direction) {
		List<CancelRequest> cancelRequestList = new ArrayList<>();

		if (direction.equals(DirectionType.SENT)) {
			cancelRequestList = cancelRequestMapper.findBySenderId(memberId);
		} else if (direction.equals(DirectionType.RECEIVED)) {
			cancelRequestList = cancelRequestMapper.findByReceiverId(memberId);
		}

		return Optional.ofNullable(cancelRequestList)
			.stream()
			.flatMap(List::stream)
			.map(CancelRequestResponseDto::from)
			.toList();
	}

	@Transactional
	public CancelRequestResponseDto saveCancelRequestByMentee(long menteeId, long menteeScheduleId, String reason) {
		menteeScheduleService.getMenteeSchedule(menteeId, menteeScheduleId);

		CoachSchedule coachSchedule = coachScheduleService.getCoachSchedule(menteeScheduleId, true);

		CancelRequest cancelRequest = CancelRequest.requestOf(menteeScheduleId, menteeId, coachSchedule.getCoachId(), reason);
		cancelRequestMapper.save(cancelRequest);

		cancelRequestEventService.produceCancelRequestEvent(menteeId, coachSchedule.getCoachId(),
			coachSchedule.getPossibleDateTime(), REQUEST_CANCEL_SCHEDULE);

		return CancelRequestResponseDto.from(cancelRequest);
	}


	@Transactional
	public void confirmCancelRequest(long memberId, long cancelRequestId, long menteeScheduleId) {
		CancelRequest cancelRequest = cancelRequestMapper.findById(cancelRequestId)
			.orElseThrow(() -> new NotFoundCancelRequestException(ErrorMessage.NOT_FOUND_CANCEL_REQUEST));

		if (cancelRequest.isConfirmYn()) {
			throw new ConfirmedCancelRequestException(ErrorMessage.CONFIRMED_CANCEL_REQUEST);
		}

		cancelRequestMapper.updateConfirmYn(cancelRequestId, true);
		menteeScheduleService.deleteMenteeSchedule(menteeScheduleId);
		coachScheduleService.updateMatchYn(menteeScheduleId, false);

		CoachSchedule coachSchedule = coachScheduleService.getCoachSchedule(menteeScheduleId, false);
		cancelRequestEventService.produceCancelRequestEvent(memberId, coachSchedule.getCoachId(),
			coachSchedule.getPossibleDateTime(), CONFIRM_CANCEL_SCHEDULE);
	}
}
