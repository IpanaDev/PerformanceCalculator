package utils;

@FunctionalInterface
public interface PentaBoolConsumer<A, B, C, D, E> {
    boolean accept(A a, B b, C c, D d, E e);

}
