package com.ramostear.unaboot.configuration.support;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * @ClassName UnaBootAuthrizingRealm
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/28 0028 6:56
 * @Version since UnaBoot-1.0
 **/
public class UnaBootAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        authorizationInfo.addRole(user.getRole());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        Optional<User> optional = userService.findByCertificate(principal);
        if(!optional.isPresent()){
            throw new AccountException("账户不存在");
        }
        User user = optional.get();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(user.getUsername()),getName());
        SecurityUtils.getSubject().getSession().setAttribute("profile",user);
        return authenticationInfo;
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(UnaBootConst.ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(UnaBootConst.HASH_ITERATIONS);
        super.setCredentialsMatcher(credentialsMatcher);
    }
}
