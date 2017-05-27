package com.xiyi.weixin.util;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.Comparators;
import com.fasterxml.jackson.databind.util.NameTransformer;


public class JsonUtils {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final ObjectMapper defaultObjectMapper = new ObjectMapper();

	private static final ObjectMapper customObjectMapper = new ObjectMapper();

	static {
		defaultObjectMapper.configure(
				SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		defaultObjectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		defaultObjectMapper.setDateFormat(new SimpleDateFormat(
				DEFAULT_DATE_FORMAT));

		customObjectMapper.configure(
				SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		customObjectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		customObjectMapper.setDateFormat(new SimpleDateFormat(
				DEFAULT_DATE_FORMAT));
		customObjectMapper
				.setSerializerFactory(CustomBeanSerializerFactory.instance);
	}



	public static <T> String objectToJson(T object) throws Exception {
		return defaultObjectMapper.writeValueAsString(object);
	}

	public static <T> String objectToJson(T object,
			boolean nullStringToEmptyString) throws Exception {
		if (nullStringToEmptyString) {
			return customObjectMapper.writeValueAsString(object);
		}
		return defaultObjectMapper.writeValueAsString(object);
	}


	public static <T> T jsonToObject(String json, Class<T> clazz)
			throws Exception {
		return defaultObjectMapper.readValue(json, clazz);
	}

	public static <T> T jsonToObject(String json, TypeReference<T> typeReference)
			throws Exception {
		return defaultObjectMapper.readValue(json, typeReference);
	}

	public static ObjectMapper getDefaultObjectMapper() {
		return defaultObjectMapper;
	}

	public static ObjectMapper getCustomObjectMapper() {
		return customObjectMapper;
	}
}

/**
 * overrides, to fixing null String to "" String
 */
class CustomBeanSerializerFactory extends BeanSerializerFactory {

	public final static BeanSerializerFactory instance = new CustomBeanSerializerFactory(
			null);

	protected CustomBeanSerializerFactory(SerializerFactoryConfig config) {
		super(config);
	}

	protected PropertyBuilder constructPropertyBuilder(
			SerializationConfig config, BeanDescription beanDesc) {
		return new CustomPropertyBuilder(config, beanDesc);
	}

}

/**
 * overrides, to fixing null String to "" String
 */
class CustomBeanPropertyWriter extends BeanPropertyWriter {

	public CustomBeanPropertyWriter(BeanPropertyDefinition propDef,
			AnnotatedMember member, Annotations contextAnnotations,
			JavaType declaredType, JsonSerializer<?> ser,
			TypeSerializer typeSer, JavaType serType, boolean suppressNulls,
			Object suppressableValue) {
		super(propDef, member, contextAnnotations, declaredType, ser, typeSer,
				serType, suppressNulls, suppressableValue);
	}

	public void serializeAsField(Object bean, JsonGenerator jgen,
			SerializerProvider prov) throws Exception {
		Object value = get(bean);
		// Null handling is bit different, check that first
		if (value == null) {
			// null String to "" String
			if (_declaredType.getRawClass() == String.class) {
				jgen.writeFieldName(_name);
				_serializer.serialize("", jgen, prov);
			} else {
				if (_nullSerializer != null) {
					jgen.writeFieldName(_name);
					_nullSerializer.serialize(null, jgen, prov);
				}
			}
			return;
		}
		// then find serializer to use
		JsonSerializer<Object> ser = _serializer;
		if (ser == null) {
			Class<?> cls = value.getClass();
			PropertySerializerMap map = _dynamicSerializers;
			ser = map.serializerFor(cls);
			if (ser == null) {
				ser = _findAndAddDynamic(map, cls, prov);
			}
		}
		// and then see if we must suppress certain values (default, empty)
		if (_suppressableValue != null) {
			if (MARKER_FOR_EMPTY == _suppressableValue) {
				if (ser.isEmpty(value)) {
					return;
				}
			} else if (_suppressableValue.equals(value)) {
				return;
			}
		}
		// For non-nulls: Custom check for direct cycles
		if (value == bean) {
			_handleSelfReference(bean, ser);
		}
		jgen.writeFieldName(_name);
		if (_typeSerializer == null) {
			ser.serialize(value, jgen, prov);
		} else {
			ser.serializeWithType(value, jgen, prov, _typeSerializer);
		}
	}

}

/**
 * overrides, to fixing null String to "" String
 */
class CustomPropertyBuilder extends PropertyBuilder {

	public CustomPropertyBuilder(SerializationConfig config,
			BeanDescription beanDesc) {
		super(config, beanDesc);
	}

	protected BeanPropertyWriter buildWriter(BeanPropertyDefinition propDef,
			JavaType declaredType, JsonSerializer<?> ser,
			TypeSerializer typeSer, TypeSerializer contentTypeSer,
			AnnotatedMember am, boolean defaultUseStaticTyping) {
		// do we have annotation that forces type to use (to declared type or
		// its super type)?
		JavaType serializationType = findSerializationType(am,
				defaultUseStaticTyping, declaredType);

		// Container types can have separate type serializers for content (value
		// / element) type
		if (contentTypeSer != null) {
			/*
			 * 04-Feb-2010, tatu: Let's force static typing for collection, if
			 * there is type information for contents. Should work well (for
			 * JAXB case); can be revisited if this causes problems.
			 */
			if (serializationType == null) {
				// serializationType = TypeFactory.type(am.getGenericType(),
				// _beanDesc.getType());
				serializationType = declaredType;
			}
			JavaType ct = serializationType.getContentType();
			/*
			 * 03-Sep-2010, tatu: This is somehow related to [JACKSON-356], but
			 * I don't completely yet understand how pieces fit together. Still,
			 * better be explicit than rely on NPE to indicate an issue...
			 */
			if (ct == null) {
				throw new IllegalStateException(
						"Problem trying to create BeanPropertyWriter for property '"
								+ propDef.getName() + "' (of type "
								+ _beanDesc.getType()
								+ "); serialization type " + serializationType
								+ " has no content");
			}
			serializationType = serializationType
					.withContentTypeHandler(contentTypeSer);
			ct = serializationType.getContentType();
		}

		Object valueToSuppress = null;
		boolean suppressNulls = false;

		JsonInclude.Include methodProps = _annotationIntrospector
				.findSerializationInclusion(am, _outputProps);
		if (methodProps != null) {
			switch (methodProps) {
			case NON_DEFAULT:
				valueToSuppress = getDefaultValue(propDef.getName(), am);
				if (valueToSuppress == null) {
					suppressNulls = true;
				} else {
					// [JACKSON-531]: Allow comparison of arrays too...
					if (valueToSuppress.getClass().isArray()) {
						valueToSuppress = Comparators
								.getArrayComparator(valueToSuppress);
					}
				}
				break;
			case NON_EMPTY:
				// always suppress nulls
				suppressNulls = true;
				// but possibly also 'empty' values:
				valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
				break;
			case NON_NULL:
				suppressNulls = true;
				// fall through
			case ALWAYS: // default
				// we may still want to suppress empty collections, as per
				// [JACKSON-254]:
				if (declaredType.isContainerType()
						&& !_config
								.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
					valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
				}
				break;
			}
		}

		BeanPropertyWriter bpw = new CustomBeanPropertyWriter(propDef, am,
				_beanDesc.getClassAnnotations(), declaredType, ser, typeSer,
				serializationType, suppressNulls, valueToSuppress);

		// [JACKSON-132]: Unwrapping
		NameTransformer unwrapper = _annotationIntrospector
				.findUnwrappingNameTransformer(am);
		if (unwrapper != null) {
			bpw = bpw.unwrappingWriter(unwrapper);
		}
		return bpw;
	}

}