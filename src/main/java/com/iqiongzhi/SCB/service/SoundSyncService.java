package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import com.iqiongzhi.SCB.utils.SoundUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SoundSyncService {

    @Autowired
    private SoundRepository soundRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SoundUtils soundUtils;

    @PostConstruct
    public void syncOnStartup() {
        syncSoundsFromMySQL();
    }

    @Scheduled(fixedRate = 600000)  // 每 10 分钟执行一次
    public void syncSoundsFromMySQL() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, s.user_id, s.title, s.category, s.cover_url, s.views, s.created_at, " +
                             "s.description, s.location, u.username, " +
                             "COALESCE(l.likes_count, 0) AS likes, " +
                             "COALESCE(c.comments_count, 0) AS comments " +
                             "FROM sound s " +
                             "JOIN user u ON s.user_id = u.id " +
                             "LEFT JOIN (SELECT sound_id, COUNT(*) AS likes_count FROM likes GROUP BY sound_id) l ON s.id = l.sound_id " +
                             "LEFT JOIN (SELECT sound_id, COUNT(*) AS comments_count FROM comments GROUP BY sound_id) c ON s.id = c.sound_id " +
                             "WHERE s.created_at >= NOW() - INTERVAL 10 MINUTE")) {  // 只同步最近10分钟的数据

            ResultSet rs = stmt.executeQuery();
            List<Sound> soundList = new ArrayList<>();

            while (rs.next()) {
                Sound sound = new Sound();
                sound.setId(rs.getInt("id"));
                sound.setTitle(rs.getString("title"));
                sound.setDescription(rs.getString("description"));
                sound.setCategory(rs.getString("category"));
                sound.setCoverUrl(rs.getString("cover_url"));
                sound.setLocation(rs.getString("location"));
                sound.setViews(rs.getInt("views"));
                sound.setLikes(rs.getInt("likes"));
                sound.setComments(rs.getInt("comments"));
                sound.setHotScore(sound.getViews() * 0.5 + sound.getLikes() * 0.2 + sound.getComments() * 0.3);
                sound.setCreatedAt(rs.getTimestamp("created_at"));
                sound.setUsername(rs.getString("username"));
                soundList.add(sound);
            }

            if (!soundList.isEmpty()) {
                soundList = soundUtils.addTags(soundList);
                soundRepository.saveAll(soundList);
            }

            System.out.println("Elasticsearch 增量数据同步完成！");

        } catch (Exception e) {
            throw new RuntimeException("同步数据失败", e);
        }
    }



}
