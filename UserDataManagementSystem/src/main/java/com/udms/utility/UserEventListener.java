package com.udms.utility;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.udms.entity.User;

@Component
public class UserEventListener extends AbstractMongoEventListener<User> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<User> event) {
        User user = event.getSource();
        if (user.getCreatedDate() == null) {
            user.setCreatedDate(LocalDateTime.now());
        }
        if (user.getApplication() == null) {
            user.setApplication(UdmsUtility.fetchAppName());
        }
        user.setLastUpdated(LocalDateTime.now());
    }
}