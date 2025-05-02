/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package io.mpolivaha.maven.plugin.editorconfig.utils;

import java.util.Arrays;

import io.mpolivaha.maven.plugin.editorconfig.common.TripleFunction;
import io.mpolivaha.maven.plugin.editorconfig.model.Option;

public class OptionUtils {

    public static final String INDENT_STYLE = "indent_style";
    public static final String INDENT_SIZE = "indent_size";
    public static final String TAB_WIDTH = "tab_width";
    public static final String END_OF_LINE = "end_of_line";
    public static final String CHARSET = "charset";
    public static final String TRIM_TRAILING_WHITESPACE = "trim_trailing_whitespace";
    public static final String INSERT_FINAL_NEW_LINE = "insert_final_newline";

    /**
     * Error message function. Accepts option we were trying to set, the value for the option that was provided, and the
     * expected set into which the value does not fit in.
     * <p>
     * <p>
     * The reader might expect that this error message function should reside in {@link Option}, where the usages of it actually occur.
     * That is a valid assumption, however, the issue is that enum instances cannot reference static elements during their instantiation
     */
    public static final TripleFunction<String, String, String[], String> ERROR_MESSAGE =
            (option, actualValue, expectedEnums) ->
                    "The '%s' : '%s' is not among the set of values allowed by the spec : %s"
                            .formatted(option, actualValue, Arrays.toString(expectedEnums));
}
