/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.maven.plugin.MojoExecutionException;
import org.editorconfig.plugin.maven.assertions.Assert;
import org.editorconfig.plugin.maven.common.TripleFunction;
import org.editorconfig.plugin.maven.model.Editorconfig;
import org.editorconfig.plugin.maven.model.GlobExpression;
import org.editorconfig.plugin.maven.model.Option;
import org.editorconfig.plugin.maven.utils.ExecutionUtils;
import org.editorconfig.plugin.maven.utils.ParsingUtils;
import org.editorconfig.plugin.maven.utils.ParsingUtils.KeyValue;

public class EditorconfigParser {

    public static final EditorconfigParser INSTANCE = new EditorconfigParser();

    private static final BiFunction<String, Integer, String> KEY_VALUE_PARSE_ERROR =
            (line, lineNumber) ->
                    "For line number '%d' with content : '%s' expected to contain key/value pair, but we cannot parse it"
                            .formatted(lineNumber, line);

    private static final TripleFunction<String, Integer, String, String> UNRECOGNIZED_KEY_ERROR =
            (line, lineNumber, key) ->
                    "For line number '%d' with content : '%s' parsed the key : '%s', which is not among the recognized keys : %s"
                            .formatted(lineNumber, line, key, Arrays.toString(Option.values()));

    public Editorconfig parse(Path path) {
        try (var reader = new BufferedReader(new FileReader(path.toFile()))) {
            return parseInternally(reader, path);
        } catch (IOException e) {
            Assert.sneakyThrows(new MojoExecutionException("Unable to read .editorconfig file", e));
            return null; // unreachable code
        }
    }

    private static Editorconfig parseInternally(BufferedReader reader, Path editorConfigLocation)
            throws IOException {
        String line;
        ParsingContext context = null;

        do {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            if (context == null) {
                context = new ParsingContext(line, editorConfigLocation);
            } else {
                context.newline(line);
            }

            parseLineInternally(context);
        } while (true);

        if (context == null) {
            // that means the file is empty
            context = new ParsingContext("\0", editorConfigLocation);
        }

        return context.completeBuild();
    }

    private static void parseLineInternally(ParsingContext context) {
        String line = context.getLine();

        if (line.isBlank()) {
            return;
        }

        if (!ParsingUtils.isComment(line)) {
            parseSignificantLine(context);
        }
    }

    private static void parseSignificantLine(ParsingContext context) {
        String line = context.getLine();

        if (ParsingUtils.isSectionStart(line)) {
            context.startNewSection(GlobExpression.from(line.trim()));
        } else {
            final var holder = new LineNumberAndLine(
                    line, context.getLineNumber()); // see javadoc on holder class
            Optional<KeyValue> optKeyValue = ParsingUtils.parseKeyValue(line);

            if (optKeyValue.isEmpty()) {
                ExecutionUtils.handleError(
                        KEY_VALUE_PARSE_ERROR.apply(holder.line, holder.lineNumber));
                return;
            }

            KeyValue keyValue = optKeyValue.get();

            Optional<Option> option = Option.from(keyValue.key());

            if (option.isEmpty()) {
                checkForRoot(context, holder, keyValue);
                return;
            }

            option.get().merge(keyValue, context.getSectionBuilder());
        }
    }

    private static void checkForRoot(
            ParsingContext context, LineNumberAndLine holder, KeyValue keyValue) {
        if (keyValue.key().equalsIgnoreCase("root")) {
            context.markAsRoot();
        } else {
            ExecutionUtils.handleError(
                    UNRECOGNIZED_KEY_ERROR.apply(holder.line, holder.lineNumber, keyValue.key()));
        }
    }

    /**
     * This class mainly exists due to limitations we have in use of local variables in lambda (effectively final constraint)
     * @param line
     * @param lineNumber
     */
    private record LineNumberAndLine(String line, int lineNumber) {}
}
