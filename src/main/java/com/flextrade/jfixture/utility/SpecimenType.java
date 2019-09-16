package com.flextrade.jfixture.utility;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import com.flextrade.jfixture.utility.GenericType.GenericTypeCreator;
import com.flextrade.jfixture.utility.GenericType.GenericTypeCreatorImpl;
import com.flextrade.jfixture.utility.GenericType.GenericTypeCreatorWithGenericContextImpl;

public abstract class SpecimenType<T> implements Type {

    private final Class rawType;
    private final GenericTypeCollection genericTypeArguments;

    protected SpecimenType() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            throw new IllegalArgumentException("No generic type argument provided");
        }

        ParameterizedType pt = (ParameterizedType) genericSuperclass;
        SpecimenTypeFields fields = getFields(pt.getActualTypeArguments()[0]);
        this.rawType = fields.rawType;
        this.genericTypeArguments = fields.genericTypeArguments;
    }

    private SpecimenType(Type type) {
        SpecimenTypeFields fields = getFields(type);
        this.rawType = fields.rawType;
        this.genericTypeArguments = fields.genericTypeArguments;
    }

    private SpecimenType(Type type, SpecimenType contextualType) {
        SpecimenType st = convertPossibleGenericTypeToSpecimenType(type, contextualType);
        this.rawType = st.rawType;
        this.genericTypeArguments = st.genericTypeArguments;
    }

    private SpecimenType(Class rawType, GenericTypeCollection genericTypeArguments) {
        this.rawType = rawType;
        this.genericTypeArguments = genericTypeArguments;
    }

    public static SpecimenType<?> of(Type type) {
        return new SpecimenType<Object>(type) {
        };
    }

    private static SpecimenType<?> of(Type type, GenericTypeCollection genericTypeArguments) {
        return new SpecimenType<Object>(type.getClass(), genericTypeArguments) {
        };
    }

    public static <T> SpecimenType<T> of(Class<T> clazz) {
        return new SpecimenType<T>(clazz) {
        };
    }

    static SpecimenType<?> withGenericContext(Type type, SpecimenType contextualType) {
        return new SpecimenType(type, contextualType) {
        };
    }

    public final Class getRawType() {
        return this.rawType;
    }

    public final GenericTypeCollection getGenericTypeArguments() {
        return this.genericTypeArguments;
    }

    private static SpecimenType convertPossibleGenericTypeToSpecimenType(Type originalType, SpecimenType contextualType) {
        if (originalType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) originalType;
            List<GenericType> genericTypesForSpecimen = getGenericTypes(parameterizedType, contextualType);

            Class rawType = (Class) parameterizedType.getRawType();
            GenericTypeCollection genericTypeCollection = new GenericTypeCollection(genericTypesForSpecimen.toArray(new GenericType[genericTypesForSpecimen.size()]));
            return new SpecimenType(rawType, genericTypeCollection) {
            };
        }

        if (originalType instanceof TypeVariable) { // e.g. <T>
            SpecimenType type = contextualType.getGenericTypeArguments().getType(originalType.toString());
            if (type == null) {
                return SpecimenType.of(TypeVariable.class);
            }
            return type;
        }

        return SpecimenType.of(originalType);
    }

    private static List<GenericType> getGenericTypes(ParameterizedType parameterizedType, SpecimenType contextualType) {
        List<SpecimenType> resolvedGenericTypes = resolveGenericArguments(parameterizedType, contextualType);
        List<GenericType> genericTypesForSpecimen = new ArrayList<GenericType>();
        for (SpecimenType resolvedGenericType : resolvedGenericTypes) {
            String typeName = contextualType.genericTypeArguments.getNameFromType(resolvedGenericType);
            GenericType gt = new GenericType(resolvedGenericType, typeName);
            genericTypesForSpecimen.add(gt);
        }
        return genericTypesForSpecimen;
    }

    private static List<SpecimenType> resolveGenericArguments(ParameterizedType parameterizedType, SpecimenType contextualType) {
        List<SpecimenType> resolvedGenericTypes = new ArrayList<SpecimenType>();

        Type[] genericTypes = parameterizedType.getActualTypeArguments();
        for (Type genericType : genericTypes) {
            SpecimenType resolved = convertPossibleGenericTypeToSpecimenType(genericType, contextualType);
            resolvedGenericTypes.add(resolved);
        }
        return resolvedGenericTypes;
    }

    private static SpecimenTypeFields getFields(Type type) {
        if (type instanceof SpecimenType) return getSpecimenTypeFields((SpecimenType) type);
        if (type instanceof Class) return getFieldsForClassType((Class) type);
        if (type instanceof ParameterizedType) return getParameterizedTypeFields((ParameterizedType) type);
        if (type instanceof GenericArrayType) return getGenericArrayFields((GenericArrayType) type);
        if (type instanceof TypeVariable) {
            // no type information for this type variable
            SpecimenTypeFields fields = new SpecimenTypeFields();
            fields.rawType = type.getClass();
            fields.genericTypeArguments = GenericTypeCollection.empty();
            return fields;
        } else if (type instanceof WildcardType) {
            if (((WildcardType) type).getUpperBounds().length == 1 && ((WildcardType) type).getLowerBounds().length == 0) {

                // no type information for this type variable
                SpecimenTypeFields fields = new SpecimenTypeFields();
                fields.rawType = (Class) ((WildcardType) type).getUpperBounds()[0];
                fields.genericTypeArguments = GenericTypeCollection.empty();

                return fields;
            } else {
                throw new UnsupportedOperationException("Wildcard types not supported");
            }
        }

        throw new UnsupportedOperationException(String.format("Unknown Type : %s", type.getClass()));
    }

    private static SpecimenTypeFields getFieldsForClassType(Class classType) {
        SpecimenTypeFields fieldsFromThisType = getClassTypeFields(classType);
        Type genericSuperclass = classType.getGenericSuperclass();
        // Enums self reference themselves in the type declaration, for an enum SomeEnum compiled class declaration becomes:
        //     public final class SomeEnum extends java.lang.Enum<SomeEnum>
        // As we handle fixturing Enums separately we can ignore them here
        if (genericSuperclass != null && !classType.isEnum()) {
            GenericTypeCollection superTypeGenericArguments = getFields(genericSuperclass).genericTypeArguments;
            fieldsFromThisType.genericTypeArguments = fieldsFromThisType.genericTypeArguments.combineWith(superTypeGenericArguments);
        }
        return fieldsFromThisType;
    }

    private static GenericTypeCollection getGenericTypeMappings(ParameterizedType parameterizedType, GenericTypeCreator genericTypeCreator) {
        Class<?> rawType = (Class) parameterizedType.getRawType();

        Type[] genericArguments = parameterizedType.getActualTypeArguments();
        TypeVariable[] typeParameters = rawType.getTypeParameters();
        List<GenericType> genericTypes = new ArrayList<GenericType>();
        for (int i = 0; i < genericArguments.length; i++) {
            Type type = genericArguments[i];
            GenericType genericType = genericTypeCreator.createGenericType(type, typeParameters[i].getName());
            if (!(TypeVariable.class.isAssignableFrom(genericType.getType().getRawType()))) { // ignore type parameters which haven't been substituted e.g. T, S, U etc
                genericTypes.add(genericType);
            }
        }

        return new GenericTypeCollection(genericTypes.toArray(new GenericType[genericTypes.size()]));
    }

    private static GenericTypeCollection getGenericTypeMapForGenericSuperclass(ParameterizedType parameterizedType, GenericTypeCollection genericTypeCollection) {
        Class<?> rawType = (Class) parameterizedType.getRawType();
        Type genericSuperclass = rawType.getGenericSuperclass();
        if (genericSuperclass != null) {
            if (genericSuperclass instanceof ParameterizedType) {
                // super class is also a generic type with the type argument possibly being passed through from it's sub class
                SpecimenType context = SpecimenType.of(rawType, genericTypeCollection);
                return genericTypeCollection.combineWith(createGenericTypeNameMap((ParameterizedType) genericSuperclass, context));
            } else if (genericSuperclass instanceof Class) {
                // super class is a class, so no type arguments are passed down but we need to get the type mappings
                // that the class might have (as that class may be inheriting from a generic type itself)
                return genericTypeCollection.combineWith(getFields(genericSuperclass).genericTypeArguments);
            }
            // return the current generic type map
        }
        return genericTypeCollection;
    }

    private static GenericTypeCollection createGenericTypeNameMap(ParameterizedType parameterizedType, SpecimenType contextType) {
        GenericTypeCollection genericTypeCollection = getGenericTypeMappings(
                parameterizedType,
                new GenericTypeCreatorWithGenericContextImpl(contextType)
        );

        return getGenericTypeMapForGenericSuperclass(parameterizedType, genericTypeCollection);
    }

    private static GenericTypeCollection createGenericTypeNameMap(ParameterizedType parameterizedType) {
        GenericTypeCollection genericTypeCollection = getGenericTypeMappings(
                parameterizedType,
                new GenericTypeCreatorImpl()
        );

        return getGenericTypeMapForGenericSuperclass(parameterizedType, genericTypeCollection);
    }

    private static SpecimenTypeFields getGenericArrayFields(GenericArrayType type) {
        SpecimenTypeFields fields = new SpecimenTypeFields();
        Type componentType = type.getGenericComponentType();
        fields.rawType = Array.newInstance(SpecimenType.of(componentType).getRawType(), 0).getClass();
        fields.genericTypeArguments = GenericTypeCollection.empty();
        return fields;
    }

    private static SpecimenTypeFields getParameterizedTypeFields(ParameterizedType type) {
        SpecimenTypeFields fields = new SpecimenTypeFields();
        fields.rawType = nonPrimitiveType(type.getRawType());
        fields.genericTypeArguments = createGenericTypeNameMap(type);
        return fields;
    }

    private static SpecimenTypeFields getClassTypeFields(Type type) {
        SpecimenTypeFields fields = new SpecimenTypeFields();
        fields.rawType = nonPrimitiveType(type);
        fields.genericTypeArguments = GenericTypeCollection.empty();
        return fields;
    }

    private static SpecimenTypeFields getSpecimenTypeFields(SpecimenType type) {
        SpecimenTypeFields fields = new SpecimenTypeFields();
        fields.rawType = type.rawType;
        fields.genericTypeArguments = type.genericTypeArguments;
        return fields;
    }

    @Override
    public final int hashCode() {
        return this.rawType.hashCode() ^ this.genericTypeArguments.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof Type)) return false;

        SpecimenType other;
        if (obj instanceof Class<?>) {
            Class<?> clazz = (Class<?>) obj;
            // Test object isn't generic so if this isn't generic either and the raw type matches then we're equal
            return this.genericTypeArguments.getLength() == 0 && clazz.equals(this.getRawType());
        }
        if (obj instanceof ParameterizedType && this.genericTypeArguments.getLength() == 0) {
            // Test object is generic, but this type isn't so no point carrying on
            return false;
        }
        if (!(obj instanceof SpecimenType)) {
            other = SpecimenType.of((Type) obj);
        } else {
            other = (SpecimenType) obj;
        }

        return this.rawType.equals(other.rawType) && this.genericTypeArguments.equals(other.genericTypeArguments);
    }

    private static Class nonPrimitiveType(Type type) {
        Class clazz = (Class) type;
        if (!(clazz.isPrimitive())) return (Class) type;
        return PrimitiveTypeMap.map.get(clazz);
    }

    @Override
    public final String toString() {
        if (this.genericTypeArguments.getLength() == 0) {
            return justClassName(this.rawType);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(justClassName(this.rawType));
        sb.append("<");
        for (int i = 0; i < this.genericTypeArguments.getLength(); i++) {
            sb.append(justClassName(this.genericTypeArguments.get(i).getType()));
            if (i < this.genericTypeArguments.getLength() - 1)
                sb.append(", ");
        }
        sb.append(">");
        return sb.toString();
    }

    private static String justClassName(Type type) {
        if (type instanceof SpecimenType) return type.toString();
        if (!(type instanceof Class)) throw new RuntimeException("This shouldn't happen");

        Class clazz = (Class) type;
        return clazz.getName();
    }

    private static class SpecimenTypeFields {
        Class rawType;
        GenericTypeCollection genericTypeArguments;
    }
}