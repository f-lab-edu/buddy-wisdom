package cobook.buddywisdom.cancellation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cobook.buddywisdom.cancellation.controller.DirectionType;
import cobook.buddywisdom.cancellation.domain.CancelRequest;
import cobook.buddywisdom.cancellation.dto.response.CancelRequestResponseDto;
import cobook.buddywisdom.cancellation.exception.ConfirmedCancelRequestException;
import cobook.buddywisdom.cancellation.exception.NotFoundCancelRequestException;
import cobook.buddywisdom.cancellation.mapper.CancelRequestMapper;
import cobook.buddywisdom.coach.domain.CoachSchedule;
import cobook.buddywisdom.coach.service.CoachScheduleService;
import cobook.buddywisdom.global.exception.ErrorMessage;
import cobook.buddywisdom.mentee.service.MenteeScheduleService;

@Service
@Transactional(readOnly = true)
public class CancelRequestService {
	private final CancelRequestMapper cancelRequestMapper;
	private final CoachScheduleService coachScheduleService;
	private final MenteeScheduleService menteeScheduleService;

	public CancelRequestService(CancelRequestMapper cancelRequestMapper, CoachScheduleService coachScheduleService,
		MenteeScheduleService menteeScheduleService) {
		this.cancelRequestMapper = cancelRequestMapper;
		this.coachScheduleService = coachScheduleService;
		this.menteeScheduleService = menteeScheduleService;
	}

	public List<CancelRequestResponseDto> getCancelRequest(long memberId, DirectionType direction) {
		List<CancelRequest> cancelRequestList = new ArrayList<>();

		if (direction.equals(DirectionType.SENT)) {
			cancelRequestList = cancelRequestMapper.findBySenderId(memberId);
		}
		if (direction.equals(DirectionType.RECEIVED)) {
			cancelRequestList = cancelRequestMapper.findByReceiverId(memberId);
		}

		return Optional.ofNullable(cancelRequestList)
			.stream()
			.flatMap(List::stream)
			.map(CancelRequestResponseDto::from)
			.collect(Collectors.toUnmodifiableList());
	}

	@Transactional
	public CancelRequestResponseDto saveCancelRequestByMentee(long menteeId, long menteeScheduleId, String reason) {
		menteeScheduleService.getMenteeSchedule(menteeId, menteeScheduleId);

		CoachSchedule coachSchedule = coachScheduleService.getCoachSchedule(menteeScheduleId, true);

		CancelRequest cancelRequest = CancelRequest.requestOf(menteeScheduleId, menteeId, coachSchedule.getCoachId(), reason);
		cancelRequestMapper.save(cancelRequest);

		return CancelRequestResponseDto.from(cancelRequest);
	}

	@Transactional
	public void confirmCancelRequest(long cancelRequestId, long menteeScheduleId) {
		CancelRequest cancelRequest = cancelRequestMapper.findById(cancelRequestId)
			.orElseThrow(() -> new NotFoundCancelRequestException(ErrorMessage.NOT_FOUND_CANCEL_REQUEST));

		if (cancelRequest.isConfirmYn()) {
			throw new ConfirmedCancelRequestException(ErrorMessage.CONFIRMED_CANCEL_REQUEST);
		}

		cancelRequestMapper.updateConfirmYn(cancelRequestId, true);

		menteeScheduleService.deleteMenteeSchedule(menteeScheduleId);
	}
}
