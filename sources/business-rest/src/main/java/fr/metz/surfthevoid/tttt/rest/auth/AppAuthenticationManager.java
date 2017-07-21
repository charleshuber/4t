package fr.metz.surfthevoid.tttt.rest.auth;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import fr.metz.surfthevoid.tttt.rest.db.entity.UserDbo;
import fr.metz.surfthevoid.tttt.rest.db.repo.UserDao;

@Service("appAuthenticationManager")
public class AppAuthenticationManager implements AuthenticationManager {
	
	Log logger = LogFactory.getLog(AppAuthenticationManager.class);
	
	@Inject
	protected UserDao userDao;
	
	@Value("${REALM_NAME}")
	protected String realm;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String pwd = (String)authentication.getCredentials();
		UserDetails userDetail = loadUserByUsername(authentication.getName());
		String phrase = name + ":" + realm + ":" + pwd;
		String digest = DigestUtils.md5DigestAsHex(phrase.getBytes());
		if(!digest.equals(userDetail.getPassword())){
			throw new BadCredentialsException("Incorrect password for user " + name);
		}
		return new UsernamePasswordAuthenticationToken(name, null, userDetail.getAuthorities());
	}
	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try{
			UserDbo userDbo = userDao.readByMail(email);
			if(userDbo == null) throw new IllegalArgumentException();
			return extractUserDetails(userDbo);
		} catch(Exception e){
			logger.error("The user with mail " + email +" could not be retrieved", e);
		}
		throw new UsernameNotFoundException("The user could not be retrived");
	}

	private UserDetails extractUserDetails(UserDbo userDbo) {
		String mail = userDbo.getEmail();
		String clearPwd = userDbo.getPassword();
		Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
		
		return new User(mail, clearPwd, authorities);
	}

}
