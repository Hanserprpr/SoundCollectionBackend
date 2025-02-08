package com.iqiongzhi.SCB.service;

import com.iqiongzhi.SCB.data.po.Sound;
import com.iqiongzhi.SCB.data.repository.SoundRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

@Service
public class SoundSyncService {

    @Autowired
    private SoundRepository soundRepository;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void syncOnStartup() {
        syncSoundsFromMySQL();
    }

    @Scheduled(fixedRate = 600000)  // 每 10 分钟执行一次
    public void syncSoundsFromMySQL() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT s.id, s.user_id, s.title, s.category, s.cover_url, s.views, s.created_at, s.description, s.location, " +
                             "(SELECT COUNT(*) FROM likes l WHERE l.sound_id = s.id) AS likes, " +
                             "(SELECT COUNT(*) FROM comments c WHERE c.sound_id = s.id) AS comments, " +
                             "u.username " +
                             "FROM sound s " +
                             "JOIN user u ON s.user_id = u.id")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String coverUrl = rs.getString("cover_url");
                String location = rs.getString("location");
                int views = rs.getInt("views");
                Date createdAt = rs.getTimestamp("created_at");

                int likes = rs.getInt("likes");
                int comments = rs.getInt("comments");
                String username = rs.getString("username");

                double hotScore = views * 0.5 + likes * 0.2 + comments * 0.3;  // 计算热度

                Sound sound = new Sound();
                sound.setId(id);
                sound.setTitle(title);
                sound.setDescription(description);
                sound.setCategory(category);
                sound.setCoverUrl(coverUrl);
                sound.setLocation(location);
                sound.setViews(views);
                sound.setLikes(likes);
                sound.setComments(comments);
                sound.setHotScore(hotScore);
                sound.setCreatedAt(createdAt);
                sound.setUsername(username);

                soundRepository.save(sound);
            }
            System.out.println("Elasticsearch 数据同步完成！");
        } catch (Exception e) {
            throw new RuntimeException("同步数据失败", e);
        }
    }


}
