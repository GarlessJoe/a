package com.joe.wechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.joe.model.wechat.Menu;
import com.joe.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author joe
 * @since 2024-06-05
 */
public interface WechatMenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();
}
