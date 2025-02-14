package com.iqiongzhi.SCB.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.po.User;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {
    private User user;
    private int followCount;
    private int fansCount;
    @JsonProperty("isFollow")
    private boolean isFollow;
    private List<Sound> SoundList;

    public UserProfileDTO(User user, int followCount, int fansCount, boolean isFollow, List<Sound> soundList) {
        this.user = user;
        this.followCount = followCount;
        this.fansCount = fansCount;
        this.isFollow = isFollow;
        this.SoundList = soundList;
    }

    public UserProfileDTO(User user, int followCount, int fansCount) {
        this.user = user;
        this.followCount = followCount;
        this.fansCount = fansCount;
    }
}
