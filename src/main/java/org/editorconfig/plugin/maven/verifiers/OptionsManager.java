/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.verifiers.impl.CharsetOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.impl.EndOfLineOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.impl.IndentationSizeOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.impl.IndentationStyleOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.impl.InsertFinalNewLineOptionVerifier;
import org.editorconfig.plugin.maven.verifiers.impl.TrimTrailingWhitespaceOptionVerifier;

public class OptionsManager {

    private static final OptionsManager INSTANCE;

    static {
        List<SpecOptionVerifier<?>> specOptionVerifiers = new ArrayList<>(List.of(
                new CharsetOptionVerifier(),
                new EndOfLineOptionVerifier(),
                new IndentationSizeOptionVerifier(),
                new InsertFinalNewLineOptionVerifier(),
                new IndentationStyleOptionVerifier(),
                new TrimTrailingWhitespaceOptionVerifier()));

        INSTANCE = new OptionsManager(specOptionVerifiers);
    }

    private final List<SpecOptionVerifier<?>> specOptionVerifiers;

    /**
     * @param specOptionVerifiers mutable {@link List} of verifiers for delegation.
     */
    OptionsManager(List<SpecOptionVerifier<?>> specOptionVerifiers) {
        specOptionVerifiers.sort(Comparator.comparing(SpecOptionVerifier::getOrder));
        this.specOptionVerifiers = specOptionVerifiers;
    }

    public static OptionsManager getInstance() {
        return INSTANCE;
    }

    /**
     * Check for possible .editorconfig violations on a single file.
     *
     * @param file - file to check for violations
     * @param section - relevant {@link Section} of the appropriate .editorconfig file to be applied for the given file
     * @return CompoundOptionValidationResult that encapsulates all validation errors that happened
     */
    @SuppressWarnings("rawtypes")
    public CompoundOptionValidationResult check(Path file, Section section) throws IOException {
        var compoundResult = new CompoundOptionValidationResult(file);
        CachingInputStream cachingInputStream = new CachingInputStream(file.toFile());
        VerifiersExecutionContext context = new VerifiersExecutionContext();

        for (SpecOptionVerifier specOptionVerifier : specOptionVerifiers) {
            OptionValidationResult check =
                    specOptionVerifier.check(cachingInputStream, section, context);
            compoundResult.add(check);
            cachingInputStream.reset();
            context.reset();
        }

        return compoundResult;
    }
}
