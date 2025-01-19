# MySQL设计

本文档描述了项目中使用的 MySQL 数据库设计，包含以下主要表结构：

1. **用户表 (user)**：存储用户的基本信息和状态。
2. **声音表 (sound)**：记录用户上传的声音信息。
3. **点赞记录表 (likes)**：记录用户对声音的点赞信息。
4. **评论表 (comments)**：支持用户对声音发表评论和回复评论。
5. **收藏记录表 (collections)**：记录用户对声音的收藏信息。
6. **声音合集表 (playlists)**：记录用户创建的声音合集。
7. **声音合集内容表 (playlist_sounds)**：存储声音合集与声音的多对多关系。
8. **搜索记录表 (search_logs)**：记录用户的搜索行为，用于热门关键词统计和推荐功能。

## 创建数据库

```sql
CREATE DATABASE SoundCollection
CHARACTER SET utf8mb4
COLLATE utf8mb4_bin;
```

---

## 用户表 (user)

用户表用于存储用户的基本信息和状态。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 用户唯一ID              |
| **username**    | `VARCHAR(50) NOT NULL UNIQUE` | 非空，唯一                              | 用户名                 |
| **email**       | `VARCHAR(100) UNIQUE`        | 唯一                                    | 邮箱                   |
| **password**    | `CHAR(128) NOT NULL`         | 非空                                    | 加密后的密码            |
| **wechat**      | `VARCHAR(100) UNIQUE`        | 唯一（可选）                             | 微信号（可选）          |
| **student_id**  | `CHAR(20) UNIQUE NOT NULL`   | 非空，唯一                              | 学号                   |
| **bio**         | `TEXT`                       | 无特殊约束                               | 个人简介               |
| **avatar_url**  | `VARCHAR(255)`               | 无特殊约束                               | 头像URL地址            |
| **created_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 注册时间               |
| **updated_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | 自动更新为当前时间                       | 最后更新时间            |
| **last_login_at** | `TIMESTAMP DEFAULT NULL`   | 可为空                                  | 最后登录时间            |
| **status**      | `TINYINT DEFAULT 1`          | 默认值为1                               | 用户状态（1: 正常，0: 禁用） |

---

### 创建指令

```sql
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    password CHAR(128) NOT NULL COMMENT '加密后的密码',
    wechat VARCHAR(100) UNIQUE COMMENT '微信号（可选）',
    student_id CHAR(20) UNIQUE NOT NULL COMMENT '学号',
    bio TEXT COMMENT '个人简介',
    avatar_url VARCHAR(255) COMMENT '头像URL地址',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    last_login_at TIMESTAMP DEFAULT NULL COMMENT '最后登录时间',
    status TINYINT DEFAULT 1 COMMENT '用户状态（1: 正常，0: 禁用）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

## 声音表 (sound)

声音表用于存储用户上传的声音信息，包括声音的标题、分类、文件路径、封面图等内容。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 声音唯一ID，用于唯一标识声音 |
| **user_id**     | `INT NOT NULL`               | 外键，关联用户表                         | 上传该声音的用户ID      |
| **title**       | `VARCHAR(100) NOT NULL`      | 非空                                    | 声音的标题             |
| **description** | `TEXT`                       | 无特殊约束                               | 声音的描述信息          |
| **category**    | `ENUM('自然声', '人声', '演奏', '合成') NOT NULL` | 非空                                    | 声音的分类             |
| **file_url**    | `VARCHAR(255) NOT NULL`      | 非空                                    | 声音文件的存储URL       |
| **cover_url**   | `VARCHAR(255)`               | 无特殊约束                               | 声音封面的图片URL       |
| **location**    | `VARCHAR(100)`               | 无特殊约束                               | 声音上传时的位置信息    |
| **created_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 声音的上传时间          |
| **updated_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | 自动更新为当前时间                       | 声音最后一次信息更新时间 |

---

### 创建指令

```sql
CREATE TABLE sound (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '声音唯一ID，用于唯一标识声音',
    user_id INT NOT NULL COMMENT '上传该声音的用户ID',
    title VARCHAR(100) NOT NULL COMMENT '声音的标题',
    description TEXT COMMENT '声音的描述信息',
    category ENUM('自然声', '人声', '演奏', '合成') NOT NULL COMMENT '声音的分类',
    file_url VARCHAR(255) NOT NULL COMMENT '声音文件的存储URL',
    cover_url VARCHAR(255) COMMENT '声音封面的图片URL',
    location VARCHAR(100) COMMENT '声音上传时的位置信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '声音的上传时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '声音最后一次信息更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='声音表';
```

## 点赞记录表 (likes)

点赞记录表用于存储用户对声音的点赞信息，包括点赞用户和对应的声音ID。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 点赞记录的唯一ID       |
| **user_id**     | `INT NOT NULL`               | 外键，关联用户表                         | 点赞者的用户ID         |
| **sound_id**    | `INT NOT NULL`               | 外键，关联声音表                         | 被点赞的声音ID         |
| **created_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 点赞时间               |

---

### 创建指令

```sql
CREATE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞记录的唯一ID',
    user_id INT NOT NULL COMMENT '点赞者的用户ID',
    sound_id INT NOT NULL COMMENT '被点赞的声音ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (sound_id) REFERENCES sound(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞记录表';
```

## 评论表 (comments)

评论表用于存储用户对声音的评论信息，包括评论内容、评论用户、评论时间等。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 评论的唯一ID           |
| **sound_id**    | `INT NOT NULL`               | 外键，关联声音表                         | 被评论的声音ID         |
| **user_id**     | `INT NOT NULL`               | 外键，关联用户表                         | 发表评论的用户ID       |
| **content**     | `TEXT NOT NULL`              | 非空                                    | 评论内容               |
| **parent_id**   | `INT DEFAULT NULL`           | 可以为空                                | 父评论ID（用于回复）   |
| **created_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 评论时间               |
| **updated_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | 自动更新为当前时间                       | 最后一次更新时间       |

---

### 创建指令

```sql
CREATE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '评论的唯一ID',
    sound_id INT NOT NULL COMMENT '被评论的声音ID',
    user_id INT NOT NULL COMMENT '发表评论的用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    parent_id INT DEFAULT NULL COMMENT '父评论ID（用于回复）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
    FOREIGN KEY (sound_id) REFERENCES sound(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';
```

## 收藏记录表 (collections)

收藏记录表用于存储用户对声音的收藏信息，包括收藏用户和对应的声音ID。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 收藏记录的唯一ID       |
| **user_id**     | `INT NOT NULL`               | 外键，关联用户表                         | 收藏者的用户ID         |
| **sound_id**    | `INT NOT NULL`               | 外键，关联声音表                         | 被收藏的声音ID         |
| **created_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 收藏时间               |

---

### 创建指令

```sql
CREATE TABLE collections (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏记录的唯一ID',
    user_id INT NOT NULL COMMENT '收藏者的用户ID',
    sound_id INT NOT NULL COMMENT '被收藏的声音ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (sound_id) REFERENCES sound(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏记录表';
```

## 声音合集表 (playlists)

声音合集表用于存储用户创建的声音合集信息，包括合集标题和描述等。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 声音合集的唯一ID       |
| **user_id**     | `INT NOT NULL`               | 外键，关联用户表                         | 创建合集的用户ID       |
| **title**       | `VARCHAR(100) NOT NULL`      | 非空                                    | 合集的标题             |
| **description** | `TEXT`                       | 无特殊约束                               | 合集的描述信息          |
| **created_at**  | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 合集创建时间           |

---

### 创建指令

```sql
CREATE TABLE playlists (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '声音合集的唯一ID',
    user_id INT NOT NULL COMMENT '创建合集的用户ID',
    title VARCHAR(100) NOT NULL COMMENT '合集的标题',
    description TEXT COMMENT '合集的描述信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '合集创建时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='声音合集表';
```

## 声音合集内容表 (playlist_sounds)

声音合集内容表用于存储每个声音合集中的声音信息。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 记录的唯一ID           |
| **playlist_id** | `INT NOT NULL`               | 外键，关联声音合集表                     | 所属声音合集的ID       |
| **sound_id**    | `INT NOT NULL`               | 外键，关联声音表                         | 合集中的声音ID         |
| **added_at**    | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 声音加入合集的时间     |

---

### 创建指令

```sql
CREATE TABLE playlist_sounds (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '记录的唯一ID',
    playlist_id INT NOT NULL COMMENT '所属声音合集的ID',
    sound_id INT NOT NULL COMMENT '合集中的声音ID',
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '声音加入合集的时间',
    FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE,
    FOREIGN KEY (sound_id) REFERENCES sound(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='声音合集内容表';
```

## 搜索记录表 (search_logs)

搜索记录表用于存储用户的搜索行为信息，包括搜索的关键词和搜索时间。

---

### 表结构

| 字段名          | 数据类型                     | 约束                                    | 描述                   |
|------------------|------------------------------|-----------------------------------------|------------------------|
| **id**          | `INT AUTO_INCREMENT PRIMARY KEY` | 主键，自动递增                           | 搜索记录的唯一ID       |
| **user_id**     | `INT NOT NULL`               | 外键，关联用户表                         | 搜索者的用户ID         |
| **keyword**     | `VARCHAR(100) NOT NULL`      | 非空                                    | 搜索的关键词           |
| **searched_at** | `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` | 自动生成，默认当前时间                   | 搜索时间               |

---

### 创建指令

```sql
CREATE TABLE search_logs (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '搜索记录的唯一ID',
    user_id INT NOT NULL COMMENT '搜索者的用户ID',
    keyword VARCHAR(100) NOT NULL COMMENT '搜索的关键词',
    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '搜索时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索记录表';
```
