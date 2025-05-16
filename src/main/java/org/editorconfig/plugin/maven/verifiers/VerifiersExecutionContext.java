/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Execution Context for {@link SpecOptionVerifier SpecOptionVerifiers}.
 *
 * @author Mikhail Polivakha
 */
public class VerifiersExecutionContext {

    private final Map<String, Object> localContext;

    private final Map<String, Object> globalContext;

    public VerifiersExecutionContext() {
        this.localContext = new HashMap<>();
        this.globalContext = new HashMap<>();
    }

    public VerifiersExecutionContext putGlobal(String key, Object value) {
        globalContext.put(key, value);
        return this;
    }

    public void putLocal(String key, Object value) {
        localContext.put(key, value);
    }

    public <T> T get(String key) {
        return (T)
                Optional.ofNullable(localContext.get(key)).orElseGet(() -> globalContext.get(key));
    }

    public void reset() {
        localContext.clear();
    }
}
