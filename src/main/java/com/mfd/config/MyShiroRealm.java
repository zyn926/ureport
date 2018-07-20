package com.mfd.config;

import com.mfd.dto.auUserInfo.AuUserInfo;
import com.mfd.dto.permission.Permission;
import com.mfd.dto.roleInfo.RoleInfo;
import com.mfd.service.auUserInfoService.AuUserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private AuUserInfoService userInfoService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        AuUserInfo userInfo  = (AuUserInfo)principals.getPrimaryPrincipal();
        /*for(RoleInfo role:userInfo.getRoleList()){
            authorizationInfo.addRole(role.getRoleName());
            for(Permission p:role.getPermissons()){
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }*/
        return authorizationInfo;

    }
    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        System.out.println("----->>username="+username);
        AuUserInfo userInfo = userInfoService.findByUsername(username);
        System.out.println("----->>userInfo="+userInfo);
        if(userInfo == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                userInfo.getPassword(), //密码
                this.getClass().getName()  //realm name
        );
        return authenticationInfo;
    }
}