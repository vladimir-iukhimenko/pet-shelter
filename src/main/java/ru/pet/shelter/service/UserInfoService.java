package ru.pet.shelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pet.shelter.dto.UserInfo;
import ru.pet.shelter.service.utils.VkTokenChecker;


@Service
public class UserInfoService {

    private final VkTokenChecker vkTokenChecker;

    @Autowired
    public UserInfoService(VkTokenChecker vkTokenChecker) {
        this.vkTokenChecker = vkTokenChecker;
    }

    public UserInfo getUserInfoFromToken(String token) {
        return vkTokenChecker.findUserInfoByToken(token).get();
    }
}
