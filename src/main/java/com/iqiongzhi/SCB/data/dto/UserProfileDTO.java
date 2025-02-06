package com.iqiongzhi.SCB.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iqiongzhi.SCB.data.po.User;
import lombok.Data;

@Data
public class UserProfileDTO {
    private User user;
    private int followCount;
    private int fansCount;
    @JsonProperty("isFollow")
    private boolean isFollow;

    public UserProfileDTO(User user, int followCount, int fansCount, boolean isFollow) {
        this.user = user;
        this.followCount = followCount;
        this.fansCount = fansCount;
        this.isFollow = isFollow;
    }
}
