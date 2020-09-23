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
package com.wl4g.devops.dts.codegen.utils;

import static com.wl4g.components.common.lang.Assert2.notNullOf;
import static com.wl4g.components.common.lang.StringUtils2.isTrue;
import static com.wl4g.components.common.reflect.ReflectionUtils2.doFullWithFields;
import static com.wl4g.components.common.reflect.ReflectionUtils2.getField;
import static com.wl4g.components.common.reflect.ReflectionUtils2.isGenericModifier;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.wl4g.components.common.reflect.TypeUtils2;
import com.wl4g.components.common.reflect.ReflectionUtils2.FieldFilter;

/**
 * {@link RenderPropertyUtils}
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version 2020-09-23
 * @sine v1.0.0
 * @see
 */
public abstract class RenderPropertyUtils {

	/**
	 * Converting object to flat map model
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static final Map<String, Object> convertToRenderingModel(final @NotNull Object bean) throws Exception {
		notNullOf(bean, "bean");

		// Resolve type annotation
		RenderProperty rp1 = findAnnotation(bean.getClass(), RenderProperty.class);
		final List<String> includes = isNull(rp1) ? emptyList() : asList(rp1.includeFieldNames());

		final Map<String, Object> model = new HashMap<>();
		doFullWithFields(bean, new FieldFilter() {
			@Override
			public boolean matches(Field field) {
				return isGenericModifier(field.getModifiers()) || includes.contains(field.getName());
			}

			@Override
			public boolean describeForObjField(Field field) {
				RenderProperty rp = field.getDeclaredAnnotation(RenderProperty.class);
				return nonNull(rp) && isTrue(rp.describeForObjField());
			}
		}, (field, objOfField) -> {
			if (Objects.isNull(objOfField)) {
				objOfField = TypeUtils2.instantiate(null, field.getType());
			}
			// Resolve field annotation
			RenderProperty rp2 = field.getDeclaredAnnotation(RenderProperty.class);
			if (nonNull(rp2)) {
				Object modelAttrVal = getField(field, objOfField);
				// Note: Combined with FreeMarker script, no value is saved when
				// there is no available value.
				if (!isNull(modelAttrVal) || (modelAttrVal instanceof String && !isBlank((String) modelAttrVal))) {
					model.put(field.getName(), modelAttrVal);
				}
			}
		});

		return model;
	}

	/**
	 * Whether the property fields of the annotation system bean will be
	 * serialized to the rendering model.
	 * 
	 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
	 * @version 2020-09-20
	 * @sine v1.0.0
	 * @see
	 */
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.TYPE })
	public static @interface RenderProperty {

		/**
		 * A list of field names that will be serialized as a rendered data
		 * model, whether it is the current class or a superclass. Note: this
		 * value is combined with the field of the compiled annotation (take its
		 * union).
		 * 
		 * @return
		 */
		String[] includeFieldNames() default {};

		/**
		 * It is used to control whether to continue to reflect the structure of
		 * the field recursively if the field traversed by reflection is of
		 * object type.
		 * 
		 * @return
		 */
		String describeForObjField() default "Yes";

	}

}