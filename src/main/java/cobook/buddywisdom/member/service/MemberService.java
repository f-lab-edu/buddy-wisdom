package cobook.buddywisdom.member.service;

import cobook.buddywisdom.member.dto.CreateMemberDto;
import cobook.buddywisdom.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    public Integer createMember(CreateMemberDto dto) throws NoSuchAlgorithmException {

        // 비밀번호 암호화
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        // 해시된 바이트 배열을 16진수 문자열로 변환하는 과정
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String encryptPassword = hexString.toString();

        Integer success = memberMapper.createMember(CreateMemberDto.of(dto, encryptPassword));

        if(success < 1) {
            throw new IllegalArgumentException("회원가입에 실패했습니다.");
        }
        return success;
    }
}
