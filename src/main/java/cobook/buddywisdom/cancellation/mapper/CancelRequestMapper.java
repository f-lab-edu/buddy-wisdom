package cobook.buddywisdom.cancellation.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import cobook.buddywisdom.cancellation.domain.CancelRequest;

@Mapper
public interface CancelRequestMapper {
	Optional<CancelRequest> findById(long id);
	List<CancelRequest> findBySenderId(long senderId);
	List<CancelRequest> findByReceiverId(long receiverId);
	void save(CancelRequest cancelRequest);
	void updateConfirmYn(long id, boolean confirmYn);
}
