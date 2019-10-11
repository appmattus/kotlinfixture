package com.appmattus.kotlinfixture;

public class FixtureTestJavaClass {
    private final String constructor;
    private String mutable;

    public FixtureTestJavaClass(String constructor) {
        this.constructor = constructor;
    }

    public void setMutable(String mutable) {
        this.mutable = mutable;
    }

    public String getConstructor() {
        return constructor;
    }

    public String getMutable() {
        return mutable;
    }
}
