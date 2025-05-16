/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.verifiers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.editorconfig.plugin.maven.common.ByteArrayLine;
import org.editorconfig.plugin.maven.common.CachingInputStream;
import org.editorconfig.plugin.maven.common.Ordered;
import org.editorconfig.plugin.maven.model.Charset;
import org.editorconfig.plugin.maven.model.GlobExpression;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.model.OptionValue;
import org.editorconfig.plugin.maven.model.Section;
import org.editorconfig.plugin.maven.model.SectionBuilder;
import org.editorconfig.plugin.maven.utils.ExecutionUtils;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link OptionsManager}.
 *
 * @author Mikhail Polivakha
 */
class OptionsManagerTest {

    @Test
    void testOrderOfVerifiersIsTakenIntoAccount() throws IOException {

        // given.
        Queue<Class<?>> invocationQueue = new LinkedList<>();

        ArrayList<SpecOptionVerifier<?>> specOptionVerifiers = new ArrayList<>(List.of(
                new Normal(Option.CHARSET, invocationQueue),
                new Latest(Option.CHARSET, invocationQueue),
                new Earliest(Option.CHARSET, invocationQueue)));

        OptionsManager optionsManager = new OptionsManager(specOptionVerifiers);

        // when.
        optionsManager.check(
                Files.createTempFile("", ""),
                new SectionBuilder(GlobExpression.from("*")).completeSection());

        // then.
        assertThat(invocationQueue)
                .containsExactly(
                        Earliest.class,
                        Normal.class,
                        Latest.class); // the exact invocation order expected
    }

    static class Earliest extends AbstractTestVerifier {

        public Earliest(Option targetOption, Queue<Class<?>> invocationQueue) {
            super(targetOption, invocationQueue);
        }

        @Override
        public int getOrder() {
            return Ordered.EARLIEST;
        }
    }

    static class Latest extends AbstractTestVerifier {

        public Latest(Option targetOption, Queue<Class<?>> invocationQueue) {
            super(targetOption, invocationQueue);
        }

        @Override
        public int getOrder() {
            return Ordered.LATEST;
        }
    }

    static class Normal extends AbstractTestVerifier {

        public Normal(Option targetOption, Queue<Class<?>> invocationQueue) {
            super(targetOption, invocationQueue);
        }

        @Override
        public int getOrder() {
            return Ordered.NORMAL;
        }
    }

    static class AbstractTestVerifier extends SpecOptionVerifier<Charset> {

        private final Queue<Class<?>> invocationQueue;

        @Override
        protected OptionValidationResult checkInternal(
                CachingInputStream content,
                Section section,
                VerifiersExecutionContext executionContext) {
            invocationQueue.add(this.getClass());
            ExecutionUtils.executeExceptionally(
                    content::readAllBytes); // we need to consume the entire content stream.
            return OptionValidationResult.skippedValidation(Option.CHARSET);
        }

        @Override
        protected void forEachLine(
                @NonNull ByteArrayLine line,
                int lineNumber,
                Charset optionValue,
                @NonNull OptionValidationResult result,
                @NonNull VerifiersExecutionContext context) {}

        @Override
        public OptionValue<Charset> getValueFromSection(Section section) {
            return OptionValue.resolve("utf-16le", Charset::from);
        }

        public AbstractTestVerifier(Option targetOption, Queue<Class<?>> invocationQueue) {
            super(targetOption);
            this.invocationQueue = invocationQueue;
        }
    }
}
