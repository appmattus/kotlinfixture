/*
 * Copyright 2019 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.detomarco.kotlinfixture;

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
