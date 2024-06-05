package com.joe.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.joe.auth.mapper.SysUserMapper;
import com.joe.auth.service.SysMenuService;
import com.joe.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.model.system.SysUser;
import com.joe.security.custom.LoginUserInfoHelper;
import com.joe.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author joe
 * @since 2024-05-26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public SysUser getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getIsDeleted, 0));
    }

    @Override
    public void upadteStatus(Long id, Integer status) {
        SysUser sysUser = baseMapper.selectById(id);
        sysUser.setStatus(status);
        baseMapper.updateById(sysUser);
    }

    @Override
    public int removeById(Long id) {
        SysUser user = baseMapper.selectById(id);
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getId , id)
                .eq(SysUser::getIsDeleted , 0)
                .set(SysUser::getIsDeleted , id);
        int update = baseMapper.update(user, wrapper);

        return update;
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        SysUser sysUser = this.getByUsername(username);

        //根据用户id获取菜单权限值
        List<RouterVo> routerVoList = sysMenuService.findUserMenuList(sysUser.getId());
        //根据用户id获取用户按钮权限
        List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());

        result.put("name", sysUser.getName());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //当前权限控制使用不到，我们暂时忽略
        result.put("roles",  new HashSet<>());
        result.put("buttons", permsList);
        result.put("routers", routerVoList);
        return result;
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        HashMap<String, Object> map = new HashMap<>();
        SysUser user = baseMapper.selectById( LoginUserInfoHelper.getUserId());
        map.put("name", user.getUsername());
        map.put("phone" ,user.getPhone());
        return map;
    }
}
