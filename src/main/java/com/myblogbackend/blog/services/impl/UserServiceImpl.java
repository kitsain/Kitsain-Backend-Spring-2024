package com.myblogbackend.blog.services.impl;

import com.myblogbackend.blog.config.security.UserPrincipal;
import com.myblogbackend.blog.event.OnUserLogoutSuccessEvent;
import com.myblogbackend.blog.exception.UserLogoutException;
import com.myblogbackend.blog.exception.commons.BlogRuntimeException;
import com.myblogbackend.blog.exception.commons.ErrorCode;
import com.myblogbackend.blog.mapper.UserMapper;
import com.myblogbackend.blog.models.UserEntity;
import com.myblogbackend.blog.repositories.RefreshTokenRepository;
import com.myblogbackend.blog.repositories.UserDeviceRepository;
import com.myblogbackend.blog.repositories.UserTokenRepository;
import com.myblogbackend.blog.repositories.UsersRepository;
import com.myblogbackend.blog.request.LogOutRequest;
import com.myblogbackend.blog.response.UserResponse;
import com.myblogbackend.blog.services.UserService;
import com.myblogbackend.blog.utils.JWTSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(PostServiceImpl.class);
    private final UserDeviceRepository userDeviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final UserTokenRepository tokenRepository;

    @Override
    public void logoutUser(final LogOutRequest logOutRequest, final UserPrincipal currentUser) {
        try {
            var deviceId = logOutRequest.getDeviceInfo().getDeviceId();
            var userDevice = userDeviceRepository.findByUserId(currentUser.getId())
                    .filter(device -> device.getDeviceId().equals(deviceId))
                    .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(),
                            "Invalid device Id supplied. No matching device found for the given user "));
            refreshTokenRepository.deleteById(userDevice.getRefreshToken().getId());
            var logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getEmail(), logOutRequest.getToken(), logOutRequest);
            applicationEventPublisher.publishEvent(logoutSuccessEvent);
            logger.info("User logout successfully");
        } catch (Exception e) {
            logger.error("User failed to logout");
        }
    }

    @Override
    public UserResponse findUserById(final UUID id) {
        try {
            var userEntity = getUserById(id);
            logger.info("Find user with id successfully: {}", id);
            return userMapper.toUserDTO(userEntity);
        } catch (Exception e) {
            logger.error("Failed to find user by id {}", id);
            throw new RuntimeException("Fail to find user by id");
        }
    }

    @Override
    public UserResponse aboutMe() {
        try {
            var signedInUser = JWTSecurityUtil.getJWTUserInfo().orElseThrow();
            var userEntity = getUserById(signedInUser.getId());
            logger.info("Get sign in user information");
            return userMapper.toUserDTO(userEntity);
        } catch (Exception e) {
            logger.error("Failed to get user's sign in information");
            throw new RuntimeException("Failed to get user's sign in information");
        }
    }

    private UserEntity getUserById(final UUID id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new BlogRuntimeException(ErrorCode.ID_NOT_FOUND));
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
