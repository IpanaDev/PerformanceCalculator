package utils;

@FunctionalInterface
public interface OctaBoolConsumer<A, B, C, D, E, F, G, H> {
    boolean accept(A a, B b, C c, D d, E e, F f, G g, H h);
}
