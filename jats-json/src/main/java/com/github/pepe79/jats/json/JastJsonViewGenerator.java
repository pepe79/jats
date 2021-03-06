package com.github.pepe79.jats.json;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.module.SimpleModule;

import com.github.pepe79.jats.json.idextractor.IdExtractor;

public class JastJsonViewGenerator {
	private Map<String, Map<Class<?>, Set<String>>> views;

	private IdExtractor idExtractor;

	public JastJsonViewGenerator() {
		this.views = new HashMap<String, Map<Class<?>, Set<String>>>();
	}

	public JastJsonViewGenerator(IdExtractor idExtractor,
			Map<String, Map<Class<?>, Set<String>>> views) {
		this.idExtractor = idExtractor;
		this.views = views;
	}

	@SuppressWarnings("deprecation")
	public void toJson(Writer writer, Object obj, String viewId,
			final boolean pretty) {
		ObjectMapper mapper = new ObjectMapper();

		// using deprecated methods because their new variants do not work...
		mapper.getSerializationConfig().disable(Feature.FAIL_ON_EMPTY_BEANS);
		mapper.getSerializationConfig().setSerializationInclusion(
				Inclusion.NON_NULL);

		final Map<Class<?>, Set<String>> viewProperties = (views == null) ? null : views
				.get(viewId);

		SimpleModule testModule = new SimpleModule("JastModule", new Version(1,
				0, 0, null)) {
			@Override
			public void setupModule(SetupContext context) {
				super.setupModule(context);
				JatsBeanSerializerModifier modifier = new JatsBeanSerializerModifier(
						idExtractor, viewProperties);
				modifier.setPretty(pretty);
				context.addBeanSerializerModifier(modifier);
			}
		};

		mapper.registerModule(testModule);

		try {
			mapper.writeValue(writer, obj);
		} catch (Exception e) {
			throw new RuntimeException("error", e);
		}
	}

}
