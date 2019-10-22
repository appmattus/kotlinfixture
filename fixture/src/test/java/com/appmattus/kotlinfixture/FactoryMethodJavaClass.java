package com.appmattus.kotlinfixture;

public class FactoryMethodJavaClass {
    private final String constructor;
    private String mutable;

    private FactoryMethodJavaClass(String constructor) {
        this.constructor = constructor;
    }

    @SuppressWarnings("unused")
    public void setMutable(String mutable) {
        this.mutable = mutable;
    }

    public String getConstructor() {
        return constructor;
    }

    public String getMutable() {
        return mutable;
    }

    @SuppressWarnings("unused")
    static FactoryMethodJavaClass create(String constructor) {
        return new FactoryMethodJavaClass(constructor);
    }
}
