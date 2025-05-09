/**
 * Copyright (c) 2025 EditorConfig Organization
 * These source file is created by EditorConfig Organization and is distributed under the MIT license.
 */
package org.editorconfig.plugin.maven.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This is a common implementation that of an {@link InputStream} that is here to
 * achieve the goal of re-reading the stream once it is fully read. The original
 * {@link FileInputStream} does not support mark/reset API, and therefore we cannot
 * re-read the stream, unless we read the same file on disk multiple times.
 *
 * @implNote This class is <strong>NOT</strong> thread-safe.
 * @author Mikhail Polivakha
 */
public class CachingInputStream extends FileInputStream {

    private static final Integer INITIAL_ESTIMATE_FILE_SIZE = 2048;

    private final ByteArrayOutputStream buffer;
    private byte[] bufferAsByteArray;
    private boolean upstreamExhausted;

    private int position;

    private final File source;

    public CachingInputStream(File file) throws FileNotFoundException {
        super(file);
        this.source = file;
        this.upstreamExhausted = false;
        this.position = -1;
        this.buffer = new ByteArrayOutputStream(getBufferSize());
        this.bufferAsByteArray = null;
    }

    private int getBufferSize() {
        try {
            return super.available()
                    + 10; // available is only an estimate, adding a padding just in case
        } catch (IOException e) {
            return INITIAL_ESTIMATE_FILE_SIZE;
        }
    }

    public File getOriginalFile() {
        return source;
    }

    @Override
    public int read() throws IOException {
        if (upstreamExhausted) {
            if (position >= bufferAsByteArray.length) {
                return -1;
            }
            return bufferAsByteArray[position++];
        } else {
            int read = super.read();
            if (read == -1) {
                switchToCachingMode();
            } else {
                buffer.write(read);
            }
            return read;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (upstreamExhausted) {
            int i = 0;
            for (; len > 0 && off < bufferAsByteArray.length; off++, len--, i++, position++) {
                b[i] = bufferAsByteArray[off];
            }
            return i;
        } else {
            int read = super.read(b, off, len);

            if (read == -1) {
                switchToCachingMode();
            } else {
                if (read != len) {
                    switchToCachingMode();
                }

                for (int i = 0; i < read; i++) {
                    buffer.write(b[i]);
                }
            }

            return read;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        if (len == 0) {
            return new byte[] {};
        }
        if (len < 0) {
            throw new IllegalArgumentException("The passed len cannot be less than zero");
        }

        if (upstreamExhausted) {
            // we cannot just pass len into Arrays.copyOfRange, because in case len will surpass the
            // length of the bufferAsByteArray, then we'll get a resulting bytes array with trailing
            // NULLs (i.e. '\0'), that is unacceptable by this implementation
            int effectiveLength = len;

            if (len - position > bufferAsByteArray.length) {
                effectiveLength = bufferAsByteArray.length;
            }

            byte[] bytes = Arrays.copyOfRange(bufferAsByteArray, position, effectiveLength);
            position += effectiveLength;
            return bytes;
        } else {
            byte[] bytes = super.readNBytes(len);

            for (byte b : bytes) {
                buffer.write(b);
            }

            if (bytes.length < len) {
                switchToCachingMode();
            }

            return bytes;
        }
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if (off < 0 || len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException(
                    "Either the off is negative, len is negative, or the len + off is larger then the length of the provided buffer");
        }

        if (upstreamExhausted) {
            int index = 0;
            for (; len > 0 && off < bufferAsByteArray.length; len--, off++) {
                b[index++] = bufferAsByteArray[off];
            }
            return index;
        } else {
            int bytesRead = super.readNBytes(b, off, len);

            if (bytesRead < len) {
                switchToCachingMode();
            }

            for (int i = 0; i < bytesRead; i++) {
                buffer.write(b[i]);
            }
            return bytesRead;
        }
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        byte[] bytes;

        if (upstreamExhausted) {
            bytes = Arrays.copyOfRange(bufferAsByteArray, position, bufferAsByteArray.length);
            position = bufferAsByteArray.length;
        } else {
            bytes = super.readAllBytes();

            for (byte b : bytes) {
                buffer.write(b);
            }

            upstreamExhausted = true;
        }
        return bytes;
    }

    private void switchToCachingMode() {
        upstreamExhausted = true;
        position = 0;
        bufferAsByteArray = buffer.toByteArray();
    }

    @Override
    public synchronized void mark(int readlimit) {
        // No-Op
        // WARNING! This violates the contract of the mark/reset API, but this class is considered
        // to be internal
    }

    @Override
    public void reset() {
        if (!upstreamExhausted) {
            throw new IllegalArgumentException(
                    "Cannot call reset() on CachingInputStream in case the upstream is not exhausted");
        }
        position = 0;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    public int getPosition() {
        return position;
    }
}
