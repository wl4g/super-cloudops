/*
 * Copyright 2017 ~ 2050 the original author or authors <Wanglsir@gmail.com, 983708408@qq.com>.
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
package com.wl4g.dopaas.lcdp.codegen.engine.generator;

import static com.wl4g.dopaas.common.constant.LcdpConstants.ModelAttributeConstants.GEN_COMMON_VUESPECS;

import javax.validation.constraints.NotNull;

import com.wl4g.dopaas.common.constant.LcdpConstants.GenProviderAlias;
import com.wl4g.dopaas.lcdp.codegen.engine.context.GenerateContext;
import com.wl4g.dopaas.lcdp.codegen.engine.generator.render.RenderModel;
import com.wl4g.dopaas.lcdp.codegen.engine.specs.VueJSSpecs;
import com.wl4g.dopaas.lcdp.codegen.engine.template.GenTemplateResource;

/**
 * {@link IamVueGeneratorProvider}
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020-09-07
 * @since
 */
public class IamVueGeneratorProvider extends BasedWebGeneratorProvider {

	public IamVueGeneratorProvider(@NotNull GenerateContext context) {
		super(context, null);
	}

	@Override
	public void doGenerate() throws Exception {
		doGenerateWithTemplates(GenProviderAlias.IAM_VUEJS);
	}

	@Override
	protected void customizeRenderingModel(@NotNull GenTemplateResource resource, @NotNull RenderModel model) {
		super.customizeRenderingModel(resource, model);

		// Add variable of naming utils.
		model.put(GEN_COMMON_VUESPECS, new VueJSSpecs());
	}

}