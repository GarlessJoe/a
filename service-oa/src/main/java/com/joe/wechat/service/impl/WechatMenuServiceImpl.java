package com.joe.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joe.model.wechat.Menu;
import com.joe.vo.wechat.MenuVo;
import com.joe.wechat.mapper.WechatMenuMapper;
import com.joe.wechat.service.WechatMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author joe
 * @since 2024-06-05
 */
@Service
public class WechatMenuServiceImpl extends ServiceImpl<WechatMenuMapper, Menu> implements WechatMenuService {

    @Override
    public List<MenuVo> findMenuInfo() {
        //先查询所有的菜单集合
        List<Menu> menus = baseMapper.selectList(null);
        //查询所有的一级菜单
        List<Menu> FirstMenu = baseMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, 0));
        //便利一级菜单 ， 得到每个一级菜单
        ArrayList<MenuVo> menuVoList = new ArrayList<>();
        for (Menu firstMenu : FirstMenu) {
            //获取每个一级菜单里面所有二级菜单 id = parentid
            //将二级菜单封装到一级菜单的children 集合里面
            MenuVo firstmenuVo = new MenuVo();
            BeanUtils.copyProperties(firstMenu , firstmenuVo);
            List<Menu> SecondMenu = menus.stream().filter(menu -> menu.getParentId().longValue() == firstMenu.getId()).collect(Collectors.toList());
            ArrayList<MenuVo> children = new ArrayList<>();
            for (Menu secondMenu : SecondMenu) {
                MenuVo SecondmenuVo = new MenuVo();
                BeanUtils.copyProperties(secondMenu , SecondmenuVo);
                children.add(SecondmenuVo);
            }
            firstmenuVo.setChildren(children);
            menuVoList.add(firstmenuVo);
        }

        return menuVoList;
    }
}
