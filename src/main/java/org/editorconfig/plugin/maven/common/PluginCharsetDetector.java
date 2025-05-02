/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.editorconfig.plugin.maven.utils.ExecutionUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.mozilla.universalchardet.UniversalDetector.InputState;

import org.editorconfig.plugin.maven.annotations.ThreadSafe;
import org.editorconfig.plugin.maven.model.Charset;

import static java.nio.charset.Charset.*;

/**
 * The problem with using the {@link org.mozilla.universalchardet.UniversalDetector} is that it cannot
 * differentiate between the {@link StandardCharsets#UTF_8 utf_8} and {@link StandardCharsets#US_ASCII plain ascii}
 * in case only ascii characters are present int the input.
 * <p>
 * Technically, it is indeed impossible to determine the correct charset in case of UTF-8 file and pure ascii usage.
 * However, it is a 100% very common case that we have the java source file, that contains only ascii and the user
 * specifies that it is indeed UTF-8. So because we cannot really tell if that is UTF-8, or ASCII or Latin1, we trust the
 * user.
 *
 * @author Mikhail Polivakha
 */
@ThreadSafe
public class PluginCharsetDetector {

    private final UniversalDetector universalDetector;
    public static final List<Charset> US_ASCII_COMPATIBLE_CHARSETS =
            List.of(Charset.UTF_8, Charset.LATIN1);

    public PluginCharsetDetector() {
        this.universalDetector = new UniversalDetector();
    }

    public synchronized List<Charset> detect(byte[] bytes) {
        universalDetector.handleData(bytes, 0, bytes.length);
        universalDetector.dataEnd();

        List<Charset> charsets;
        if (universalDetector.isDone()) {
            // "done" means that the detector is strongly convinced that it detected the charset
            // right
            charsets = extractSingleDetectedCharset();
        } else {
            // we can still have detected charset out of what we have even if the detector is not
            // done
            String detectedCharset = universalDetector.getDetectedCharset();

            if (detectedCharset != null && !detectedCharset.isEmpty()) {
                var javaCharset = forName(universalDetector.getDetectedCharset());

                if (StandardCharsets.US_ASCII.equals(javaCharset)) {
                    return US_ASCII_COMPATIBLE_CHARSETS;
                } else {
                    return Charset.from(javaCharset)
                            .map(Collections::singletonList)
                            .orElseGet(List::of);
                }
            }

            // if the detector is not sure at all, we still fallback to current state of input
            // characters as a final option
            InputState inputState = getInternalInputState();

            if (inputState == InputState.PURE_ASCII) {
                charsets = US_ASCII_COMPATIBLE_CHARSETS;
            } else {
                // we give up, we do not know the charset
                charsets = List.of();
            }
        }

        universalDetector.reset();
        return charsets;
    }

    private InputState getInternalInputState() {
        return ExecutionUtils.mapExceptionally(() -> {
            Field state = UniversalDetector.class.getDeclaredField("inputState");
            state.setAccessible(true);
            return (InputState) state.get(universalDetector);
        });
    }

    private List<Charset> extractSingleDetectedCharset() {
        return Optional.ofNullable(universalDetector.getDetectedCharset())
                .map(java.nio.charset.Charset::forName)
                .flatMap(Charset::from)
                .map(Collections::singletonList)
                .orElseGet(List::of);
    }
}
