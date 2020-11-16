package com.cr.toutiao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cr
 * @date 2020-11-16 23:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String link;

    private String image;

    private Integer likeCount;

    private Integer commentCount;

    private Date createdDate;

    private Integer userId;
}
