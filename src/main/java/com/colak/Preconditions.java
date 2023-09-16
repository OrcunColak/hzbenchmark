package com.colak;

/**
 * A utility class for validating arguments and state.
 */
public final class Preconditions {

    private Preconditions() {
    }


    /**
     * Tests if an argument is not null.
     *
     * @param argument     the argument tested to see if it is not null.
     * @param errorMessage the errorMessage
     * @return the argument that was tested.
     * @throws NullPointerException if argument is null
     */
    public static <T> T checkNotNull(T argument, String errorMessage) {
        if (argument == null) {
            throw new NullPointerException(errorMessage);
        }
        return argument;
    }

    /**
     * Tests if an argument is not null.
     *
     * @param argument the argument tested to see if it is not null.
     * @return the argument that was tested.
     * @throws NullPointerException if argument is null
     */
    public static <T> T checkNotNull(T argument) {
        if (argument == null) {
            throw new NullPointerException();
        }
        return argument;
    }
}
