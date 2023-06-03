package cobook.buddywisdom.cancellation.service;

import cobook.buddywisdom.cancellation.domain.CancelRequest;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.exception.ConfirmedCancelRequestException;
import cobook.buddywisdom.cancellation.exception.NotFoundCancelRequestException;
import cobook.buddywisdom.cancellation.mapper.CancelRequestMapper;
import cobook.buddywisdom.cancellation.vo.DirectionType;
import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;
import cobook.buddywisdom.messaging.producer.FeedMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cobook.buddywisdom.global.vo.MessageTemplate.CONFIRM_CANCEL_SCHEDULE;
import static cobook.buddywisdom.global.vo.MessageTemplate.REQUEST_CANCEL_SCHEDULE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CancelRequestService {

	private final CancelRequestMapper cancelRequestMapper;
	private final CoachScheduleService coachScheduleService;
	private final MenteeScheduleService menteeScheduleService;
	private final FeedMessageProducer feedMessageProducer;

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

		feedMessageProducer.produceScheduleEvent(menteeId, coachSchedule.getCoachId(),
				() -> MessageFormat.format(REQUEST_CANCEL_SCHEDULE, coachSchedule.getPossibleDateTime()));

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
		feedMessageProducer.produceScheduleEvent(memberId, coachSchedule.getCoachId(),
				() -> MessageFormat.format(CONFIRM_CANCEL_SCHEDULE, coachSchedule.getPossibleDateTime()));

	}
}
