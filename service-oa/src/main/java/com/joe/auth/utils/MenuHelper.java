package com.joe.auth.utils;

import com.joe.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> list){
        List<SysMenu> sysMenu = new ArrayList<>();
        for (SysMenu menu : list) {
            if(menu.getParentId() == 0){
                sysMenu.add(getChildren(menu, list));
            }
        }
        return sysMenu;
    }
    public static SysMenu getChildren(SysMenu sysMenu , List<SysMenu> SysMenuList){
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu menu : SysMenuList) {
           if(sysMenu.getId().longValue() == menu.getParentId().longValue())
           {
               if(sysMenu.getChildren() == null){
               sysMenu.setChildren(new ArrayList<>());
           }
               sysMenu.getChildren().add(getChildren(menu ,SysMenuList));
           }
        }
        return sysMenu;
    }
}
