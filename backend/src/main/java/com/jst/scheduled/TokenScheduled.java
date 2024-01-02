package com.jst.scheduled;
import com.auth0.jwt.JWT;
import com.jst.domain.login.repository.TokenEntity;
import com.jst.domain.login.repository.TokenRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Log4j2
@Component
public class TokenScheduled {
    @Autowired
    private TokenRepository tokenRepository;
    @Scheduled(fixedDelay = 60000)
    private void checkExpiredRefreshToken(){
        log.debug("checkExpiredRefreshToken!!!!!!!!!!! start");
        List<TokenEntity> tokenList= tokenRepository.findAll();
        tokenList.stream().forEach(tokenEntity -> {
            log.debug("removeRefreshToken!!!!!!!!!!!");
            if(JWT.decode(tokenEntity.getRefreshToken()).getExpiresAt().before(new Date())){
                log.debug("removeRefreshToken!!!!!!!!!!!");
                tokenRepository.delete(tokenEntity);
            }
        });
    }
}
