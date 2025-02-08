package com.iqiongzhi.SCB.data.po;

import lombok.Data;

@Data
public class SoundHotness {
    private Sound sound;
    private double hotness;

    public SoundHotness(Sound sound, double hotness) {
        this.sound = sound;
        this.hotness = hotness;
    }

}

