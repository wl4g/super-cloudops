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
package com.wl4g.devops.iam.captcha.verification;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.wl4g.devops.iam.captcha.gif.Captcha;
import com.wl4g.devops.iam.captcha.gif.GifCaptcha;
import com.wl4g.devops.iam.captcha.gif.Randoms;
import com.wl4g.devops.iam.handler.verification.GraphBasedVerification;

/**
 * Gap slider CAPTCHA verification handler.
 * 
 * @author Wangl.sir
 * @version v1.0 2019年8月28日
 * @since
 */
public class GapSliderVerification extends GraphBasedVerification {

	@Override
	protected String generateCode() {
		StringBuffer alpha = new StringBuffer();
		for (int i = 0; i < 5; i++) {
			alpha.append(Randoms.alpha());
		}
		return alpha.toString();
	}

	@Override
	protected void write(HttpServletResponse response, String verifyCode) throws IOException {
		Captcha captcha = new GifCaptcha(verifyCode);
		captcha.out(response.getOutputStream());
	}

}