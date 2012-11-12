package com.github.pepe79.jats.json;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;

import com.github.pepe79.jats.json.idextractor.IdExtractor;

public class JatsBeanSerializerModifier extends BeanSerializerModifier
{

	private Set<String> propertyWhiteList;

	private IdExtractor idExtractor;

	public JatsBeanSerializerModifier(IdExtractor idExtractor,
			Set<String> propertyWhiteList)
	{
		this.propertyWhiteList = propertyWhiteList;
		this.idExtractor = idExtractor;
	}

	@Override
	public List<BeanPropertyWriter> changeProperties(
			SerializationConfig config, BasicBeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties)
	{
		{
			Iterator<BeanPropertyWriter> it = beanProperties.iterator();
			while (it.hasNext())
			{
				BeanPropertyWriter bpw = it.next();

				if (propertyWhiteList != null
						&& !propertyWhiteList.contains(bpw.getName()))
				{
					it.remove();
				}
			}
		}

		return beanProperties;
	}

	@Override
	public JsonSerializer<?> modifySerializer(SerializationConfig config,
			BasicBeanDescription beanDesc, JsonSerializer<?> serializer)
	{
		return new JatsJsonSerializer<>(serializer, idExtractor);
	}

}
