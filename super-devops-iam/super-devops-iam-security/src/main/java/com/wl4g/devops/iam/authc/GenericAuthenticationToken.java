/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wl4g.devops.iam.authc;

import static com.wl4g.devops.tool.common.lang.Assert2.*;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.RememberMeAuthenticationToken;

import com.wl4g.devops.iam.common.authc.AbstractIamAuthenticationToken;
import com.wl4g.devops.iam.common.authc.ClientRef;
import com.wl4g.devops.iam.verification.SecurityVerifier.VerifyKind;

/**
 * General (Username/Password) authentication token
 *
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0
 * @date 2018年11月19日
 * @since
 */
public class GenericAuthenticationToken extends AbstractIamAuthenticationToken
		implements RememberMeAuthenticationToken, VerifyAuthenticationToken {
	private static final long serialVersionUID = 8587329689973009598L;

	/**
	 * The username principal
	 */
	final private String principal;

	/**
	 * The password credentials
	 */
	final private String credentials;

	/**
	 * The signature of nonce passed from the client, where nonce is generated
	 * when the client calls the check interface during authentication.
	 */
	final private String signature;

	/**
	 * Whether or not 'rememberMe' should be enabled for the corresponding login
	 * attempt; default is <code>false</code>
	 */
	final private boolean rememberMe;

	/**
	 * User client type.
	 */
	final private ClientRef clientRef;

	/**
	 * Verification code verifiedToken.
	 */
	final private String verifiedToken;

	/**
	 * Verifier type.
	 */
	final private VerifyKind verifyType;

	/**
	 * Client user other attributes properties. (e.g.
	 * _csrf_token=xxx&lang=zh_CN&umid=xxx&ua=xxx)
	 * 
	 * @see {@link com.wl4g.devops.iam.config.properties.ServerParamProperties#riskControlParam}
	 */
	private Map<String, String> userProperties = new HashMap<>();

	public GenericAuthenticationToken(final String remoteHost, final RedirectInfo redirectInfo, final String principal,
			final String credentials, final String signature, final String clientRef, final String verifiedToken,
			final VerifyKind verifyType) {
		this(remoteHost, redirectInfo, principal, credentials, signature, clientRef, verifiedToken, verifyType, false);
	}

	public GenericAuthenticationToken(final String remoteHost, final RedirectInfo redirectInfo, final String principal,
			final String credentials, final String signature, final String clientRef, final String verifiedToken,
			final VerifyKind verifyType, final boolean rememberMe) {
		super(remoteHost, redirectInfo);
		hasTextOf(principal, "principal");
		hasTextOf(credentials, "credentials");
		hasTextOf(signature, "signature");
		hasTextOf(clientRef, "clientRef");
		// hasTextOf(verifiedToken, "verifiedToken");
		notNullOf(verifyType, "verifyType");
		this.principal = principal;
		this.credentials = credentials;
		this.signature = signature;
		this.clientRef = ClientRef.of(clientRef);
		this.verifiedToken = verifiedToken;
		this.verifyType = verifyType;
		this.rememberMe = rememberMe;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	public String getSignature() {
		return signature;
	}

	@Override
	public boolean isRememberMe() {
		return rememberMe;
	}

	public ClientRef getClientRef() {
		return clientRef;
	}

	@Override
	public String getVerifiedToken() {
		return verifiedToken;
	}

	@Override
	public VerifyKind getVerifyType() {
		return verifyType;
	}

	public Map<String, String> getUserProperties() {
		return userProperties;
	}

	public GenericAuthenticationToken setUserProperties(Map<String, String> userAttributes) {
		if (!isEmpty(userAttributes)) {
			this.userProperties.putAll(userAttributes);
		}
		return this;
	}

	@Override
	public String toString() {
		return "GenericAuthenticationToken [principal=" + principal + ", credentials=" + credentials + ", rememberMe="
				+ rememberMe + ", clientRef=" + clientRef + ", verifiedToken=" + verifiedToken + ", verifyType=" + verifyType
				+ ", userAttributes=" + userProperties + "]";
	}

}