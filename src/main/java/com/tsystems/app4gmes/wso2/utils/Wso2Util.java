package com.tsystems.app4gmes.wso2.utils;

import java.io.InputStream;

import javax.security.auth.login.AccountException;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;

public class Wso2Util {


	public static boolean authenticate(String username, String password) throws AccountException{
		if (username != null && username.trim().length() > 0) {
	        try {
	            CarbonContext context = CarbonContext.getThreadLocalCarbonContext();
	            UserRealm realm = context.getUserRealm();
	            return realm.getUserStoreManager().authenticate(username, password);
	        } catch (UserStoreException e) {
	           throw new AccountException(e.getMessage());
	        }
	    }
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getResourceValue(String path, Class<T> type) throws com.tsystems.app4gmes.wso2.utils.RegistryException{
		Resource resource = getResource(path);
		if (resource!=null)
			try {
				return (T)resource.getContent();
			} catch (RegistryException e) {
				throw new com.tsystems.app4gmes.wso2.utils.RegistryException(e);
			}
		else
			return null;
		
	}
	
	public static InputStream getResourceAsStream(String path) throws com.tsystems.app4gmes.wso2.utils.RegistryException{
		Resource resource = getResource(path);
		if (resource!=null)
			try {
				return resource.getContentStream();
			} catch (RegistryException e) {
				throw new com.tsystems.app4gmes.wso2.utils.RegistryException(e);
			}
		else
			return null;
		
	}
	
	private static <T> Resource getResource(String path) throws com.tsystems.app4gmes.wso2.utils.RegistryException{
		CarbonContext cCtx = CarbonContext.getThreadLocalCarbonContext();
		Registry registry = cCtx.getRegistry(RegistryType.LOCAL_REPOSITORY);
		try {
			return registry.get(path);
			
		} catch (RegistryException e) {
			throw new com.tsystems.app4gmes.wso2.utils.RegistryException(e);
		}
	}
	
	public static void updateResourceAsStream(String path, InputStream value) throws com.tsystems.app4gmes.wso2.utils.RegistryException{
		Resource resource = getResource(path);
		try {
			
			if (resource!=null)
				resource.setContentStream(value);
			
		} catch (RegistryException e) {
			throw new com.tsystems.app4gmes.wso2.utils.RegistryException(e);
		}
	}
	
	public static void updateResource(String path, Object value) throws com.tsystems.app4gmes.wso2.utils.RegistryException{
		Resource resource = getResource(path);
		try {
			
			if (resource!=null)
				resource.setContent(value);
			
		} catch (RegistryException e) {
			throw new com.tsystems.app4gmes.wso2.utils.RegistryException(e);
		}
	}
	
	public static void addUser(String username, String password) throws AccountException{
		 if (username != null && username.trim().length() > 0) {
		        CarbonContext context = CarbonContext.getThreadLocalCarbonContext();
		        UserRealm realm = context.getUserRealm();
	        try {
				if (!realm.getUserStoreManager().isExistingUser(username)) {
				    realm.getUserStoreManager().addUser(username, password, null, null, null);
				} else {
					throw new UserStoreException("User already exist");
				}
			} catch (UserStoreException e) {
				throw new AccountException(e.getMessage());
			}

		 }
	}
	public static String[] getAllUser() throws AccountException{
		return getAllUser(100);
	}
	
	public static String[] getAllUser(int size) throws AccountException{
		CarbonContext context = CarbonContext.getThreadLocalCarbonContext();
	    UserRealm realm = context.getUserRealm();
	    try {
	    	return realm.getUserStoreManager().listUsers("*", size);
		} catch (UserStoreException e) {
			throw new AccountException(e.getMessage());
		}
	}
}
