package com.joe.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joe.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author joe
 * @since 2024-05-27
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> findListByUserId(@Param("userId") Long userId);
}
