package com.ramostear.unaboot.config.support;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.common.State;
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
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 17:35.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class UnaBootRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link org.apache.shiro.authz.SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see org.apache.shiro.authz.SimpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        assert user != null;
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(user.getRole());
        return info;
    }

    /**
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     *         authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String)token.getPrincipal();
        Optional<User> optional = userService.findByPrincipal(principal);
        if(optional.isPresent()){
            User user = optional.get();
            if(user.getState() == State.CLOSE.getCode()){
                throw new LockedAccountException("Account has been locked by the admin.");
            }
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    user,user.getPassword(), ByteSource.Util.bytes(user.getUsername()),getName());
            SecurityUtils.getSubject().getSession().setAttribute(Constants.LOGIN_SESSION_KEY,user);
            return authenticationInfo;
        }else{
            throw new UnknownAccountException("Unknown account or the account is not available.");
        }
    }
    /**
     * Sets the CredentialsMatcher used during an authentication attempt to verify submitted credentials with those
     * stored in the system.  The implementation of this matcher can be switched via configuration to
     * support any number of schemes, including plain text comparisons, hashing comparisons, and others.
     * <p/>
     * <p>Unless overridden by this method, the default value is a
     * {@link org.apache.shiro.authc.credential.SimpleCredentialsMatcher} instance.
     *
     * @param credentialsMatcher the matcher to use.
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(Constants.ALGORITHM_NAME);
        matcher.setHashIterations(Constants.HASH_ITERATIONS);
        super.setCredentialsMatcher(credentialsMatcher);
    }
}
