package com.iqiongzhi.SCB.utils;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.mapper.TagMapper;
import com.iqiongzhi.SCB.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SoundUtils {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private UserMapper userMapper;

    public List<Sound> addTags(List<Sound> sounds) {
        List<Integer> soundIds = sounds.stream().map(Sound::getId).toList();
        List<Map<String, Object>> tagsData = tagMapper.getTagsBySoundIds(soundIds);
        Map<Integer, List<String>> tagsMap = new HashMap<>();
        for (Map<String, Object> entry : tagsData) {
            Integer soundId = (Integer) entry.get("sound_id");
            String tag = (String) entry.get("name");
            tagsMap.computeIfAbsent(soundId, k -> new ArrayList<>()).add(tag);
        }

        for (Sound sound : sounds) {
            sound.setTags(tagsMap.getOrDefault(sound.getId(), new ArrayList<>()));
        }
        return sounds;
    }

    public List<Sound> addTagsAndUsername(List<Sound> sounds) {
        List<Integer> soundIds = sounds.stream().map(Sound::getId).toList();

        // 查询所有的 tag
        List<Map<String, Object>> tagsData = tagMapper.getTagsBySoundIds(soundIds);
        Map<Integer, List<String>> tagsMap = new HashMap<>();
        for (Map<String, Object> entry : tagsData) {
            Integer soundId = (Integer) entry.get("sound_id");
            String tag = (String) entry.get("name");
            tagsMap.computeIfAbsent(soundId, k -> new ArrayList<>()).add(tag);
        }

        // 查询原始数据
        List<Map<String, Object>> rawUsernames = userMapper.getUsernamesBySoundIds(soundIds);

        // 手动转换为 Map<Integer, String>
        Map<Integer, String> usernameMap = rawUsernames.stream()
                .collect(Collectors.toMap(
                        entry -> (Integer) entry.get("sound_id"),
                        entry -> (String) entry.get("username")
                ));

        // 赋值 tags 和 username
        for (Sound sound : sounds) {
            sound.setTags(tagsMap.getOrDefault(sound.getId(), new ArrayList<>()));
            sound.setUsername(usernameMap.getOrDefault(sound.getId(), "未知用户"));
        }

        return sounds;
    }

}
