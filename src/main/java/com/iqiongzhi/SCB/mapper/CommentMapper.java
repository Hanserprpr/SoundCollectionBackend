package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.dto.CommentDTO;
import com.iqiongzhi.SCB.data.po.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comments (user_id, sound_id, content) values (#{userId}, #{soundId}, #{content})")
    void addComment(Comment comment);

    @Insert("insert into comments_like (user_id, comment_id) values (#{userId}, #{commentId})")
    void likeComment(String userId, String commentId);

    @Delete("delete from comments where id=#{commentId} and user_id=#{userId}")
    void delComment(String commentId, String userId);

    @Delete("delete from comments_like where user_id=#{userId} and comment_id=#{commentId}")
    void unlikeComment(String userId, String commentId);

    @Select("""
                SELECT c.id AS commentId, c.sound_id, c.user_id, c.content, c.created_at,
                       COALESCE(l.like_count, 0) AS likes,
                       u.username, u.avatar_url,
                       IF(cl.user_id IS NOT NULL, true, false) AS isLiked
                FROM comments c
                LEFT JOIN (SELECT comment_id, COUNT(*) AS like_count FROM comments_like GROUP BY comment_id) l
                ON c.id = l.comment_id
                LEFT JOIN user u ON c.user_id = u.id
                LEFT JOIN comments_like cl ON c.id = cl.comment_id AND cl.user_id = #{userId}
                WHERE c.sound_id = #{soundId}
                ORDER BY c.id DESC
                LIMIT 15 OFFSET #{offset}
            """)
    List<CommentDTO> getLatestComments(@Param("soundId") String soundId, @Param("offset") int offset, @Param("userId") String userId);


    @Select("""
                SELECT c.id AS commentId, c.sound_id, c.user_id, c.content, c.created_at,
                       COALESCE(l.like_count, 0) AS likes,
                       u.username, u.avatar_url,
                       CASE WHEN cl.user_id IS NOT NULL THEN true ELSE false END AS isLiked
                FROM comments c
                LEFT JOIN (SELECT comment_id, COUNT(*) AS like_count FROM comments_like GROUP BY comment_id) l
                ON c.id = l.comment_id
                LEFT JOIN user u ON c.user_id = u.id
                LEFT JOIN comments_like cl ON c.id = cl.comment_id AND cl.user_id = #{userId}
                WHERE c.sound_id = #{soundId}
                ORDER BY likes DESC, c.created_at DESC  -- 按点赞数降序排序
                LIMIT 15 OFFSET #{offset}
            """)
    List<CommentDTO> getHotComments(@Param("soundId") String soundId, @Param("offset") int offset, @Param("userId") String userId);


    @Select("select count(*) from comments where sound_id=#{soundId}")
    int getCommentCount(String soundId);

    @Update("update comments set likes = likes + 1 where id = #{commentId}")
    void likeCommentCount(String commentId);

    @Update("update comments set likes = likes - 1 where id = #{commentId}")
    void unlikeCommentCount(String commentId);
}
